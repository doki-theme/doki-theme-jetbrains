package io.unthrottled.doki.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.StickerHideActor

class HideOnHoverAction : BaseToggleAction() {
  override fun isSelected(e: AnActionEvent): Boolean = ThemeConfig.instance.hideOnHover

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    StickerHideActor.setStickerHideStuff(
      state,
      ThemeConfig.instance.hideDelayMS,
    )
  }
}