package io.unthrottled.doki.discreet

import com.intellij.openapi.application.ApplicationManager

class ApplicationDiscreetModeListener : AbstractDiscreetModeListener() {
  override fun dispatchExtraEvents() {
    ApplicationManager.getApplication().messageBus
      .syncPublisher(IntegratedDiscreetModeListener.INTEGRATED_DISCREET_MODE_TOPIC)
      .modeChanged(currentMode)
  }
}
