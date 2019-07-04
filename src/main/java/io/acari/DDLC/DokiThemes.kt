package io.acari.DDLC

import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

object DokiThemes {
  private val themes: Map<String, DDLCThemeFacade>

  init {
    val dokiDokiLiteratureClubThemes = DDLCThemes.getAllThemes()
    val anthroThemes = AnthroThemes.getAllThemes()
    themes = Stream.concat(dokiDokiLiteratureClubThemes.stream(), anthroThemes.stream())
        .collect(Collectors.toMap({ it.name.toUpperCase() }, { it }, { a, b -> a }))
  }

  @JvmStatic
  fun getThemeById(themeId: String): DDLCThemeFacade = themes.getOrDefault(themeId.toUpperCase(), DDLCThemes.MONIKA)

}