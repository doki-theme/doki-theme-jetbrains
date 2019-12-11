package io.acari.doki.settings.actors

import com.intellij.ide.actions.QuickChangeLookAndFeel
import com.intellij.ide.ui.LafManager
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.toOptional

object ThemeActor {
  fun applyTheme(selectedTheme: String) {
    ThemeManager.instance.currentTheme
      .filter {
        it.name != selectedTheme
      }.flatMap { ThemeManager.instance.themeByName(selectedTheme) }
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