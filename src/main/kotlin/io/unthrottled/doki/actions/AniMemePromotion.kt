package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.promotions.AniMemePluginPromotion

class AniMemePromotion : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    AniMemePluginPromotion.runPromotion({}) { }
  }
}
