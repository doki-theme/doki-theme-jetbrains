package io.acari.DDLC

import io.acari.DDLC.themes.AnthroTheme
import io.acari.DDLC.themes.anthro.NeeraTheme
import io.acari.DDLC.themes.anthro.SanyaTheme
import io.acari.DDLC.themes.anthro.WylaTheme
import java.util.*

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
  val NEERA: AnthroTheme = NeeraTheme()
  val SANYA: AnthroTheme = SanyaTheme()
  val WYLA: AnthroTheme = WylaTheme()

  fun getAllThemes(): List<DDLCThemeFacade> = listOf(
      NEERA,
      SANYA,
      WYLA
  )

}


