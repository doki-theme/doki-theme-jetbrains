package io.acari.DDLC.actions

import com.chrisrm.ideaddlc.config.MTConfigurable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

/**
 * Used for the toolbar and dropdown icon action.
 */
class ConfigAction : AnAction() {
  override fun actionPerformed(event: AnActionEvent) {
    ShowSettingsUtil.getInstance()
        .showSettingsDialog(event.project, MTConfigurable::class.java)
  }
}