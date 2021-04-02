package io.unthrottled.doki.stickers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import java.util.Optional

class EmptyFrameWallpaperService {

  companion object {
    val instance: EmptyFrameWallpaperService
      get() = ServiceManager.getService(EmptyFrameWallpaperService::class.java)
  }

  fun enableEmptyFrameWallpaper() {
    ThemeManager.instance.currentTheme.doOrElse({ dokiTheme ->
      installFrameBackgroundImage(dokiTheme)
      repaintWindows()
    }) {
      remove()
    }
  }

  fun activateForTheme(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      if (ThemeConfig.instance.isEmptyFrameBackground) {
        installFrameBackgroundImage(dokiTheme)
      }
    }
  }

  fun checkForUpdates(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      getLocallyInstalledBackgroundImagePath(dokiTheme)
    }
  }

  private fun installFrameBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledBackgroundImagePath(dokiTheme)
      .doOrElse({
        setBackgroundImageProperty(
          it.first,
          "100",
          IdeBackgroundUtil.Fill.SCALE.name,
          it.second.position.name,
          DOKI_BACKGROUND_PROP
        )
      }) {
        remove()
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

  fun remove() {
    val propertiesComponent = PropertiesComponent.getInstance()
    propertiesComponent.unsetValue(DOKI_BACKGROUND_PROP)
    repaintWindows()
  }
}
