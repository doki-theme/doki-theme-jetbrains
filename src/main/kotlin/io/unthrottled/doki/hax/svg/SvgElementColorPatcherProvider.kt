package io.unthrottled.doki.hax.svg

import com.intellij.ui.svg.SvgAttributePatcher

interface SvgElementColorPatcherProvider {
  fun attributeForPath(path: String): SvgAttributePatcher? = null

  /**
   * Returns a digest of the current SVG color patcher.
   *
   * Consider using a two-element array, where the first element is a hash of the input data for the patcher,
   * and the second is an ID of the patcher (see [com.intellij.ui.icons.ColorPatcherIdGenerator]).
   */
  fun digest(): LongArray
}
