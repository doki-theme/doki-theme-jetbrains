package io.acari.DDLC.actions

import io.acari.DDLC.DDLCThemes
import io.acari.DDLC.actions.themes.literature.club.BaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.DDLC.chibi.ChibiOrchestrator
import io.acari.DDLC.DDLCConfig

/**
 * Forged in the flames of battle by alex.
 */
class JoyAction: BaseThemeAction() {
    override fun isSelected(e: AnActionEvent): Boolean = JoyManager.isOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        super.setSelected(e, state)
        JoyManager.toggle()
        val selectedTheme = DDLCConfig.getInstance().getSelectedTheme() as DDLCThemes
        ChibiOrchestrator.activateChibiForTheme(selectedTheme)
    }
}