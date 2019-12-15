package io.acari.doki.icon.patcher

import com.intellij.openapi.util.IconLoader
import io.acari.doki.config.ThemeConfig

object MaterialPathPatcherManager {
  fun attemptToAddIcons() {
    val materialPathPatchers = MaterialPathPatcherFactory.materialPathPatchers
    val themeConfig = ThemeConfig.instance
    listOf(
      materialPathPatchers.glyphs to themeConfig.isMaterialPSIIcons
    ).filter {
      it.second
    }.flatMap {
      it.first
    }.forEach {
        IconLoader.installPathPatcher(it)
      }
  }
}