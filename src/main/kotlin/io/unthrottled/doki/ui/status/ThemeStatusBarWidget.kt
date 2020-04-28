package io.unthrottled.doki.ui.status

import com.intellij.icons.AllIcons
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.config.ThemeConfigListener
import io.unthrottled.doki.settings.ThemeSettings.Companion.THEME_SETTINGS_DISPLAY_NAME
import io.unthrottled.doki.themes.ThemeManager
import java.awt.event.MouseEvent
import javax.swing.Icon

class ThemeStatusBarWidget(private val project: Project) :
  StatusBarWidget,
  StatusBarWidget.MultipleTextValuesPresentation {
  companion object {
    private const val ID = "Doki Theme Status Component"
  }

  private val connect = ApplicationManager.getApplication().messageBus.connect()

  init {
    connect.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      if (this::myStatusBar.isInitialized) {
        myStatusBar.updateWidget(ID)
      }
    })
    val self = this
    connect.subscribe(THEME_CONFIG_TOPIC, object : ThemeConfigListener {
      override fun themeConfigUpdated(themeConfig: ThemeConfig) {
        if (self::myStatusBar.isInitialized) {
          self.myStatusBar.updateWidget(ID)
        }
      }
    })
  }

  private lateinit var myStatusBar: StatusBar

  override fun getTooltipText(): String? = "Current Theme"

  override fun getSelectedValue(): String? =
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.showThemeStatusBar }
      .map { it.displayName }
      .orElseGet { null }

  override fun ID(): String = ID

  override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

  override fun install(statusBar: StatusBar) {
    myStatusBar = statusBar
  }

  override fun dispose() {
    if (this::myStatusBar.isInitialized) {
      try {
        myStatusBar.dispose()
      } catch (e: Throwable) {
      }
    }
  }

  override fun getIcon(): Icon? =
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.showThemeStatusBar }
      .map { AllIcons.Nodes.Favorite }
      .orElseGet { null }

  override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
    ApplicationManager.getApplication().invokeLater({
      ShowSettingsUtil.getInstance().showSettingsDialog(
        project, THEME_SETTINGS_DISPLAY_NAME
      )
    }, ModalityState.NON_MODAL)
  }

  override fun getPopupStep(): ListPopup? = null
}
