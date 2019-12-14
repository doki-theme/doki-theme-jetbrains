package io.acari.doki.ui.status

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project

class ThemeStatusBarOrchestrator(private val project: Project): Disposable,
  DumbAware {

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  override fun dispose() {
    connection.disconnect()
  }
}