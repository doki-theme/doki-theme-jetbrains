package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.FileColorActor

class FileColorToggleAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isDokiFileColors

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    FileColorActor.enableFileColors(state)
  }
}