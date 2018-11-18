package io.acari.DDLC

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.components.ApplicationComponent
import io.acari.DDLC.wizard.DDLCWizardDialog
import io.acari.DDLC.wizard.DDLCWizardStepsProvider

class DDLCApplicationInitializationComponent : ApplicationComponent {
  companion object {
    init {
      MTThemeManager.holdStartupState()
    }
  }

  override fun initComponent() {
    super.initComponent()
    if (MTThemeManager.isDDLCActive())
      checkWizard()
  }

  private fun checkWizard() {
    val isWizardShown = DDLCConfig.getInstance().getIsWizardShown()
    if (!isWizardShown) {
      DDLCWizardDialog(DDLCWizardStepsProvider()).show()
      DDLCConfig.getInstance().setIsWizardShown(true)
    }
  }
}