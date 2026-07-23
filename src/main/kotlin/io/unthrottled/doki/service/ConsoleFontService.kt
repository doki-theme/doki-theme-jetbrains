package io.unthrottled.doki.service

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.ConsoleFontActor
import io.unthrottled.doki.themes.ThemeManager

object ConsoleFontService {
  fun applyConsoleFont() {
    ThemeManager.getInstance().currentTheme
      .filter { ThemeConfig.getInstance().isOverrideConsoleFont }
      .ifPresent {
        EditorColorsManager.getInstance().schemeForCurrentUITheme
          .consoleFontName = ThemeConfig.getInstance().consoleFontName
        ApplicationManager.getApplication().invokeLater {
          ConsoleFontActor.refreshConsole()
        }
      }
  }
}
