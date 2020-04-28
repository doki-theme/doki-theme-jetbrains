package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.MaterialIconsActor

class MaterialFilesconsAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isMaterialFiles

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    MaterialIconsActor.enableFileIcons(state)
  }
}