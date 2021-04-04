package io.unthrottled.doki.settings.actors

import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.service.CustomFontSizeService

object CustomFontSizeActor {
  fun enableCustomFontSize(enabled: Boolean, customFontSize: Int) {
    val previousFontSize = ThemeConfig.instance.customFontSize
    ThemeConfig.instance.isGlobalFontSize = enabled
    ThemeConfig.instance.customFontSize = customFontSize
    CustomFontSizeService.applyCustomFontSize()
    if (previousFontSize != customFontSize && enabled) {
      ProjectManager.getInstance().openProjects.forEach {
        ProjectManager.getInstance().reloadProject(it)
      }
    }
  }
}
