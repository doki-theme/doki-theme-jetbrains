package io.acari.DDLC

import io.acari.DDLC.themes.AnthroTheme
import io.acari.DDLC.themes.anthro.NeeraTheme
import java.util.*

//NEERA,
//SANYA,
//SYRENA,
//FEN,
//ELENIEL,
//CLEO,
//WYLA,
//ZAHRA,

object AnthroThemes {
  val NEERA: AnthroTheme = NeeraTheme()

  fun getAllThemes(): List<DDLCThemeFacade> = listOf(
      NEERA
  )

}


