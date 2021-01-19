package io.unthrottled.doki.assets

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toOptional
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Optional

object LocalStorageService {
  private val log = Logger.getInstance(this::class.java)
  const val ASSET_DIRECTORY = "dokiThemeAssets"

  fun createDirectories(directoriesToCreate: Path) {
    try {
      Files.createDirectories(directoriesToCreate.parent)
    } catch (e: IOException) {
      log.error("Unable to create directories $directoriesToCreate for raisins", e)
    }
  }

  fun getLocalAssetDirectory(): String {
    return Paths.get(
      PathManager.getConfigPath(),
      ASSET_DIRECTORY
    ).toAbsolutePath().toString()
  }

  fun getGlobalAssetDirectory(): Optional<String> =
    Paths.get(
      PathManager.getConfigPath(),
      "..",
      ASSET_DIRECTORY
    ).toAbsolutePath()
      .normalize()
      .toOptional()
      .filter { Files.isWritable(it.parent) }
      .map {
        if (Files.exists(it).not()) {
          runSafely({
            Files.createDirectories(it)
          }) {
            log.warn("Unable to create global directory for raisins", it)
          }
        }
        it.toString()
      }
}
