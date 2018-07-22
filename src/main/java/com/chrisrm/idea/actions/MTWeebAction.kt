package com.chrisrm.idea.actions

import com.chrisrm.idea.actions.themes.MTBaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MTWeebAction : MTBaseThemeAction() {

    override fun isSelected(e: AnActionEvent): Boolean =
            ClubMemberOrchestrator.weebShitOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        ClubMemberOrchestrator.toggleWeebShit()
    }
}
