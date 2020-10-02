package io.unthrottled.doki.hax

import com.intellij.util.SVGLoader
import io.unthrottled.doki.icon.ColorPatcher
import java.util.*

object SvgLoaderHacker {

  private lateinit var otherColorPatcher: SVGLoader.SvgElementColorPatcherProvider

  /**
   * Enables the ability to have more than one color patcher.
   */
  fun setSVGColorPatcher() {
    SVGLoader.setColorPatcherProvider(collectOtherPatcher()
      .map { patcher ->
        ColorPatcher(patcher)
      }
      .orElseGet {
        ColorPatcher(object : SVGLoader.SvgElementColorPatcherProvider {
        })
      })
  }

  private fun collectOtherPatcher(): Optional<SVGLoader.SvgElementColorPatcherProvider> =
    Optional.ofNullable(SVGLoader::class.java.declaredFields
      .firstOrNull { it.name == "ourColorPatcher" })
      .map { ourColorPatcherField ->
        ourColorPatcherField.isAccessible = true
        ourColorPatcherField.get(null)
      }
      .filter { it is SVGLoader.SvgElementColorPatcherProvider }
      .filter { it !is ColorPatcher }
      .map {
        val otherPatcher = it as SVGLoader.SvgElementColorPatcherProvider
        otherColorPatcher = otherPatcher
        otherPatcher
      }
      .map { Optional.of(it) }
      .orElseGet { useFallBackPatcher() }

  private fun useFallBackPatcher(): Optional<SVGLoader.SvgElementColorPatcherProvider> =
    if (this::otherColorPatcher.isInitialized) Optional.of(otherColorPatcher)
    else Optional.empty()
}