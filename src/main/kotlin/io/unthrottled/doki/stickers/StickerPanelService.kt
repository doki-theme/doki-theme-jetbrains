package io.unthrottled.doki.stickers

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.toOptional
import java.awt.AWTEvent
import java.awt.Component
import java.awt.Toolkit
import java.awt.event.WindowEvent
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JFrame

class StickerPanelService {

  companion object {
    val instance: StickerPanelService
      get() = ApplicationManager.getApplication().getService(StickerPanelService::class.java)
  }

  private val windowsToAddStickersTo = ConcurrentHashMap<Component, StickerPane>()
  private lateinit var currentTheme: DokiTheme

  init {
    Toolkit.getDefaultToolkit().addAWTEventListener({ awtEvent ->
      when (awtEvent.id) {
        WindowEvent.WINDOW_OPENED -> {
          when (val window = awtEvent.source) {
            is JFrame -> captureFrame(window)
          }
        }
        WindowEvent.WINDOW_CLOSING -> {
          when (val window = awtEvent.source) {
            is JFrame -> disposeFrame(window)
          }
        }
      }
    }, AWTEvent.WINDOW_EVENT_MASK)
  }

  fun init() {}

  fun activateForTheme(dokiTheme: DokiTheme) {
    currentTheme = dokiTheme
    displayStickers { stickerUrl ->
      {
        stickers.forEach { it.displaySticker(stickerUrl) }
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

    displayStickers { stickerUrl ->
      { stickerPane.displaySticker(stickerUrl) }
    }
  }

  private fun displayStickers(
    stickerWorkerSupplier: (String) -> () -> Unit
  ) {
    if (this::currentTheme.isInitialized.not()) return

    ApplicationManager.getApplication().executeOnPooledThread {
      getLocallyInstalledStickerPath(currentTheme).ifPresent { stickerUrl ->
        ApplicationManager.getApplication()
          .invokeLater(stickerWorkerSupplier(stickerUrl))
      }
    }
  }

  private val stickers: List<StickerPane>
    get() = windowsToAddStickersTo.entries.map { it.value }

  fun remove() {
    ApplicationManager.getApplication().invokeLater {
      stickers.forEach { it.detach() }
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
}