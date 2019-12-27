import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

open class BuildThemes: DefaultTask() {

  private val gson = Gson()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run(){
    val themeDirectory = Paths.get(project.rootDir.absolutePath, "themes")
    Files.walk(Paths.get(themeDirectory.toString(), "definitions"))
      .filter { !Files.isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".doki.json") }
      .map { Files.newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          DokiBuildThemeDefinition::class.java
        )
      }.forEach {
        println(it)
      }
  }
}

data class BuildStickers(
  val default: String,
  val secondary: String?
)

data class DokiBuildThemeDefinition(
  val name: String,
  val displayName: String?,
  val dark: Boolean,
  val stickers: BuildStickers,
  val group: String,
  val colors: Map<String, Any>,
  val ui: Map<String, Any>
)

