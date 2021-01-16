package io.unthrottled.doki.stickers.impl

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLayeredPane
import com.intellij.ui.jcef.HwFacadeJPanel
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.stickers.StickerService
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toOptional
import org.intellij.lang.annotations.Language
import java.awt.AWTEvent
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.WindowEvent
import java.util.Optional
import javax.swing.JComponent
import javax.swing.JLayeredPane
import javax.swing.JPanel

const val DOKI_BACKGROUND_PROP: String = "io.unthrottled.doki.background"

const val DOKI_STICKER_PROP: String = "io.unthrottled.doki.stickers"
private const val PREVIOUS_STICKER = "io.unthrottled.doki.sticker.previous"

class StickerServiceImpl : StickerService {

  override fun activateForTheme(dokiTheme: DokiTheme) {
    listOf(
      {
        installSticker(dokiTheme)
      },
      {
        installBackgroundImage(dokiTheme)
      }
    ).forEach {
      getApplication().executeOnPooledThread(it)
    }
  }

  override fun checkForUpdates(dokiTheme: DokiTheme) {
    getApplication().executeOnPooledThread {
      getLocallyInstalledBackgroundImagePath(dokiTheme)
      getLocallyInstalledStickerPath(dokiTheme)
    }
  }

  private fun installSticker(dokiTheme: DokiTheme) =
    getLocallyInstalledStickerPath(dokiTheme)
      .ifPresent { stickerPath ->
        StickerPanelService.installSticker(stickerPath)
      }

  private fun installBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledBackgroundImagePath(dokiTheme)
      .ifPresent {
        setBackgroundImageProperty(
          it.first,
          "100",
          IdeBackgroundUtil.Fill.SCALE.name,
          it.second.position.name,
          DOKI_BACKGROUND_PROP
        )
      }

  private fun getLocallyInstalledBackgroundImagePath(
    dokiTheme: DokiTheme
  ): Optional<Pair<String, Background>> =
    dokiTheme.getBackground()
      .flatMap { background ->
        AssetManager.resolveAssetUrl(
          AssetCategory.BACKGROUNDS,
          background.name
        ).map { it to background }
      }

  private fun getLocallyInstalledStickerPath(
    dokiTheme: DokiTheme
  ): Optional<String> =
    dokiTheme.getStickerPath()
      .flatMap {
        AssetManager.resolveAssetUrl(
          AssetCategory.STICKERS,
          it
        )
      }

  override fun remove() {
    val propertiesComponent = PropertiesComponent.getInstance()

    // todo: clean up  legacy properties
    propertiesComponent.unsetValue(DOKI_STICKER_PROP)
    propertiesComponent.unsetValue(PREVIOUS_STICKER)


    propertiesComponent.unsetValue(DOKI_BACKGROUND_PROP)
    repaintWindows()
  }

  private fun repaintWindows() = runSafely({
    IdeBackgroundUtil.repaintAllWindows()
  })

}

private fun setBackgroundImageProperty(
  imagePath: String,
  opacity: String,
  fill: String,
  anchor: String,
  propertyKey: String
) {
  // org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
  // as to why this looks this way
  val propertyValue = listOf(imagePath, opacity, fill, anchor)
    .reduceRight { a, b -> "$a,$b" }
  setPropertyValue(propertyKey, propertyValue)
}

private fun setPropertyValue(propertyKey: String, propertyValue: String) {
  PropertiesComponent.getInstance().unsetValue(propertyKey)
  PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
}

object StickerPanelService {

  fun installSticker(stickerUrl: String) {
    Toolkit.getDefaultToolkit().addAWTEventListener({ awtEvent ->
      when (awtEvent.id) {
        WindowEvent.WINDOW_OPENED -> {
          val source = awtEvent.source
          println(source)
        }
      }
    }, AWTEvent.WINDOW_EVENT_MASK)
    ProjectManager.getInstance().openProjects.forEach { project ->
      val ideFrame = getIDEFrame(project)
      UIUtil.getRootPane(
        ideFrame.component
      )?.layeredPane.toOptional()
        .ifPresent { layeredPane ->
          StickerPane(layeredPane, stickerUrl)
        }
    }
  }

  private fun getIDEFrame(project: Project) =
    (
      WindowManager.getInstance().getIdeFrame(project)
        ?: WindowManager.getInstance().allProjectFrames.first()
      )

}

class StickerPane(
  private val drawablePane: JLayeredPane,
  private val stickerUrl: String,
) : HwFacadeJPanel() {

  private val memeDisplay: JComponent

  companion object {
    private const val NOTIFICATION_Y_OFFSET = 10
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

  init {
    isOpaque = false
    layout = null

    val (memeContent, memeDisplay) = createMemeContentPanel()
    this.memeDisplay = memeDisplay
    add(memeContent)
    this.size = memeContent.size

    positionMemePanel(
      memeContent.size.width,
      memeContent.size.height,
    )


    drawablePane.add(this)
    drawablePane.setLayer(this, JBLayeredPane.DEFAULT_LAYER)

    drawablePane.addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        val layer = e.component
        if (layer !is JBLayeredPane) return

        val deltaX = layer.width - parentX
        val deltaY = layer.height - parentY
        setLocation(x + deltaX, y + deltaY)
        parentX = layer.width
        parentY = layer.height
      }
    })

    addMouseListener(object : MouseListener {
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

    })

    addMouseMotionListener(object : MouseMotionListener {
      override fun mouseDragged(e: MouseEvent) {
        val deltaX = e.xOnScreen - screenX
        val deltaY = e.yOnScreen - screenY

        setLocation(myX + deltaX, myY + deltaY)
      }

      override fun mouseMoved(e: MouseEvent?) {}
    })
  }

  private fun createMemeContentPanel(): Pair<JComponent, JComponent> {
    val memeContent = JPanel()
    memeContent.layout = null
    @Language("html")
    val memeDisplay = JBLabel(
      """<html>
           <img src='${stickerUrl}' />
         </html>
      """
    )
    val memeSize = memeDisplay.preferredSize
    memeContent.size = Dimension(
      memeSize.width,
      memeSize.height,
    )
    memeContent.isOpaque = false
    memeContent.add(memeDisplay)
    val parentInsets = memeDisplay.insets
    memeDisplay.setBounds(
      parentInsets.left,
      parentInsets.top,
      memeSize.width,
      memeSize.height
    )

    return memeContent to memeDisplay
  }

  private fun positionMemePanel(width: Int, height: Int) {
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
    memePanelBoundingBox: Rectangle,
  ): Pair<Int, Int> =
    parentWidth - memePanelBoundingBox.width - NOTIFICATION_Y_OFFSET to
      parentHeight - memePanelBoundingBox.height - NOTIFICATION_Y_OFFSET
}