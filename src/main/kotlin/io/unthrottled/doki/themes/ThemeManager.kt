package io.unthrottled.doki.themes

import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import com.intellij.openapi.application.ApplicationManager
import java.util.Optional

interface ThemeManager {
  companion object {
    const val DEFAULT_THEME_NAME = "Franxx: Zero Two Dark Obsidian"
    fun getInstance(): ThemeManager = ApplicationManager.getApplication().getService(ThemeManager::class.java)
  }

  val isCurrentThemeDoki: Boolean

  val currentTheme: Optional<DokiTheme>

  val allThemes: List<DokiTheme>

  val defaultTheme: DokiTheme

  fun processLaf(currentLaf: UIThemeLookAndFeelInfo): Optional<DokiTheme>

  fun themeByName(selectedTheme: String): Optional<DokiTheme>
}
