package com.chrisrm.idea.actions

import com.chrisrm.idea.MTThemes
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.FRAME_PROP
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.BasicFileAttributeView
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Forged in the flames of battle by alex.
 */
object ClubMemberOrchestrator {
    private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
    private const val SAVED_THEME = "CLUB_MEMBER_THEME_PROPERTY"
    private val isOn = AtomicBoolean(true)
    private var currentTheme = getSavedTheme()

    private fun getSavedTheme(): MTThemes =
            MTThemes.getTheme(PropertiesComponent.getInstance().getValue(SAVED_THEME))

    fun toggleWeebShit() {
        val weebShitIsOn = isOn.get()
        handleWeebShit(weebShitIsOn)
        setOnStatus(weebShitIsOn)
    }

    fun activate(theme: MTThemes) {
        this.currentTheme = theme
        removeWeebShit()
        IdeBackgroundUtil.repaintAllWindows()
        turnOnIfNecessary()
    }

    private fun turnOnIfNecessary() {
        if (isOn.get())
            turnOnWeebShit()
        IdeBackgroundUtil.repaintAllWindows()
    }

    private fun setOnStatus(weebShitIsOn: Boolean) {
        isOn.getAndSet(!weebShitIsOn)
        PropertiesComponent.getInstance()
                .setValue(CLUB_MEMBER_ON, isOn.get())
    }

    private fun removeWeebShit() {
        PropertiesComponent.getInstance().setValue(EDITOR_PROP, null)
    }

    private fun handleWeebShit(weebShitIsOn: Boolean) {
        if (weebShitIsOn) {
            removeWeebShit()
        } else {
            turnOnWeebShit()
        }
        IdeBackgroundUtil.repaintAllWindows()
    }

    private fun turnOnWeebShit() {
        setProperty(getImagePath(),
                "80",
                IdeBackgroundUtil.Fill.PLAIN.name,
                IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
                EDITOR_PROP)
        setProperty(getFrameBackground(),
                "80",
                IdeBackgroundUtil.Fill.SCALE.name,
                IdeBackgroundUtil.Anchor.CENTER.name,
                FRAME_PROP)

        PropertiesComponent.getInstance().setValue(SAVED_THEME, getTheme().getName())
    }

    private fun getImagePath(): String {
        val literatureClubMember = getLiteratureClubMember()
        val theAnimesPath = "/club_members/$literatureClubMember"
        val weebStuff = Paths.get(".", theAnimesPath).normalize().toAbsolutePath()
        if (!Files.exists(weebStuff) || checksumMatches(weebStuff)) {
            createDirectories(weebStuff)
            copyAnimes(theAnimesPath, weebStuff)
        }
        return weebStuff.toString()
    }

    private fun checksumMatches(weebStuff: Path): Boolean {
        val fileAttributeView = Files.getFileAttributeView(weebStuff, BasicFileAttributeView::class.java)
        try {
            val basicFileAttributes = fileAttributeView.readAttributes()
            return basicFileAttributes.size() < 100L
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return true
    }

    private fun createDirectories(weebStuff: Path) {
        try {
            Files.createDirectories(weebStuff.parent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyAnimes(theAnimesPath: String, weebStuff: Path) {
        try {
            BufferedInputStream(this.javaClass
                    .classLoader
                    .getResourceAsStream(theAnimesPath)).use { inputStream ->
                Files.newOutputStream(weebStuff, StandardOpenOption.CREATE).use { bufferedWriter ->
                    IOUtils.copy(inputStream, bufferedWriter)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getLiteratureClubMember() = getTheme().literatureClubMember

    private fun getTheme(): MTThemes {
        return this.currentTheme
    }

    private fun getFrameBackground(): String {
        return "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources/themes/" + getLiteratureClubMember()
    }

    private fun setProperty(imagePath: String, opacity: String, fill: String, anchor: String, editorProp: String) {
        //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
        //as to why this looks this way
        val property = listOf(imagePath, opacity, fill, anchor).reduceRight { a, b -> "$a, $b" }
        println(property)
        PropertiesComponent.getInstance().setValue(editorProp, property)
    }

}