package io.acari.DDLC.chibi

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.FRAME_PROP
import com.intellij.util.io.isFile
import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.DDLCThemes
import io.acari.DDLC.toOptional
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.security.MessageDigest
import java.util.*
import javax.xml.bind.DatatypeConverter

/**
 * Forged in the flames of battle by alex.
 */
object ChibiOrchestrator {

  private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
  const val DDLC_CHIBI_PROP: String = "io.acari.ddlc.chibi"
  private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
  const val DDLC_BACKGROUND_PROP: String = "io.acari.ddlc.background"
  private val oldChibiProps = listOf(EDITOR_PROP, FRAME_PROP)
  const val SAVED_THEME: String = "CLUB_MEMBER_THEME_PROPERTY"
  private const val RESOURCES_DIRECTORY = "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources"

  private var chibiLevel = ChibiLevel.ON
  private var currentTheme: Lazy<DDLCThemeFacade> = lazy { getSavedTheme() }

  init {
    checkLegacyChibiToggle()
    removeLegacyProperties()
    if (MTThemeManager.isDDLCActive()) {
      setChibiLevel(DDLCConfig.getInstance().getChibiLevel())
    }
    MTThemeManager.addMaterialThemeActivatedListener {
      if (it) {
        removeWeebShit()
      } else if (MTThemeManager.isDDLCActive()) {
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

  private fun getSavedTheme(): DDLCThemeFacade =
      DDLCConfig.getInstance().getSelectedTheme()

  fun currentActiveTheme(): Lazy<DDLCThemeFacade> = currentTheme

  fun setChibiLevel(chibiLevel: ChibiLevel) {
    ChibiOrchestrator.chibiLevel = chibiLevel
    DDLCConfig.getInstance().setChibiLevel(chibiLevel)
    updateChibi()
  }

  fun currentChibiLevel(): ChibiLevel = chibiLevel

  fun activateChibiForTheme(theme: DDLCThemeFacade) {
    currentTheme = lazy { theme }
    updateChibi()
  }

  private fun updateChibi() {
    removeWeebShit()
    turnOnIfNecessary()
  }

  private fun weebShitOn(): Boolean =
      chibiLevel != ChibiLevel.OFF

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
    val currentTheme = getTheme()
    val chibiOpacity = if (currentTheme is DDLCThemes) 80 else 99
    setProperty(getImagePath(),
        "$chibiOpacity",
        IdeBackgroundUtil.Fill.PLAIN.name,
        IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
        DDLC_CHIBI_PROP)
    setProperty(getFrameBackground(),
        "$chibiOpacity",
        IdeBackgroundUtil.Fill.SCALE.name,
        IdeBackgroundUtil.Anchor.CENTER.name,
        DDLC_BACKGROUND_PROP)

    setPropertyValue(SAVED_THEME, currentTheme.name)
    IdeBackgroundUtil.repaintAllWindows()
  }

  private fun setPropertyValue(propertyKey: String, propertyValue: String) {
    PropertiesComponent.getInstance().unsetValue(propertyKey)
    PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
  }

  private fun getImagePath(): String {
    val literatureClubMember = getChibi()
    val theAnimesPath = "/club_members/$literatureClubMember"
    return getLocalClubMemberParentDirectory()
        .map { localParentDirectory ->
          val weebStuff =
              Paths.get(localParentDirectory, theAnimesPath)
                  .normalize()
                  .toAbsolutePath()
          if (shouldCopyToDisk(weebStuff, theAnimesPath)) {
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
          .map { property -> property.split(",") }
          .filter { properties -> properties.isNotEmpty() }
          .map { paths -> paths[paths.size - 1] }
          .map { property ->
            val directory = Paths.get(property)
            if (directory.isFile()) {
              directory.parent
            } else {
              directory
            }.toAbsolutePath().toString()
          }


  private fun shouldCopyToDisk(weebStuff: Path, theAnimesPath: String) =
      !(Files.exists(weebStuff) && checksumMatches(weebStuff, theAnimesPath))

  private fun checksumMatches(weebStuff: Path, theAnimesPath: String): Boolean =
      try {
        getAnimesInputStream(theAnimesPath)
            .map { IOUtils.toByteArray(it) }
            .map { computeCheckSum(it) }
            .map { computedCheckSum ->
              val onDiskCheckSum = getOnDiskCheckSum(weebStuff)
              computedCheckSum == onDiskCheckSum
            }.orElseGet { false }
      } catch (e: IOException) {
        e.printStackTrace()
        false
      }

  private fun getOnDiskCheckSum(weebStuff: Path): String =
      computeCheckSum(Files.readAllBytes(weebStuff))

  private fun computeCheckSum(byteArray: ByteArray): String {
    messageDigest.update(byteArray)
    val digest = messageDigest.digest()
    return DatatypeConverter.printHexBinary(digest).toUpperCase()
  }

  private fun createDirectories(weebStuff: Path) {
    try {
      Files.createDirectories(weebStuff.parent)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun getAnimesInputStream(theAnimesPath: String): Optional<BufferedInputStream> =
      try {
        this.javaClass
            .classLoader
            .getResourceAsStream(theAnimesPath).toOptional()
            .map { BufferedInputStream(it) }
      } catch (e: IOException) {
        Optional.empty()
      }

  private fun copyAnimes(theAnimesPath: String, weebStuff: Path): Optional<String> =
      try {
        getAnimesInputStream(theAnimesPath)
            .map { bufferedInputStream ->
              bufferedInputStream.use { inputStream ->
                Files.newOutputStream(weebStuff, CREATE, TRUNCATE_EXISTING).use { bufferedWriter ->
                  IOUtils.copy(inputStream, bufferedWriter)
                }
              }
              weebStuff.toString()
            }
      } catch (e: IOException) {
        Optional.empty()
      }

  private fun getChibi() =
      getTheme().chibi

  fun getNormalClubMember(): String =
      getTheme().normalChibi

  private fun getTheme(): DDLCThemeFacade {
    return currentTheme.value
  }

  private fun getFrameBackground(): String {
    return RESOURCES_DIRECTORY + "/themes/" + getChibi()
  }

  private fun getClubMemberFallback(): String {
    return RESOURCES_DIRECTORY + "/club_members/" + getChibi()
  }

  private fun setProperty(imagePath: String,
                          opacity: String,
                          fill: String, anchor: String,
                          propertyKey: String) {
    //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
    //as to why this looks this way
    val propertyValue = listOf(imagePath, opacity, fill, anchor)
        .reduceRight { a, b -> "$a,$b" }
    setPropertyValue(propertyKey, propertyValue)
  }

  fun init() {
  }

}