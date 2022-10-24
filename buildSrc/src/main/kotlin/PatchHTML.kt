import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.nio.file.Files
import java.nio.file.StandardOpenOption

open class PatchHTML : DefaultTask() {

  init {
    group = "documentation"
    description = "Patches the HTML to make it pretty."
  }

  @TaskAction
  fun run() {
    val htmlDirectory = createHtmlDirectory(project)
    Files.walk(htmlDirectory)
      .filter { !Files.isDirectory(it) }
      .forEach {
          htmlFileToPatch ->
        val readmeHTML = Jsoup.parse(htmlFileToPatch.toFile(), Charsets.UTF_8.name())
        readmeHTML.getElementsByTag("img")
          .forEach {
              image ->
            image.attr("width", "700")
              .parent()?.insertChildren(0, Element("br"))
          }

        Files.newBufferedWriter(htmlFileToPatch, StandardOpenOption.TRUNCATE_EXISTING)
          .use {
            it.write(readmeHTML.html())
          }
      }
  }
}
