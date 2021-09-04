package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.discreet.DiscreetModeListener
import io.unthrottled.doki.discreet.discreetModeService
import io.unthrottled.doki.discreet.toDiscreetMode

class DiscreetModeAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    e.project?.discreetModeService()?.isDiscreetMode == true

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    ApplicationManager.getApplication().messageBus
      .syncPublisher(DiscreetModeListener.DISCREET_MODE_TOPIC)
      .modeChanged(state.toDiscreetMode())
  }
}
