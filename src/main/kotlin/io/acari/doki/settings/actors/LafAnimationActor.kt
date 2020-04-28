package io.acari.doki.settings.actors

import com.intellij.openapi.util.registry.Registry
import io.acari.doki.config.ThemeConfig
import io.acari.doki.notification.UpdateNotification

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