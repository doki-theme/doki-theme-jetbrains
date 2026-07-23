package io.unthrottled.doki.settings

import io.unthrottled.doki.stickers.CurrentSticker

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var currentTheme: String,
  var showThemeStatusBar: Boolean,
  var currentSticker: CurrentSticker,
  var isNotShowReadmeAtStartup: Boolean,
  var isMoveableStickers: Boolean,
  var isDokiBackground: Boolean,
  var discreetMode: Boolean,
  var isEmptyFrameBackground: Boolean,
  var isCustomSticker: Boolean,
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
  var notificationOpacity: Int,
)
