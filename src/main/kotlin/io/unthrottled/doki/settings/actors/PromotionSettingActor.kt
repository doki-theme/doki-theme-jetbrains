package io.unthrottled.doki.settings.actors

import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.promotions.PromotionManager

object PromotionSettingActor {
  fun optInToPromotion(allowedToPromote: Boolean) {
    if (allowedToPromote != ThemeConfig.instance.allowPromotions) {
      ThemeConfig.instance.allowPromotions = allowedToPromote
      PromotionManager.setPromotionsEnabled(allowedToPromote)
    }
  }
}
