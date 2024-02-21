package io.unthrottled.doki.settings.actors

import com.intellij.ide.actions.QuickChangeLookAndFeel
import com.intellij.ide.ui.LafManager
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toOptional
import java.util.Optional

object ThemeActor : Logging {
  fun applyTheme(selectedTheme: String) {
    val currentTheme = ThemeManager.instance.currentTheme
    val possibleDokiTheme = getDokiTheme(currentTheme, selectedTheme)
    setDokiTheme(possibleDokiTheme)
  }

  private fun getDokiTheme(
    currentTheme: Optional<DokiTheme>,
    selectedTheme: String,
  ) = getTheme(currentTheme, selectedTheme)
    .flatMap { ThemeManager.instance.themeByName(selectedTheme) }

  private fun getTheme(
    currentTheme: Optional<DokiTheme>,
    selectedTheme: String,
  ): Optional<String> {
    return if (currentTheme.isPresent) {
      currentTheme
        .filter {
          it.name != selectedTheme
        }.map { "Not Same" }
    } else {
      "Not Doki Theme".toOptional()
    }
  }

  fun setDokiTheme(possibleDokiTheme: Optional<DokiTheme>) {
    possibleDokiTheme
      .flatMap { dokiTheme ->
        LafManager.getInstance().installedLookAndFeels
          .firstOrNull { dokiTheme.name == it.name }
          .toOptional()
      }
      .ifPresent {
        runSafely({
          QuickChangeLookAndFeel.switchLafAndUpdateUI(
            LafManager.getInstance(),
            it,
            true,
          )
        }) {
          logger().warn("Unable to set doki theme for reasons", it)
        }
      }
  }
}
