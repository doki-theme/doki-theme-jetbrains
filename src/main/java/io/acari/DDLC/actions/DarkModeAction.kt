package io.acari.DDLC.actions

import com.chrisrm.idea.MTConfig
import com.chrisrm.idea.DDLCThemes
import io.acari.DDLC.actions.themes.literature.club.BaseThemeAction
import io.acari.DDLC.actions.themes.literature.club.JustMonikaThemeAction
import io.acari.DDLC.actions.themes.literature.club.NatsukiThemeAction
import io.acari.DDLC.actions.themes.literature.club.SayoriThemeAction
import io.acari.DDLC.actions.themes.literature.club.YuriThemeAction
import com.intellij.openapi.actionSystem.AnActionEvent

private val JustMonikaThemeAction = JustMonikaThemeAction()
private val SayoriThemeAction = SayoriThemeAction()
private val NatsukiThemeAction = NatsukiThemeAction()
private val YuriThemeAction = YuriThemeAction()

/**
 * Forged in the flames of battle by alex.
 */
class DarkModeAction : BaseThemeAction() {

    override fun isSelected(e: AnActionEvent) = DarkMode.isOn()

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        DarkMode.toggle()
        when (MTConfig.getInstance().getSelectedTheme()) {
            DDLCThemes.MONIKA -> JustMonikaThemeAction.setSelected(e, state)
            DDLCThemes.SAYORI -> SayoriThemeAction.setSelected(e, state)
            DDLCThemes.NATSUKI -> NatsukiThemeAction.setSelected(e, state)
            DDLCThemes.YURI -> YuriThemeAction.setSelected(e, state)
        }
    }


}