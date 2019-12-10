package io.acari.DDLC.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.actions.themes.literature.club.BaseThemeAction

class FileColorAction : BaseThemeAction() {
  private val dokiFileColors = DDLCAddFileColorsAction()

  override fun isSelected(e: AnActionEvent): Boolean =
      DDLCConfig.getInstance().isDokiFileColors ||
          dokiFileColors.isSet(e.project)

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    val dokiConfig = DDLCConfig.getInstance()
    val project = e.project
    dokiConfig.isDokiFileColors =
        if (dokiConfig.isDokiFileColors || dokiFileColors.isSet(project)) {
          dokiFileColors.removeFileScopes(project)
          false
        } else {
          dokiFileColors.setFileScopes(project)
          true
        }
  }
}