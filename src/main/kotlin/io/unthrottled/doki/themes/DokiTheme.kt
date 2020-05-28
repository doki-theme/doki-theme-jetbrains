package io.unthrottled.doki.themes

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.util.toColor
import io.unthrottled.doki.util.toOptional
import java.awt.Color
import java.util.*

class Stickers(
  val default: String,
  val secondary: String?
)

class DokiThemeDefinition(
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val stickers: Stickers,
  val group: String,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val meta: Map<String, String>
)

class DokiTheme(private val uiTheme: DokiThemeDefinition) {

  init {
    validateThemeDefinition()
  }

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
      CurrentSticker.DEFAULT -> uiTheme.stickers.default
      CurrentSticker.SECONDARY -> uiTheme.stickers.secondary ?: uiTheme.stickers.default
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
    val ACCENT_COLOR = "Doki.Accent.color"

    // todo: read from json...
    val requiredNamedColors = listOf(
      ACCENT_COLOR,
      "Doki.startColor",
      "Doki.stopColor"
    )
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
