package io.unthrottled.doki.themes

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import java.util.Optional
import javax.swing.UIManager

interface ThemeManager : Disposable {
  companion object {
    const val DEFAULT_THEME_NAME = "Slime: Rimiru Tempest"

    val instance: ThemeManager
      get() = ApplicationManager.getApplication().getService(ThemeManager::class.java)
  }

  val isCurrentThemeDoki: Boolean

  val currentTheme: Optional<DokiTheme>

  val allThemes: List<DokiTheme>

  val defaultTheme: DokiTheme

  fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme>

  fun themeByName(selectedTheme: String): Optional<DokiTheme>
}
