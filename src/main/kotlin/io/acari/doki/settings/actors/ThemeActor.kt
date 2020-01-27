package io.acari.doki.settings.actors

import com.intellij.ide.actions.QuickChangeLookAndFeel
import com.intellij.ide.ui.LafManager
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.toOptional

object ThemeActor {
  fun applyTheme(selectedTheme: String) {
    val currentTheme = ThemeManager.instance.currentTheme
    if (currentTheme.isPresent) {
      currentTheme
        .filter {
          it.name != selectedTheme
        }.map { "Not Same" }
    } else {
      "Not Doki Theme".toOptional()
    }
      .flatMap { ThemeManager.instance.themeByName(selectedTheme) }
      .flatMap { dokiTheme ->
        LafManager.getInstance().installedLookAndFeels
          .first { dokiTheme.name == it.name }
          .toOptional()
      }
      .ifPresent {
        QuickChangeLookAndFeel.switchLafAndUpdateUI(
          LafManager.getInstance(),
          it,
          true
        )
      }
  }
}