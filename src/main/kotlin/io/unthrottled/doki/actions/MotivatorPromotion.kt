package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.promotions.MotivatorPluginPromotion

class MotivatorPromotion : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    MotivatorPluginPromotion.runPromotion({}) { }
  }
}
