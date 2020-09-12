package io.unthrottled.doki.assets

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object LocalStorageService {
  private val log = Logger.getInstance(this::class.java)

  fun createDirectories(directoriesToCreate: Path) {
    try {
      Files.createDirectories(directoriesToCreate.parent)
    } catch (e: IOException) {
      log.error("Unable to create directories $directoriesToCreate for raisins", e)
    }
  }

  fun getLocalAssetDirectory(): String =
    Paths.get(
      PathManager.getConfigPath(),
      "dokiThemeAssets"
    ).toAbsolutePath().toString()
}
