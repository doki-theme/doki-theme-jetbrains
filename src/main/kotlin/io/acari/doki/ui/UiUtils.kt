package io.acari.doki.ui

import java.awt.RenderingHints
import java.util.*

object UiUtils {
  private val RENDERING_HINTS = RenderingHints(
    RenderingHints.KEY_ALPHA_INTERPOLATION,
    RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
  )

  val renderingHints: Map<Any, Any>
  get() = Collections.unmodifiableMap(RENDERING_HINTS)

  init {
    RENDERING_HINTS[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
    RENDERING_HINTS[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_SPEED
    RENDERING_HINTS[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    RENDERING_HINTS[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
  }

}