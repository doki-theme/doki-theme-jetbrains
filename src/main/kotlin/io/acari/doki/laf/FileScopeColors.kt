package io.acari.doki.laf

import com.intellij.openapi.project.ProjectManager
import io.acari.doki.config.ThemeConfig

object FileScopeColors {

  fun attemptToInstallColors(){
    if(ThemeConfig.instance.isDokiFileColors){
      addColors()
    }
  }

  fun attemptToRemoveColors() {
    if(ThemeConfig.instance.isDokiFileColors) {
      removeColors()
    }
  }

  fun addColors() {
    ProjectManager.getInstance()
      .openProjects
      .forEach { project -> DokiAddFileColorsAction.setFileScopes(project) }
  }

  fun removeColors() {
    ProjectManager.getInstance()
      .openProjects
      .forEach { project ->
        DokiAddFileColorsAction.removeFileScopes(project)
      }
  }


}