package io.unthrottled.doki.internal

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import io.unthrottled.doki.actions.EditorNotificationManager
import java.util.function.Function
import javax.swing.JComponent

class DokiEditorNotificationProvider : EditorNotificationProvider {
  private fun buildNotification(fileEditor: FileEditor): EditorNotificationPanel? {
    if (!EditorNotificationManager.shouldShowNotification()) return null

    val panel = EditorNotificationPanel(fileEditor)
    panel.text("This is a test, bro")
    panel.createActionLabel("Link one") {
    }
    panel.createActionLabel("Link two") {
    }
    return panel
  }

  override fun collectNotificationData(
    p0: Project,
    p1: VirtualFile
  ): Function<in FileEditor, out JComponent?> {
    return Function {fileEditor ->
      buildNotification(fileEditor)
    }
  }
}
