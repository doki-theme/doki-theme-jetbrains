package io.acari.doki.themes

import com.intellij.openapi.components.ServiceManager
import java.util.*
import javax.swing.UIManager

interface ThemeManager {
  companion object {
    const val MONIKA_DARK = "Monika Dark"

    val instance: ThemeManager
      get() = ServiceManager.getService(ThemeManager::class.java)
  }

  val currentTheme: Optional<DokiTheme>

  val allThemes: List<DokiTheme>

  fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme>

  fun themeByName(selectedTheme: String): Optional<DokiTheme>
}