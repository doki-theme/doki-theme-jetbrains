package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.stickers.StickerPaneService

object MoveableStickerActor {
  fun moveableStickers(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isMoveableStickers) {
      ThemeConfig.instance.isMoveableStickers = enabled
      StickerPaneService.instance.setStickerPositioning(enabled)
      if (enabled) {
        UpdateNotification.showNotificationAcrossProjects(
          MessageBundle.message("stickers.movable.sticker.title"),
          MessageBundle.message("stickers.movable.sticker.body"),
        )
      } else {
        UpdateNotification.showNotificationAcrossProjects(
          MessageBundle.message("sticker.stationary.title"),
          MessageBundle.message("sticker.stationary.body"),
        )
      }
    }
  }
}
