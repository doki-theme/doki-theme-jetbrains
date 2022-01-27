package io.unthrottled.doki.hax

import com.intellij.util.SVGLoader
import io.unthrottled.doki.icon.ColorPatcher
import java.net.URL
import java.util.Optional

typealias SVGL = SVGLoader
typealias PatcherProvider = SVGLoader.SvgElementColorPatcherProvider
typealias Patcher = SVGLoader.SvgElementColorPatcher

object SvgLoaderHacker {

  private lateinit var otherColorPatcher: PatcherProvider

  /**
   * Enables the ability to have more than one color patcher.
   */
  fun setSVGColorPatcher() {
//    SVGLoader.setColorPatcherProvider(
//      collectOtherPatcher()
//        .map { patcher ->
//          ColorPatcher(patcher)
//        }
//        .orElseGet {
//          ColorPatcher(
//            object : PatcherProvider {
//              override fun forURL(url: URL?): SVGLoader.SvgElementColorPatcher? = null
//            }
//          )
//        }
//    )
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
        otherColorPatcher = otherPatcher
        otherPatcher
      }
      .map { Optional.of(it) }
      .orElseGet { useFallBackPatcher() }

  private fun useFallBackPatcher(): Optional<PatcherProvider> =
    if (this::otherColorPatcher.isInitialized) Optional.of(otherColorPatcher)
    else Optional.empty()
}
