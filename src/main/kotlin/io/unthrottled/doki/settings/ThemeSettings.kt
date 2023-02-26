package io.unthrottled.doki.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.FontComboBox
import com.intellij.util.ui.FontInfo
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.BackgroundActor
import io.unthrottled.doki.settings.actors.ConsoleFontActor
import io.unthrottled.doki.settings.actors.CustomFontSizeActor
import io.unthrottled.doki.settings.actors.DiscreetModeActor
import io.unthrottled.doki.settings.actors.EmptyFrameBackgroundActor
import io.unthrottled.doki.settings.actors.LafAnimationActor
import io.unthrottled.doki.settings.actors.MoveableStickerActor
import io.unthrottled.doki.settings.actors.PromotionSettingActor
import io.unthrottled.doki.settings.actors.SeeThroughNotificationsActor
import io.unthrottled.doki.settings.actors.ShowReadmeActor
import io.unthrottled.doki.settings.actors.StickerActor
import io.unthrottled.doki.settings.actors.StickerHideActor
import io.unthrottled.doki.settings.actors.ThemeActor
import io.unthrottled.doki.settings.actors.ThemeStatusBarActor
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.CustomStickerService
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.themes.ThemeManager
import java.net.URI
import java.util.Vector
import javax.swing.DefaultComboBoxModel

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var isLafAnimation: Boolean,
  var currentTheme: String,
  var showThemeStatusBar: Boolean,
  var currentSticker: CurrentSticker,
  var isNotShowReadmeAtStartup: Boolean,
  var isMoveableStickers: Boolean,
  var isDokiBackground: Boolean,
  var discreetMode: Boolean,
  var isEmptyFrameBackground: Boolean,
  var isCustomSticker: Boolean,
  var allowPromotionalContent: Boolean,
  var customStickerPath: String,
  var isCustomFontSize: Boolean,
  var customFontSizeValue: Int,
  var isOverrideConsoleFont: Boolean,
  var capStickerDimensions: Boolean,
  var maxStickerHeight: Int,
  var maxStickerWidth: Int,
  var showSmallStickers: Boolean,
  var smallMaxStickerHeight: Int,
  var smallMaxStickerWidth: Int,
  var consoleFontValue: String,
  var ignoreScaling: Boolean,
  var hideOnHover: Boolean,
  var hideDelayMS: Int,
  var isSeeThroughNotifications: Boolean,
  var notificationOpacity: Int
) {

  fun duplicate(): ThemeSettingsModel = copy()
}

object ThemeSettings {

  const val SETTINGS_ID = "io.unthrottled.doki.settings.ThemeSettings"
  const val THEME_SETTINGS_DISPLAY_NAME = "Doki Theme Settings"
  val CHANGELOG_URI =
    URI("https://github.com/doki-theme/doki-theme-jetbrains/blob/main/changelog/CHANGELOG.md")
  private const val REPOSITORY = "https://github.com/doki-theme/doki-theme-jetbrains"
  const val ULTIMATE_INSTRUCTIONS = "$REPOSITORY/wiki/Ultimate-Theme-Setup"
  val ISSUES_URI = URI("$REPOSITORY/issues")
  val REVIEW_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-theme/reviews")

  @JvmStatic
  fun createThemeSettingsModel(): ThemeSettingsModel =
    ThemeSettingsModel(
      areStickersEnabled = ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
      isLafAnimation = ThemeConfig.instance.isLafAnimation,
      currentTheme = ThemeManager.instance.currentTheme.map { it.name }.orElseGet { ThemeManager.DEFAULT_THEME_NAME },
      showThemeStatusBar = ThemeConfig.instance.showThemeStatusBar,
      currentSticker = ThemeConfig.instance.currentSticker,
      isNotShowReadmeAtStartup = ThemeConfig.instance.isNotShowReadmeAtStartup,
      isMoveableStickers = ThemeConfig.instance.isMoveableStickers,
      isDokiBackground = ThemeConfig.instance.isDokiBackground,
      discreetMode = ThemeConfig.instance.discreetMode,
      isEmptyFrameBackground = ThemeConfig.instance.isEmptyFrameBackground,
      isCustomSticker = CustomStickerService.isCustomStickers,
      customStickerPath = CustomStickerService.getCustomStickerPath().orElse(""),
      isCustomFontSize = ThemeConfig.instance.isGlobalFontSize,
      customFontSizeValue = ThemeConfig.instance.customFontSize,
      isSeeThroughNotifications = ThemeConfig.instance.isSeeThroughNotifications,
      notificationOpacity = ThemeConfig.instance.notificationOpacity,
      isOverrideConsoleFont = ThemeConfig.instance.isOverrideConsoleFont,
      consoleFontValue = ThemeConfig.instance.consoleFontName,
      maxStickerHeight = ThemeConfig.instance.maxStickerHeight,
      maxStickerWidth = ThemeConfig.instance.maxStickerWidth,
      capStickerDimensions = ThemeConfig.instance.capStickerDimensions,
      smallMaxStickerHeight = ThemeConfig.instance.smallMaxStickerHeight,
      smallMaxStickerWidth = ThemeConfig.instance.smallMaxStickerWidth,
      showSmallStickers = ThemeConfig.instance.showSmallStickers,
      ignoreScaling = ThemeConfig.instance.ignoreScaling,
      hideOnHover = ThemeConfig.instance.hideOnHover,
      hideDelayMS = ThemeConfig.instance.hideDelayMS,
      allowPromotionalContent = ThemeConfig.instance.allowPromotions
    )

  fun apply(themeSettingsModel: ThemeSettingsModel) {
    LafAnimationActor.enableAnimation(themeSettingsModel.isLafAnimation)
    ShowReadmeActor.dontShowReadmeOnStartup(themeSettingsModel.isNotShowReadmeAtStartup)
    StickerActor.enableStickers(themeSettingsModel.areStickersEnabled, false)
    StickerActor.swapStickers(themeSettingsModel.currentSticker, false)
    StickerActor.ignoreScaling(themeSettingsModel.ignoreScaling)
    StickerActor.setCustomSticker(
      themeSettingsModel.customStickerPath,
      themeSettingsModel.isCustomSticker,
      false
    )
    StickerActor.setDimensionCapping(
      themeSettingsModel.capStickerDimensions,
      themeSettingsModel.maxStickerWidth,
      themeSettingsModel.maxStickerHeight
    )
    StickerActor.setSmolStickers(
      themeSettingsModel.showSmallStickers,
      themeSettingsModel.smallMaxStickerWidth,
      themeSettingsModel.smallMaxStickerHeight
    )
    ThemeActor.applyTheme(themeSettingsModel.currentTheme)
    ThemeStatusBarActor.applyConfig(themeSettingsModel.showThemeStatusBar)
    MoveableStickerActor.moveableStickers(themeSettingsModel.isMoveableStickers)
    BackgroundActor.handleBackgroundUpdate(themeSettingsModel.isDokiBackground)
    EmptyFrameBackgroundActor.handleBackgroundUpdate(themeSettingsModel.isEmptyFrameBackground)
    CustomFontSizeActor.enableCustomFontSize(
      themeSettingsModel.isCustomFontSize,
      themeSettingsModel.customFontSizeValue
    )
    ConsoleFontActor.enableCustomFontSize(
      themeSettingsModel.isOverrideConsoleFont,
      themeSettingsModel.consoleFontValue
    )
    SeeThroughNotificationsActor.enableSeeThroughNotifications(
      themeSettingsModel.isSeeThroughNotifications,
      themeSettingsModel.notificationOpacity
    )
    DiscreetModeActor.enableDiscreetMode(themeSettingsModel.discreetMode)
    StickerHideActor.setStickerHideStuff(themeSettingsModel.hideOnHover, themeSettingsModel.hideDelayMS)
    PromotionSettingActor.optInToPromotion(themeSettingsModel.allowPromotionalContent)
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeConfig.instance)
  }

  fun createThemeComboBoxModel(settingsSupplier: () -> ThemeSettingsModel): ComboBox<String> {
    val themeComboBox = ComboBox(
      DefaultComboBoxModel(
        Vector(
          ThemeManager.instance.allThemes
            .sortedBy { theme -> theme.name }
            .map { it.name }
        )
      )
    )
    themeComboBox.model.selectedItem = settingsSupplier().currentTheme
    themeComboBox.addActionListener {
      settingsSupplier().currentTheme = themeComboBox.model.selectedItem as String
    }
    return themeComboBox
  }

  fun createConsoleFontComboBoxModel(settingsSupplier: () -> ThemeSettingsModel): FontComboBox {
    val fontComboBox = FontComboBox()
    fontComboBox.addActionListener {
      val fontInfo = fontComboBox.model.selectedItem as? FontInfo
      if (fontInfo != null) {
        settingsSupplier().consoleFontValue = fontInfo.font.name
      }
    }
    return fontComboBox
  }
}
