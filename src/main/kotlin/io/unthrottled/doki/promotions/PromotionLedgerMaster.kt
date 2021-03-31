package io.unthrottled.doki.promotions

import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
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

object PromotionLedgerMaster : LedgerMaster<PromotionLedger>(
  AssetManager.constructGlobalAssetPath(
    AssetCategory.PROMOTION,
    "ledger.json"
  ).orElseGet {
    AssetManager.constructLocalAssetPath(
      AssetCategory.PROMOTION,
      "ledger.json"
    )
  },
  PromotionLedger::class.java
) {
  override fun initialLedger(): PromotionLedger =
    PromotionLedger(UUID.randomUUID(), mutableMapOf(), mutableMapOf(), true)
}

