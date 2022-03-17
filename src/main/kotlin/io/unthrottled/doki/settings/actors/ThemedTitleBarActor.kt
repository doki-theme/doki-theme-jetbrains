package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle

object ThemedTitleBarActor {
  fun enableThemedTitleBar(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isThemedTitleBar) {
      ThemeConfig.instance.isThemedTitleBar = enabled
      UpdateNotification.showDokiNotification(
        MessageBundle.getMessage("notification.restart.title"),
        MessageBundle.getMessage("notification.restart.body"),
      )
    }
  }
}
