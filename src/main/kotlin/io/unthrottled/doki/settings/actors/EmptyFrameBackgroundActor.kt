package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.EmptyFrameWallpaperService

object EmptyFrameBackgroundActor {
  fun handleBackgroundUpdate(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isEmptyFrameBackground) {
      ThemeConfig.instance.isEmptyFrameBackground = enabled
      if (enabled) {
        EmptyFrameWallpaperService.instance.enableEmptyFrameWallpaper()
      } else {
        EmptyFrameWallpaperService.instance.remove()
      }
    }
  }
}
