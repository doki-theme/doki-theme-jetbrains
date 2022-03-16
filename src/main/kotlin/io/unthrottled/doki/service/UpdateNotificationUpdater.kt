package io.unthrottled.doki.service

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.themes.DokiTheme

object UpdateNotificationUpdater {
  fun attemptToRefreshUpdateNotification(dokiTheme: DokiTheme) {
    ProjectManager.getInstance().openProjects.forEach { project ->
      val instance = FileEditorManager.getInstance(project)
      val title = UpdateNotification.getPluginUpdateTitle()
      val updateNotifications = instance.openFiles.filter {
        it.name == title
      }

      if (updateNotifications.isNotEmpty()) {
        updateNotifications.forEach { openEditor ->
          val previousUrl =
            openEditor.get().keys
              .map {
                openEditor.getUserData(it)
              }.filterIsInstance<String>()
              .firstOrNull { it.startsWith("https://doki-theme.unthrottled.io") }

          val (newUrl, content) = UpdateNotification.reconstructUrlAndContent(
            previousUrl,
            dokiTheme,
          )
          instance.closeFile(openEditor)
          HTMLEditorProvider.openEditor(
            project,
            title,
            newUrl,
            content
          )
        }
      }
    }
  }
}
