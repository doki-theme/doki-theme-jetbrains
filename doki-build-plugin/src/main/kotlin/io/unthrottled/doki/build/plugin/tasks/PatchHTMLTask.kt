package io.unthrottled.doki.build.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption


@CacheableTask
abstract class PatchHTMLTask : DefaultTask() {

  @get:OutputDirectory
  abstract val htmlDirectory: DirectoryProperty

  init {
    group = "documentation"
    description = "Patches the HTML to make it pretty."
  }
  private fun buildDirectory(buildDir: DirectoryProperty): Path {
    val buildPath = buildDir.get().asFile.toPath()
    Files.createDirectories(buildPath)
    return buildPath
  }

  @TaskAction
  fun run() {
    val htmlDirectory = buildDirectory(htmlDirectory)
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
