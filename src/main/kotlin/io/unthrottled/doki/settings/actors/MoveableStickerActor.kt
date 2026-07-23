package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.StickerPaneService

object MoveableStickerActor {
  fun moveableStickers(enabled: Boolean) {
    if (enabled != ThemeConfig.getInstance().isMoveableStickers) {
      ThemeConfig.getInstance().isMoveableStickers = enabled
      StickerPaneService.getInstance().setStickerPositioning(enabled)
    }
  }
}
