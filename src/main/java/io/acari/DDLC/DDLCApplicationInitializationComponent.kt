package io.acari.DDLC

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.components.ApplicationComponent
import io.acari.DDLC.chibi.ChibiOrchestrator
import io.acari.DDLC.wizard.DDLCWizardDialog
import io.acari.DDLC.wizard.DDLCWizardStepsProvider
import java.util.*

/**
 * Dear future maintainer,
 * There is something wonky that happens when you subscribe to the application events.
 * Please be sure to test integrations with other themes, as it may be broken.
 */
class DDLCApplicationInitializationComponent : ApplicationComponent {
  companion object {
    private val listeners = LinkedList<Runnable>()

    fun addInitializationListener(callback: Runnable){
      listeners.add(callback)
    }
    fun notifyListeners(){
      listeners.forEach { it.run() }
    }
  }

  override fun initComponent() {
    super.initComponent()

    //This is stupid, but the manager does not get created until after.
    addInitializationListener(MTThemeManager.getInstance().createListener())
    ChibiOrchestrator.init()
    if (MTThemeManager.isDDLCActive())
      checkWizard()

    notifyListeners()
  }

  private fun checkWizard() {
//    val isWizardShown = DDLCConfig.getInstance().isWizardShown()
//    if (!isWizardShown) {
      DDLCWizardDialog(DDLCWizardStepsProvider()).show()
//      DDLCConfig.getInstance().setIsWizardShown(true)
//    }
  }
}