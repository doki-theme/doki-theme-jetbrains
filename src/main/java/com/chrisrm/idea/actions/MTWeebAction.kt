package com.chrisrm.idea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class MTWeebAction : ToggleAction("Some Weeb Shit.") {

    override fun isSelected(e: AnActionEvent): Boolean =
            ClubMemberOrchestrator.weebShitOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        ClubMemberOrchestrator.toggleWeebShit()
    }
}
