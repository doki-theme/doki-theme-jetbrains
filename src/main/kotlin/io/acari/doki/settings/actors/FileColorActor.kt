package io.acari.doki.settings.actors

import io.acari.doki.config.ThemeConfig
import io.acari.doki.laf.FileScopeColors

object FileColorActor {
  fun enableFileColors(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isDokiFileColors) {
      ThemeConfig.instance.isDokiFileColors = enabled
      if (enabled) {
        FileScopeColors.addColors()
      } else {
        FileScopeColors.removeColors()
      }
    }
  }
}