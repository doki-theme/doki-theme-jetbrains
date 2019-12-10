package io.acari.DDLC

import io.acari.DDLC.themes.AnthroTheme
import io.acari.DDLC.themes.anthro.*

//ELENIEL,
//CLEO,
//NEERA,
//SANYA,
//SYRENA,
//WYLA,

//ZAHRA,

object AnthroThemes {
  val CLEO: AnthroTheme = CleoTheme()
  val ELENIEL: AnthroTheme = ElenielTheme()
  val NEERA: AnthroTheme = NeeraTheme()
  val SANYA: AnthroTheme = SanyaTheme()
  val SYRENA: AnthroTheme = SyrenaTheme()
  val WYLA: AnthroTheme = WylaTheme()

  fun getAllThemes(): List<DDLCThemeFacade> = listOf(
      CLEO,
      ELENIEL,
      NEERA,
      SANYA,
      SYRENA,
      WYLA
  )

}


