import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

open class BuildThemes : DefaultTask() {

  private val gson = Gson()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run() {
    val themeDirectory = Paths.get(project.rootDir.absolutePath, "themes")
    val themeTemplates = Files.walk(Paths.get(themeDirectory.toString(), "templates"))
      .filter { !Files.isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".template.json") }
      .map { Files.newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          ThemeTemplateDefinition::class.java
        )
      }.collect(Collectors.toMap({ it.name }, { it }, { a, _ -> a }))

    val themeDefDir = Paths.get(themeDirectory.toString(), "definitions")
    Files.walk(themeDefDir)
      .filter { !Files.isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".doki.json") }
      .map { it to Files.newInputStream(it) }
      .map {
        it.first to gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          DokiBuildThemeDefinition::class.java
        )
      }
      .map { constructIntellijTheme(
        it,
        themeTemplates,
        themeDefDir
      ) }
      .forEach {
        println(it)
      }
  }

  private fun constructIntellijTheme(
    dokiBuildThemeDefinition: Pair<Path, DokiBuildThemeDefinition>,
    themeTemplates: Map<String, ThemeTemplateDefinition>,
    themeDefDir: Path
  ): Any {
    return dokiBuildThemeDefinition.second
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

