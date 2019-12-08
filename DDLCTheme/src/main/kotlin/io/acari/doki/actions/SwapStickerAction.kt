package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager

class SwapStickerAction : ToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      if(state){
        ThemeConfig.instance.currentSticker =
          CurrentSticker.SECONDARY
      } else {
        ThemeConfig.instance.currentSticker =
          CurrentSticker.DEFAULT
      }
    ThemeManager.instance.currentTheme.ifPresent {
      StickerService.instance.activateForTheme(it)
    }
  }
}