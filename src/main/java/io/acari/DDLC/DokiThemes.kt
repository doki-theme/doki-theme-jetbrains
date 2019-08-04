package io.acari.DDLC

import java.util.stream.Collectors
import java.util.stream.Stream

object DokiThemes {
  private val themes: Map<String, DDLCThemeFacade>
  private val themesByColorScheme: Map<String, DDLCThemeFacade>

  init {
    themes = getAllThemes()
        .collect(Collectors.toMap({ it.name.toUpperCase() }, { it }, { a, b -> a }))
    themesByColorScheme = getAllThemes()
        .collect(Collectors.toMap({ it.themeColorScheme.toUpperCase() }, { it }, { a, b -> a }))

  }

  private fun getAllThemes(): Stream<DDLCThemeFacade> {
    val dokiDokiLiteratureClubThemes = DDLCThemes.getAllThemes()
    val anthroThemes = AnthroThemes.getAllThemes()
    val themes = Stream.concat(dokiDokiLiteratureClubThemes.stream(), anthroThemes.stream())
    return themes
  }

  @JvmStatic
  fun getThemeById(themeId: String): DDLCThemeFacade = themes.getOrDefault(themeId.toUpperCase(), DDLCThemes.MONIKA)

  @JvmStatic
  fun getThemeByColorScheme(colorSchemes: String): DDLCThemeFacade = themesByColorScheme.getOrDefault(colorSchemes.toUpperCase(), DDLCThemes.MONIKA)

  @JvmStatic
  fun isDokiColorScheme(colorSchemes: String): Boolean = themesByColorScheme.containsKey(colorSchemes.toUpperCase())


}