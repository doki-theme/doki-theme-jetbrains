package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig

object ThemeStatusBarActor {
  fun applyConfig(showThemeStatusBar: Boolean) {
    if (ThemeConfig.instance.showThemeStatusBar != showThemeStatusBar) {
      ThemeConfig.instance.showThemeStatusBar = showThemeStatusBar
    }
  }
}