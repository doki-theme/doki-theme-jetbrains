package io.acari.DDLC.chibi

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.FRAME_PROP
import com.intellij.util.io.isFile
import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.DDLCThemes
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.attribute.BasicFileAttributeView
import java.util.*

/**
 * Forged in the flames of battle by alex.
 */
object ChibiOrchestrator {
    const val DDLC_CHIBI_PROP = "io.acari.ddlc.chibi"
    private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
    const val DDLC_BACKGROUND_PROP = "io.acari.ddlc.background"
    private val oldChibiProps = listOf(EDITOR_PROP, FRAME_PROP)
    const val SAVED_THEME = "CLUB_MEMBER_THEME_PROPERTY"
    private const val RESOURCES_DIRECTORY = "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources"

    private var chibiLevel = ChibiLevel.ON
    private var currentTheme =  lazy { getSavedTheme() }

    init {
        checkLegacyChibiToggle()
        removeLegacyProperties()
        if(MTThemeManager.isDDLCActive()){
            setChibiLevel(DDLCConfig.getInstance().getChibiLevel())
        }
        MTThemeManager.addMaterialThemeActivatedListener {
            if (it) {
                removeWeebShit()
            } else if(MTThemeManager.isDDLCActive()) {
                turnOnIfNecessary()
            }
        }
    }

    private fun checkLegacyChibiToggle() {
        if (PropertiesComponent.getInstance().isValueSet(CLUB_MEMBER_ON)) {
            val clubMemberOn = PropertiesComponent.getInstance().getBoolean(CLUB_MEMBER_ON)
            if (clubMemberOn) {
                setChibiLevel(ChibiLevel.ON)
            } else {
                setChibiLevel(ChibiLevel.OFF)
            }
            PropertiesComponent.getInstance().unsetValue(CLUB_MEMBER_ON)
        }
    }

    private fun removeLegacyProperties() {
        oldChibiProps.forEach {
            PropertiesComponent.getInstance().unsetValue(it)
        }
    }

    private fun getSavedTheme(): DDLCThemes =
            DDLCConfig.getInstance().getSelectedTheme() as DDLCThemes

    fun currentActiveTheme() = currentTheme


    fun setChibiLevel(chibiLevel: ChibiLevel) {
        ChibiOrchestrator.chibiLevel = chibiLevel
        DDLCConfig.getInstance().setChibiLevel(chibiLevel)
        updateChibi()
    }

    fun currentChibiLevel() = chibiLevel

    fun activateChibiForTheme(theme: DDLCThemes) {
        currentTheme =  lazy {theme }
        updateChibi()
    }

    private fun updateChibi() {
        removeWeebShit()
        turnOnIfNecessary()
    }

    fun weebShitOn(): Boolean = chibiLevel != ChibiLevel.OFF

    private fun turnOnIfNecessary() {
        if (weebShitOn())
            turnOnWeebShit()
    }

    private fun removeWeebShit() {
        PropertiesComponent.getInstance().unsetValue(DDLC_CHIBI_PROP)
        PropertiesComponent.getInstance().unsetValue(DDLC_BACKGROUND_PROP)
        IdeBackgroundUtil.repaintAllWindows()
    }

    private fun turnOnWeebShit() {
        setProperty(getImagePath(),
                "80",
                IdeBackgroundUtil.Fill.PLAIN.name,
                IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
                DDLC_CHIBI_PROP)
        setProperty(getFrameBackground(),
                "80",
                IdeBackgroundUtil.Fill.SCALE.name,
                IdeBackgroundUtil.Anchor.CENTER.name,
                DDLC_BACKGROUND_PROP)

        setPropertyValue(SAVED_THEME, getTheme().getName())
        IdeBackgroundUtil.repaintAllWindows()
    }

    private fun setPropertyValue(propertyKey: String, propertyValue: String) {
        PropertiesComponent.getInstance().unsetValue(propertyKey)
        PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
    }

    private fun getImagePath(): String {
        val literatureClubMember = getLiteratureClubMember()
        val theAnimesPath = "/club_members/$literatureClubMember"
        return getLocalClubMemberParentDirectory()
                .map { localParentDirectory ->
                    val weebStuff = Paths.get(localParentDirectory, theAnimesPath).normalize().toAbsolutePath()
                    if (shouldLoadLocally(weebStuff)) {
                        createDirectories(weebStuff)
                        copyAnimes(theAnimesPath, weebStuff)
                                .orElseGet(this::getClubMemberFallback)
                    } else {
                        weebStuff.toString()
                    }
                }.orElseGet {
                    getClubMemberFallback()
                }
    }

    private fun getLocalClubMemberParentDirectory(): Optional<String> =
            Optional.ofNullable(System.getProperties()["jb.vmOptionsFile"] as? String
                    ?: System.getProperties()["idea.config.path"] as? String)
                    .map { property ->
                        val directory = Paths.get(property)
                        if (directory.isFile()) {
                            directory.parent
                        } else {
                            directory
                        }.toAbsolutePath().toString()
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

    private fun copyAnimes(theAnimesPath: String, weebStuff: Path) =
            try {
                BufferedInputStream(this.javaClass
                        .classLoader
                        .getResourceAsStream(theAnimesPath)).use { inputStream ->
                    Files.newOutputStream(weebStuff, CREATE, TRUNCATE_EXISTING).use { bufferedWriter ->
                        IOUtils.copy(inputStream, bufferedWriter)
                    }
                }
                Optional.of(weebStuff)
                        .map { it.toString() }
            } catch (e: IOException) {
                Optional.empty<String>()
            }


    private fun getLiteratureClubMember() =
            getTheme().literatureClubMember

    fun getNormalClubMember() = getTheme().normalClubMember!!

    private fun getTheme(): DDLCThemes {
        return currentTheme.value
    }

    private fun getFrameBackground(): String {
        return RESOURCES_DIRECTORY + "/themes/" + getLiteratureClubMember()
    }

    private fun getClubMemberFallback(): String {
        return RESOURCES_DIRECTORY + "/club_members/" + getLiteratureClubMember()
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

    fun init() {
    }

}