package io.acari.doki.settings.actors

import io.acari.doki.config.ThemeConfig
import io.acari.doki.notification.UpdateNotification

object ThemedTitleBarActor {
  fun enableThemedTitleBar(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isDokiFileColors) {
      ThemeConfig.instance.isThemedTitleBar = enabled
      UpdateNotification.displayRestartMessage()
    }
  }
}