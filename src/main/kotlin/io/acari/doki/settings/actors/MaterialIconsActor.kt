package io.acari.doki.settings.actors

import io.acari.doki.config.ThemeConfig

object MaterialIconsActor {

  fun enableDirectoryIcons(enabled: Boolean){
    if(ThemeConfig.instance.isMaterialDirectories != enabled) {
      ThemeConfig.instance.isMaterialDirectories = enabled
      // refresh directories
    }
  }

  fun enableFileIcons(enabled: Boolean){
    if(ThemeConfig.instance.isMaterialFiles != enabled) {
      ThemeConfig.instance.isMaterialFiles = enabled
      // refresh files
    }
  }

  fun enablePSIIcons(enabled: Boolean){
    if(ThemeConfig.instance.isMaterialPSIIcons != enabled) {
      ThemeConfig.instance.isMaterialPSIIcons = enabled
      // refresh icons
    }
  }
}