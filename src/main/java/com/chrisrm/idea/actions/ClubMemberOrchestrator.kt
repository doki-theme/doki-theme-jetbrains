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
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
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

    fun weebShitOn(): Boolean = isOn.get()

    private fun turnOnIfNecessary() {
        if (isOn.get())
            turnOnWeebShit()
        IdeBackgroundUtil.repaintAllWindows()
    }

    private fun setOnStatus(weebShitIsOn: Boolean) {
        isOn.getAndSet(!weebShitIsOn)
        setPropertyValue(CLUB_MEMBER_ON, isOn.get())
    }

    private fun removeWeebShit() {
        PropertiesComponent.getInstance().unsetValue(EDITOR_PROP)
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

        setPropertyValue(SAVED_THEME, getTheme().getName())
    }

    private fun setPropertyValue(propertyKey: String, propertyValue: String) {
        PropertiesComponent.getInstance().unsetValue(propertyKey)
        PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
    }

    private fun setPropertyValue(propertyKey: String, propertyValue: Boolean) {
        PropertiesComponent.getInstance().unsetValue(propertyKey)
        PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
    }

    private fun getImagePath(): String {
        val literatureClubMember = getLiteratureClubMember()
        val theAnimesPath = "/club_members/$literatureClubMember"
        val weebStuff = Paths.get(".", theAnimesPath).normalize().toAbsolutePath()
        if (shouldLoadLocally(weebStuff)) {
            createDirectories(weebStuff)
            copyAnimes(theAnimesPath, weebStuff)
        }
        return weebStuff.toString()
    }

    private fun shouldLoadLocally(weebStuff: Path) =
            !Files.exists(weebStuff) || checksumMatches(weebStuff)

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
                Files.newOutputStream(weebStuff, CREATE, TRUNCATE_EXISTING).use { bufferedWriter ->
                    IOUtils.copy(inputStream, bufferedWriter)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getLiteratureClubMember() =
            getTheme().literatureClubMember

    private fun getTheme(): MTThemes {
        return this.currentTheme
    }

    private fun getFrameBackground(): String {
        return "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources/themes/" + getLiteratureClubMember()
    }

    private fun setProperty(imagePath: String,
                            opacity: String,
                            fill: String, anchor: String,
                            propertyKey: String) {
        //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
        //as to why this looks this way
        val propertyValue = listOf(imagePath, opacity, fill, anchor).reduceRight { a, b -> "$a,$b" }

        setPropertyValue(propertyKey, propertyValue)
    }

}