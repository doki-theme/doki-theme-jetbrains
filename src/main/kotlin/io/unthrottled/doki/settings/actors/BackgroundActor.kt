package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.stickers.BackgroundWallpaperService

object BackgroundActor {
  fun handleBackgroundUpdate(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isDokiBackground) {
      ThemeConfig.instance.isDokiBackground = enabled
      if (enabled) {
        BackgroundWallpaperService.instance.enableEditorBackground()
        UpdateNotification.showNotificationAcrossProjects(
          "Themed wallpaper set!",
          """
            IMPORTANT! Background will remain after uninstalling the plugin
            You can edit/remove the background using the "Set Background Image" action.
          """.trimIndent()
        )
      } else {
        BackgroundWallpaperService.instance.removeEditorBackground()
      }
    }
  }
}
