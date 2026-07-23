package io.unthrottled.doki.settings.actors

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.*
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.performWithAnimation

object StickerActor {
  fun swapStickers(
    newStickerType: CurrentSticker,
    withAnimation: Boolean = true,
  ) {
    if (ThemeConfig.getInstance().currentSticker != newStickerType) {
      performWithAnimation(withAnimation) {
        ThemeConfig.getInstance().currentSticker = newStickerType
        ThemeManager.getInstance().currentTheme.ifPresent {
          StickerComponent.activateForTheme(it)
        }
      }
    }
  }

  fun ignoreScaling(ignoreScaling: Boolean) {
    if (ThemeConfig.getInstance().ignoreScaling != ignoreScaling) {
      ApplicationManager.getApplication().executeOnPooledThread {
        ThemeConfig.getInstance().ignoreScaling = ignoreScaling
        ThemeManager.getInstance().currentTheme.ifPresent {
          StickerPaneService.getInstance().setIgnoreScaling(ignoreScaling)
        }
      }
    }
  }

  fun enableStickers(
    enabled: Boolean,
    withAnimation: Boolean = true,
  ) {
    if (enabled != (ThemeConfig.getInstance().currentStickerLevel == StickerLevel.ON)) {
      setStickers(withAnimation, enabled)
    }
  }

  fun setStickers(
    withAnimation: Boolean,
    enabled: Boolean,
  ) {
    performWithAnimation(withAnimation) {
      if (enabled) {
        ThemeConfig.getInstance().stickerLevel = StickerLevel.ON.name
        ThemeManager.getInstance().currentTheme.ifPresent {
          StickerPaneService.getInstance().activateForTheme(it)
        }
      } else {
        ThemeConfig.getInstance().stickerLevel = StickerLevel.OFF.name
        StickerPaneService.getInstance().remove(StickerType.ALL)
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
    val config = ThemeConfig.getInstance()
    val isDifferentDimensions =
      config.maxStickerWidth != maxStickerWidth
        || config.maxStickerHeight != maxStickerHeight
        || config.capStickerDimensions != capStickerDimensions

    config.maxStickerWidth = maxStickerWidth
    config.maxStickerHeight = maxStickerHeight
    config.capStickerDimensions = capStickerDimensions
    if (isDifferentDimensions) {
      activateNewSticker(false)
    }
  }

  fun setSmolStickers(
    isSmolStickers: Boolean,
    smolMaxStickerWidth: Int,
    smolMaxStickerHeight: Int,
  ) {
    val config = ThemeConfig.getInstance()
    val isDifferentDimensions = config.smallMaxStickerWidth != smolMaxStickerWidth
      || config.smallMaxStickerHeight != smolMaxStickerHeight
      || config.showSmallStickers != isSmolStickers

    config.smallMaxStickerWidth = smolMaxStickerWidth
    config.smallMaxStickerHeight = smolMaxStickerHeight
    config.showSmallStickers = isSmolStickers

    if (isDifferentDimensions) {
      if (ThemeConfig.getInstance().showSmallStickers) {
        activateNewSticker(false)
      } else {
        StickerPaneService.getInstance().remove(StickerType.SMOL)
      }
    }
  }

  private fun activateNewSticker(withAnimation: Boolean) {
    performWithAnimation(withAnimation) {
      ThemeManager.getInstance().currentTheme.ifPresent {
        StickerPaneService.getInstance().activateForTheme(it)
      }
    }
  }
}
