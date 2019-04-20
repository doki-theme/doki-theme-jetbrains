package io.acari.DDLC

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.IdeFrame
import com.intellij.util.messages.MessageBusConnection
import io.acari.DDLC.chibi.ChibiOrchestrator
import io.acari.DDLC.wizard.DDLCWizardDialog
import io.acari.DDLC.wizard.DDLCWizardStepsProvider
import java.util.*

class DDLCApplicationInitializationComponent : Disposable {

  private val messageBusConnection: MessageBusConnection = ApplicationManager.getApplication().messageBus.connect()

  init {
    messageBusConnection.subscribe(ApplicationActivationListener.TOPIC, object: ApplicationActivationListener {
      override fun applicationActivated(ideFrame: IdeFrame) {
        initComponent()
      }
    })
  }

  companion object {
    private val listeners = LinkedList<Runnable>()
    fun addInitializationListener(callback: Runnable){
      listeners.add(callback)
    }

    fun notifyListeners(){
      listeners.forEach { it.run() }
    }
  }
  override fun dispose() {
    messageBusConnection.dispose()
  }

  fun initComponent() {
    //This is stupid, but the manager does not get created until after.
    addInitializationListener(MTThemeManager.getInstance().createListener())
    ChibiOrchestrator.init()
    if (MTThemeManager.isDDLCActive())
      checkWizard()

    notifyListeners()
  }

  private fun checkWizard() {
    val isWizardShown = DDLCConfig.getInstance().isWizardShown()
    if (!isWizardShown) {
      DDLCWizardDialog(DDLCWizardStepsProvider()).show()
      DDLCConfig.getInstance().setIsWizardShown(true)
    }
  }
}