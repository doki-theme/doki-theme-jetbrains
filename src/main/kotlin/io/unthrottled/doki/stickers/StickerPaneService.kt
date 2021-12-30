package io.unthrottled.doki.stickers

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.DialogWrapperDialog
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.CustomStickerService.getCustomStickerUrl
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toOptional
import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.WindowEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JFrame

enum class StickerType {
  REGULAR, SMOL
}

private data class StickerBundle(
  val stickerPane: StickerPane,
  val stickerType: StickerType,
)

class StickerPaneService {

  companion object {
    val instance: StickerPaneService
      get() = ApplicationManager.getApplication().getService(StickerPaneService::class.java)
  }

  private val windowsToAddStickersTo = ConcurrentHashMap<Any, StickerBundle>()
  private lateinit var currentTheme: DokiTheme

  init {
    Toolkit.getDefaultToolkit().addAWTEventListener(
      { awtEvent ->
        when (awtEvent.id) {
          WindowEvent.WINDOW_OPENED -> {
            when (val window = awtEvent.source) {
              is JFrame -> captureFrame(window)
              is DialogWrapperDialog -> captureDialogWrapper(window)
            }
          }
          WindowEvent.WINDOW_CLOSED -> {
            when (val window = awtEvent.source) {
              is JFrame -> disposeFrame(window)
              is DialogWrapper -> disposeFrame(window)
            }
          }
        }
      },
      AWTEvent.WINDOW_EVENT_MASK
    )
  }

  fun init() {}

  fun activateForTheme(dokiTheme: DokiTheme) {
    currentTheme = dokiTheme

    val primaryStickersOn = ThemeConfig.instance.currentStickerLevel == StickerLevel.ON
    val smolStickersOn = ThemeConfig.instance.showSmallStickers
    if (primaryStickersOn || smolStickersOn) {
      val (candidateStickers, detatchableStickers) = stickers.partition {
        (it.stickerType == StickerType.SMOL && smolStickersOn) ||
          (it.stickerType == StickerType.REGULAR && primaryStickersOn)
      }
      displayStickers({ stickerUrl ->
        candidateStickers.forEach { it.stickerPane.displaySticker(stickerUrl) }
        detatchableStickers.forEach { it.stickerPane.detach() }
      }) {
        stickers.forEach { it.stickerPane.detach() }
      }
    }
  }

  fun setStickerPositioning(shouldPosition: Boolean) {
    stickers.forEach { it.stickerPane.positionable = shouldPosition }
  }

  private fun disposeFrame(window: Any) {
    windowsToAddStickersTo[window].toOptional()
      .ifPresent {
        it.stickerPane.dispose()
        windowsToAddStickersTo.remove(window)
      }
  }

  private val allowedFrames = setOf(
    "com.intellij.openapi.ui.FrameWrapper\$MyJFrame",
    "com.intellij.openapi.wm.impl.IdeFrameImpl",
  )

  private fun captureFrame(window: JFrame) {
    if (isRightClass(window)) return
    val drawablePane = window.rootPane.layeredPane
    val stickerPane = StickerPane(drawablePane)
    windowsToAddStickersTo[window] = StickerBundle(stickerPane, StickerType.REGULAR)
    if (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON) {
      showSingleSticker(stickerPane)
    }
  }

  private fun captureDialogWrapper(wrapper: DialogWrapperDialog) {
    val drawablePane = wrapper.dialogWrapper.rootPane.layeredPane
    val stickerPane = StickerPane(drawablePane)
    windowsToAddStickersTo[wrapper] = StickerBundle(stickerPane, StickerType.SMOL)
    if (ThemeConfig.instance.showSmallStickers) {
      showSingleSticker(stickerPane)
    }
  }

  private fun showSingleSticker(stickerPane: StickerPane) {
    displayStickers({ stickerUrl ->
      stickerPane.displaySticker(stickerUrl)
    }) {
      stickerPane.detach()
    }
  }

  private fun isRightClass(window: Any): Boolean {
    if (allowedFrames.contains(window.javaClass.name).not()) return true
    return false
  }

  private fun displayStickers(
    stickerWorkerSupplier: (String) -> Unit,
    stickerRemoval: () -> Unit,
  ) {
    if (this::currentTheme.isInitialized.not()) return

    ApplicationManager.getApplication().executeOnPooledThread {
      if (CustomStickerService.isCustomStickers) {
        getCustomStickerUrl()
      } else {
        getLocallyInstalledStickerPath(currentTheme)
      }.doOrElse({ stickerUrl ->
        stickerWorkerSupplier(stickerUrl)
      }) {
        ApplicationManager.getApplication()
          .invokeLater { stickerRemoval() }
      }
    }
  }

  private val stickers: List<StickerBundle>
    get() = windowsToAddStickersTo.entries.map { it.value }

  fun remove() {
    ApplicationManager.getApplication().invokeLater {
      stickers.forEach { it.stickerPane.detach() }
      repaintWindows() // removes sticker residue (see https://github.com/doki-theme/doki-theme-jetbrains/issues/362)
    }
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

  fun checkForUpdates(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      getLocallyInstalledStickerPath(dokiTheme)
    }
  }
}

fun repaintWindows() = runSafely({
  ApplicationManager.getApplication().invokeLater {
    IdeBackgroundUtil.repaintAllWindows()
  }
})
