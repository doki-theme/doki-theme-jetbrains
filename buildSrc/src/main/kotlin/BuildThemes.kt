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

    newBufferedWriter(themeJson, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        gson.toJson(themeDefinition, writer)
      }
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
  val ui: Map<String, Any>
)

