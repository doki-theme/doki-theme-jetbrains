package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerService
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.performWithAnimation

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
    if (enabled != (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON)) {
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