package com.chrisrm.idea.actions

import com.chrisrm.idea.MTConfig
import com.chrisrm.idea.MTThemes
import com.chrisrm.idea.actions.themes.BaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.DDLC.chibi.ChibiOrchestrator

/**
 * Forged in the flames of battle by alex.
 */
class JoyAction: BaseThemeAction() {
    override fun isSelected(e: AnActionEvent): Boolean = JoyManager.isOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        super.setSelected(e, state)
        JoyManager.toggle()
        val selectedTheme = MTConfig.getInstance().getSelectedTheme() as MTThemes
        ChibiOrchestrator.activate(selectedTheme)
    }
}