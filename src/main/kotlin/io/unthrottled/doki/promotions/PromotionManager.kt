package io.unthrottled.doki.promotions

import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.exists
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService.createDirectories
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant

object PromotionManager {

  private val log = Logger.getInstance(PromotionManager::class.java)

  private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

  private val ledgerPath = AssetManager.constructLocalAssetPath(
    AssetCategory.PROMOTION,
    "ledger.json"
  )

  private val promotionLedger: PromotionLedger =
    if (ledgerPath.exists()) {
      readLedger()
    } else {
      PromotionLedger(mutableMapOf())
    }

  private fun readLedger(): PromotionLedger {
    return try {
      Files.newInputStream(ledgerPath)
        .use {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            PromotionLedger::class.java
          )
        }
    } catch (e: Throwable) {
      log.warn("Unable to read promotion ledger for raisins.", e)
      PromotionLedger(mutableMapOf())
    }
  }


  fun attemptToRegisterPromotion() {

  }

  fun markVersionInstall(newVersion: String) {
    if (promotionLedger.versionInstallDates.containsKey(newVersion).not()) {
      promotionLedger.versionInstallDates[newVersion] = Instant.now()
      persistLedger()
    }
  }

  private fun persistLedger() {
    if (ledgerPath.exists().not()) {
      createDirectories(ledgerPath)
    }

    try {
      Files.newBufferedWriter(
        ledgerPath,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
      ).use {
        it.write(
          gson.toJson(promotionLedger)
        )
      }
    } catch (e: Throwable) {
      log.warn("Unable to persist ledger for raisins", e)
    }
  }
}

data class PromotionLedger(
  val versionInstallDates: MutableMap<String, Instant>
)