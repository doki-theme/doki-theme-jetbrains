package io.acari.DDLC.schemes

import io.acari.DDLC.AnthroThemes.CLEO
import io.acari.DDLC.AnthroThemes.ELENIEL
import io.acari.DDLC.AnthroThemes.NEERA
import io.acari.DDLC.AnthroThemes.SANYA
import io.acari.DDLC.AnthroThemes.SYRENA
import io.acari.DDLC.AnthroThemes.WYLA
import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.DDLCThemes
import io.acari.DDLC.actions.themes.anthro.*
import io.acari.DDLC.actions.themes.literature.club.JustMonikaThemeAction
import io.acari.DDLC.actions.themes.literature.club.NatsukiThemeAction
import io.acari.DDLC.actions.themes.literature.club.SayoriThemeAction
import io.acari.DDLC.actions.themes.literature.club.YuriThemeAction

object DokiConfigChangedActor {

  private val monikaAction: JustMonikaThemeAction = JustMonikaThemeAction()
  private val sayoriAction: SayoriThemeAction = SayoriThemeAction()
  private val natsukiThemeAction: NatsukiThemeAction = NatsukiThemeAction()
  private val yuriThemeAction: YuriThemeAction = YuriThemeAction()

  private val cleoThemeAction: CleoThemeAction = CleoThemeAction()
  private val elenielThemeAction: ElenielThemeAction = ElenielThemeAction()
  private val neeraThemeAction: NeeraThemeAction = NeeraThemeAction()
  private val sanyaThemeAction: SanyaThemeAction = SanyaThemeAction()
  private val syrenaThemeAction: SyrenaThemeAction = SyrenaThemeAction()
  private val wylaThemeAction: WylaThemeAction = WylaThemeAction()

  fun consumeDeltas(ddlcConfig: DDLCConfig) {
    activateClubMember(ddlcConfig.getSelectedTheme())
  }

  private fun activateClubMember(selectedTheme: DDLCThemeFacade) {
    when (selectedTheme) {
      DDLCThemes.MONIKA -> monikaAction.selectionActivation()
      DDLCThemes.SAYORI -> sayoriAction.selectionActivation()
      DDLCThemes.NATSUKI -> natsukiThemeAction.selectionActivation()
      DDLCThemes.YURI -> yuriThemeAction.selectionActivation()
      
      CLEO -> cleoThemeAction.selectionActivation()
      ELENIEL -> elenielThemeAction.selectionActivation()
      NEERA -> neeraThemeAction.selectionActivation()
      SANYA -> sanyaThemeAction.selectionActivation()
      SYRENA -> syrenaThemeAction.selectionActivation()
      WYLA -> wylaThemeAction.selectionActivation()
    }
  }
}