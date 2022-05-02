package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.StickerHideConfig
import io.unthrottled.doki.stickers.StickerPaneService

object StickerHideActor {

  fun setStickerHideStuff(
    hideOnHover: Boolean,
    hideDelayMS: Int,
  ) {
    if (hideOnHover != ThemeConfig.instance.hideOnHover) {
      ThemeConfig.instance.hideOnHover = hideOnHover
      StickerPaneService.instance.setStickerHideConfig(
        StickerHideConfig(
          hideOnHover, hideDelayMS
        )
      )
    }
  }
}
