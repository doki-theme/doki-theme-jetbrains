package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.EmptyFrameWallpaperService

object EmptyFrameBackgroundActor {
  fun handleBackgroundUpdate(enabled: Boolean) {
    if (enabled != ThemeConfig.getInstance().isEmptyFrameBackground) {
      ThemeConfig.getInstance().isEmptyFrameBackground = enabled
      if (enabled) {
        EmptyFrameWallpaperService.getInstance().enableEmptyFrameWallpaper()
      } else {
        EmptyFrameWallpaperService.getInstance().remove()
      }
    }
  }
}
