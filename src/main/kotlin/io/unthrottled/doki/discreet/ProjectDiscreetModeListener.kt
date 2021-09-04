package io.unthrottled.doki.discreet

import com.intellij.openapi.project.Project
import io.unthrottled.doki.discreet.discreetModeService

class ProjectDiscreetModeListener(private val project: Project) : DiscreetModeListener {
  override fun modeChanged(discreetMode: DiscreetMode) {
    when (discreetMode) {
      DiscreetMode.ACTIVE -> project.discreetModeService().applyDiscreetMode()
      DiscreetMode.INACTIVE -> project.discreetModeService().liftDiscreetMode()
    }
  }
}
