package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.StickerHideConfig
import io.unthrottled.doki.stickers.StickerPaneService

object StickerHideActor {
  fun setStickerHideStuff(
    hideOnHover: Boolean,
    hideDelayMS: Int,
  ) {
    if (hideOnHover != ThemeConfig.getInstance().hideOnHover) {
      ThemeConfig.getInstance().hideOnHover = hideOnHover
      StickerPaneService.getInstance().setStickerHideConfig(
        StickerHideConfig(
          hideOnHover,
          hideDelayMS,
        ),
      )
    }
  }
}
