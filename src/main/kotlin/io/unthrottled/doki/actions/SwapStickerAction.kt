package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.StickerActor
import io.unthrottled.doki.stickers.CurrentSticker

class SwapStickerAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean = ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY

  override fun setSelected(
    e: AnActionEvent,
    state: Boolean,
  ) {
    StickerActor.swapStickers(if (state) CurrentSticker.SECONDARY else CurrentSticker.DEFAULT)
  }
}
