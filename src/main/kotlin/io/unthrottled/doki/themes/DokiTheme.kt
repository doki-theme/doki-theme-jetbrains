package io.unthrottled.doki.themes

import com.google.gson.Gson
import com.intellij.ide.ui.UIThemeMetadata
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.promotions.CulturedContentManager
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toColor
import io.unthrottled.doki.util.toOptional
import java.awt.Color
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Optional

class Stickers(
  val default: String,
  val secondary: String?
)

class Background(
  val name: String,
  val position: IdeBackgroundUtil.Anchor,
  val opacity: Int?,
)

class Backgrounds(
  val default: Background,
  val secondary: Background?
)

@Suppress("LongParameterList")
class JetBrainsThemeDefinition(
  val id: String,
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val stickers: Stickers,
  val backgrounds: Backgrounds,
  val group: String,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val meta: Map<String, String>
)

class DokiTheme(private val uiTheme: JetBrainsThemeDefinition) {

  init {
    validateThemeDefinition()
  }

  val stickers: Stickers
    get() = uiTheme.stickers

  val id: String
    get() = uiTheme.id

  val groupName: String
    get() = uiTheme.group
  val isDark: Boolean
    get() = uiTheme.dark

  val name: String
    get() = uiTheme.name

  val displayName: String
    get() = uiTheme.displayName ?: throw IllegalStateException(
      """
|$name's theme.json requires "displayName" to be defined""".trimMargin()
    )

  fun getStickerPath(): Optional<String> {
    return when (ThemeConfig.instance.currentSticker) {
      CurrentSticker.DEFAULT -> uiTheme.stickers.default.toOptional()
      CurrentSticker.SECONDARY -> CulturedContentManager.safelyGet(this)
    }
  }

  fun getBackground(): Optional<Background> {
    return when (ThemeConfig.instance.currentSticker) {
      CurrentSticker.DEFAULT -> uiTheme.backgrounds.default
      CurrentSticker.SECONDARY -> uiTheme.backgrounds.secondary ?: uiTheme.backgrounds.default
    }.toOptional()
  }

  fun getSticker(): Optional<String> =
    getStickerPath()
      .map { it.substring(it.lastIndexOf("/") + 1) }

  val isVivid: Boolean
    get() = uiTheme.meta.getOrDefault("isVivid", "false") == "true"

  val nonProjectFileScopeColor: String
    get() = uiTheme.colors["nonProjectFileScopeColor"] as? String
      ?: throw IllegalStateException("Expected 'colors.nonProjectFileScopeColor' to be present in $name theme json")

  val testScopeColor: String
    get() = uiTheme.colors["testScopeColor"] as? String
      ?: throw IllegalStateException("Expected 'colors.testScopeColor' to be present in theme $name json.")

  val contrastColor: Color
    get() = (uiTheme.colors["contrastColor"] as? String)?.toColor()
      ?: throw IllegalStateException("Expected 'colors.contrastColor' to be present in theme $name json.")

  companion object {
    const val ACCENT_COLOR = "Doki.Accent.color"

    val requiredNamedColors: List<String>
    private val log = Logger.getInstance(this::class.java)

    init {
      val gson = Gson()
      requiredNamedColors = runSafelyWithResult({
        this::class.java.classLoader
          .getResourceAsStream("theme-schema/DokiTheme.themeMetadata.json")
          .use {
            gson.fromJson(
              InputStreamReader(it!!, StandardCharsets.UTF_8),
              UIThemeMetadata::class.java
            )
          }.uiKeyMetadata.map {
            it.key
          }.toMutableList().plus(ACCENT_COLOR)
      }) {
        log.warn("Unable to get required named colors for raisins", it)
        listOf()
      }
    }
  }

  private fun validateThemeDefinition() {
    nonProjectFileScopeColor
    testScopeColor
    displayName

    requiredNamedColors.forEach { requiredColor ->
      val requiredNamedColor = uiTheme.ui[requiredColor] as? String
      try {
        if (requiredNamedColor!!.startsWith("#")) {
          requiredNamedColor.toColor()
        } else {
          (uiTheme.colors[requiredNamedColor] as String).toColor()
        }
      } catch (e: Throwable) {
        throw IllegalStateException(
          """
          Expected "$requiredColor" to be present in "colors" as a valid hex color in $name's theme json.
           "$requiredNamedColor" is not a valid hex color or reference to a defined color
          """.trimIndent()
        )
      }
    }
  }
}
