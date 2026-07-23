package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.EditorBackgroundWallpaperService

object BackgroundActor {
  fun handleBackgroundUpdate(enabled: Boolean) {
    if (enabled != ThemeConfig.getInstance().isDokiBackground) {
      ThemeConfig.getInstance().isDokiBackground = enabled
      if (enabled) {
        EditorBackgroundWallpaperService.getInstance().enableEditorBackground()
      } else {
        EditorBackgroundWallpaperService.getInstance().removeEditorBackground()
      }
    }
  }
}
