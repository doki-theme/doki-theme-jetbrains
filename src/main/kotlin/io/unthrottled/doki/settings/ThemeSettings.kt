package io.unthrottled.doki.settings

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.*
import io.unthrottled.doki.stickers.CustomStickerService
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerPaneService
import io.unthrottled.doki.themes.ThemeManager

object ThemeSettings {
  const val SETTINGS_ID = "io.unthrottled.doki.settings.ThemeSettings"
  const val THEME_SETTINGS_DISPLAY_NAME = "Doki Theme Settings"

  @JvmStatic
  fun createThemeSettingsModel(): ThemeSettingsModel =
    ThemeSettingsModel(
      areStickersEnabled = ThemeConfig.getInstance().currentStickerLevel == StickerLevel.ON,
      currentTheme = ThemeManager.getInstance().currentTheme.map { it.name }
        .orElseGet { ThemeManager.DEFAULT_THEME_NAME },
      showThemeStatusBar = ThemeConfig.getInstance().showThemeStatusBar,
      currentSticker = ThemeConfig.getInstance().currentSticker,
      isNotShowReadmeAtStartup = ThemeConfig.getInstance().isNotShowReadmeAtStartup,
      isMoveableStickers = ThemeConfig.getInstance().isMoveableStickers,
      isDokiBackground = ThemeConfig.getInstance().isDokiBackground,
      discreetMode = ThemeConfig.getInstance().discreetMode,
      isEmptyFrameBackground = ThemeConfig.getInstance().isEmptyFrameBackground,
      isCustomSticker = CustomStickerService.isCustomStickers,
      customStickerPath = CustomStickerService.getCustomStickerPath().orElse(""),
      isCustomFontSize = ThemeConfig.getInstance().isGlobalFontSize,
      customFontSizeValue = ThemeConfig.getInstance().customFontSize,
      isSeeThroughNotifications = ThemeConfig.getInstance().isSeeThroughNotifications,
      notificationOpacity = ThemeConfig.getInstance().notificationOpacity,
      isOverrideConsoleFont = ThemeConfig.getInstance().isOverrideConsoleFont,
      consoleFontValue = ThemeConfig.getInstance().consoleFontName,
      maxStickerHeight = ThemeConfig.getInstance().maxStickerHeight,
      maxStickerWidth = ThemeConfig.getInstance().maxStickerWidth,
      capStickerDimensions = ThemeConfig.getInstance().capStickerDimensions,
      smallMaxStickerHeight = ThemeConfig.getInstance().smallMaxStickerHeight,
      smallMaxStickerWidth = ThemeConfig.getInstance().smallMaxStickerWidth,
      showSmallStickers = ThemeConfig.getInstance().showSmallStickers,
      ignoreScaling = ThemeConfig.getInstance().ignoreScaling,
      hideOnHover = ThemeConfig.getInstance().hideOnHover,
      hideDelayMS = ThemeConfig.getInstance().hideDelayMS,
    )

  fun apply(themeSettingsModel: ThemeSettingsModel) {
    StickerActor.enableStickers(themeSettingsModel.areStickersEnabled, false)
    StickerActor.swapStickers(themeSettingsModel.currentSticker, false)
    StickerActor.ignoreScaling(themeSettingsModel.ignoreScaling)
    StickerActor.setCustomSticker(
      themeSettingsModel.customStickerPath,
      themeSettingsModel.isCustomSticker,
      false,
    )
    StickerActor.setDimensionCapping(
      themeSettingsModel.capStickerDimensions,
      themeSettingsModel.maxStickerWidth,
      themeSettingsModel.maxStickerHeight,
    )
    StickerActor.setSmolStickers(
      themeSettingsModel.showSmallStickers,
      themeSettingsModel.smallMaxStickerWidth,
      themeSettingsModel.smallMaxStickerHeight,
    )
    ThemeStatusBarActor.applyConfig(themeSettingsModel.showThemeStatusBar)
    MoveableStickerActor.moveableStickers(themeSettingsModel.isMoveableStickers)
    BackgroundActor.handleBackgroundUpdate(themeSettingsModel.isDokiBackground)
    EmptyFrameBackgroundActor.handleBackgroundUpdate(themeSettingsModel.isEmptyFrameBackground)
    CustomFontSizeActor.enableCustomFontSize(
      themeSettingsModel.isCustomFontSize,
      themeSettingsModel.customFontSizeValue,
    )
    ConsoleFontActor.enableCustomFontSize(
      themeSettingsModel.isOverrideConsoleFont,
      themeSettingsModel.consoleFontValue,
    )
    SeeThroughNotificationsActor.enableSeeThroughNotifications(
      themeSettingsModel.isSeeThroughNotifications,
      themeSettingsModel.notificationOpacity,
    )
    DiscreetModeActor.enableDiscreetMode(themeSettingsModel.discreetMode)
    StickerHideActor.setStickerHideStuff(themeSettingsModel.hideOnHover, themeSettingsModel.hideDelayMS)
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC,
    ).themeConfigUpdated(ThemeConfig.getInstance())
  }

  fun resetStickerPosition() {
    StickerPaneService.getInstance().resetMargins()
  }
}
