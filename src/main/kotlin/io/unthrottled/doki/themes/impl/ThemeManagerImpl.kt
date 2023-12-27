package io.unthrottled.doki.themes.impl

import com.google.gson.Gson
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfoImpl
import com.intellij.util.io.inputStream
import io.unthrottled.doki.TheDokiTheme
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.JetBrainsThemeDefinition
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional
import java.io.InputStreamReader
import java.net.URI.create
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems.newFileSystem
import java.nio.file.Files.walk
import java.util.Optional
import java.util.stream.Collectors

class ThemeManagerImpl : ThemeManager {

  private val themeMap: Map<String, DokiTheme>

  init {
    val gson = Gson()
    val themeURI = javaClass.classLoader
      .getResource("doki/themes")
      ?.toURI()
      .toString()
      .split("!")
    val currentVersion = TheDokiTheme.getVersion().orElse("69")
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
          JetBrainsThemeDefinition::class.java
        )
      }
      .map { DokiTheme(it, currentVersion) }
      .collect(
        Collectors.toMap(
          { it.name },
          { it },
          { a, _ -> a }
        )
      )
  }

  override val isCurrentThemeDoki: Boolean
    get() = currentTheme.isPresent

  override val currentTheme: Optional<DokiTheme>
    get() = processLaf(LafManager.getInstance().currentUIThemeLookAndFeel)

  override val allThemes: List<DokiTheme>
    get() = themeMap.values.toList()

  override val defaultTheme: DokiTheme
    get() = themeMap[ThemeManager.DEFAULT_THEME_NAME]!!

  override fun processLaf(currentLaf: UIThemeLookAndFeelInfo): Optional<DokiTheme> {
    return currentLaf.toOptional()
      .filter { it is UIThemeLookAndFeelInfoImpl }
      .map { it as UIThemeLookAndFeelInfoImpl }
      .map { themeMap[it.name] }
  }

  override fun themeByName(selectedTheme: String): Optional<DokiTheme> =
    themeMap[selectedTheme].toOptional()

  override fun dispose() {
  }
}
