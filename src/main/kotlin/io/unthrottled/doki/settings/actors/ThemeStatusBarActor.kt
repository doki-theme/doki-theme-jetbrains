package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig

object ThemeStatusBarActor {
  fun applyConfig(showThemeStatusBar: Boolean) {
    if (ThemeConfig.getInstance().showThemeStatusBar != showThemeStatusBar) {
      ThemeConfig.getInstance().showThemeStatusBar = showThemeStatusBar
    }
  }
}
