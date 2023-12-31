package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.MoveableStickerActor

class MoveableStickerToggleAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean = ThemeConfig.instance.isMoveableStickers

  override fun setSelected(
    e: AnActionEvent,
    state: Boolean,
  ) {
    MoveableStickerActor.moveableStickers(state)
  }
}
