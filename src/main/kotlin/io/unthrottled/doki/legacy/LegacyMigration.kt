package io.unthrottled.doki.legacy

import io.unthrottled.doki.settings.actors.ThemeActor.setDokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

object LegacyMigration {
  fun migrateIfNecessary() {
    handleThemeRenames()
  }

  private val renamedThemes = setOf(
    "19b65ec8-133c-4655-a77b-13623d8e97d3", // Ryuko Dark
    "3a78b13e-dbf2-410f-bb20-12b57bff7735", // Satsuki Light
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
