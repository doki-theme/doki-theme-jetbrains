package io.acari.DDLC.schemes

import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.DDLCThemes
import io.acari.DDLC.actions.themes.literature.club.JustMonikaThemeAction
import io.acari.DDLC.actions.themes.literature.club.NatsukiThemeAction
import io.acari.DDLC.actions.themes.literature.club.SayoriThemeAction
import io.acari.DDLC.actions.themes.literature.club.YuriThemeAction

object DokiConfigChangedActor {

  private val monikaAction: JustMonikaThemeAction = JustMonikaThemeAction()
  private val sayoriAction: SayoriThemeAction = SayoriThemeAction()
  private val natsukiThemeAction: NatsukiThemeAction = NatsukiThemeAction()
  private val yuriThemeAction: YuriThemeAction = YuriThemeAction()

  fun consumeDeltas(ddlcConfig: DDLCConfig) {
    activateClubMember(ddlcConfig.getSelectedTheme())
  }

  private fun activateClubMember(selectedTheme: DDLCThemeFacade) {
    when (selectedTheme) {
      DDLCThemes.MONIKA -> monikaAction.selectionActivation()
      DDLCThemes.SAYORI -> sayoriAction.selectionActivation()
      DDLCThemes.NATSUKI -> natsukiThemeAction.selectionActivation()
      DDLCThemes.YURI -> yuriThemeAction.selectionActivation()
    }
  }
}