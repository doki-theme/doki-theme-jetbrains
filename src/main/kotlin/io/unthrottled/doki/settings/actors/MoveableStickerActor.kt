package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.stickers.StickerPaneService

object MoveableStickerActor {
  fun moveableStickers(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isMoveableStickers) {
      ThemeConfig.instance.isMoveableStickers = enabled
      StickerPaneService.instance.setStickerPositioning(enabled)
      if (enabled) {
        UpdateNotification.showDokiNotification(
          "Stickers are now draggable",
          """
            You won't be able to click through the sticker now,
            so you'll need to move it or toggle this action.
          """.trimIndent()
        )
      } else {
        UpdateNotification.showDokiNotification(
          "Stickers are now stationary",
          """
            You can now click through the stickers.
            To enable re-positioning, please toggle this action
          """.trimIndent()
        )
      }
    }
  }
}
