package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import io.unthrottled.doki.util.toOptional

class FileIconAssociation : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    val currentProject = e.project
    val message = StringBuffer("${e.presentation.text} selected!")
    val title = e.presentation.description

    val fullMessage = e.getData(CommonDataKeys.NAVIGATABLE)
      .toOptional()
      .map { message.append("\nSelected Element: $it").toString() }
      .orElseGet { message.toString() }

    Messages.showMessageDialog(currentProject, fullMessage, title, Messages.getInformationIcon())
  }

  override fun update(e: AnActionEvent) {
    val selectedItem = e.getData(CommonDataKeys.NAVIGATABLE)
    val isEnabled = selectedItem is PsiElement
    e.presentation.isEnabledAndVisible = isEnabled
  }
}

