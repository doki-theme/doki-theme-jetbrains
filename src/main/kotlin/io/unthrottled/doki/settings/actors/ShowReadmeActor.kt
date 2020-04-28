package io.unthrottled.doki.settings.actors

import com.intellij.openapi.util.registry.Registry
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification

object ShowReadmeActor {
  fun dontShowReadmeOnStartup(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isNotShowReadmeAtStartup) {
      ThemeConfig.instance.isNotShowReadmeAtStartup = enabled
      Registry.get("ide.open.readme.md.on.startup").setValue(!enabled)
      if (enabled) {
        UpdateNotification.displayReadmeInstallMessage()
      }
    }
  }
}