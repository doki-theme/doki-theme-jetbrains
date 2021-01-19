package io.unthrottled.doki.settings.actors

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
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
            IMPORTANT! The background will remain after uninstalling the plugin
            You can edit/remove the background using the "Set Background Image" action.
          """.trimIndent(),
          actions = listOf {
            object : NotificationAction("Show settings") {
              override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                ActionManager.getInstance().getAction("Images.SetBackgroundImage")?.actionPerformed(e)
              }
            }
          }
        )
      } else {
        BackgroundWallpaperService.instance.removeEditorBackground()
      }
    }
  }
}
