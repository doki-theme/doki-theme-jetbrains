package io.acari.doki.stickers.impl

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.util.io.isFile
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.DokiTheme
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.toOptional
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.util.*
import java.util.Optional.empty
import javax.xml.bind.DatatypeConverter

const val DOKI_BACKGROUND_PROP: String = "io.acari.doki.background"
private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
private const val SOURCE_CODE = "https://raw.githubusercontent.com/Unthrottled/ddlc-jetbrains-theme/master"

// todo: migrate to assets
private const val RESOURCES_DIRECTORY = "${SOURCE_CODE}/src/main/resources"
private const val ASSETS_SOURCE = "https://doki.assets.unthrottled.io"
private const val BACKGROUND_DIRECTORY = "${ASSETS_SOURCE}/backgrounds"
const val DOKI_STICKER_PROP: String = "io.acari.doki.stickers"
private const val PREVIOUS_STICKER = "io.unthrottled.doki.sticker.previous"

class StickerServiceImpl : StickerService {

  companion object {
    private val httpClient = HttpClients.createMinimal()
  }

  private val stickerLevel: StickerLevel
    get() = ThemeConfig.instance.currentStickerLevel

  override fun activateForTheme(dokiTheme: DokiTheme) {
    removeWeebShit()
    turnOnIfNecessary(dokiTheme)
  }

  override fun remove() {
    removeWeebShit()
  }

  override fun getPreviousSticker(): Optional<String> =
    PropertiesComponent.getInstance().getValue(PREVIOUS_STICKER).toOptional()

  override fun clearPreviousSticker() {
    PropertiesComponent.getInstance().unsetValue(PREVIOUS_STICKER)
  }

  override fun checkForUpdates() {
    ThemeManager.instance.currentTheme
      .filter { weebShitOn() }
      .ifPresent {
        if (stickerHasChanged(it)) {
          downloadNewSticker(it)
        }
      }
  }

  private fun downloadNewSticker(dokiTheme: DokiTheme) {
    //todo: need to do
  }

  private fun removeWeebShit() {
    // todo: do not do dis, move to first class theme settings
    val propertiesComponent = PropertiesComponent.getInstance()
    val previousSticker = propertiesComponent.getValue(DOKI_STICKER_PROP, "")
    if (previousSticker.isNotEmpty()) {
      propertiesComponent.setValue(
        PREVIOUS_STICKER,
        previousSticker
      )
    }

    propertiesComponent.unsetValue(DOKI_STICKER_PROP)
    propertiesComponent.unsetValue(DOKI_BACKGROUND_PROP)
    repaintWindows()
  }

  private fun repaintWindows() = try {
    IdeBackgroundUtil.repaintAllWindows()
  } catch (e: Throwable) {
  }

  private fun weebShitOn(): Boolean =
    stickerLevel != StickerLevel.OFF

  private fun turnOnIfNecessary(dokiTheme: DokiTheme) {
    if (weebShitOn())
      turnOnWeebShit(dokiTheme)
  }

  private fun turnOnWeebShit(dokiTheme: DokiTheme) {
    val stickerOpacity = 100
    getImagePath(dokiTheme)
      .ifPresent {
        setProperty(
          it,
          "$stickerOpacity",
          IdeBackgroundUtil.Fill.PLAIN.name,
          IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
          DOKI_STICKER_PROP
        )
      }

    getFrameBackground(dokiTheme)
      .ifPresent {
        setProperty(
          it,
          "$stickerOpacity",
          IdeBackgroundUtil.Fill.SCALE.name,
          IdeBackgroundUtil.Anchor.CENTER.name,
          DOKI_BACKGROUND_PROP
        )

      }
    repaintWindows()
  }


  private fun getFrameBackground(dokiTheme: DokiTheme): Optional<String> {
    return dokiTheme.getSticker()
      .map { "$BACKGROUND_DIRECTORY/$it" }
  }

  private fun stickerHasChanged(dokiTheme: DokiTheme): Boolean {
    return getLocalInstalledStickerPath(dokiTheme)
      .filter {
        !Files.exists(it) ||
          isLocalDifferentFromRemote(it, dokiTheme)
      }.isPresent
  }

  private fun isLocalDifferentFromRemote(
    localInstallPath: Path, dokiTheme: DokiTheme
  ): Boolean {
    return getOnDiskCheckSum(localInstallPath) ==
      getRemoteChecksum(dokiTheme)
  }

  private fun getRemoteChecksum(dokiTheme: DokiTheme): String {
    return getClubMemberFallback(dokiTheme)
      .map { "$it.checksum.txt" }
      .flatMap {
        val request = HttpGet(it)
        val response = httpClient.execute(request)
        if (response.statusLine.statusCode == 200) {
          response.entity.content.use { responseBody ->
            String(responseBody.readAllBytes())
          }.toOptional()
        } else {
          empty()
        }
      }
      .orElseGet {
        "I AM BECOME DEATH, DESTROYER OF WORLDS"
      }
  }

  private fun getLocalInstalledStickerPath(dokiTheme: DokiTheme): Optional<Path> =
    dokiTheme.getStickerPath()
      .flatMap { themeStickerLocation ->
        getLocalClubMemberParentDirectory()
          .map { localInstallDirectory ->
            Paths.get(
              localInstallDirectory, themeStickerLocation
            ).normalize().toAbsolutePath()
          }
      }

  private fun getImagePath(dokiTheme: DokiTheme): Optional<String> =
    dokiTheme.getStickerPath()
      .flatMap { fullstickerClasspath ->
        getLocalClubMemberParentDirectory()
          .map { localParentDirectory ->
            val weebStuff =
              Paths.get(localParentDirectory, fullstickerClasspath)
                .normalize()
                .toAbsolutePath()
            if (shouldCopyToDisk(weebStuff, fullstickerClasspath)) {
              createDirectories(weebStuff)
              copyAnimes(fullstickerClasspath, weebStuff)
                .map { it.toOptional() }
                .orElseGet { getClubMemberFallback(dokiTheme) }
            } else {
              weebStuff.toString().toOptional()
            }
          }
          .orElseGet {
            getClubMemberFallback(dokiTheme)
          }
      }

  private fun shouldCopyToDisk(weebStuff: Path, theAnimesPath: String) =
    !(Files.exists(weebStuff) && checksumMatches(weebStuff, theAnimesPath))

  private fun checksumMatches(weebStuff: Path, theAnimesPath: String): Boolean =
    try {
      computeChecksumFromClasspath(theAnimesPath)
        .map { computedCheckSum ->
          val onDiskCheckSum = getOnDiskCheckSum(weebStuff)
          computedCheckSum == onDiskCheckSum
        }.orElseGet { false }
    } catch (e: IOException) {
      e.printStackTrace()
      false
    }

  private fun computeChecksumFromClasspath(theAnimesPath: String): Optional<String> {
    return getAnimesInputStream(theAnimesPath)
      .map { IOUtils.toByteArray(it) }
      .map { computeCheckSum(it) }
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
            Files.newOutputStream(
              weebStuff,
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING
            ).use { bufferedWriter ->
              IOUtils.copy(inputStream, bufferedWriter)
            }
          }
          weebStuff.toString()
        }
    } catch (e: IOException) {
      Optional.empty()
    }


  private fun getClubMemberFallback(dokiTheme: DokiTheme): Optional<String> {
    return dokiTheme.getStickerPath()
      .map { "${ASSETS_SOURCE}$it" }
  }


  private fun getLocalClubMemberParentDirectory(): Optional<String> =
    Optional.ofNullable(
      System.getProperties()["jb.vmOptionsFile"] as? String
        ?: System.getProperties()["idea.config.path"] as? String
    )
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


  private fun setProperty(
    imagePath: String,
    opacity: String,
    fill: String, anchor: String,
    propertyKey: String
  ) {
    //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
    //as to why this looks this way
    val propertyValue = listOf(imagePath, opacity, fill, anchor)
      .reduceRight { a, b -> "$a,$b" }
    setPropertyValue(propertyKey, propertyValue)
  }

  private fun setPropertyValue(propertyKey: String, propertyValue: String) {
    PropertiesComponent.getInstance().unsetValue(propertyKey)
    PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
  }


}
