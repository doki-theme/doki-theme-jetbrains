package io.acari.DDLC

import io.acari.DDLC.themes.AnthroTheme
import io.acari.DDLC.themes.anthro.NeeraTheme
import io.acari.DDLC.themes.anthro.SanyaTheme
import java.util.*

//NEERA,
//SANYA,
//SYRENA,
//FEN,
//ELENIEL,
//CLEO,
//WYLA,
//ZAHRA,
//JESS,

object AnthroThemes {
  val NEERA: AnthroTheme = NeeraTheme()
  val SANYA: AnthroTheme = SanyaTheme()

  fun getAllThemes(): List<DDLCThemeFacade> = listOf(
      NEERA,
      SANYA
  )

}


