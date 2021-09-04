package io.unthrottled.doki.discreet.integrations.amii

import io.unthrottled.amii.discreet.DiscreetMode
import io.unthrottled.amii.discreet.IntegratedDiscreetModeListener
import io.unthrottled.doki.discreet.AbstractDiscreetModeListener

class AMIIIntegratedDiscreetModeListener :
  IntegratedDiscreetModeListener,
  AbstractDiscreetModeListener() {

  override fun dispatchExtraEvents() {
  }

  override fun modeChanged(discreetMode: DiscreetMode) {
    this.modeChanged(
      when (discreetMode) {
        DiscreetMode.ACTIVE -> io.unthrottled.doki.discreet.DiscreetMode.ACTIVE
        DiscreetMode.INACTIVE -> io.unthrottled.doki.discreet.DiscreetMode.INACTIVE
      }
    )
  }
}
