package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.LafAnimationActor

class ThemeAnimationToggle : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isLafAnimation

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    LafAnimationActor.enableAnimation(state)
  }
}