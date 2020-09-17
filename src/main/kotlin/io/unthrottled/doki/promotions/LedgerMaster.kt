package io.unthrottled.doki.promotions

import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.exists
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService
import io.unthrottled.doki.util.runSafelyWithResult
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.util.UUID

data class Promotion(
  val id: UUID,
  val datePromoted: Instant,
  val result: PromotionStatus
)

data class PromotionLedger(
  val user: UUID,
  val versionInstallDates: MutableMap<String, Instant>,
  val seenPromotions: MutableMap<UUID, Promotion>,
  var allowedToPromote: Boolean
)

object LedgerMaster {
  private val log = Logger.getInstance(LedgerMaster::class.java)

  private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

  private val ledgerPath = AssetManager.constructGlobalAssetPath(
    AssetCategory.PROMOTION,
    "ledger.json"
  ).orElseGet {
    AssetManager.constructLocalAssetPath(
      AssetCategory.PROMOTION,
      "ledger.json"
    )
  }

  fun getInitialLedger(): PromotionLedger =
    if (ledgerPath.exists()) {
      readLedger()
    } else {
      PromotionLedger(UUID.randomUUID(), mutableMapOf(), mutableMapOf(), true)
    }

  fun persistLedger(promotionLedger: PromotionLedger) {
    if (ledgerPath.exists().not()) {
      LocalStorageService.createDirectories(ledgerPath)
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

  private fun readLedger(): PromotionLedger =
    runSafelyWithResult({
      Files.newInputStream(ledgerPath)
        .use {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            PromotionLedger::class.java
          )
        }
    }) {
      log.warn("Unable to read promotion ledger for raisins.", it)
      PromotionLedger(UUID.randomUUID(), mutableMapOf(), mutableMapOf(), true)
    }
}