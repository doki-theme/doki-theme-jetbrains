package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.discreet.toDiscreetMode
import io.unthrottled.doki.settings.actors.DiscreetModeActor

class DiscreetModeAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean = ThemeConfig.instance.discreetMode

  override fun setSelected(
    e: AnActionEvent,
    state: Boolean,
  ) {
    DiscreetModeActor.dispatchDiscreetMode(state.toDiscreetMode())
  }
}
