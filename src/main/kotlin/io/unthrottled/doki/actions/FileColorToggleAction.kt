package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.FileColorActor

class FileColorToggleAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isDokiFileColors

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    FileColorActor.enableFileColors(state)
  }
}
