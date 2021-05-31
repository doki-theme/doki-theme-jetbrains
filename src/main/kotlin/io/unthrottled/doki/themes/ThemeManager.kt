package io.unthrottled.doki.themes

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ServiceManager
import java.util.Optional
import javax.swing.UIManager

interface ThemeManager : Disposable {
  companion object {
    const val DEFAULT_THEME_NAME = "Re:Zero: Rem"

    val instance: ThemeManager
      get() = ServiceManager.getService(ThemeManager::class.java)
  }

  val isCurrentThemeDoki: Boolean

  val currentTheme: Optional<DokiTheme>

  val allThemes: List<DokiTheme>

  val defaultTheme: DokiTheme

  fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme>

  fun themeByName(selectedTheme: String): Optional<DokiTheme>
}
