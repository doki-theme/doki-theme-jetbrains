package io.unthrottled.doki.hax

import com.intellij.util.SVGLoader
import io.unthrottled.doki.icon.ColorPatcher
import io.unthrottled.doki.themes.DokiTheme
import java.util.Optional

typealias PatcherProvider = SVGLoader.SvgElementColorPatcherProvider
typealias Patcher = SVGLoader.SvgElementColorPatcher

object SvgLoaderHacker {

  /**
   * Enables the ability to have more than one color patcher.
   */
  fun setSVGColorPatcher(dokiTheme: DokiTheme) {
    collectOtherPatcher()
      .ifPresent { otherPatcher ->
        ColorPatcher.setOtherPatcher(otherPatcher)
      }

    ColorPatcher.setDokiTheme(dokiTheme)

    SVGLoader.colorPatcherProvider = ColorPatcher
  }

  private fun collectOtherPatcher(): Optional<PatcherProvider> =
    Optional.ofNullable(
      SVGLoader::class.java.declaredFields
        .firstOrNull { it.name == "ourColorPatcher" }
    )
      .map { ourColorPatcherField ->
        ourColorPatcherField.isAccessible = true
        ourColorPatcherField.get(null)
      }
      .filter { it is PatcherProvider }
      .filter { it !is ColorPatcher }
      .map {
        val otherPatcher = it as PatcherProvider
        otherPatcher
      }
}
