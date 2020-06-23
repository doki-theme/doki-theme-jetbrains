package io.unthrottled.doki.ui.status

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.impl.status.widget.StatusBarWidgetsManager
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.config.ThemeConfigListener
import io.unthrottled.doki.util.toOptional

class ThemeStatusBarProvider : StatusBarWidgetFactory {

  private val connect = ApplicationManager.getApplication().messageBus.connect()

  init {
    connect.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      createChangeListener(this)
    })
    val self = this
    connect.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
      override fun themeConfigUpdated(themeConfig: ThemeConfig) {
        createChangeListener(self)
      }
    })
  }

  private fun createChangeListener(self: ThemeStatusBarProvider) {
    ProjectManager.getInstanceIfCreated().toOptional()
      .map { projectManager ->
        projectManager.openProjects
          .mapNotNull { it.getService(StatusBarWidgetsManager::class.java) }
          .forEach { statusBarManager ->
            statusBarManager.updateWidget(self)
          }
      }
  }

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
