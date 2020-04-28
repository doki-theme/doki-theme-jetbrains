package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.StickerActor
import io.acari.doki.stickers.StickerLevel.ON

class StickersToggleAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.currentStickerLevel == ON

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    StickerActor.enableStickers(state)
  }
}

