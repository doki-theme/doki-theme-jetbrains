package io.unthrottled.doki.promotions

import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.doki.assets.AssetManager.ASSETS_SOURCE
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.integrations.RestClient.performGet
import io.unthrottled.doki.service.AppService
import io.unthrottled.doki.service.PluginService.isMotivatorInstalled
import io.unthrottled.doki.stickers.StickerLevel
import java.time.Instant
import java.util.UUID

private val MOTIVATION_PROMOTION_ID = UUID.fromString("63e1da85-1285-40c4-873a-3ed1122995e1")

object PromotionManager {

  private val log = Logger.getInstance(PromotionManager::class.java)

  private var initialized = false

  private val promotionLedger: PromotionLedger = LedgerMaster.getInitialLedger()

  fun registerPromotion(newVersion: String) {
    if (initialized.not()) {
      promotionRegistry(newVersion)
    }
  }

  private fun promotionRegistry(newVersion: String) {
    val versionInstallDates = promotionLedger.versionInstallDates
    if (versionInstallDates.containsKey(newVersion).not()) {
      versionInstallDates[newVersion] = Instant.now()
      LedgerMaster.persistLedger(promotionLedger)
    } else {
//      val latestInstallDate = versionInstallDates[newVersion]!!
//      if (Duration.between(latestInstallDate, Instant.now()).toDays() > 2) {
      setupPromotion()
//      }
    }
  }

  private fun setupPromotion() {
    if (isMotivatorInstalled().not() && shouldPromote() && isOnline()) {
      try {
        if (LockMaster.acquireLock(id)) {
          MotivatorPluginPromotion {
            promotionLedger.allowedToPromote = it.status != PromotionStatus.BLOCKED
            promotionLedger.seenPromotions[MOTIVATION_PROMOTION_ID] =
              Promotion(MOTIVATION_PROMOTION_ID, Instant.now(), it.status)
            LedgerMaster.persistLedger(promotionLedger)
            LockMaster.releaseLock(id)
          }
        }
      } catch (e: Throwable) {
        log.warn("Unable to promote for raisins.", e)
      }
    }
  }

  private fun isOnline(): Boolean =
    // todo: fall back asset source
    performGet("$ASSETS_SOURCE/misc/am-i-online.txt")
      .map { it.trim() == "yes" }
      .orElse(false)

  private val id: String
    get() = AppService.getApplicationName()

  private fun shouldPromote(): Boolean =
//    promotionLedger.seenPromotions.containsKey(MOTIVATOR_PLUGIN_ID).not() &&
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON

}

data class Lock(
  val lockedBy: String,
  val lockedDate: Instant
)

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