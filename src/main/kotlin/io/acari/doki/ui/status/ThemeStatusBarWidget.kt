package io.acari.doki.ui.status

import com.intellij.icons.AllIcons
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import io.acari.doki.settings.ThemeSettings.Companion.THEME_SETTINGS_DISPLAY_NAME
import java.awt.Component
import java.awt.event.MouseEvent
import javax.swing.Icon

class ThemeStatusBarWidget(private val project: Project) :
  StatusBarWidget,
  StatusBarWidget.IconPresentation,
  StatusBarWidget.TextPresentation {
  companion object {
    private const val ID = "Doki Theme Status Component"
  }

  private lateinit var myStatusBar: StatusBar

  override fun getTooltipText(): String? =
    "Ravioli"

  override fun ID(): String = ID

  override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

  override fun install(statusBar: StatusBar) {
    myStatusBar = statusBar
  }

  override fun dispose() {
    if(this::myStatusBar.isInitialized){
      myStatusBar.dispose()
    }
  }

  override fun getIcon(): Icon =
    AllIcons.Nodes.Favorite

  override fun getClickConsumer(): Consumer<MouseEvent> = Consumer {
    ApplicationManager.getApplication().invokeLater({
      ShowSettingsUtil.getInstance().showSettingsDialog(
        project, THEME_SETTINGS_DISPLAY_NAME
      )
    }, ModalityState.NON_MODAL)
  }

  override fun getAlignment(): Float {
    return Component.CENTER_ALIGNMENT
  }

  override fun getText(): String {
    return "Just Monika"
  }
}
