package io.unthrottled.doki.settings.actors

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.CustomStickerService
import io.unthrottled.doki.stickers.StickerComponent
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerPaneService
import io.unthrottled.doki.stickers.StickerType
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.performWithAnimation

object StickerActor {
  fun swapStickers(
    newStickerType: CurrentSticker,
    withAnimation: Boolean = true,
  ) {
    if (ThemeConfig.instance.currentSticker != newStickerType) {
      performWithAnimation(withAnimation) {
        ThemeConfig.instance.currentSticker = newStickerType
        ThemeManager.instance.currentTheme.ifPresent {
          StickerComponent.activateForTheme(it)
        }
      }
    }
  }

  fun ignoreScaling(ignoreScaling: Boolean) {
    if (ThemeConfig.instance.ignoreScaling != ignoreScaling) {
      ApplicationManager.getApplication().executeOnPooledThread {
        ThemeConfig.instance.ignoreScaling = ignoreScaling
        ThemeManager.instance.currentTheme.ifPresent {
          StickerPaneService.instance.setIgnoreScaling(ignoreScaling)
        }
      }
    }
  }

  fun enableStickers(
    enabled: Boolean,
    withAnimation: Boolean = true,
  ) {
    if (enabled != (ThemeConfig.instance.currentStickerLevel == StickerLevel.ON)) {
      setStickers(withAnimation, enabled)
    }
  }

  fun setStickers(
    withAnimation: Boolean,
    enabled: Boolean,
  ) {
    performWithAnimation(withAnimation) {
      if (enabled) {
        ThemeConfig.instance.stickerLevel = StickerLevel.ON.name
        ThemeManager.instance.currentTheme.ifPresent {
          StickerPaneService.instance.activateForTheme(it)
        }
      } else {
        ThemeConfig.instance.stickerLevel = StickerLevel.OFF.name
        StickerPaneService.instance.remove(StickerType.ALL)
      }
    }
  }

  fun setCustomSticker(
    customStickerPath: String,
    isCustomStickers: Boolean,
    withAnimation: Boolean,
  ) {
    val isCustomStickersChanged = CustomStickerService.isCustomStickers != isCustomStickers
    CustomStickerService.isCustomStickers = isCustomStickers

    val isNewStickerPath =
      CustomStickerService.getCustomStickerPath()
        .map { it != customStickerPath }
        .orElse(true) && customStickerPath.isNotBlank()
    if (isNewStickerPath) {
      CustomStickerService.setCustomStickerPath(customStickerPath)
    }

    val shouldReDraw = isNewStickerPath || isCustomStickersChanged
    if (shouldReDraw) {
      activateNewSticker(withAnimation)
    }
  }

  fun setDimensionCapping(
    capStickerDimensions: Boolean,
    maxStickerWidth: Int,
    maxStickerHeight: Int,
  ) {
    val ogWidth = ThemeConfig.instance.maxStickerWidth
    ThemeConfig.instance.maxStickerWidth = maxStickerWidth
    val ogHeight = ThemeConfig.instance.maxStickerHeight
    ThemeConfig.instance.maxStickerHeight = maxStickerHeight
    val ogActivation = ThemeConfig.instance.capStickerDimensions
    ThemeConfig.instance.capStickerDimensions = capStickerDimensions
    if (
      ogHeight != maxStickerHeight ||
      ogWidth != maxStickerWidth ||
      ogActivation != capStickerDimensions
    ) {
      activateNewSticker(false)
    }
  }

  fun setSmolStickers(
    isSmolStickers: Boolean,
    smolMaxStickerWidth: Int,
    smolMaxStickerHeight: Int,
  ) {
    val ogWidth = ThemeConfig.instance.smallMaxStickerWidth
    ThemeConfig.instance.smallMaxStickerWidth = smolMaxStickerWidth
    val ogHeight = ThemeConfig.instance.smallMaxStickerHeight
    ThemeConfig.instance.smallMaxStickerHeight = smolMaxStickerHeight
    val ogActivation = ThemeConfig.instance.showSmallStickers
    ThemeConfig.instance.showSmallStickers = isSmolStickers
    if (
      ogHeight != smolMaxStickerHeight ||
      ogWidth != smolMaxStickerWidth ||
      ogActivation != isSmolStickers
    ) {
      if (ThemeConfig.instance.showSmallStickers) {
        activateNewSticker(false)
      } else {
        StickerPaneService.instance.remove(StickerType.SMOL)
      }
    }
  }

  private fun activateNewSticker(withAnimation: Boolean) {
    performWithAnimation(withAnimation) {
      ThemeManager.instance.currentTheme.ifPresent {
        StickerPaneService.instance.activateForTheme(it)
      }
    }
  }
}
