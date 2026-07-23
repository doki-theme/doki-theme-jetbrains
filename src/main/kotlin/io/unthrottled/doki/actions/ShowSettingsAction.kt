package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.settings.ThemeSettings.THEME_SETTINGS_DISPLAY_NAME

class ShowSettingsAction : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    ApplicationManager.getApplication().invokeLater(
      {
        ShowSettingsUtil.getInstance().showSettingsDialog(
          ProjectManager.getInstance().defaultProject,
          THEME_SETTINGS_DISPLAY_NAME,
        )
      },
      ModalityState.nonModal(),
    )
  }
}
