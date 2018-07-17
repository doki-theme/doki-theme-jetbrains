package com.chrisrm.idea.actions

import com.chrisrm.idea.MTConfig
import com.chrisrm.idea.MTThemes
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

/**
 * Forged in the flames of battle by alex.
 */
class JoyAction: ToggleAction("Some Joy") {
    override fun isSelected(e: AnActionEvent?) = JoyManager.isOn()

    // todo: will need to integrate with the new stuff on master.
    override fun setSelected(e: AnActionEvent?, state: Boolean) {
        JoyManager.toggle()
        val selectedTheme = MTConfig.getInstance().getSelectedTheme() as? MTThemes
        ClubMemberManager.getInstance().activate(selectedTheme)
    }

}