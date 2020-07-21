package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.TheDokiTheme
import io.unthrottled.doki.notification.UpdateNotification

class ShowUpdateNotification : AnAction(), DumbAware {

  override fun actionPerformed(e: AnActionEvent) {
    TheDokiTheme.getVersion()
      .ifPresent {
          UpdateNotification.display(
              e.project!!,
              it
          )
      }
  }
}