package io.acari.doki

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.hax.HackComponent.hackLAF
import io.acari.doki.hax.SvgLoaderHacker
import io.acari.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.acari.doki.laf.DokiAddFileColorsAction.removeFileScopes
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.laf.LookAndFeelInstaller
import io.acari.doki.themes.DokiThemes

class TheDokiTheme : Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    //////////// hax ////////////
    setSVGColorPatcher()
    hackLAF()
    //////////// ._. ////////////

    connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      //todo: opt in to colors
      val projects = ProjectManager.getInstance().openProjects
      DokiThemes.processLaf(LafManager.getInstance().currentLookAndFeel) //todo: get theme more better
        .ifPresentOrElse({
          setSVGColorPatcher()
          LookAndFeelInstaller.installAllUIComponents()
          projects.forEach { project -> setFileScopes(project) }
        })
        {
          projects.forEach { project -> removeFileScopes(project)
          } } // todo: only remove if was set.
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