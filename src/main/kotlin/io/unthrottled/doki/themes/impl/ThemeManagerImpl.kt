package io.unthrottled.doki.themes.impl

import com.google.gson.Gson
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.util.io.inputStream
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.DokiThemeDefinition
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional
import java.io.InputStreamReader
import java.net.URI.create
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems.newFileSystem
import java.nio.file.Files.walk
import java.util.*
import java.util.stream.Collectors
import javax.swing.UIManager

class ThemeManagerImpl : ThemeManager {

  private val themeMap: Map<String, DokiTheme>

  init {
    val gson = Gson()
    val themeURI = javaClass.classLoader
      .getResource("/doki/themes")
      ?.toURI()
      .toString()
      .split("!")
    val fileSystem = newFileSystem(create(themeURI[0]), mapOf<String, String>())
    themeMap = walk(
      fileSystem.getPath(
        themeURI[1]
      )
    )
      .filter { it.fileName.toString().endsWith(".theme.json") }
      .map { it.inputStream() }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          DokiThemeDefinition::class.java
        )
      }
      .map { DokiTheme(it) }
      .collect(
        Collectors.toMap(
          { it.name },
          { it },
          { a, _ -> a }
        ))
  }

  override val currentTheme: Optional<DokiTheme>
    get() = processLaf(LafManagerImpl.getInstance().currentLookAndFeel)

  override val allThemes: List<DokiTheme>
    get() = themeMap.values.toList()

  override fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme> {
    return currentLaf.toOptional()
      .filter { it is UIThemeBasedLookAndFeelInfo }
      .map { it as UIThemeBasedLookAndFeelInfo }
      .map { themeMap[it.name] }
  }

  override fun themeByName(selectedTheme: String): Optional<DokiTheme> =
    themeMap[selectedTheme].toOptional()
}