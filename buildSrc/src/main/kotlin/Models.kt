data class SchemaDefinition(
  val required: List<String>?
)

data class ThemeDefinitionSchema(
  val properties: Map<String, SchemaDefinition>
)

data class ThemeTemplateDefinition(
  val type: String?,
  val extends: String?,
  val name: String,
  val ui: Map<String, Any>?,
  val icons: Map<String, Any>?,
  val colors: Map<String, String>?
)

data class BuildStickers(
  val default: String,
  val secondary: String?
)

data class EditorSchemeOverrides(
  val colors: Map<String, Any>
)

data class Overrides(
  val editorScheme: EditorSchemeOverrides?
)

data class DokiBuildJetbrainsThemeDefinition(
 val id: String,
 val editorScheme: Map<String, Any>,
 val overrides: Overrides?,
 val ui: Map<String, Any>,
 val icons: Map<String, Any>
)

data class DokiBuildMasterThemeDefinition(
  val id: String,
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val author: String?,
  val group: String,
  val product: String?,
  val stickers: BuildStickers,
  val colors: Map<String, Any>,
  val meta: Map<String, String>?
) {
  val usableName: String
    get() = name.replace(' ', '_')
  val usableGroup: String
    get() = group.replace(' ', '_')
}

data class JetbrainsThemeDefinition(
  val id: String,
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val author: String?,
  val editorScheme: String,
  val group: String,
  val stickers: BuildStickers,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>,
  val meta: Map<String, String>
)