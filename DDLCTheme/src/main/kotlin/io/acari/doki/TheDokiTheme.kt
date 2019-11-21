package io.acari.doki

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.laf.DokiAddFileColorsAction
import io.acari.doki.themes.ThemeManager

class TheDokiTheme: Disposable {
    val connection = ApplicationManager.getApplication().messageBus.connect()

    init {
        ThemeManager.instance
        connection.subscribe(ProjectLifecycleListener.TOPIC, object: ProjectLifecycleListener{
            override fun projectComponentsInitialized(project: Project) {

                //todo: opt in to colors
                DokiAddFileColorsAction.setFileScopes(project)
            }
        })
    }

    override fun dispose() {
        connection.dispose()
    }
}