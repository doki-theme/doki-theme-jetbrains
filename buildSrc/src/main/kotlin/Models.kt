data class SchemaDefinition(
  val required: List<String>?
)

data class ThemeDefinitionSchema(
  val properties: Map<String, SchemaDefinition>
)

class Background(
  val name: String,
  val position: String,
  val opacity: Int? = null
)

class Backgrounds(
  val default: Background,
  val secondary: Background?
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

data class BackgroundDefinition(
  val name: String?,
  val position: String?,
  val opacity: Int?
)
data class BackgroundsDefinition(
  val default: BackgroundDefinition?,
  val secondary: BackgroundDefinition?
)

data class DokiBuildJetbrainsThemeDefinition(
 val id: String,
 val editorScheme: Map<String, Any>,
 val overrides: Overrides?,
 val backgrounds: BackgroundsDefinition?,
 val ui: Map<String, Any>,
 val uiBase: String?,
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
  val overrides: Overrides?,
  val colors: Map<String, Any>,
  val meta: Map<String, String>?
) {
  val usableName: String
    get() = name.replace(' ', '_')
      .replace(":", "")
  val usableGroup: String
    get() = group.replace(' ', '_')
      .replace(":", "")
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
  val backgrounds: Backgrounds,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>,
  val meta: Map<String, String>
)
