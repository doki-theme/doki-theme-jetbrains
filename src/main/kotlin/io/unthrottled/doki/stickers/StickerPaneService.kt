package io.unthrottled.doki.stickers

import com.intellij.openapi.application.ApplicationManager
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
import java.awt.Component
import java.awt.Toolkit
import java.awt.event.WindowEvent
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JFrame

class StickerPaneService {

  companion object {
    val instance: StickerPaneService
      get() = ApplicationManager.getApplication().getService(StickerPaneService::class.java)
  }

  private val windowsToAddStickersTo = ConcurrentHashMap<Component, StickerPane>()
  private lateinit var currentTheme: DokiTheme

  init {
    Toolkit.getDefaultToolkit().addAWTEventListener(
      { awtEvent ->
        when (awtEvent.id) {
          WindowEvent.WINDOW_OPENED -> {
            when (val window = awtEvent.source) {
              is JFrame -> captureFrame(window)
            }
          }
          WindowEvent.WINDOW_CLOSED -> {
            when (val window = awtEvent.source) {
              is JFrame -> disposeFrame(window)
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

    if (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON) {
      displayStickers({ stickerUrl ->
        {
          stickers.forEach { it.displaySticker(stickerUrl) }
        }
      }) {
        stickers.forEach { it.detach() }
      }
    }
  }

  fun setStickerPositioning(shouldPosition: Boolean) {
    stickers.forEach { it.positionable = shouldPosition }
  }

  private fun disposeFrame(window: JFrame) {
    windowsToAddStickersTo[window].toOptional()
      .ifPresent {
        it.dispose()
        windowsToAddStickersTo.remove(window)
      }
  }

  private val allowedFrames = setOf(
    "com.intellij.openapi.ui.FrameWrapper\$MyJFrame",
    "com.intellij.openapi.wm.impl.IdeFrameImpl"
  )

  private fun captureFrame(window: JFrame) {
    if (allowedFrames.contains(window.javaClass.name).not()) return

    val stickerPane = StickerPane(window.rootPane.layeredPane)
    windowsToAddStickersTo[window] =
      stickerPane

    if (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON) {
      displayStickers({ stickerUrl ->
        { stickerPane.displaySticker(stickerUrl) }
      }) {
        stickerPane.detach()
      }
    }
  }

  private fun displayStickers(
    stickerWorkerSupplier: (String) -> () -> Unit,
    stickerRemoval: () -> Unit,
  ) {
    if (this::currentTheme.isInitialized.not()) return

    ApplicationManager.getApplication().executeOnPooledThread {
      if (CustomStickerService.isCustomStickers) {
        getCustomStickerUrl()
      } else {
        getLocallyInstalledStickerPath(currentTheme)
      }.doOrElse({ stickerUrl ->
        ApplicationManager.getApplication()
          .invokeLater(stickerWorkerSupplier(stickerUrl))
      }) {
        ApplicationManager.getApplication()
          .invokeLater { stickerRemoval() }
      }
    }
  }

  private val stickers: List<StickerPane>
    get() = windowsToAddStickersTo.entries.map { it.value }

  fun remove() {
    ApplicationManager.getApplication().invokeLater {
      stickers.forEach { it.detach() }
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
