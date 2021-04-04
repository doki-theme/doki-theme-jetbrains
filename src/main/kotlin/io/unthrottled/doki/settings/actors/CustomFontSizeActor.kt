package io.unthrottled.doki.settings.actors

import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.service.CustomFontSizeService

object CustomFontSizeActor {
  fun enableCustomFontSize(enabled: Boolean, customFontSize: Int) {
    val previousEnablement = ThemeConfig.instance.isGlobalFontSize
    ThemeConfig.instance.isGlobalFontSize = enabled
    val previousFontSize = ThemeConfig.instance.customFontSize
    ThemeConfig.instance.customFontSize = customFontSize
    CustomFontSizeService.applyCustomFontSize()

    val fontSizeChanged = previousFontSize != customFontSize
    val enablementChanged = previousEnablement != enabled
    if (fontSizeChanged || enablementChanged) {
      ProjectManager.getInstance().openProjects.forEach {
        ProjectManager.getInstance().reloadProject(it)
      }
    }
  }
}
