package io.acari.doki.settings.actors

import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.performWithAnimation

object StickerActor {

  fun swapStickers(enabled: Boolean, withAnimation: Boolean = true) {
    if (enabled != (ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY)) {
      performWithAnimation(withAnimation) {
        if (enabled) {
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
  }

  fun enableStickers(enabled: Boolean, withAnimation: Boolean = true) {
    if (enabled != (ThemeConfig.instance.stickerLevel == StickerLevel.ON.name)) {
      performWithAnimation(withAnimation) {
        if (enabled) {
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
}