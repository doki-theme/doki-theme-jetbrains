package io.acari.DDLC.actions

import com.intellij.openapi.actionSystem.AnActionEvent

class LightModeAction : DarkModeAction() {
    override fun isSelected(e: AnActionEvent): Boolean = !super.isSelected(e)
}