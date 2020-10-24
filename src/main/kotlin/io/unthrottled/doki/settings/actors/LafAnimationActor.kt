package io.unthrottled.doki.settings.actors

import com.intellij.openapi.util.registry.Registry
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification

object LafAnimationActor {
  fun enableAnimation(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isLafAnimation) {
      ThemeConfig.instance.isLafAnimation = enabled
      Registry.get("ide.intellij.laf.enable.animation").setValue(enabled)
      if (enabled) {
        UpdateNotification.displayAnimationInstallMessage()
      }
    }
  }
}
