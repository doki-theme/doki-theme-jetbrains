package io.unthrottled.doki.stickers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.Fill
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService.ASSET_DIRECTORY
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional

const val DOKI_BACKGROUND_PROP: String = "io.unthrottled.doki.background"
private const val PREVIOUS_BACKGROUND = "io.unthrottled.doki.previous-background"

@Suppress("TooManyFunctions") // cuz I said so
internal class EditorBackgroundWallpaperService {
  companion object {
    val instance: EditorBackgroundWallpaperService
      get() = ApplicationManager.getApplication().getService(EditorBackgroundWallpaperService::class.java)
  }

  fun activateForTheme(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      if (ThemeConfig.instance.isDokiBackground) {
        installEditorBackgroundImage(dokiTheme)
      }
    }
  }

  fun checkForUpdates(dokiTheme: DokiTheme) {
    ApplicationManager.getApplication().executeOnPooledThread {
      getLocallyInstalledWallpaperImagePath(dokiTheme)
    }
  }

  private fun installEditorBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledWallpaperImagePath(dokiTheme)
      .doOrElse({
        val isSameWallpaper =
          getCurrentBackgroundValue()
            .startsWith(it.first)

        if (isSameWallpaper) return@doOrElse

        capturePrevious()
        setBackgroundImageProperty(
          it.first,
          it.second.opacity?.toString() ?: if (dokiTheme.isDark) "5" else "10",
          Fill.SCALE.name,
          it.second.position.name,
          EDITOR_PROP,
        )
      }) {
        if (getNonDokiBackground().isPresent().not()) { // y u no like isEmpty() ???
          remove()
        }
      }

  fun getCurrentBackgroundValue() =
    PropertiesComponent.getInstance()
      .getValue(EDITOR_PROP, "")

  fun setBackgroundValue(backgroundProperty: String) {
    PropertiesComponent.getInstance()
      .setValue(EDITOR_PROP, backgroundProperty)
    repaintWindows()
  }

  private fun capturePrevious() {
    getNonDokiBackground()
      .ifPresent {
        PropertiesComponent.getInstance().setValue(PREVIOUS_BACKGROUND, it)
      }
  }

  private fun getNonDokiBackground() =
    PropertiesComponent.getInstance().getValue(EDITOR_PROP)
      .toOptional()
      .filter { it.contains(ASSET_DIRECTORY).not() }

  private fun getLocallyInstalledWallpaperImagePath(dokiTheme: DokiTheme): Optional<Pair<String, Background>> =
    dokiTheme.getBackground()
      .flatMap { background ->
        AssetManager.resolveAssetUrl(
          AssetCategory.BACKGROUNDS,
          "wallpapers/${background.name}",
        ).map { it to background }
      }

  fun remove() {
    val propertiesComponent = PropertiesComponent.getInstance()
    removeEditorWallpaper(propertiesComponent)
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

  fun enableEditorBackground() {
    ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
      installEditorBackgroundImage(dokiTheme)
      repaintWindows()
    }
  }
}

internal fun setBackgroundImageProperty(
  imagePath: String,
  opacity: String,
  fill: String,
  anchor: String,
  propertyKey: String,
) {
  // org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
  // as to why this looks this way
  val propertyValue =
    listOf(imagePath, opacity, fill, anchor)
      .reduceRight { a, b -> "$a,$b" }
  setPropertyValue(propertyKey, propertyValue)
}

private fun setPropertyValue(
  propertyKey: String,
  propertyValue: String,
) {
  PropertiesComponent.getInstance().unsetValue(propertyKey)
  PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
}
