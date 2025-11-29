package io.unthrottled.doki.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.AlarmDebouncer

object UpdateNotificationUpdater : Disposable {
  private val debouncer: AlarmDebouncer<Unit> by lazy { AlarmDebouncer<Unit>(80) }

  fun attemptToRefreshUpdateNotification(dokiTheme: DokiTheme) {
    debouncer.debounce {
      ProjectManager.getInstance().openProjects.forEach { project ->
        val instance = FileEditorManager.getInstance(project)
        val title = UpdateNotification.getPluginUpdateTitle()
        val updateNotifications =
          instance.openFiles.filter {
            it.name == title
          }

        if (updateNotifications.isNotEmpty()) {
          updateNotifications.forEach { openEditor ->
            val (newUrl, content) =
              UpdateNotification.reconstructUrlAndContent(
                dokiTheme,
              )
            instance.closeFile(openEditor)
            HTMLEditorProvider.openEditor(
              project,
              title,
              newUrl,
              content,
            )
          }
        }
      }
    }
  }

  override fun dispose() {
    this.debouncer.dispose()
  }
}
