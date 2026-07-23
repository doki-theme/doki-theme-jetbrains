package io.unthrottled.doki.build.plugin.util

import io.unthrottled.doki.build.jvm.models.Backgrounds
import io.unthrottled.doki.build.jvm.models.BackgroundsDefinition
import io.unthrottled.doki.build.jvm.models.HasId
import io.unthrottled.doki.build.jvm.models.JetbrainsStickers
import io.unthrottled.doki.build.jvm.models.Overrides
import io.unthrottled.doki.build.jvm.models.StringDictionary

data class JetbrainsThemeOnlyDefinition(
  val name: String,
  val dark: Boolean,
  val parentTheme: String?,
  val author: String?,
  val editorScheme: String,
  val colors: StringDictionary<Any>,
  val ui: StringDictionary<Any>,
  val icons: StringDictionary<Any>,
)

data class JetbrainsAppDefinition(
  override val id: String,
  val parentTheme: String?,
  val editorScheme: StringDictionary<Any>,
  val overrides: Overrides?,
  val backgrounds: BackgroundsDefinition?,
  val ui: StringDictionary<Any>,
  val uiBase: String?,
  val icons: StringDictionary<Any>
): HasId

data class JetbrainsThemeMetaDefinition(
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val author: String?,
  val parentTheme: String?,
  val editorScheme: String,
  val group: String,
  val stickers: JetbrainsStickers,
  val backgrounds: Backgrounds,
  val colors: StringDictionary<Any>,
  val ui: StringDictionary<Any>,
  val icons: StringDictionary<Any>,
  val meta: StringDictionary<String>
)

data class DokiThemeTemplate(
  override val id: String,
  val parentTheme: String?,
  val editorScheme: StringDictionary<Any>,
  val overrides: Overrides?,
  val ui: StringDictionary<Any>,
  val uiBase: String?,
) : HasId
