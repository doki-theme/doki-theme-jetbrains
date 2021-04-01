package io.unthrottled.doki.legacy

import io.unthrottled.doki.settings.actors.setDokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

object LegacyMigration {
  fun migrateIfNecessary() {
    handleThemeRenames()
  }

  private fun handleThemeRenames() {
    ThemeManager.instance.currentTheme
      .filter {
        it.id == "c5e92ad9-2fa0-491e-b92a-48ab92d13597"
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
