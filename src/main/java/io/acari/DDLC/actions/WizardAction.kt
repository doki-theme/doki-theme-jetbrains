package io.acari.DDLC.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.DDLC.wizard.DDLCWizardDialog
import io.acari.DDLC.wizard.DDLCWizardStepsProvider

class WizardAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    DDLCWizardDialog(DDLCWizardStepsProvider()).show()
  }
}