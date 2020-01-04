package io.acari.doki.ide

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.openapi.util.text.StringUtil.endsWithIgnoreCase
import io.acari.doki.util.toOptional
import org.jetbrains.annotations.NonNls

object ThemeJsonUtil {
  private const val DOKI_THEME_EXTENSION = "doki.json"
  private val COLORS_PROPERTY_NAME = "colors"


  @JvmStatic
  fun isThemeFilename(string: String): Boolean = endsWithIgnoreCase(string, DOKI_THEME_EXTENSION)

  @JvmStatic
  fun getNamedColors(themeFile: JsonFile): List<JsonProperty> =
    themeFile.toOptional()
      .filter { it is JsonObject }
      .map { (it as JsonObject).findProperty(COLORS_PROPERTY_NAME) }
      .map { it?.value }
      .filter { it is JsonObject }
      .map { (it as JsonObject).propertyList }
      .orElseGet { emptyList() }
}