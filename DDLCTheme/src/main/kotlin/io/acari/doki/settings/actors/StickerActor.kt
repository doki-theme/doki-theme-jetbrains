package io.acari.doki.settings.actors

import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager

object StickerActor {

  fun swapStickers(enabled: Boolean) {
    if (enabled != (ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY)) {
      if(enabled){
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

  fun enableStickers(enabled: Boolean) {
    if (enabled != (ThemeConfig.instance.stickerLevel == StickerLevel.ON.name)) {
      if(enabled){
        ThemeConfig.instance.stickerLevel = StickerLevel.ON.name
        ThemeManager.instance.currentTheme.ifPresent {
          StickerService.instance.activateForTheme(it)
        }
      } else {
        ThemeConfig.instance.stickerLevel = StickerLevel.OFF.name
        StickerService.instance.remove()
      }
    }
  }
}