package io.unthrottled.doki.promotions

import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.stickers.CurrentSticker

data class CulturedContentLedger(
  val allowedCulturedContent: MutableMap<String, MutableSet<CurrentSticker>>,
)

object CulturedContentLedgerMaster : LedgerMaster<CulturedContentLedger>(
  AssetManager.constructGlobalAssetPath(
    AssetCategory.MISC,
    "cultured-ledger.json"
  ).orElseGet {
    AssetManager.constructLocalAssetPath(
      AssetCategory.MISC,
      "cultured-ledger.json"
    )
  },
  CulturedContentLedger::class.java
) {
  override fun initialLedger(): CulturedContentLedger =
    CulturedContentLedger(mutableMapOf())
}
