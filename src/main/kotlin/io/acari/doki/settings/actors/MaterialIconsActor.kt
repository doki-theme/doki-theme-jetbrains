package io.acari.doki.settings.actors

import com.intellij.openapi.util.IconLoader
import io.acari.doki.config.ThemeConfig
import io.acari.doki.icon.patcher.MaterialPathPatcher
import io.acari.doki.icon.patcher.MaterialPathPatcherFactory

object MaterialIconsActor {

  fun enableDirectoryIcons(enabled: Boolean) {
    if (ThemeConfig.instance.isMaterialDirectories != enabled) {
      ThemeConfig.instance.isMaterialDirectories = enabled
    }
  }

  fun enableFileIcons(enabled: Boolean) {
    if (ThemeConfig.instance.isMaterialFiles != enabled) {
      ThemeConfig.instance.isMaterialFiles = enabled
    }
  }

  fun enablePSIIcons(enabled: Boolean) {
    if (ThemeConfig.instance.isMaterialPSIIcons != enabled) {
      ThemeConfig.instance.isMaterialPSIIcons = enabled
      addOrRemovePatchers(
        enabled,
        MaterialPathPatcherFactory.materialPathPatchers.glyphs
      )
    }
  }

  private fun addOrRemovePatchers(
    enabled: Boolean,
    patchers: List<MaterialPathPatcher>
  ) {
    if (enabled) {
      patchers.forEach {
        IconLoader.installPathPatcher(it)
      }
    } else {
      patchers.forEach {
        IconLoader.removePathPatcher(it)
      }
    }
  }
}