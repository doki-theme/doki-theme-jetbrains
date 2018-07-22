package com.chrisrm.idea.actions

import com.chrisrm.idea.MTConfig
import com.chrisrm.idea.MTThemes
import com.chrisrm.idea.actions.themes.MTBaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Forged in the flames of battle by alex.
 */
class JoyAction: MTBaseThemeAction() {
    override fun isSelected(e: AnActionEvent) = JoyManager.isOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        JoyManager.toggle()
        val selectedTheme = MTConfig.getInstance().getSelectedTheme() as MTThemes
        ClubMemberOrchestrator.activate(selectedTheme)
    }
}