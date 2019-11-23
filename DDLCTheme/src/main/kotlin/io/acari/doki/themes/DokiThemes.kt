package io.acari.doki.themes

import com.intellij.ide.ui.UITheme
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import io.acari.doki.util.toOptional
import java.util.*
import javax.swing.UIManager

object DokiThemes {
    private val themeSet = setOf(
        "Cleo",
        "Eleniel",
        "Neera",
        "Sanya",
        "Syrena",
        "Wyla",
        "Monika Light",
        "Natsuki Light",
        "Sayori Light",
        "Yuri Light",
        "Monika Dark",
        "Natsuki Dark",
        "Yuri Dark"
    )


    fun processLaf(currentLaf: UIManager.LookAndFeelInfo?): Optional<DokiTheme> {
        return currentLaf.toOptional()
            .filter { it is UIThemeBasedLookAndFeelInfo}
            .map { it as UIThemeBasedLookAndFeelInfo }
            .filter { themeSet.contains(it.name) }
            .map { DokiTheme(it.theme) }
    }
}

class DokiTheme(private val uiTheme: UITheme) {

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