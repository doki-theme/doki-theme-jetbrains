package io.acari.doki.stickers.impl

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.util.io.isFile
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.DokiTheme
import io.acari.doki.util.toOptional
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.util.*
import java.util.Optional.empty
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import javax.xml.bind.DatatypeConverter

const val DOKI_BACKGROUND_PROP: String = "io.unthrottled.doki.background"
private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")

private const val ASSETS_SOURCE = "https://doki.assets.unthrottled.io"
private const val BACKGROUND_DIRECTORY = "${ASSETS_SOURCE}/backgrounds"
const val DOKI_STICKER_PROP: String = "io.unthrottled.doki.stickers"
private const val PREVIOUS_STICKER = "io.unthrottled.doki.sticker.previous"

class StickerServiceImpl : StickerService {

  companion object {
    private val httpClient = HttpClients.createMinimal()
    private val log = Logger.getInstance(this::class.java)
  }

  override fun activateForTheme(dokiTheme: DokiTheme) {
    listOf({
      installSticker(dokiTheme)
    },
      {
        installBackgroundImage(dokiTheme)
      }
    ).map {
      getApplication().executeOnPooledThread(it)
    }
  }

  private fun waitForTasksToComplete(tasks: List<Future<*>>) {
    try {
      tasks.fold(true) { acc, future ->
        future.get()
        future.isDone && acc
      }
    } catch (t: Throwable) {
      log.error("Unable to activate stickers for raisins", t)
    }
  }

  override fun checkForUpdates(dokiTheme: DokiTheme) {
    getApplication().executeOnPooledThread {
      getLocalInstalledStickerPath(dokiTheme)
        .ifPresent { localStickerPath ->
          if (stickerHasChanged(localStickerPath, dokiTheme)) {
            downloadNewSticker(localStickerPath, dokiTheme)
          }
        }
    }
  }

  private fun installSticker(dokiTheme: DokiTheme) {
    getImagePath(dokiTheme)
      .ifPresent {
        setProperty(
          it,
          "98",
          IdeBackgroundUtil.Fill.PLAIN.name,
          IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
          DOKI_STICKER_PROP
        )
      }
  }

  private fun getImagePath(dokiTheme: DokiTheme): Optional<String> =
    getLocalClubMemberParentDirectory()
      .map { Paths.get(it) }
      .filter { Files.isWritable(it.parent) }
      .map {
        getLocalInstalledStickerPath(dokiTheme)
          .flatMap { localStickerPath ->
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

  private fun downloadNewSticker(localStickerPath: Path, dokiTheme: DokiTheme): Optional<Path> {
    createDirectories(localStickerPath)
    return getClubMemberFallback(dokiTheme)
      .flatMap {
        downloadRemoteSticker(localStickerPath, it)
      }
  }

  private fun createDirectories(directoriesToCreate: Path) {
    try {
      Files.createDirectories(directoriesToCreate.parent)
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun downloadRemoteSticker(
    localStickerPath: Path,
    remoteStickerUrl: String
  ): Optional<Path> = try {
    log.info("Attempting to download $remoteStickerUrl")
    val remoteStickerRequest = createGetRequest(remoteStickerUrl)
    val stickerResponse = httpClient.execute(remoteStickerRequest)
    if (stickerResponse.statusLine.statusCode == 200) {
      stickerResponse.entity.content.use { inputStream ->
        Files.newOutputStream(
          localStickerPath,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING
        ).use { bufferedWriter ->
          IOUtils.copy(inputStream, bufferedWriter)
        }
      }
    }
    localStickerPath.toOptional()
  } catch (e: Throwable) {
    log.error("Unable to get remote sticker $remoteStickerUrl for raisins", e)
    localStickerPath.toOptional()
  }

  private fun createGetRequest(remoteUrl: String): HttpGet {
    val remoteStickerRequest = HttpGet(remoteUrl)
    remoteStickerRequest.config = RequestConfig.custom()
      .setConnectTimeout(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS).toInt())
      .build()
    return remoteStickerRequest
  }


  private fun stickerHasChanged(localInstallPath: Path, dokiTheme: DokiTheme): Boolean =
    !Files.exists(localInstallPath) ||
        isLocalDifferentFromRemote(localInstallPath, dokiTheme)

  private fun isLocalDifferentFromRemote(
    localInstallPath: Path, dokiTheme: DokiTheme
  ): Boolean =
    getOnDiskCheckSum(localInstallPath) !=
        getRemoteChecksum(dokiTheme)


  private fun getRemoteChecksum(dokiTheme: DokiTheme): String {
    return getClubMemberFallback(dokiTheme)
      .map { "$it.checksum.txt" }
      .flatMap {
        log.info("Attempting to fetch checksum $it")
        val request = createGetRequest(it)
        try {
          val response = httpClient.execute(request)
          log.info("Checksum has responded for $it")
          if (response.statusLine.statusCode == 200) {
            response.entity.content.use { responseBody ->
              String(responseBody.readAllBytes())
            }.toOptional()
          } else {
            empty()
          }
        } catch (e: Exception) {
          log.warn("Unable to get remote checksum for $it for raisins", e)
          empty<String>()
        }
      }
      .orElseGet {
        "I AM BECOME DEATH, DESTROYER OF WORLDS"
      }
  }

  private fun getOnDiskCheckSum(weebStuff: Path): String =
    computeCheckSum(Files.readAllBytes(weebStuff))

  private fun computeCheckSum(byteArray: ByteArray): String {
    messageDigest.update(byteArray)
    val digest = messageDigest.digest()
    return DatatypeConverter.printHexBinary(digest).toLowerCase()
  }

  private fun getClubMemberFallback(dokiTheme: DokiTheme): Optional<String> {
    return dokiTheme.getStickerPath()
      .map { "${ASSETS_SOURCE}/stickers/jetbrains$it" }
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


  private fun getLocalClubMemberParentDirectory(): Optional<String> =
    Optional.ofNullable(
      PathManager.getConfigPath()
    ).map {
      Paths.get(it, "dokiThemeAssets").toAbsolutePath().toString()
    }

  private fun installBackgroundImage(dokiTheme: DokiTheme) {
    getFrameBackground(dokiTheme)
      .ifPresent {
        setProperty(
          it,
          "100",
          IdeBackgroundUtil.Fill.SCALE.name,
          IdeBackgroundUtil.Anchor.CENTER.name,
          DOKI_BACKGROUND_PROP
        )
      }
  }

  private fun getFrameBackground(dokiTheme: DokiTheme): Optional<String> =
    dokiTheme.getSticker()
      .map { "$BACKGROUND_DIRECTORY/$it" }


  override fun remove() {
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


  override fun getPreviousSticker(): Optional<String> =
    PropertiesComponent.getInstance().getValue(PREVIOUS_STICKER).toOptional()

  override fun clearPreviousSticker() {
    PropertiesComponent.getInstance().unsetValue(PREVIOUS_STICKER)
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
