package io.unthrottled.doki.promotions

import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.exists
import io.unthrottled.doki.assets.LocalStorageService
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

abstract class LedgerMaster<T>(
  private val ledgerPath: Path,
  private val clazz: Class<T>,
) {
  private val log = Logger.getInstance(PromotionLedgerMaster::class.java)

  abstract fun initialLedger(): T

  private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

  fun getInitialLedger(): T =
    if (ledgerPath.exists()) {
      readLedger()
    } else {
      initialLedger()
    }

  fun readLedger(): T =
    runSafelyWithResult({
      Files.newInputStream(ledgerPath)
        .use {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            clazz
          )
        }
    }) {
      log.warn("Unable to read promotion ledger for raisins.", it)
      initialLedger()
    }

  fun persistLedger(promotionLedger: T) {
    if (ledgerPath.exists().not()) {
      LocalStorageService.createDirectories(ledgerPath)
    }

    runSafely({
      Files.newBufferedWriter(
        ledgerPath,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
      ).use {
        it.write(
          gson.toJson(promotionLedger)
        )
      }
    }) {
      log.warn("Unable to persist ledger for raisins", it)
    }
  }
}
