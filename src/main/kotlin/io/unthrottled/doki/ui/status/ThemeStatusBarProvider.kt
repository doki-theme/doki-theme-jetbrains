package io.unthrottled.doki.ui.status

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import io.unthrottled.doki.config.ThemeConfig

class ThemeStatusBarProvider : StatusBarWidgetFactory {

  override fun getId(): String =
    "io.unthrottled.doki.ui.status.StatusBarFactory"

  override fun getDisplayName(): String =
    "Doki Theme Display"

  override fun disposeWidget(widget: StatusBarWidget) {
    widget.dispose()
  }

  override fun isAvailable(project: Project): Boolean =
    true

  override fun createWidget(project: Project): StatusBarWidget =
    ThemeStatusBarWidget(project)

  override fun canBeEnabledOn(statusBar: StatusBar): Boolean =
    true

  override fun isConfigurable(): Boolean = true

  override fun isEnabledByDefault(): Boolean = ThemeConfig.instance.showThemeStatusBar
}
