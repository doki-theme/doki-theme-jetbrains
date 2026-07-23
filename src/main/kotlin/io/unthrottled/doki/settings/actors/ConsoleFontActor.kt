package io.unthrottled.doki.settings.actors

import com.intellij.openapi.editor.colors.EditorColorsManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.service.ConsoleFontService
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafely

object ConsoleFontActor : Logging {
  fun enableCustomFontSize(
    enabled: Boolean,
    consoleFontName: String,
  ) {
    val previousEnablement = ThemeConfig.getInstance().isOverrideConsoleFont
    ThemeConfig.getInstance().isOverrideConsoleFont = enabled
    val previousFontSize = ThemeConfig.getInstance().consoleFontName
    ThemeConfig.getInstance().consoleFontName = consoleFontName
    ConsoleFontService.applyConsoleFont()

    val fontSizeChanged = previousFontSize != consoleFontName
    val enablementChanged = previousEnablement != enabled
    if (fontSizeChanged || enablementChanged) {
      refreshConsole()
    }
  }

  fun refreshConsole() {
    runSafely({
      val editorColorManager = EditorColorsManager.getInstance()
      editorColorManager.javaClass.declaredMethods
        .filter {
          it.name == "schemeChangedOrSwitched" && it.canAccess(editorColorManager)
        }.forEach {
          it.invoke(editorColorManager, null)
        }
    }) {
      logger().warn("Unable to refresh console", it)
    }
  }
}
