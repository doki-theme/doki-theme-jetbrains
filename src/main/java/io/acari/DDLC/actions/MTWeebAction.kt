package io.acari.DDLC.actions

import io.acari.DDLC.actions.themes.literature.club.BaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MTWeebAction : BaseThemeAction() {

    override fun isSelected(e: AnActionEvent): Boolean =
            ClubMemberOrchestrator.weebShitOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        super.setSelected(e, state)
        ClubMemberOrchestrator.toggleWeebShit()
    }
}
