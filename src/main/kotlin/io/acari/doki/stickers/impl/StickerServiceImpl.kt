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
      .flatMap {
        getLocalInstalledStickerPath(it).map { path ->
          path to it
        }
      }
      .ifPresent {
        val (localStickePath, dokiTheme) = it
        if (stickerHasChanged(localStickePath, dokiTheme)) {
          downloadNewSticker(localStickePath, dokiTheme)
        }
      }
  }

  private fun downloadNewSticker(localStickerPath: Path, dokiTheme: DokiTheme): Optional<Path> {
    createDirectories(localStickerPath)
    return getClubMemberFallback(dokiTheme)
      .flatMap {
        downloadRemoteSticker(localStickerPath, it)
      }
  }

  private fun downloadRemoteSticker(
    localStickerPath: Path,
    remoteUrl: String
  ): Optional<Path> {
    return try {
      val remoteStickerRequest = HttpGet(remoteUrl)
      val stickerResponse = httpClient.execute(remoteStickerRequest)
      if(stickerResponse.statusLine.statusCode == 200) {
        stickerResponse.entity.content.use { inputStream ->
          Files.newOutputStream(
            localStickerPath,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
          ).use { bufferedWriter ->
            IOUtils.copy(inputStream, bufferedWriter)
          }
        }
        localStickerPath.toOptional()
      } else {
        empty()
      }
    } catch (e: Throwable) {
      empty()
    }
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

  private fun stickerHasChanged(localInstallPath: Path, dokiTheme: DokiTheme): Boolean =
    !Files.exists(localInstallPath) ||
      isLocalDifferentFromRemote(localInstallPath, dokiTheme)

  private fun isLocalDifferentFromRemote(
    localInstallPath: Path, dokiTheme: DokiTheme
  ): Boolean =
    getOnDiskCheckSum(localInstallPath) ==
      getRemoteChecksum(dokiTheme)

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
              localInstallDirectory, "stickers", themeStickerLocation
            ).normalize().toAbsolutePath()
          }
      }

  private fun getImagePath(dokiTheme: DokiTheme): Optional<String> {
    return getLocalClubMemberParentDirectory()
      .map { Paths.get(it) }
      .filter { Files.isWritable(it) }
      .map {
        getLocalInstalledStickerPath(dokiTheme)
          .map { localStickerPath ->
            if (stickerHasChanged(localStickerPath, dokiTheme)) {
              downloadNewSticker(localStickerPath, dokiTheme)
            } else {
              localStickerPath.toOptional()
            }
          }.map { it.toString() }
      }
      .orElseGet {
        getClubMemberFallback(dokiTheme)
      }
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

  private fun getClubMemberFallback(dokiTheme: DokiTheme): Optional<String> {
    return dokiTheme.getStickerPath()
      .map { "${ASSETS_SOURCE}/stickers/jetbrains$it" }
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
