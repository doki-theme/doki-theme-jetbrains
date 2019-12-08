package io.acari.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.StickerLevel.OFF
import io.acari.doki.stickers.StickerLevel.ON
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager

class StickersToggleAction : ToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean =
    ThemeConfig.instance.currentStickerLevel == ON

  override fun setSelected(e: AnActionEvent, state: Boolean) {
      if(state){
        ThemeConfig.instance.stickerLevel = ON.name
        ThemeManager.instance.currentTheme.ifPresent {
          StickerService.instance.activateForTheme(it)
        }
      } else {
        ThemeConfig.instance.stickerLevel = OFF.name
        StickerService.instance.remove()
      }
  }
}

