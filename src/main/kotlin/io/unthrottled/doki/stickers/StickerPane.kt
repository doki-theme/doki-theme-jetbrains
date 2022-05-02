package io.unthrottled.doki.stickers

import com.intellij.openapi.Disposable
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JreHiDpiUtil
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLayeredPane
import com.intellij.ui.jcef.HwFacadeJPanel
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.Alarm
import com.intellij.util.ui.Animator
import com.intellij.util.ui.ImageUtil
import com.intellij.util.ui.StartupUiUtil
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import java.awt.AWTEvent
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.InputEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.RGBImageFilter
import java.io.File
import java.net.URI
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JLayeredPane
import javax.swing.JPanel
import javax.swing.MenuElement
import javax.swing.SwingUtilities

enum class StickerType {
  REGULAR, SMOL, ALL
}

data class Margin(
  val marginX: Double,
  val marginY: Double,
)

interface StickerListener {

  fun onDoubleClick(margin: Margin)
}

@Suppress("TooManyFunctions")
internal class StickerPane(
  private val drawablePane: JLayeredPane,
  val type: StickerType,
  initialMargin: Margin,
  private val stickerListener: StickerListener,
) : HwFacadeJPanel(), Disposable, Logging {

  private lateinit var stickerContent: JPanel

  var ignoreScaling = ThemeConfig.instance.ignoreScaling
    set(value) {
      field = value
      if (value) {
        this.size = getScaledDimension()
      } else if (this::stickerContent.isInitialized) {
        this.size = stickerContent.size
      }
      positionStickerPanel(
        this.size.width, this.size.height
      )
    }

  internal var hideConfig: StickerHideConfig =
    StickerHideConfig(ThemeConfig.instance.hideOnHover, ThemeConfig.instance.hideDelayMS)

  private val isSmol = StickerType.SMOL == type
  var positionable: Boolean =
    if (isSmol) true
    else ThemeConfig.instance.isMoveableStickers
    set(value) {
      field = value
      if (value) {
        addListeners()
      } else if (!isSmol) {
          removeListeners()
        }
      }

  @Volatile
  private var screenX = 0

  @Volatile
  private var screenY = 0

  @Volatile
  private var myX = 0

  @Volatile
  private var myY = 0

  @Volatile
  private var parentX = drawablePane.width

  @Volatile
  private var parentY = drawablePane.height

  private val dragListenerInitiationListener = object : MouseListener {
    private val doubleClickAlarm = Alarm(this@StickerPane)
    private var clickCount = 0
    override fun mouseClicked(e: MouseEvent?) {
      clickCount += 1
      if (clickCount > 1) {
        stickerListener.onDoubleClick(captureMargin())
      }
      doubleClickAlarm.cancelAllRequests()
      doubleClickAlarm.addRequest(
        {
          clickCount = 0
        },
        250
      )
    }

    override fun mousePressed(e: MouseEvent) {
      screenX = e.xOnScreen
      screenY = e.yOnScreen
      myX = x
      myY = y
    }

    override fun mouseReleased(e: MouseEvent?) {
      saveMargin()
    }

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}
  }

  private fun saveMargin() {
    _margin = captureMargin()
  }

  private fun captureMargin(): Margin {
    val stickerPos = location
    val stickerSize = stickerContent.size
    return Margin(
      (parentX - (stickerPos.x + stickerSize.width)) / parentX.toDouble(),
      (parentY - (stickerPos.y + stickerSize.height)) / parentY.toDouble(),
    )
  }

  private val dragListener = object : MouseMotionListener {
    override fun mouseDragged(e: MouseEvent) {
      val deltaX = e.xOnScreen - screenX
      val deltaY = e.yOnScreen - screenY

      setLocation(myX + deltaX, myY + deltaY)
    }

    override fun mouseMoved(e: MouseEvent?) {}
  }

  private val mouseListener: AWTEventListener = createMouseListener()

  private val hoverAlarm = Alarm(this)

  private fun createMouseListener(): AWTEventListener {
    var hoveredInside = false
    return AWTEventListener { event ->
      if (
        !this.hideConfig.hideOnHover ||
        event !is InputEvent ||
        UIUtil.isDescendingFrom(event.component, drawablePane).not()
      ) return@AWTEventListener

      if (event is MouseEvent) {
        val wasInside = isInsideMemePanel(event)
        if (event.id == MouseEvent.MOUSE_MOVED && wasInside) {
          if (!hoveredInside) {
            hoverAlarm.addRequest({
              runFadeAnimation(runForwards = false)
              if (positionable) {
                removeListeners()
              }
            }, this.hideConfig.hideDelayMS)
          }
          hoveredInside = true
        } else if (event.id == MouseEvent.MOUSE_MOVED && hoveredInside && !wasInside) {
          hoverAlarm.cancelAllRequests()
          hoverAlarm.addRequest({
            hoveredInside = false
            if (positionable) {
              addListeners()
            }
            runFadeAnimation(runForwards = true)
          }, fadeInDelay)
        }
      }
    }
  }

  private fun isInsideMemePanel(e: MouseEvent): Boolean =
    isInsideComponent(e, this)

  private fun isInsideComponent(e: MouseEvent, rootPane1: JComponent): Boolean {
    val target = RelativePoint(e)
    val ogComponent = target.originalComponent
    return when {
      ogComponent.isShowing.not() -> true
      ogComponent is MenuElement -> false
      UIUtil.isDescendingFrom(ogComponent, rootPane1) -> true
      this.isShowing.not() -> false
      else -> {
        val point = target.screenPoint
        SwingUtilities.convertPointFromScreen(point, rootPane1)
        rootPane1.contains(point)
      }
    }
  }


  init {
    isOpaque = false
    layout = null

    Toolkit.getDefaultToolkit().addAWTEventListener(
      mouseListener,
      AWTEvent.MOUSE_MOTION_EVENT_MASK
    )


    drawablePane.addComponentListener(
      object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent) {
          val layer = e.component
          if (layer !is JComponent) return

          parentX = layer.width
          parentY = layer.height

          if (this@StickerPane::stickerContent.isInitialized.not()) return

          positionStickerPanel(
            stickerContent.width,
            stickerContent.height,
          )
        }
      }
    )

    if (positionable) {
      addListeners()
    }
  }

  private fun addListeners() {
    addMouseListener(dragListenerInitiationListener)
    addMouseMotionListener(dragListener)
  }

  private fun removeListeners() {
    removeMouseListener(dragListenerInitiationListener)
    removeMouseMotionListener(dragListener)
  }

  fun displaySticker(stickerUrl: String) {
    // clean up old sticker
    if (componentCount > 0) {
      runSafely({
        remove(0)
      }) {
        logger().warn("Unable to remove previous sticker for raisins.", it)
      }
    }

    // allows the sticker to be
    // re-positioned if scaling is ignored.
    positioned = false

    // don't show on small stickers on
    // small dialog windows
    if (type == StickerType.SMOL &&
      drawablePane.height < 400
    ) {
      return
    }

    // add new sticker
    this.stickerContent = createStickerContentPanel(stickerUrl)
    add(stickerContent)
    this.size = stickerContent.size

    positionStickerPanel(
      this.size.width,
      this.size.height,
    )

    drawablePane.remove(this)
    drawablePane.add(this)
    drawablePane.setLayer(this, JBLayeredPane.DEFAULT_LAYER)
    doDumbStuff()
  }

  /**
   * I'm not going to pretend like I know what I am doing.
   * I do know that the render issue goes away, when another
   * component is added to the root pane. Finna treat the symptom
   * and not fix the cause.
   */
  private fun doDumbStuff() {
    val ghostHax = JPanel()
    drawablePane.add(ghostHax)
    drawablePane.setLayer(ghostHax, JBLayeredPane.DRAG_LAYER)
    drawablePane.revalidate()
    drawablePane.repaint()
    drawablePane.remove(ghostHax)
    drawablePane.revalidate()
    drawablePane.repaint()
  }

  private var currentScaleX = 1.0
  private var currentScaleY = 1.0
  private var positioned = false

  private fun createStickerContentPanel(stickerUrl: String): JPanel {
    val stickerContent = JPanel()
    stickerContent.layout = null
    val stickerDimension = getUsableStickerDimension(stickerUrl)

    val originalImage = ImageIcon(URL(stickerUrl)).image
    val lessGarbageImage = originalImage.getScaledInstance(
      stickerDimension.width,
      stickerDimension.height,
      if (stickerUrl.contains(".gif")) Image.SCALE_DEFAULT else Image.SCALE_SMOOTH
    )
    val stickerDisplay = object : JBLabel(ImageIcon(lessGarbageImage)) {

      // Java 9+ Does automatic DPI scaling,
      // but we want to ignore that, cause the sticker
      // will grow to be pixelated
      // fixes https://github.com/doki-theme/doki-theme-jetbrains/issues/465
      override fun paintComponent(g: Graphics) {
        if (g is Graphics2D) {
          val t: AffineTransform = g.transform
          currentScaleX = t.scaleX
          currentScaleY = t.scaleY
          if (g.transform.scaleX.compareTo(1.0) > 0 && ignoreScaling) {
            if (!positioned) {
              positioned = true
              val scaledDimension = getScaledDimension()
              this@StickerPane.size = scaledDimension
              this@StickerPane.positionStickerPanel(
                scaledDimension.width, scaledDimension.height
              )
            }
            val xTrans = t.translateX
            val yTrans = t.translateY
            t.setToScale(1.0, 1.0)
            t.translate(xTrans, yTrans)
            g.transform = t
          }
        }
        super.paintComponent(g)
      }
    }

    val stickerSize = stickerDisplay.preferredSize
    stickerContent.size = Dimension(
      stickerSize.width,
      stickerSize.height,
    )
    stickerContent.isOpaque = false
    stickerContent.add(stickerDisplay)
    val parentInsets = stickerDisplay.insets
    stickerDisplay.setBounds(
      parentInsets.left,
      parentInsets.top,
      stickerSize.width,
      stickerSize.height
    )

    return stickerContent
  }

  private fun getScaledDimension(): Dimension {
    val stickerContentSize =
      if (this@StickerPane::stickerContent.isInitialized) {
        this@StickerPane.stickerContent.size
      } else {
        Dimension(1, 1)
      }
    return Dimension(
      (stickerContentSize.width / currentScaleX).toInt(),
      (stickerContentSize.height / currentScaleY).toInt(),
    )
  }

  private fun getUsableStickerDimension(stickerUrl: String): Dimension {
    val originalDimension = getImageDimensions(stickerUrl)
    return when {
      type == StickerType.SMOL ->
        DimensionCappingService.getCappingStyle(
          originalDimension,
          Dimension(
            ThemeConfig.instance.smallMaxStickerWidth,
            ThemeConfig.instance.smallMaxStickerHeight,
          )
        )
      type == StickerType.REGULAR &&
        ThemeConfig.instance.capStickerDimensions ->
        DimensionCappingService.getCappingStyle(
          originalDimension,
          Dimension(
            ThemeConfig.instance.maxStickerWidth,
            ThemeConfig.instance.maxStickerHeight,
          )
        )
      else -> originalDimension
    }
  }

  private fun getImageDimensions(stickerUrl: String): Dimension =
    runSafelyWithResult({
      ImageIO.createImageInputStream(File(URI(stickerUrl)))
        .use {
          val readers = ImageIO.getImageReaders(it)
          if (readers.hasNext()) {
            val reader = readers.next()
            reader.input = it
            Dimension(reader.getWidth(0), reader.getHeight(0))
          } else {
            Dimension(1, 1)
          }
        }
    }) { Dimension(1, 1) }

  private fun positionStickerPanel(width: Int, height: Int) {
    val (x, y) = getPosition(
      drawablePane.width,
      drawablePane.height,
      Rectangle(width, height)
    )
    myX = x
    myY = y
    setLocation(x, y)
  }

  private var _margin = initialMargin

  private fun getPosition(
    parentWidth: Int,
    parentHeight: Int,
    stickerPanelBoundingBox: Rectangle,
  ): Pair<Int, Int> =
    parentWidth - stickerPanelBoundingBox.width - (parentWidth * _margin.marginX).toInt() to
      parentHeight - stickerPanelBoundingBox.height - (parentHeight * _margin.marginY).toInt()

  fun detach() {
    drawablePane.remove(this)
  }

  override fun dispose() {
    detach()
  }

  fun updateMargin(margin: Margin) {
    _margin = margin
    positionStickerPanel(
      size.width,
      size.height,
    )
  }

  private var alpha = 1.0f
  private var overlay: BufferedImage? = null
  private fun clear() {
    alpha = CLEARED_ALPHA
    overlay = null
  }

  private fun makeColorTransparent(image: Image, color: Color): Image {
    val markerRGB = color.rgb or -0x1000000
    return ImageUtil.filter(
      image,
      object : RGBImageFilter() {
        override fun filterRGB(x: Int, y: Int, rgb: Int): Int =
          if (rgb or -0x1000000 == markerRGB) {
            WHITE_HEX and rgb // set alpha to 0
          } else rgb
      }
    )
  }


  private fun fancyPaintChildren(imageGraphics2d: Graphics2D) {
    // Paint to an image without alpha to preserve fonts subpixel antialiasing
    val image: BufferedImage = ImageUtil.createImage(
      imageGraphics2d,
      width,
      height,
      BufferedImage.TYPE_INT_RGB
    )

    val fillColor = MessageType.INFO.popupBackground
    UIUtil.useSafely(image.createGraphics()) { imageGraphics: Graphics2D ->
      imageGraphics.paint = Color(fillColor.rgb) // create a copy to remove alpha
      imageGraphics.fillRect(0, 0, width, height)
      super.paintChildren(imageGraphics)
    }

    val g2d = imageGraphics2d.create() as Graphics2D

    try {
      if (JreHiDpiUtil.isJreHiDPI(g2d)) {
        val s = 1 / JBUIScale.sysScale(g2d)
        g2d.scale(s.toDouble(), s.toDouble())
      }
      StartupUiUtil.drawImage(g2d, makeColorTransparent(image, fillColor), 0, 0, null)
    } finally {
      g2d.dispose()
    }
  }

  private fun initComponentImage() {
    if (overlay != null) return

    overlay = UIUtil.createImage(this, width, height, BufferedImage.TYPE_INT_ARGB)
    UIUtil.useSafely(overlay!!.graphics) { imageGraphics: Graphics2D ->
      fancyPaintChildren(imageGraphics)
    }
  }

  override fun paintComponent(g: Graphics?) {
    super.paintComponent(g)
    if (g !is Graphics2D) return

    if (overlay == null && alpha != CLEARED_ALPHA) {
      initComponentImage()
    }

    if (overlay != null && alpha != CLEARED_ALPHA) {
      g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
      StartupUiUtil.drawImage(g, overlay!!, 0, 0, null)
    }
  }


  /**
   * In short, the fade in/out animations work by first painting the
   * panel, taking an image still, then display the image over top and
   * perform the transparency to the image, so that it looks like it
   * fades in/out.
   */
  private fun runFadeAnimation(runForwards: Boolean = true) {
    val self = this
    val animator = object : Animator(
      "Sticker Fadeout",
      TOTAL_FRAMES,
      CYCLE_DURATION,
      false,
      runForwards
    ) {
      override fun paintNow(frame: Int, totalFrames: Int, cycle: Int) {
        alpha = frame.toFloat() / totalFrames
        paintImmediately(0, 0, width, height)
      }

      override fun paintCycleEnd() {
        if (isForward) {
          clear()
          self.repaint()
        }

        Disposer.dispose(this)
      }
    }

    animator.resume()
  }

  companion object {
    private const val fadeInDelay = 500
    private const val TOTAL_FRAMES = 8
    private const val CYCLE_DURATION = 250
    private const val CLEARED_ALPHA = -1f
    private const val WHITE_HEX = 0x00FFFFFF
  }
}
