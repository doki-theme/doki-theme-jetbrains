package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.StickerActor
import io.acari.doki.stickers.CurrentSticker

class SwapStickerAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    StickerActor.swapStickers(state)
  }
}