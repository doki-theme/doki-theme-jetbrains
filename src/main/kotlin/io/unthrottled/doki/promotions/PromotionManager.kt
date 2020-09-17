package io.unthrottled.doki.promotions

import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.doki.assets.AssetManager.ASSETS_SOURCE
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.integrations.RestClient.performGet
import io.unthrottled.doki.promotions.LedgerMaster.getInitialLedger
import io.unthrottled.doki.promotions.LedgerMaster.persistLedger
import io.unthrottled.doki.promotions.LockMaster.acquireLock
import io.unthrottled.doki.promotions.LockMaster.releaseLock
import io.unthrottled.doki.promotions.MotivatorPromotionService.runPromotion
import io.unthrottled.doki.service.AppService.getApplicationName
import io.unthrottled.doki.service.PluginService.isMotivatorInstalled
import io.unthrottled.doki.stickers.StickerLevel
import java.time.Duration
import java.time.Instant
import java.util.UUID

val MOTIVATION_PROMOTION_ID: UUID = UUID.fromString("63e1da85-1285-40c4-873a-3ed1122995e1")

object PromotionManager: PromotionManagerImpl()

open class PromotionManagerImpl {

  private val log = Logger.getInstance(PromotionManager::class.java)

  private var initialized = false

  private val promotionLedger: PromotionLedger = getInitialLedger()

  fun registerPromotion(newVersion: String, forceRegister: Boolean = false) {
    if (initialized.not() || forceRegister) {
      promotionRegistry(newVersion)
      initialized = true
    }
  }

  private fun promotionRegistry(newVersion: String) {
    val versionInstallDates = promotionLedger.versionInstallDates
    if (versionInstallDates.containsKey(newVersion).not()) {
      versionInstallDates[newVersion] = Instant.now()
      persistLedger(promotionLedger)
    } else {
      // todo: put me back
      val latestInstallDate = versionInstallDates[newVersion]!!
      if (Duration.between(latestInstallDate, Instant.now()).toDays() > 2) {
        setupPromotion()
      }
    }
  }

  private fun setupPromotion() {
    if (isMotivatorInstalled().not() && shouldPromote() && isOnline()) {
      try {
        if (acquireLock(id)) {
          runPromotion {
            promotionLedger.allowedToPromote = it.status != PromotionStatus.BLOCKED
            promotionLedger.seenPromotions[MOTIVATION_PROMOTION_ID] =
              Promotion(MOTIVATION_PROMOTION_ID, Instant.now(), it.status)
            persistLedger(promotionLedger)
            releaseLock(id)
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
    get() = getApplicationName()

  private fun shouldPromote(): Boolean =
  // todo: put me back
    promotionLedger.seenPromotions.containsKey(MOTIVATION_PROMOTION_ID).not() &&
      WeebService.isWeebStuffOn()
}

object WeebService {

  fun isWeebStuffOn(): Boolean = ThemeConfig.instance.currentStickerLevel == StickerLevel.ON
}
