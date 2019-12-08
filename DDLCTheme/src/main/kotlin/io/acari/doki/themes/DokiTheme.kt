package io.acari.doki.themes

import io.acari.doki.util.toOptional
import java.util.*

class Stickers(
  val default: String,
  val secondary: String?
)

class DokiThemeDefinition(
  val name: String,
  val dark: Boolean,
  val stickers: Stickers,
  val colors: Map<String, Any>
)

class DokiTheme(private val uiTheme: DokiThemeDefinition) {

  val isDark: Boolean
    get() = uiTheme.dark

  val name: String
    get() = uiTheme.name

  fun getStickerPath(): Optional<String> {
    return uiTheme.stickers.default
      .toOptional()
  }

  fun getSticker(): Optional<String> =
    getStickerPath()
      .map { it.substring(it.lastIndexOf("/") + 1) }


  // todo: make this color mandatory
  val nonProjectFileScopeColor: String
    get() = uiTheme.colors["nonProjectFileScopeColor"] as String

  // todo: make this color mandatory
  val testScopeColor: String
    get() = uiTheme.colors["testScopeColor"] as String

}