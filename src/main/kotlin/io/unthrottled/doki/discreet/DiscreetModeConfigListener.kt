package io.unthrottled.doki.discreet

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.config.ThemeConfigListener

class DiscreetModeConfigListener : ThemeConfigListener {
  private var currentMode = ThemeConfig.instance.discreetMode.toDiscreetMode()

  override fun themeConfigUpdated(themeConfig: ThemeConfig) {
    val discreetMode = themeConfig.discreetMode.toDiscreetMode()
    if (discreetMode != currentMode) {
      ApplicationManager.getApplication().messageBus.syncPublisher(DiscreetModeListener.DISCREET_MODE_TOPIC)
        .modeChanged(discreetMode)
    }
  }
}
