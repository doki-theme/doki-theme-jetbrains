package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.FileColorActor
import io.acari.doki.settings.actors.LafAnimationActor

class ThemeAnimationToggle : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isLafAnimation

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      LafAnimationActor.enableAnimation(state)
  }
}