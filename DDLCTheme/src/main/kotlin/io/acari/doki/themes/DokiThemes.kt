package io.acari.doki.themes

import com.google.gson.Gson
import com.intellij.ide.ui.UITheme
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.util.io.inputStream
import io.acari.doki.util.toOptional
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import javax.swing.UIManager


object DokiThemes {
    const val MONIKA_LIGHT = "Monika Light"

    private val themeMap: Map<String, UITheme>

    init {
        val gson = Gson()
        val path = this.javaClass.classLoader.getResource("themes").path
        themeMap = Files.walk(Paths.get(path))
            .filter { it.endsWith(".theme.json") }
            .map { it.inputStream() }
            .map {
                gson.fromJson(
                    InputStreamReader(it, StandardCharsets.UTF_8),
                    UITheme::class.java
                )
            }
            .collect(Collectors.toMap(
                { it.name },
                { it },
                { a, _ -> a }
            ))

        println(themeMap)
    }

    val currentTheme: Optional<DokiTheme>
        get() = processLaf(LafManagerImpl.getInstance().currentLookAndFeel)


    fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme> {
        return currentLaf.toOptional()
            .filter { it is UIThemeBasedLookAndFeelInfo}
            .map { it as UIThemeBasedLookAndFeelInfo }
            .filter { themeMap.containsKey(it.name) }
            .map { DokiTheme(it) }
    }
}

class DokiTheme(private val laf: UIThemeBasedLookAndFeelInfo) {

    val uiTheme = laf.theme

    val isDark: Boolean
        get() = uiTheme.isDark

    val name: String
        get() = uiTheme.name

    fun getChibiPath(): Optional<String> {
        return uiTheme.background["image"]
            .toOptional()
            .filter { it is String }
            .map { it as String }
    }

    fun getChibi(): Optional<String> =
        getChibiPath()
            .map { it.substring(it.lastIndexOf("/") + 1 ) }


    // todo: make this color mandatory
    val nonProjectFileScopeColor: String
        get() = uiTheme.colors["nonProjectFileScopeColor"] as String

    // todo: make this color mandatory
    val testScopeColor: String
        get() = uiTheme.colors["testScopeColor"] as String


}