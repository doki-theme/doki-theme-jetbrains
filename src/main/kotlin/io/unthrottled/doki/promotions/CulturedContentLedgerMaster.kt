package io.unthrottled.doki.promotions

import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import java.util.UUID

data class CulturedContentLedger(
  val allowedThemes: MutableSet<String>,
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
    CulturedContentLedger(mutableSetOf())
}