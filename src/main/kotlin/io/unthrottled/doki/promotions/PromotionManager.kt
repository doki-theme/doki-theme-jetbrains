package io.unthrottled.doki.promotions

import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.doki.assets.AssetManager.ASSET_SOURCE
import io.unthrottled.doki.assets.AssetManager.FALLBACK_ASSET_SOURCE
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.integrations.RestClient.performGet
import io.unthrottled.doki.promotions.AniMemePromotionService.runPromotion
import io.unthrottled.doki.promotions.LockMaster.acquireLock
import io.unthrottled.doki.promotions.LockMaster.releaseLock
import io.unthrottled.doki.promotions.OnlineService.isOnline
import io.unthrottled.doki.promotions.PromotionLedgerMaster.getInitialLedger
import io.unthrottled.doki.promotions.PromotionLedgerMaster.persistLedger
import io.unthrottled.doki.service.AppService.getApplicationName
import io.unthrottled.doki.service.PluginService.canAmiiBeInstalled
import io.unthrottled.doki.service.PluginService.isAmiiInstalled
import io.unthrottled.doki.service.PluginService.isMotivatorInstalled
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.util.toOptional
import java.time.Duration
import java.time.Instant
import java.util.UUID

val MOTIVATION_PROMOTION_ID: UUID = UUID.fromString("63e1da85-1285-40c4-873a-3ed1122995e1")

object PromotionManager : PromotionManagerImpl()

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
      val latestInstallDate = versionInstallDates[newVersion]!!
      if (Duration.between(latestInstallDate, Instant.now()).toDays() > 2) {
        setupPromotion()
      }
    }
  }

  @Suppress("UnusedPrivateMember")
  private fun setupPromotion() {
    if (areAniMemePluginsInstalled().not() && shouldPromote() && isOnline()) {
      try {
        if (acquireLock(id)) {
          runPromotion({
            promotionLedger.allowedToPromote = it.status != PromotionStatus.BLOCKED
            promotionLedger.seenPromotions[MOTIVATION_PROMOTION_ID] =
              Promotion(MOTIVATION_PROMOTION_ID, Instant.now(), it.status)
            persistLedger(promotionLedger)
            releaseLock(id)
          }) {
            releaseLock(id)
          }
        }
      } catch (e: Throwable) {
        log.warn("Unable to promote for raisins.", e)
      }
    }
  }

  private fun areAniMemePluginsInstalled() =
    isMotivatorInstalled() || isAmiiInstalled()

  private val id: String
    get() = getApplicationName()

  private fun shouldPromote(): Boolean =
    promotionLedger.allowedToPromote &&
      (
        promotionLedger.seenPromotions.containsKey(MOTIVATION_PROMOTION_ID).not() ||
          promotionLedger.seenPromotions[MOTIVATION_PROMOTION_ID]?.result == PromotionStatus.ACCEPTED
        ) &&
      WeebService.isWeebStuffOn() &&
      canAmiiBeInstalled()
}

object WeebService {

  fun isWeebStuffOn(): Boolean =
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON ||
      ThemeConfig.instance.isDokiBackground
}

object OnlineService {

  fun isOnline(): Boolean =
    isOnlineCheck(ASSET_SOURCE)
      .map { it.toOptional() }
      .orElseGet { isOnlineCheck(FALLBACK_ASSET_SOURCE) }
      .orElse(false)

  private fun isOnlineCheck(assetsSource: String) =
    performGet("$assetsSource/misc/am-i-online.txt")
      .map { it.trim() == "yes" }
      .filter { it }
}
