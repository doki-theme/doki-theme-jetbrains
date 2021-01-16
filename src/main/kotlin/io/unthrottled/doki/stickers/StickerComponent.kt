package io.unthrottled.doki.stickers

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import javax.swing.UIManager

class StickerComponent :
  LafManagerListener,
  Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    StickerPaneService.instance.init()
    processLaf(LafManagerImpl.getInstance().currentLookAndFeel)
    connection.subscribe(LafManagerListener.TOPIC, this)
  }

  companion object {
    fun activateForTheme(dokiTheme: DokiTheme) {
      BackgroundWallpaperService.instance.activateForTheme(dokiTheme)
      StickerPaneService.instance.activateForTheme(dokiTheme)
    }

    fun remove() {
      BackgroundWallpaperService.instance.remove()
      StickerPaneService.instance.remove()
    }
  }

  override fun lookAndFeelChanged(source: LafManager) =
    processLaf(source.currentLookAndFeel)

  override fun dispose() {
    connection.dispose()
  }

  private fun processLaf(currentLaf: UIManager.LookAndFeelInfo?) {
    ThemeManager.instance.processLaf(currentLaf)
      .filter { ThemeConfig.instance.currentStickerLevel == StickerLevel.ON }
      .doOrElse({
        activateForTheme(it)
      }) {
        remove()
      }
  }
}
