package io.acari.doki

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.laf.DokiAddFileColorsAction.removeFileScopes
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.themes.DokiThemes
import io.acari.doki.themes.ThemeManager

class TheDokiTheme : Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    ThemeManager.instance
    connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      //todo: opt in to colors
      val projects = ProjectManager.getInstance().openProjects
        DokiThemes.processLaf(LafManager.getInstance().currentLookAndFeel) //todo: get theme more better
          .ifPresentOrElse({ projects.forEach { project -> setFileScopes(project) } })
          { projects.forEach { project -> removeFileScopes(project) } } // todo: only remove if was set.
    })
    connection.subscribe(ProjectLifecycleListener.TOPIC, object : ProjectLifecycleListener {
      override fun projectComponentsInitialized(project: Project) {
        setFileScopes(project)
      }
    })
  }

  override fun dispose() {
    connection.dispose()
  }
}