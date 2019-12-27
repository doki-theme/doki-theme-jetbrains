import com.google.gson.GsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors
import java.util.stream.Stream

open class BuildThemes : DefaultTask() {

  private val gson = GsonBuilder().setPrettyPrinting().create()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run() {
    val themeDirectory = get(project.rootDir.absolutePath, "themes")
    val themeTemplates =
      walk(get(themeDirectory.toString(), "templates"))
        .filter { !isDirectory(it) }
        .filter { it.fileName.toString().endsWith(".template.json") }
        .map { newInputStream(it) }
        .map {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            ThemeTemplateDefinition::class.java
          )
        }.collect(Collectors.toMap({ it.name }, { it }, { a, _ -> a }))

    val themeDefDir = get(themeDirectory.toString(), "definitions")
    walk(themeDefDir)
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".doki.json") }
      .map { it to newInputStream(it) }
      .map {
        it.first to gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          DokiBuildThemeDefinition::class.java
        )
      }
      .forEach {
        constructIntellijTheme(
          it,
          themeTemplates,
          themeDefDir
        )
      }
  }

  private fun constructIntellijTheme(
    dokiBuildThemeDefinition: Pair<Path, DokiBuildThemeDefinition>,
    themeTemplates: Map<String, ThemeTemplateDefinition>,
    themeDefDir: Path
  ) {
    val (definitionDirectory, themeDefinition) = dokiBuildThemeDefinition
    val resourceDirectory = get(
      project.rootDir.absolutePath,
      "src",
      "main",
      "resources",
      "doki",
      "themes",
      themeDefinition.group.toLowerCase()
    )
    if (!exists(resourceDirectory)) {
      createDirectories(resourceDirectory)
    }

    val themeJson = get(resourceDirectory.toString(), "${themeDefinition.name}.theme.json")

    if (exists(themeJson)) {
      delete(themeJson)
    }

    val templateName = if (themeDefinition.dark) "dark" else "light"
    val finalTheme = IntellijDokiThemeDefinition(
      name = themeDefinition.name,
      displayName = themeDefinition.displayName,
      dark = themeDefinition.dark,
      author = themeDefinition.author,
      editorScheme = createEditorScheme(themeDefinition, themeTemplates, definitionDirectory),
      group = themeDefinition.group,
      stickers = themeDefinition.stickers,
      colors = themeDefinition.colors,
      ui = getUIDef(
        themeDefinition.ui,
        themeTemplates[templateName] ?: throw IllegalStateException("Theme $templateName does not exist."),
        themeTemplates
      ),
      icons = getIcons(themeDefinition.icons, themeTemplates)
    )

    newBufferedWriter(themeJson, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        gson.toJson(finalTheme, writer)
      }
  }

  private fun getIcons(
    icons: Map<String, Any>,
    themeTemplates: Map<String, ThemeTemplateDefinition>
  ): Map<String, Any> {
    return mapOf()
  }

  private fun getUIDef(
    ui: Map<String, Any>,
    themeTemplates: ThemeTemplateDefinition,
    themeTemplates1: Map<String, ThemeTemplateDefinition>
  ): Map<String, Any> {
    return Stream.of(
      getAllEntries(themeTemplates, themeTemplates1) { it.ui },
      ui.entries.stream()
    )
      .flatMap { it }
      .collect(Collectors.toMap({ it.key }, { it.value }, { a, b -> b }))
  }

  private fun getAllEntries(
    themeTemplates: ThemeTemplateDefinition,
    allThemeTemplates: Map<String, ThemeTemplateDefinition>,
    entryExtractor: (ThemeTemplateDefinition) -> Map<String, Any>
  ): Stream<Map.Entry<String, Any>> {
    return if (themeTemplates.extends == null) {
      entryExtractor(themeTemplates).entries.stream()
    } else {
      Stream.concat(
        getAllEntries(
          allThemeTemplates[themeTemplates.extends]
            ?: throw IllegalStateException("Theme template ${themeTemplates.extends} is not a valid parent template"),
          allThemeTemplates,
          entryExtractor
        ), entryExtractor(themeTemplates).entries.stream()
      )
    }.sorted(Comparator.comparing { it.key })
  }

  private fun createEditorScheme(
    editorScheme: DokiBuildThemeDefinition,
    themeTemplates: Map<String, ThemeTemplateDefinition>,
    themeDefDir: Path
  ): String {
    return "yeet"
  }
}


data class ThemeTemplateDefinition(
  val extends: String?,
  val name: String,
  val ui: Map<String, Any>,
  val icons: Map<String, Any>
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
)


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
