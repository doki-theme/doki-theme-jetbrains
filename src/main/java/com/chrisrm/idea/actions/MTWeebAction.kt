package com.chrisrm.idea.actions

import com.chrisrm.idea.actions.themes.BaseThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.DDLC.chibi.ChibiOrchestrator

class MTWeebAction : BaseThemeAction() {

    override fun isSelected(e: AnActionEvent): Boolean =
            ChibiOrchestrator.weebShitOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        super.setSelected(e, state)
    }
}
