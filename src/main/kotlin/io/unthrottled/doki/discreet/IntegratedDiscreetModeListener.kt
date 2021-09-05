package io.unthrottled.doki.discreet

import com.intellij.util.messages.Topic
import java.util.EventListener

fun interface IntegratedDiscreetModeListener : EventListener {
  companion object {
    val INTEGRATED_DISCREET_MODE_TOPIC: Topic<IntegratedDiscreetModeListener> =
      Topic(IntegratedDiscreetModeListener::class.java)
  }

  fun modeChanged(discreetMode: DiscreetMode)
}
