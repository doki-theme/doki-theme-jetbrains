package io.acari.doki.util

object LegacyThemeUtilities {

  private val legacyThemes = mapOf(
    "monika" to "Monika Light",
    "just monika" to "Monika Dark",
    "natsuki" to "Natsuki Light",
    "only play with me" to "Natsuki Dark",
    "sayori" to "Sayori Light",
    "deleted character" to "Sayori Dark",
    "yuri" to "Yuri Light",
    "edgy" to "Yuri Dark"
  )

  fun legacyThemeNameMapping(oldThemeName: String) : String {
    val lowercaseThemeName = oldThemeName.toLowerCase()
    val legacyTheme = legacyThemes[lowercaseThemeName]
    return legacyTheme ?: lowercaseThemeName
  }
}