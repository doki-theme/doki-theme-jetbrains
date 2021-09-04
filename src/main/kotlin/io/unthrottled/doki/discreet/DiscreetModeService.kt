package io.unthrottled.doki.discreet

import com.intellij.openapi.project.Project
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.util.Logging

fun Project.discreetModeService(): DiscreetModeService =
  this.getService(DiscreetModeService::class.java)

class DiscreetModeService(private val project: Project) : Logging {
  private var currentMode = ThemeConfig.instance.discreetMode.toDiscreetMode()

  val isDiscreetMode: Boolean
    get() = ThemeConfig.instance.discreetMode

  fun applyDiscreetMode() {
    if (currentMode == DiscreetMode.ACTIVE) return
    currentMode = DiscreetMode.ACTIVE
//    project.memeService().clearMemes()
  }

  fun liftDiscreetMode() {
    if (currentMode == DiscreetMode.INACTIVE) return
    currentMode = DiscreetMode.INACTIVE
  }
}
