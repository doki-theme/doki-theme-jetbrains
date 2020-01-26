package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.MaterialIconsActor

class MaterialPSIconsAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isMaterialPSIIcons

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      MaterialIconsActor.enablePSIIcons(state)
  }
}