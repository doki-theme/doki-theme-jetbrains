package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.laf.FileScopeColors
import io.unthrottled.doki.notification.UpdateNotification

object FileColorActor {
  fun enableFileColors(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isDokiFileColors) {
      ThemeConfig.instance.isDokiFileColors = enabled
      if (enabled) {
        FileScopeColors.addColors()
        UpdateNotification.displayFileColorInstallMessage()
      } else {
        FileScopeColors.removeColors()
      }
    }
  }
}
