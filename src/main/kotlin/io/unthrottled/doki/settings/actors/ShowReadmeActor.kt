package io.unthrottled.doki.settings.actors

import com.intellij.openapi.util.registry.Registry
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle

object ShowReadmeActor {
  fun dontShowReadmeOnStartup(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isNotShowReadmeAtStartup) {
      ThemeConfig.instance.isNotShowReadmeAtStartup = enabled
      Registry.get("ide.open.readme.md.on.startup").setValue(!enabled)
      if (enabled) {
        UpdateNotification.showDokiNotification(
          MessageBundle.message("notification.no.show.readme.title"),
          MessageBundle.message("notification.no.show.readme.body")
        )
      }
    }
  }
}
