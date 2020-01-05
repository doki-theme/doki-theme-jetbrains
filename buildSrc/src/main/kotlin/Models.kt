data class SchemaDefinition(
  val required: List<String>?
)

data class ThemeDefinitionSchema(
  val properties: Map<String, SchemaDefinition>
)


data class ThemeTemplateDefinition(
  val extends: String?,
  val name: String,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>?
)

data class BuildStickers(
  val default: String,
  val secondary: String?
)

data class DokiBuildThemeDefinition(
  val id: String,
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val author: String?,
  val group: String,
  val editorScheme: Map<String, Any>,
  val stickers: BuildStickers,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>
) {
 val usableName: String
  get() = name.replace(' ', '_')
 val usableGroup: String
  get() = group.replace(' ', '_')
}

data class IntellijDokiThemeDefinition(
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val author: String?,
  val editorScheme: String,
  val group: String,
  val stickers: BuildStickers,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>
)