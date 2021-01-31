package io.unthrottled.doki.assets

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import io.unthrottled.doki.integrations.RestClient
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toOptional
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

private enum class AssetChangedStatus {
  SAME, DIFFERENT, LUL_DUNNO
}

object LocalAssetService {
  private val log = Logger.getInstance(this::class.java)
  private val gson = GsonBuilder().setPrettyPrinting().create()
  private val assetChecks: ConcurrentHashMap<String, Instant> = readPreviousAssetChecks()

  fun hasAssetChanged(
    localInstallPath: Path,
    remoteAssetUrl: String
  ): Boolean =
    !Files.exists(localInstallPath) ||
      (
        !hasBeenCheckedToday(localInstallPath) &&
          isLocalDifferentFromRemote(localInstallPath, remoteAssetUrl) == AssetChangedStatus.DIFFERENT
        )

  private fun computeCheckSum(byteArray: ByteArray): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    messageDigest.update(byteArray)
    return StringUtil.toHexString(messageDigest.digest())
  }

  private fun getRemoteAssetChecksum(remoteAssetUrl: String): Optional<String> =
    RestClient.performGet("$remoteAssetUrl.checksum.txt")

  private fun isLocalDifferentFromRemote(
    localInstallPath: Path,
    remoteAssetUrl: String
  ): AssetChangedStatus =
    getRemoteAssetChecksum(remoteAssetUrl)
      .map {
        writeCheckedDate(localInstallPath)
        val onDiskCheckSum = computeCheckSum(Files.readAllBytes(localInstallPath))
        if (it == onDiskCheckSum) {
          AssetChangedStatus.SAME
        } else {
          log.warn(
            """
                      Local asset: $localInstallPath
                      is different from remote asset $remoteAssetUrl
                      Local Checksum: $onDiskCheckSum
                      Remote Checksum: $it
            """.trimIndent()
          )
          AssetChangedStatus.DIFFERENT
        }
      }.orElseGet { AssetChangedStatus.LUL_DUNNO }

  private fun hasBeenCheckedToday(localInstallPath: Path): Boolean =
    assetChecks[getAssetCheckKey(localInstallPath)]?.truncatedTo(ChronoUnit.DAYS) ==
      Instant.now().truncatedTo(ChronoUnit.DAYS)

  private fun writeCheckedDate(localInstallPath: Path) {
    assetChecks[getAssetCheckKey(localInstallPath)] = Instant.now()
    val assetCheckPath = getAssetChecksFile()
    LocalStorageService.createDirectories(assetCheckPath)
    runSafely({
      Files.newBufferedWriter(
        assetCheckPath,
        Charset.defaultCharset(),
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
      ).use { writer ->
        writer.write(gson.toJson(assetChecks))
      }
    }) {
      log.warn("Unable to persist checked date because of reasons", it)
    }
  }

  private fun readPreviousAssetChecks(): ConcurrentHashMap<String, Instant> =
    runSafelyWithResult({
      getAssetChecksFile().toOptional()
        .filter { Files.exists(it) }
        .map {
          Files.newBufferedReader(it).use { reader ->
            gson.fromJson<ConcurrentHashMap<String, Instant>>(
              reader,
              object : TypeToken<ConcurrentHashMap<String, Instant>>() {}.type
            )
          }
        }.orElseGet { ConcurrentHashMap() }
    }) {
      log.warn("Unable to get local asset checks for raisins", it)
      ConcurrentHashMap()
    }

  private fun getAssetChecksFile() =
    Paths.get(LocalStorageService.getLocalAssetDirectory(), "assetChecks.json")

  private fun getAssetCheckKey(localInstallPath: Path) =
    localInstallPath.toAbsolutePath().toString()
}
