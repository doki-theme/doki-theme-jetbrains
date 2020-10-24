package io.unthrottled.doki.ide

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.openapi.util.text.StringUtil.endsWithIgnoreCase
import io.unthrottled.doki.util.toOptional

object DokiThemeJsonUtil {
  const val DOKI_THEME_EXTENSION = "definition.json"
  private const val DOKI_TEMPLATE_EXTENSION = "template.json"
  private const val COLORS_PROPERTY_NAME = "colors"

  fun isThemeFilename(string: String): Boolean =
    endsWithIgnoreCase(string, DOKI_THEME_EXTENSION) ||
      endsWithIgnoreCase(string, DOKI_TEMPLATE_EXTENSION)

  fun getNamedColors(themeFile: JsonFile): List<JsonProperty> =
    themeFile.toOptional()
      .map { it.topLevelValue }
      .filter { it is JsonObject }
      .map { (it as JsonObject).findProperty(COLORS_PROPERTY_NAME) }
      .map { it?.value }
      .filter { it is JsonObject }
      .map { (it as JsonObject).propertyList }
      .orElseGet { emptyList() }
}
