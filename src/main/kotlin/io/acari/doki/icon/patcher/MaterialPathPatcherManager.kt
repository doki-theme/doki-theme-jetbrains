package io.acari.doki.icon.patcher

import com.intellij.openapi.util.IconLoader
import io.acari.doki.config.ThemeConfig
import io.acari.doki.themes.ThemeManager

object MaterialPathPatcherManager {
  fun attemptToAddIcons() {
    if(removed){
      ThemeManager.instance.currentTheme
        .ifPresent {
          getAllEnabledPatchers().forEach {
            IconLoader.installPathPatcher(it)
          }
          removed = false
        }
    }
  }

  private var removed = true

  fun attemptToRemoveIcons() {
    if(!removed){
      getAllEnabledPatchers().forEach {
        IconLoader.removePathPatcher(it)
      }
      removed = true
    }
  }

  private fun getAllEnabledPatchers(): List<MaterialPathPatcher> {
    val materialPathPatchers = MaterialPathPatcherFactory.materialPathPatchers
    val themeConfig = ThemeConfig.instance
    return listOf(
      materialPathPatchers.glyphs to themeConfig.isMaterialPSIIcons
    ).filter {
      it.second
    }.flatMap {
      it.first
    }
  }
}