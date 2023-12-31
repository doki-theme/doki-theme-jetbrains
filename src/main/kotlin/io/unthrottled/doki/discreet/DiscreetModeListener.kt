package io.unthrottled.doki.discreet

import com.intellij.util.messages.Topic
import java.util.EventListener

enum class DiscreetMode {
  ACTIVE,
  INACTIVE,
}

fun Boolean.toDiscreetMode(): DiscreetMode = if (this) DiscreetMode.ACTIVE else DiscreetMode.INACTIVE

fun interface DiscreetModeListener : EventListener {
  companion object {
    val DISCREET_MODE_TOPIC: Topic<DiscreetModeListener> =
      Topic(DiscreetModeListener::class.java)
  }

  fun modeChanged(discreetMode: DiscreetMode)
}
