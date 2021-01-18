package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.BackgroundActor

class BackgroundToggleAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isDokiBackground

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    BackgroundActor.handleBackgroundUpdate(state)
  }
}
