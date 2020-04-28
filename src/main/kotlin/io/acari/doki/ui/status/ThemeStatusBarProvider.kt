package io.acari.doki.ui.status

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider

class ThemeStatusBarProvider : StatusBarWidgetProvider {
  override fun getWidget(project: Project): StatusBarWidget {
    return ThemeStatusBarWidget(project)
  }
}