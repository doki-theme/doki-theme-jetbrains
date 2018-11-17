package io.acari.DDLC

import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.components.ApplicationComponent
import io.acari.DDLC.wizard.DDLCWizardDialog
import io.acari.DDLC.wizard.DDLCWizardStepsProvider

class DDLCApplicationInitializationComponent: ApplicationComponent {
    override fun initComponent() {
        super.initComponent()
        if (MTThemeManager.isDDLCActive())
            checkWizard()
    }

    private fun checkWizard() {
        val isWizardShown = DDLCConfig.getInstance().getIsWizardShown()
        if (!isWizardShown) {
            DDLCWizardDialog(DDLCWizardStepsProvider()).show()
            MTConfig.getInstance().setIsWizardShown(true)
        }
    }
}