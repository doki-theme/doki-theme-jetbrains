package io.acari.doki.settings.actors

import com.intellij.ide.ui.LafManager
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.LAFAnimator

object StickerActor {

  fun swapStickers(enabled: Boolean) {
    if (enabled != (ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY)) {
      val animator = LAFAnimator.showSnapshot() //todo: only animate if others are not going to
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
      animator.hideSnapshotWithAnimation()
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