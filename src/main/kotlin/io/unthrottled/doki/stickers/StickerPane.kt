package io.unthrottled.doki.stickers

import com.intellij.openapi.Disposable
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLayeredPane
import com.intellij.ui.jcef.HwFacadeJPanel
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.util.runSafelyWithResult
import org.intellij.lang.annotations.Language
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.geom.AffineTransform
import java.io.File
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.JComponent
import javax.swing.JLayeredPane
import javax.swing.JPanel
import kotlin.math.roundToInt

internal class StickerPane(
  private val drawablePane: JLayeredPane,
) : HwFacadeJPanel(), Disposable {

  companion object {
    private const val STICKER_Y_OFFSET = 45
    private const val STICKER_X_OFFSET = 25
  }

  var positionable: Boolean = ThemeConfig.instance.isMoveableStickers
    set(value) {
      field = value
      if (value) {
        addListeners()
      } else {
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
    override fun mouseClicked(e: MouseEvent?) {}

    override fun mousePressed(e: MouseEvent) {
      screenX = e.xOnScreen
      screenY = e.yOnScreen
      myX = x
      myY = y
    }

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}
  }

  private val dragListener = object : MouseMotionListener {
    override fun mouseDragged(e: MouseEvent) {
      val deltaX = e.xOnScreen - screenX
      val deltaY = e.yOnScreen - screenY

      setLocation(myX + deltaX, myY + deltaY)
    }

    override fun mouseMoved(e: MouseEvent?) {}
  }

  init {
    isOpaque = false
    layout = null

    drawablePane.addComponentListener(
      object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent) {
          val layer = e.component
          if (layer !is JComponent) return

          val deltaX = layer.width - parentX
          val deltaY = layer.height - parentY
          setLocation(x + deltaX, y + deltaY)
          parentX = layer.width
          parentY = layer.height
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

  private var positioned = false

  fun displaySticker(stickerUrl: String) {
    // clean up old sticker
    if (componentCount > 0) {
      remove(0)
    }

    // add new sticker
    val stickerContent = createStickerContentPanel(stickerUrl)
    add(stickerContent)
    this.size = stickerContent.size

    positionStickerPanel(
      stickerContent.size.width,
      stickerContent.size.height,
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

  private fun createStickerContentPanel(stickerUrl: String): JPanel {
    val stickerContent = JPanel()
    stickerContent.layout = null
    val (width, height) = getImageDimensions(stickerUrl)
    @Language("html")
    val stickerDisplay = object : JBLabel(
      """<html>
           <img src='$stickerUrl' width='${width}px' height='${height}px' />
         </html>
      """
    ) {

      // Java 9+ Does automatic DPI scaling,
      // but we want to ignore that, cause the sticker
      // will grow to be pixelated
      // fixes https://github.com/doki-theme/doki-theme-jetbrains/issues/465
      override fun paintComponent(g: Graphics) {
        if (g is Graphics2D && g.transform.scaleX.compareTo(1.0) > 0) {
          val t: AffineTransform = g.transform
          if (!positioned) {
            positioned = true
            positionStickerPanel(
              stickerContent.size.width - ((t.scaleX - 1.0) * stickerContent.size.width).roundToInt(),
              stickerContent.size.height - ((t.scaleY - 1.0) * stickerContent.size.height).roundToInt(),
            )
          }
          val xTrans = t.translateX
          val yTrans = t.translateY
          t.setToScale(1.0, 1.0)
          t.translate(xTrans, yTrans)
          g.transform = t
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

  private fun getImageDimensions(stickerUrl: String): Pair<Int, Int> =
    runSafelyWithResult({
      ImageIO.createImageInputStream(File(URI(stickerUrl)))
        .use {
          val readers = ImageIO.getImageReaders(it)
          if (readers.hasNext()) {
            val reader = readers.next()
            reader.input = it
            reader.getWidth(0) to reader.getHeight(0)
          } else {
            0 to 0
          }
        }
    }) { 0 to 0 }

  private fun positionStickerPanel(width: Int, height: Int) {
    val (x, y) = getPosition(
      drawablePane.x + drawablePane.width,
      drawablePane.y + drawablePane.height,
      Rectangle(width, height)
    )
    myX = x
    myY = y
    setLocation(x, y)
  }

  private fun getPosition(
    parentWidth: Int,
    parentHeight: Int,
    stickerPanelBoundingBox: Rectangle,
  ): Pair<Int, Int> =
    parentWidth - stickerPanelBoundingBox.width - STICKER_X_OFFSET to
      parentHeight - stickerPanelBoundingBox.height - STICKER_Y_OFFSET

  fun detach() {
    drawablePane.remove(this)
  }

  override fun dispose() {
    detach()
  }
}
