package io.acari.DDLC

import io.acari.DDLC.themes.AnthroTheme
import io.acari.DDLC.themes.anthro.*

//NEERA,
//SANYA,
//WYLA,
//SYRENA,
//CLEO,
//FEN,
//ELENIEL,
//ZAHRA,
//JESS,

object AnthroThemes {
  val CLEO: AnthroTheme = CleoTheme()
  val NEERA: AnthroTheme = NeeraTheme()
  val SANYA: AnthroTheme = SanyaTheme()
  val SYRENA: AnthroTheme = SyrenaTheme()
  val WYLA: AnthroTheme = WylaTheme()

  fun getAllThemes(): List<DDLCThemeFacade> = listOf(
      CLEO,
      NEERA,
      SANYA,
      SYRENA,
      WYLA
  )

}


