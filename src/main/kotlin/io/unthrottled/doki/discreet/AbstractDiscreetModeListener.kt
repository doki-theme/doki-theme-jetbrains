package io.unthrottled.doki.discreet

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.BackgroundActor
import io.unthrottled.doki.settings.actors.EmptyFrameBackgroundActor
import io.unthrottled.doki.settings.actors.StickerActor
import io.unthrottled.doki.settings.actors.ThemeStatusBarActor
import io.unthrottled.doki.stickers.EditorBackgroundWallpaperService
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafelyWithResult

abstract class AbstractDiscreetModeListener : DiscreetModeListener, Logging {
  private val gson = Gson()
  protected var currentMode = ThemeConfig.instance.discreetMode.toDiscreetMode()

  override fun modeChanged(discreetMode: DiscreetMode) {
    if (discreetMode != currentMode) {
      if (discreetMode == DiscreetMode.ACTIVE) {
        applyDiscreetMode()
      } else {
        liftDiscreetMode()
      }
    }
  }

  private fun liftDiscreetMode() {
    if (currentMode == DiscreetMode.INACTIVE) return
    currentMode = DiscreetMode.INACTIVE
    val discreetModeConfig = ThemeConfig.instance.discreetModeConfig
    val restorationConfig = runSafelyWithResult({
      gson.fromJson(
        discreetModeConfig,
        object : TypeToken<DiscreetModeRestorationConfig>() {}.type
      )
    }) {
      logger().warn("Unable to read discreet mode restoration config $discreetModeConfig", it)
      captureRestorationConfig()
    }
    ThemeConfig.instance.discreetModeConfig = "{}"
    ThemeConfig.instance.showThemeStatusBar = restorationConfig.statusBarWidgetEnabled ?: true

    StickerActor.setStickers(true, restorationConfig.stickersEnabled == true)

    EmptyFrameBackgroundActor.handleBackgroundUpdate(restorationConfig.emptyWallpaperEnabled == true)

    ThemeConfig.instance.isDokiBackground = restorationConfig.editorWallpaperEnabled ?: true
    val savedBackground = restorationConfig.savedBackground
    if (savedBackground?.isNotEmpty() == true) {
      EditorBackgroundWallpaperService.instance.setBackgroundValue(savedBackground)
    }

    ThemeConfig.instance.discreetMode = false
    publishChanges()
  }

  private fun applyDiscreetMode() {
    if (currentMode == DiscreetMode.ACTIVE) return
    currentMode = DiscreetMode.ACTIVE
    val restorationConfig = gson.toJson(captureRestorationConfig())
    ThemeConfig.instance.discreetModeConfig = restorationConfig
    ThemeStatusBarActor.applyConfig(false)
    BackgroundActor.handleBackgroundUpdate(false)
    EmptyFrameBackgroundActor.handleBackgroundUpdate(false)
    StickerActor.enableStickers(enabled = false, withAnimation = true)
    ThemeConfig.instance.discreetMode = true
    publishChanges()
  }

  private fun publishChanges() {
    ApplicationManager.getApplication().messageBus.syncPublisher(THEME_CONFIG_TOPIC)
      .themeConfigUpdated(ThemeConfig.instance)
    dispatchExtraEvents()
    ProjectManager.getInstance().openProjects.forEach {
      it.messageBus.syncPublisher(DiscreetModeListener.DISCREET_MODE_TOPIC)
        .modeChanged(currentMode)
    }
  }

  abstract fun dispatchExtraEvents()

  private fun captureRestorationConfig() = DiscreetModeRestorationConfig(
    ThemeConfig.instance.showThemeStatusBar,
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
    ThemeConfig.instance.isDokiBackground,
    EditorBackgroundWallpaperService.instance.getCurrentBackgroundValue(),
    ThemeConfig.instance.isEmptyFrameBackground,
  )
}

data class DiscreetModeRestorationConfig(
  val statusBarWidgetEnabled: Boolean?,
  val stickersEnabled: Boolean?,
  val editorWallpaperEnabled: Boolean?,
  val savedBackground: String?,
  val emptyWallpaperEnabled: Boolean?,
)
