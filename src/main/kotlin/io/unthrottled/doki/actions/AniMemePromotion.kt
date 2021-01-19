package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.promotions.AniMemePluginPromotion

class AniMemePromotion : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    AniMemePluginPromotion.runPromotion({}) { }
  }
}
