package io.unthrottled.doki.stickers

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional

@Service
class StickerComponent :
  LafManagerListener,
  Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    StickerPaneService.getInstance().init()
    initializeTheme()
    connection.subscribe(LafManagerListener.TOPIC, this)
  }

  private fun initializeTheme() {
    LafManager.getInstance()?.currentUIThemeLookAndFeel.toOptional()
      .ifPresent { currentLaf ->
        ThemeManager.getInstance().processLaf(
          currentLaf,
        ).doOrElse({
          processLaf(currentLaf) // is doki theme
        }) {
          // allow custom stickers to show up
          if (CustomStickerService.isCustomStickers) {
            StickerPaneService.getInstance().activateForTheme(
              ThemeManager.getInstance().defaultTheme,
            )
          }
        }
      }
  }

  companion object {
    fun getInstance(): StickerComponent = ApplicationManager.getApplication().getService(StickerComponent::class.java)

    fun activateForTheme(dokiTheme: DokiTheme) {
      if (ThemeConfig.getInstance().discreetMode) return

      EditorBackgroundWallpaperService.getInstance().activateForTheme(dokiTheme)
      EmptyFrameWallpaperService.getInstance().activateForTheme(dokiTheme)
      StickerPaneService.getInstance().activateForTheme(dokiTheme)
    }

    fun remove() {
      if (ThemeConfig.getInstance().discreetMode) return

      EditorBackgroundWallpaperService.getInstance().remove()
      EmptyFrameWallpaperService.getInstance().remove()

      if (CustomStickerService.isCustomStickers) return
      StickerPaneService.getInstance().remove(StickerType.ALL)
    }
  }

  override fun lookAndFeelChanged(source: LafManager) = processLaf(source.currentUIThemeLookAndFeel)

  override fun dispose() {
    connection.dispose()
  }

  private fun processLaf(currentLaf: UIThemeLookAndFeelInfo) {
    ThemeManager.getInstance().processLaf(currentLaf)
      .doOrElse({
        activateForTheme(it)
      }) {
        remove()
      }
  }

  fun init() {
  }
}
