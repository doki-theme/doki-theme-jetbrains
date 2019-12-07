package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.doki.laf.FileScopeColors
import io.acari.doki.config.ThemeConfig

class FileColorToggleAction : ToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.isDokiFileColors

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      ThemeConfig.instance.isDokiFileColors = state
      if(state){
        FileScopeColors.addColors()
      } else {
        FileScopeColors.removeColors()
      }
  }
}