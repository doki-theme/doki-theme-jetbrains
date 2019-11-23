package io.acari.doki

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.themes.ThemeManager

class TheDokiTheme : Disposable {
    private val connection = ApplicationManager.getApplication().messageBus.connect()
    lateinit var project: Project

    init {
        ThemeManager.instance
        connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
            //todo: opt in to colors
            if (::project.isInitialized) {
                setFileScopes(project)
            }
        })
        val self = this
        connection.subscribe(ProjectLifecycleListener.TOPIC, object : ProjectLifecycleListener {
            override fun projectComponentsInitialized(project: Project) {
                self.project = project
                //todo: opt in to colors
                setFileScopes(project)
            }
        })
    }

    override fun dispose() {
        connection.dispose()
    }
}