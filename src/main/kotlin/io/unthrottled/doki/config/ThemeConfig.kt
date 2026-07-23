package io.unthrottled.doki.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.SerializablePersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerLevel
import java.util.Locale

@Service
@State(
  name = "DokiDokiThemeConfig",
  storages = [Storage("doki_doki_theme.xml")],
)
class ThemeConfig : SerializablePersistentStateComponent<ThemeConfig.ThemeState>(ThemeState()) {
  companion object {
    fun getInstance(): ThemeConfig = ApplicationManager.getApplication().getService(ThemeConfig::class.java)
  }

  var ignoreScaling: Boolean
    get() = state.ignoreScaling
    set(value) {
      updateState {
        it.copy(ignoreScaling = value)
      }
    }
  var hideOnHover: Boolean
    get() = state.hideOnHover
    set(value) {
      updateState {
        it.copy(hideOnHover = value)
      }
    }
  var hideDelayMS: Int
    get() = state.hideDelayMS
    set(value) {
      updateState {
        it.copy(hideDelayMS = value)
      }
    }
  var savedMargins: String
    get() = state.savedMargins
    set(value) {
      updateState {
        it.copy(savedMargins = value)
      }
    }
  var userId: String
    get() = state.userId
    set(value) {
      updateState {
        it.copy(userId = value)
      }
    }
  var isMoveableStickers: Boolean
    get() = state.isMoveableStickers
    set(value) {
      updateState {
        it.copy(isMoveableStickers = value)
      }
    }
  var isNotShowReadmeAtStartup: Boolean
    get() = state.isNotShowReadmeAtStartup
    set(value) {
      updateState {
        it.copy(isNotShowReadmeAtStartup = value)
      }
    }
  var version: String
    get() = state.version
    set(value) {
      updateState {
        it.copy(version = value)
      }
    }
  var stickerLevel: String
    get() = state.stickerLevel
    set(value) {
      updateState {
        it.copy(stickerLevel = value)
      }
    }
  var isDokiBackground: Boolean
    get() = state.isDokiBackground
    set(value) {
      updateState {
        it.copy(isDokiBackground = value)
      }
    }
  var isEmptyFrameBackground: Boolean
    get() = state.isEmptyFrameBackground
    set(value) {
      updateState {
        it.copy(isEmptyFrameBackground = value)
      }
    }
  var showThemeStatusBar: Boolean
    get() = state.showThemeStatusBar
    set(value) {
      updateState {
        it.copy(showThemeStatusBar = value)
      }
    }
  var currentStickerName: String
    get() = state.currentStickerName
    set(value) {
      updateState {
        it.copy(currentStickerName = value)
      }
    }
  var isGlobalFontSize: Boolean
    get() = state.isGlobalFontSize
    set(value) {
      updateState {
        it.copy(isGlobalFontSize = value)
      }
    }
  var customFontSize: Int
    get() = state.customFontSize
    set(value) {
      updateState {
        it.copy(customFontSize = value)
      }
    }
  var isOverrideConsoleFont: Boolean
    get() = state.isOverrideConsoleFont
    set(value) {
      updateState {
        it.copy(isOverrideConsoleFont = value)
      }
    }
  var consoleFontName: String
    get() = state.consoleFontName
    set(value) {
      updateState {
        it.copy(consoleFontName = value)
      }
    }
  var capStickerDimensions: Boolean
    get() = state.capStickerDimensions
    set(value) {
      updateState {
        it.copy(capStickerDimensions = value)
      }
    }
  var maxStickerWidth: Int
    get() = state.maxStickerWidth
    set(value) {
      updateState {
        it.copy(maxStickerWidth = value)
      }
    }
  var maxStickerHeight: Int
    get() = state.maxStickerHeight
    set(value) {
      updateState {
        it.copy(maxStickerHeight = value)
      }
    }
  var showSmallStickers: Boolean
    get() = state.showSmallStickers
    set(value) {
      updateState {
        it.copy(showSmallStickers = value)
      }
    }
  var smallMaxStickerWidth: Int
    get() = state.smallMaxStickerWidth
    set(value) {
      updateState {
        it.copy(smallMaxStickerWidth = value)
      }
    }
  var smallMaxStickerHeight: Int
    get() = state.smallMaxStickerHeight
    set(value) {
      updateState {
        it.copy(smallMaxStickerHeight = value)
      }
    }
  var isSeeThroughNotifications: Boolean
    get() = state.isSeeThroughNotifications
    set(value) {
      updateState {
        it.copy(isSeeThroughNotifications = value)
      }
    }
  var notificationOpacity: Int
    get() = state.notificationOpacity
    set(value) {
      updateState {
        it.copy(notificationOpacity = value)
      }
    }
  var discreetMode: Boolean
    get() = state.discreetMode
    set(value) {
      updateState {
        it.copy(discreetMode = value)
      }
    }
  var discreetModeConfig: String
    get() = state.discreetModeConfig
    set(value) {
      updateState {
        it.copy(discreetModeConfig = value)
      }
    }

  var currentSticker: CurrentSticker
    get() {
      val stickerNameType = currentStickerName.uppercase(Locale.getDefault())
      return if (CurrentSticker.entries.none { it.name == stickerNameType }) {
        val defaultSticker = CurrentSticker.DEFAULT
        currentSticker = defaultSticker
        defaultSticker
      } else {
        CurrentSticker.valueOf(stickerNameType)
      }
    }
    set(value) {
      currentStickerName = value.name
    }

  val currentStickerLevel: StickerLevel
    get() {
      val theStickerLevel = stickerLevel.uppercase(Locale.getDefault())
      return if (StickerLevel.entries.none { it.name == theStickerLevel }) {
        val defaultStickerLevel = StickerLevel.ON
        stickerLevel = defaultStickerLevel.name
        defaultStickerLevel
      } else {
        StickerLevel.valueOf(theStickerLevel)
      }
    }

  data class ThemeState(
    @JvmField var ignoreScaling: Boolean = false,
    @JvmField var hideOnHover: Boolean = false,
    @JvmField var hideDelayMS: Int = 750,
    @JvmField var savedMargins: String = "{}",
    @JvmField var userId: String = "",
    @JvmField var isMoveableStickers: Boolean = false,
    @JvmField var isNotShowReadmeAtStartup: Boolean = false,
    @JvmField var version: String = "0.0.0",
    @JvmField var stickerLevel: String = StickerLevel.ON.name,
    @JvmField var isDokiBackground: Boolean = false,
    @JvmField var isEmptyFrameBackground: Boolean = true,
    @JvmField var showThemeStatusBar: Boolean = true,
    @JvmField var currentStickerName: String = CurrentSticker.DEFAULT.name,
    @JvmField var isGlobalFontSize: Boolean = false,
    @JvmField var customFontSize: Int = 13,
    @JvmField var isOverrideConsoleFont: Boolean = false,
    @JvmField var consoleFontName: String = "JetBrains Mono",
    @JvmField var capStickerDimensions: Boolean = false,
    @JvmField var maxStickerWidth: Int = -1,
    @JvmField var maxStickerHeight: Int = -1,
    @JvmField var showSmallStickers: Boolean = false,
    @JvmField var smallMaxStickerWidth: Int = 100,
    @JvmField var smallMaxStickerHeight: Int = 100,
    @JvmField var isSeeThroughNotifications: Boolean = false,
    @JvmField var notificationOpacity: Int = 90,
    @JvmField var discreetMode: Boolean = false,
    @JvmField var discreetModeConfig: String = "{}",
  )
}
