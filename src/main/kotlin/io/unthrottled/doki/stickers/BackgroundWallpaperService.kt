package io.unthrottled.doki.stickers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.Fill
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.repaintAllWindows
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService.ASSET_DIRECTORY
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toOptional
import java.util.Optional

const val DOKI_BACKGROUND_PROP: String = "io.unthrottled.doki.background"
private const val PREVIOUS_BACKGROUND = "io.unthrottled.doki.previous-background"

const val DOKI_STICKER_PROP: String = "io.unthrottled.doki.stickers"
private const val PREVIOUS_STICKER = "io.unthrottled.doki.sticker.previous"

// todo: fix this
@Suppress("TooManyFunctions")
internal class BackgroundWallpaperService {

  companion object {
    val instance: BackgroundWallpaperService
      get() = ServiceManager.getService(BackgroundWallpaperService::class.java)
  }

  init {
    // todo: remove after next major release
    val propertiesComponent = PropertiesComponent.getInstance()
    propertiesComponent.unsetValue(DOKI_STICKER_PROP)
    propertiesComponent.unsetValue(PREVIOUS_STICKER)
  }

  fun activateForTheme(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      installFrameBackgroundImage(dokiTheme)
      if (ThemeConfig.instance.isDokiBackground) {
        installEditorBackgroundImage(dokiTheme)
      }
    }
  }

  fun checkForUpdates(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      getLocallyInstalledBackgroundImagePath(dokiTheme)
    }
  }

  private fun installFrameBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledBackgroundImagePath(dokiTheme)
      .ifPresent {
        setBackgroundImageProperty(
          it.first,
          "100",
          Fill.SCALE.name,
          it.second.position.name,
          DOKI_BACKGROUND_PROP
        )
      }

  private fun installEditorBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledWallpaperImagePath(dokiTheme)
      .ifPresent {
        capturePrevious()
        setBackgroundImageProperty(
          it.first,
          if (dokiTheme.isDark) "5" else "10",
          Fill.SCALE.name,
          it.second.position.name,
          EDITOR_PROP
        )
      }

  private fun capturePrevious() {
    PropertiesComponent.getInstance().getValue(EDITOR_PROP)
      .toOptional()
      .filter { it.contains(ASSET_DIRECTORY).not() }
      .ifPresent {
        PropertiesComponent.getInstance().setValue(PREVIOUS_BACKGROUND, it)
      }
  }

  private fun getLocallyInstalledBackgroundImagePath(
    dokiTheme: DokiTheme
  ): Optional<Pair<String, Background>> =
    dokiTheme.getBackground()
      .flatMap { background ->
        AssetManager.resolveAssetUrl(
          AssetCategory.BACKGROUNDS,
          background.name
        ).map { it to background }
      }

  private fun getLocallyInstalledWallpaperImagePath(
    dokiTheme: DokiTheme
  ): Optional<Pair<String, Background>> =
    dokiTheme.getBackground()
      .flatMap { background ->
        AssetManager.resolveAssetUrl(
          AssetCategory.BACKGROUNDS,
          "wallpapers/${background.name}"
        ).map { it to background }
      }

  fun remove() {
    val propertiesComponent = PropertiesComponent.getInstance()
    propertiesComponent.unsetValue(DOKI_BACKGROUND_PROP)
    if (ThemeConfig.instance.isDokiBackground) {
      removeEditorWallpaper(propertiesComponent)
    }
    repaintWindows()
  }

  private fun removeEditorWallpaper(propertiesComponent: PropertiesComponent) {
    propertiesComponent.getValue(PREVIOUS_BACKGROUND).toOptional()
      .doOrElse({
        propertiesComponent.setValue(EDITOR_PROP, it)
        propertiesComponent.unsetValue(PREVIOUS_BACKGROUND)
      }) {
        PropertiesComponent.getInstance().getValue(EDITOR_PROP)
          .toOptional()
          .filter { it.contains(ASSET_DIRECTORY) }
          .ifPresent { propertiesComponent.unsetValue(EDITOR_PROP) }
      }
  }

  fun removeEditorBackground() {
    val propertiesComponent = PropertiesComponent.getInstance()
    removeEditorWallpaper(propertiesComponent)
    repaintWindows()
  }

  private fun repaintWindows() = runSafely({
    repaintAllWindows()
  })

  fun enableEditorBackground() {
    ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
      installEditorBackgroundImage(dokiTheme)
    }
  }
}

private fun setBackgroundImageProperty(
  imagePath: String,
  opacity: String,
  fill: String,
  anchor: String,
  propertyKey: String
) {
  // org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
  // as to why this looks this way
  val propertyValue = listOf(imagePath, opacity, fill, anchor)
    .reduceRight { a, b -> "$a,$b" }
  setPropertyValue(propertyKey, propertyValue)
}

private fun setPropertyValue(propertyKey: String, propertyValue: String) {
  PropertiesComponent.getInstance().unsetValue(propertyKey)
  PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
}
