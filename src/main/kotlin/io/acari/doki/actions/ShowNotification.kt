package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowNotification : AnAction() {
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