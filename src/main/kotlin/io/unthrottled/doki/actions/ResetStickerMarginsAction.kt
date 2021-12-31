package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.stickers.StickerPaneService

class ResetStickerMarginsAction : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    StickerPaneService.instance.resetMargins()
  }
}