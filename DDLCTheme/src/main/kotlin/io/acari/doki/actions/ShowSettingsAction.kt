package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.ProjectManager
import io.acari.doki.settings.ThemeSettings.Companion.THEME_SETTINGS_DISPLAY_NAME

class ShowSettingsAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    ApplicationManager.getApplication().invokeLater({
      ShowSettingsUtil.getInstance().showSettingsDialog(
        ProjectManager.getInstance().defaultProject,
        THEME_SETTINGS_DISPLAY_NAME
      )
    }, ModalityState.NON_MODAL)
  }
}