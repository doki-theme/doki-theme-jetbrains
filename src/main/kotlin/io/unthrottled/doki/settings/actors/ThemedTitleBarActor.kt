package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification

object ThemedTitleBarActor {
  fun enableThemedTitleBar(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isThemedTitleBar) {
      ThemeConfig.instance.isThemedTitleBar = enabled
      UpdateNotification.displayRestartMessage()
    }
  }
}
