package io.acari.doki.ui.status

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider
import com.intellij.psi.codeStyle.statusbar.CodeStyleStatusBarWidget

class ThemeStatusBarProvider: StatusBarWidgetProvider {
  override fun getWidget(project: Project): StatusBarWidget {
    return ThemeStatusBarWidget(project)
  }
}