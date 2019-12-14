package io.acari.doki.ui.status

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager
import io.acari.doki.themes.DokiTheme
import io.acari.doki.util.toOptional
import java.util.*


object ThemeStatusBarOrchestrator {

  private var project: Optional<Project> = Optional.empty()

  fun consumeProject(project: Project) {
    this.project = project.toOptional()
  }

  fun updateToTheme(theme: DokiTheme) {

  }

  fun removeComponent() {

  }

  private fun addToStatusBar() {
    getStatusBar().ifPresent {
    }
  }

  private fun getStatusBar(): Optional<StatusBar> =
    project.map {
    WindowManager.getInstance().getStatusBar(it)
  }
}