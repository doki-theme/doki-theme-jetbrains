package io.unthrottled.doki.service

import com.intellij.openapi.editor.colors.EditorColorsManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.ThemeManager

object CustomFontSizeService {

  fun applyCustomFontSize() {
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.isGlobalFontSize }
      .ifPresent {
        EditorColorsManager.getInstance().schemeForCurrentUITheme
          .editorFontSize = ThemeConfig.instance.customFontSize
      }
  }
}
