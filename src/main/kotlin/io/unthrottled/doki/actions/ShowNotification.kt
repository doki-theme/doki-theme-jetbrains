package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

class ShowNotification : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    EditorNotificationManager.toggle()
  }
}

object EditorNotificationManager {
  private var shouldShow: Boolean = false

  fun toggle() {
    shouldShow = !shouldShow
  }

  fun shouldShowNotification(): Boolean = shouldShow
}
