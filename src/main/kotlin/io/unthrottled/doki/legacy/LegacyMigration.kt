package io.unthrottled.doki.legacy

import io.unthrottled.doki.settings.actors.ThemeActor.setDokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

object LegacyMigration {
  fun migrateIfNecessary() {
    handleThemeRenames()
  }

  private val renamedThemes = setOf(
    "bc12b380-1f2a-4a9d-89d8-388a07f1e15f", // Hatsune Miku
    "c5e92ad9-2fa0-491e-b92a-48ab92d13597", // Rias Crimson
  )

  private fun handleThemeRenames() {
    ThemeManager.instance.currentTheme
      .filter {
        renamedThemes.contains(it.id)
      }
      .ifPresent { renamedTheme ->
        setDokiTheme(
          ThemeManager.instance.allThemes.first {
            renamedTheme.id != it.id
          }.toOptional()
        )
        setDokiTheme(renamedTheme.toOptional())
      }
  }
}
