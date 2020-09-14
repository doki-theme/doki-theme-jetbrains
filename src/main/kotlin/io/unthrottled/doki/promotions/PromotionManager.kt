package io.unthrottled.doki.promotions

import com.google.gson.GsonBuilder
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.io.exists
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService.createDirectories
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.stickers.StickerLevel
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.util.UUID

const val MOTIVATOR_PLUGIN_ID = "zd.zero.waifu-motivator-plugin"

object PromotionManager {

  private val log = Logger.getInstance(PromotionManager::class.java)

  private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

  private var initialized = false

  private val ledgerPath = AssetManager.constructGlobalAssetPath(
    AssetCategory.PROMOTION,
    "ledger.json"
  ).orElseGet {
    AssetManager.constructLocalAssetPath(
      AssetCategory.PROMOTION,
      "ledger.json"
    )
  }

  private val lockPath = AssetManager.constructGlobalAssetPath(
    AssetCategory.PROMOTION,
    "lock.json"
  ).orElseGet {
    AssetManager.constructLocalAssetPath(
      AssetCategory.PROMOTION,
      "lock.json"
    )
  }

  private val promotionLedger: PromotionLedger =
    if (ledgerPath.exists()) {
      readLedger()
    } else {
      PromotionLedger(UUID.randomUUID(), mutableMapOf())
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
      PromotionLedger(UUID.randomUUID(), mutableMapOf())
    }
  }

  fun registerPromotion(newVersion: String) {
    if (initialized.not()) {
      promotionRegistry(newVersion)
    }
  }

  private fun promotionRegistry(newVersion: String) {
    val versionInstallDates = promotionLedger.versionInstallDates
    if (versionInstallDates.containsKey(newVersion).not()) {
      versionInstallDates[newVersion] = Instant.now()
      persistLedger()
    } else {
//      val latestInstallDate = versionInstallDates[newVersion]!!
//      if (Duration.between(latestInstallDate, Instant.now()).toDays() > 2) {
      setupPromotion()
//      }
    }
  }

  private fun setupPromotion() {
    if (isMotivatorInstalled().not() && shouldPromote()) {
      // todo: check if online first
      MotivatorPluginPromotion {
        // mark promoted
      }
    }
  }

  // todo: has been promoted as well
  // todo: not locked
  private fun shouldPromote(): Boolean =
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON

  private fun isMotivatorInstalled(): Boolean =
    PluginManagerCore.isPluginInstalled(
      PluginId.getId(MOTIVATOR_PLUGIN_ID)
    )

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

data class Lock(
  val lockedBy: String,
  val lockedDate: Instant
)

data class PromotionLedger(
  val user: UUID,
  val versionInstallDates: MutableMap<String, Instant>
)