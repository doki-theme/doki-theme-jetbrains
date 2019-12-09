package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.ThemedTitleBarActor

class ThemedTitleBarAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isThemedTitleBar

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    ThemedTitleBarActor.enableThemedTitleBar(state)
  }
}