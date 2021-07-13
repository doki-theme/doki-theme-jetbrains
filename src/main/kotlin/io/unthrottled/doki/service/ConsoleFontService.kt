package io.unthrottled.doki.service

import com.intellij.openapi.editor.colors.EditorColorsManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.ThemeManager

object ConsoleFontService {

  fun applyConsoleFont() {
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.isOverrideConsoleFont }
      .ifPresent {
        EditorColorsManager.getInstance().schemeForCurrentUITheme
          .consoleFontName = ThemeConfig.instance.consoleFontName
      }
  }
}