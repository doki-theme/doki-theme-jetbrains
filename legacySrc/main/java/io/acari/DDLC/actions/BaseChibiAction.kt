package io.acari.DDLC.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.DDLC.actions.themes.literature.club.BaseThemeAction
import io.acari.DDLC.chibi.ChibiLevel
import io.acari.DDLC.chibi.ChibiOrchestrator

open class BaseChibiAction(private val chibiLevel: ChibiLevel) : BaseThemeAction() {

    override fun isSelected(e: AnActionEvent): Boolean =
            ChibiOrchestrator.currentChibiLevel() == chibiLevel

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        super.setSelected(e, state)
        ChibiOrchestrator.setChibiLevel(chibiLevel)
    }
}