package io.unthrottled.doki.stickers.impl

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil.toHexString
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import io.unthrottled.doki.stickers.StickerService
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.readAllTheBytes
import io.unthrottled.doki.util.toOptional
import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.util.*
import java.util.Optional.empty
import java.util.Optional.ofNullable
import java.util.concurrent.TimeUnit

const val DOKI_BACKGROUND_PROP: String = "io.unthrottled.doki.background"

private const val ASSETS_SOURCE = "https://doki.assets.unthrottled.io"
private const val BACKGROUND_DIRECTORY = "$ASSETS_SOURCE/backgrounds"
const val DOKI_STICKER_PROP: String = "io.unthrottled.doki.stickers"
private const val PREVIOUS_STICKER = "io.unthrottled.doki.sticker.previous"

private enum class AssetChangedStatus {
  SAME, DIFFERENT, LUL_DUNNO
}

class StickerServiceImpl : StickerService {

  companion object {
    private val httpClient = HttpClients.custom()
      .setUserAgent(ApplicationInfoEx.getInstance().fullApplicationName)
      .build()
    private val log = Logger.getInstance(this::class.java)
  }

  override fun activateForTheme(dokiTheme: DokiTheme) {
    listOf({
      installSticker(dokiTheme)
    },
      {
        installBackgroundImage(dokiTheme)
      }
    ).forEach {
      getApplication().executeOnPooledThread(it)
    }
  }

  override fun checkForUpdates(dokiTheme: DokiTheme) {
    getApplication().executeOnPooledThread {
      listOf(
        getLocalStickerPath(dokiTheme) to getRemoteStickerUrl(dokiTheme),
        getLocalBackgroundImagePath(dokiTheme) to getRemoteBackgroundUrl(dokiTheme)
      )
        .forEach { localAndRemoteAssetPaths ->
          val (localAssetPath, remoteAssetUrl) = localAndRemoteAssetPaths
          localAssetPath
            .filter { hasAssetChanged(it, remoteAssetUrl) }
            .ifPresent {
              downloadAsset(it, remoteAssetUrl)
            }
        }
    }
  }

  private fun installSticker(dokiTheme: DokiTheme) =
    getLocallyInstalledStickerPath(dokiTheme)
      .ifPresent {
        setBackgroundImageProperty(
          it,
          "98",
          IdeBackgroundUtil.Fill.PLAIN.name,
          IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
          DOKI_STICKER_PROP
        )
      }

  private fun installBackgroundImage(dokiTheme: DokiTheme) =
    getLocallyInstalledBackgroundImagePath(dokiTheme)
      .ifPresent {
        setBackgroundImageProperty(
          it,
          "100",
          IdeBackgroundUtil.Fill.SCALE.name,
          IdeBackgroundUtil.Anchor.CENTER.name,
          DOKI_BACKGROUND_PROP
        )
      }

  private fun getLocallyInstalledBackgroundImagePath(
    dokiTheme: DokiTheme
  ): Optional<String> =
    getLocallyInstalledAssetPath({
      getLocalBackgroundImagePath(dokiTheme)
    }) {
      getRemoteBackgroundUrl(dokiTheme)
    }

  private fun getLocallyInstalledStickerPath(
    dokiTheme: DokiTheme
  ): Optional<String> =
    getLocallyInstalledAssetPath({
      getLocalStickerPath(dokiTheme)
    }) {
      getRemoteStickerUrl(dokiTheme)
    }

  private fun getLocallyInstalledAssetPath(
    localAssetPathSupplier: () -> Optional<Path>,
    remoteAssetUrlSupplier: () -> Optional<String>
  ): Optional<String> {
    val remoteAssetUrl = remoteAssetUrlSupplier()
    return canWriteAssetsLocally()
      .map {
        val localAssetPath = localAssetPathSupplier()
        localAssetPath
          .flatMap { localStickerPath ->
            if (hasAssetChanged(localStickerPath, remoteAssetUrl)) {
              downloadAsset(localStickerPath, remoteAssetUrl)
            } else {
              localStickerPath.toString().toOptional()
            }
          }
      }
      .orElseGet {
        remoteAssetUrl
      }
  }

  private fun canWriteAssetsLocally(): Optional<Boolean> =
    getLocalAssetDirectory()
      .map { get(it) }
      .filter { Files.isWritable(it.parent) }
      .map { true }

  private fun downloadAsset(
    localStickerPath: Path,
    remoteAssetUrl: Optional<String>
  ): Optional<String> {
    createDirectories(localStickerPath)
    return remoteAssetUrl
      .flatMap {
        downloadRemoteAsset(localStickerPath, it)
      }
  }

  private fun createDirectories(directoriesToCreate: Path) {
    try {
      Files.createDirectories(directoriesToCreate.parent)
    } catch (e: IOException) {
      log.error("Unable to create directories $directoriesToCreate for raisins", e)
    }
  }

  private fun downloadRemoteAsset(
    localAssetPath: Path,
    remoteAssetPath: String
  ): Optional<String> = try {
    log.warn("Attempting to download asset $remoteAssetPath")
    val remoteAssetRequest = createGetRequest(remoteAssetPath)
    val remoteAssetResponse = httpClient.execute(remoteAssetRequest)
    if (remoteAssetResponse.statusLine.statusCode == 200) {
      remoteAssetResponse.entity.content.use { inputStream ->
        Files.newOutputStream(
          localAssetPath,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING
        ).use { bufferedWriter ->
          IOUtils.copy(inputStream, bufferedWriter)
        }
      }
      localAssetPath.toString().toOptional()
    } else {
      log.warn("Asset request for $remoteAssetPath responded with $remoteAssetResponse")
      getFallbackURL(localAssetPath, remoteAssetPath)
    }
  } catch (e: Throwable) {
    log.error("Unable to get remote remote asset $remoteAssetPath for raisins", e)
    getFallbackURL(localAssetPath, remoteAssetPath)
  }

  private fun getFallbackURL(localAssetPath: Path, remoteAssetPath: String) =
    when {
      Files.exists(localAssetPath) -> localAssetPath.toUri().toString()
      else -> remoteAssetPath
    }.toOptional()

  private fun createGetRequest(remoteUrl: String): HttpGet {
    val remoteAssetRequest = HttpGet(remoteUrl)
    remoteAssetRequest.config = RequestConfig.custom()
      .setConnectTimeout(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS).toInt())
      .build()
    return remoteAssetRequest
  }

  private fun hasAssetChanged(
    localInstallPath: Path,
    remoteAssetUrl: Optional<String>
  ): Boolean =
    !Files.exists(localInstallPath) ||
      isLocalDifferentFromRemote(localInstallPath, remoteAssetUrl) == AssetChangedStatus.DIFFERENT

  private fun isLocalDifferentFromRemote(
    localInstallPath: Path,
    remoteAssetUrl: Optional<String>
  ): AssetChangedStatus =
    getRemoteAssetChecksum(remoteAssetUrl)
      .map {
        val onDiskCheckSum = getOnDiskCheckSum(localInstallPath)
        if (it == onDiskCheckSum) {
          AssetChangedStatus.SAME
        } else {
          log.warn("""
            Local asset: $localInstallPath
            is different from remote asset ${remoteAssetUrl.orElse("No Remote Asset.")}
            Local Checksum: $onDiskCheckSum
            Remote Checksum: $it
          """.trimIndent())
          AssetChangedStatus.DIFFERENT
        }
      }.orElseGet { AssetChangedStatus.LUL_DUNNO }

  private fun getRemoteAssetChecksum(remoteAssetUrl: Optional<String>): Optional<String> =
    remoteAssetUrl
      .map { "$it.checksum.txt" }
      .flatMap {
        log.info("Attempting to fetch checksum for remote asset: $it")
        val request = createGetRequest(it)
        try {
          val response = httpClient.execute(request)
          log.info("Checksum has responded for remote asset: $it")
          if (response.statusLine.statusCode == 200) {
            response.entity.content.use { responseBody ->
              String(responseBody.readAllTheBytes())
            }.toOptional()
          } else {
            empty()
          }
        } catch (e: Throwable) {
          log.warn("Unable to get checksum for remote asset: $it for raisins", e)
          empty<String>()
        }
      }

  private fun getOnDiskCheckSum(weebStuff: Path): String =
    computeCheckSum(Files.readAllBytes(weebStuff))

  private fun computeCheckSum(byteArray: ByteArray): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    messageDigest.update(byteArray)
    return toHexString(messageDigest.digest())
  }

  private fun getRemoteStickerUrl(dokiTheme: DokiTheme): Optional<String> =
    dokiTheme.getStickerPath()
      .map { "$ASSETS_SOURCE/stickers/jetbrains$it" }

  private fun getRemoteBackgroundUrl(dokiTheme: DokiTheme): Optional<String> =
    dokiTheme.getSticker()
      .map { "$BACKGROUND_DIRECTORY/$it" }

  private fun getLocalStickerPath(dokiTheme: DokiTheme): Optional<Path> =
    constructLocalAssetPath("stickers") {
      dokiTheme.getStickerPath()
    }

  private fun getLocalBackgroundImagePath(
    dokiTheme: DokiTheme
  ): Optional<Path> =
    constructLocalAssetPath("backgrounds") {
      dokiTheme.getSticker()
    }

  private fun constructLocalAssetPath(
    assetCategory: String,
    stickerPath: () -> Optional<String>
  ): Optional<Path> =
    stickerPath()
      .flatMap { themeStickerLocation ->
        getLocalAssetDirectory()
          .map { localInstallDirectory ->
            get(
              localInstallDirectory, assetCategory, themeStickerLocation
            ).normalize().toAbsolutePath()
          }
      }

  private fun getLocalAssetDirectory(): Optional<String> =
    ofNullable(
      PathManager.getConfigPath()
    ).map {
      get(it, "dokiThemeAssets").toAbsolutePath().toString()
    }

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

  private fun setBackgroundImageProperty(
    imagePath: String,
    opacity: String,
    fill: String,
    anchor: String,
    propertyKey: String
  ) {
    // org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
    // as to why this looks this way
    val propertyValue = listOf(imagePath, opacity, fill, anchor)
      .reduceRight { a, b -> "$a,$b" }
    setPropertyValue(propertyKey, propertyValue)
  }

  private fun setPropertyValue(propertyKey: String, propertyValue: String) {
    PropertiesComponent.getInstance().unsetValue(propertyKey)
    PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
  }
}
