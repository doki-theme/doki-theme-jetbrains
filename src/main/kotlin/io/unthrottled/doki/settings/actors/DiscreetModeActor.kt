package io.unthrottled.doki.settings.actors

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.discreet.DiscreetMode
import io.unthrottled.doki.discreet.DiscreetModeListener
import io.unthrottled.doki.discreet.toDiscreetMode

object DiscreetModeActor {
  fun enableDiscreetMode(discreetMode: Boolean) {
    if (ThemeConfig.instance.discreetMode != discreetMode) {
      ThemeConfig.instance.discreetMode = discreetMode
      dispatchDiscreetMode(discreetMode.toDiscreetMode())
    }
  }

  fun dispatchDiscreetMode(discreetMode: DiscreetMode) {
    ApplicationManager.getApplication().invokeLater {
      ApplicationManager.getApplication().messageBus
        .syncPublisher(DiscreetModeListener.DISCREET_MODE_TOPIC)
        .modeChanged(discreetMode)
    }
  }
}
