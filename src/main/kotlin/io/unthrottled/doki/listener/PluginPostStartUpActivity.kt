package io.unthrottled.doki.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import io.unthrottled.doki.TheDokiTheme

internal class PluginPostStartUpActivity : StartupActivity {
  override fun runActivity(project: Project) {
    TheDokiTheme.instance.projectOpened(project)
  }
}