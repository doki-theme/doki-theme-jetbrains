package io.unthrottled.doki.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.unthrottled.doki.TheDokiTheme

class PluginPostStartUpActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    doStuff(project)
  }

  private fun doStuff(project: Project) {
    TheDokiTheme.instance.projectOpened(project)
  }
}
