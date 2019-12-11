package io.acari.doki.util

object LegacyThemeUtilities {
  const val SAVED_STATE = "JOY_MODE_ON"
  const val DARK_MODE_PROP = "DARK_MODE_ON"

  private val legacyThemes = mapOf(
    "monika" to mapOf(true to "Monika Dark", false to "Monika Light"),
    "natsuki" to mapOf(true to "Natsuki Dark", false to "Natsuki Light"),
    "sayori" to mapOf(true to "Sayori Dark", false to "Sayori Light"),
    "yuri" to mapOf(true to "Yuri Dark", false to "Yuri Light")
  )

  fun legacyThemeNameMapping(oldThemeName: String, darkMode: Boolean) : String {
    val lowercaseThemeName = oldThemeName.toLowerCase()
    val legacyTheme = legacyThemes[lowercaseThemeName]
    return legacyTheme?.get(darkMode) ?: lowercaseThemeName
  }
}