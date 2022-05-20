package io.unthrottled.doki.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil.copyBean
import com.intellij.util.xmlb.XmlSerializerUtil.createCopy
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerLevel
import java.util.Locale

@State(
  name = "DokiDokiThemeConfig",
  storages = [Storage("doki_doki_theme.xml")]
)
class ThemeConfig : PersistentStateComponent<ThemeConfig>, Cloneable {
  companion object {
    val instance: ThemeConfig
      get() = ServiceManager.getService(ThemeConfig::class.java)
  }

  var ignoreScaling: Boolean = false
  var hideOnHover: Boolean = false
  var hideDelayMS: Int = 750
  var savedMargins: String = "{}"
  var userId: String = ""
  var isLafAnimation: Boolean = false
  var isMoveableStickers: Boolean = false
  var isNotShowReadmeAtStartup: Boolean = false
  var version: String = "0.0.0"
  var chibiLevel: String = StickerLevel.ON.name
  var stickerLevel: String = StickerLevel.ON.name
  var isFirstTime: Boolean = true
  var isDokiFileColors: Boolean = false
  var isDokiBackground: Boolean = false
  var isEmptyFrameBackground: Boolean = true
  var isThemedTitleBar: Boolean = false // todo: add migration if build is 2022.2
  var showThemeStatusBar: Boolean = true
  var currentStickerName: String = CurrentSticker.DEFAULT.name

  var isGlobalFontSize: Boolean = false
  var customFontSize: Int = 13

  var isOverrideConsoleFont: Boolean = false
  var consoleFontName: String = "JetBrains Mono"

  var isMaterialDirectories: Boolean = false
  var isMaterialFiles: Boolean = false
  var isMaterialPSIIcons: Boolean = false

  var capStickerDimensions: Boolean = false
  var maxStickerWidth: Int = -1
  var maxStickerHeight: Int = -1

  var showSmallStickers: Boolean = false
  var smallMaxStickerWidth: Int = 100
  var smallMaxStickerHeight: Int = 100

  var isSeeThroughNotifications: Boolean = false
  var notificationOpacity: Int = 90

  var discreetMode = false
  var discreetModeConfig = "{}"

  override fun getState(): ThemeConfig? =
    createCopy(this)

  override fun loadState(state: ThemeConfig) {
    copyBean(state, this)
  }

  fun asJson(): Map<String, Any> = mapOf(
    "version" to version,
    "chibiLevel" to chibiLevel,
    "stickerLevel" to stickerLevel,
    "isDokiFileColors" to isDokiFileColors,
    "currentSticker" to currentSticker,
    "isMaterialDirectories" to isMaterialDirectories,
    "isMaterialFiles" to isMaterialFiles,
    "isMaterialPSIIcons" to isMaterialPSIIcons
  )

  var currentSticker: CurrentSticker
    get() {
      val stickerNameType = currentStickerName.uppercase(Locale.getDefault())
      return if (CurrentSticker.values().none { it.name == stickerNameType }) {
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
      return if (StickerLevel.values().none { it.name == theStickerLevel }) {
        val defaultStickerLevel = StickerLevel.ON
        stickerLevel = defaultStickerLevel.name
        defaultStickerLevel
      } else {
        StickerLevel.valueOf(theStickerLevel)
      }
    }
}
