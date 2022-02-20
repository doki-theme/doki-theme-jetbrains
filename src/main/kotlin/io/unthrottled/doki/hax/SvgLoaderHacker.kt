package io.unthrottled.doki.hax

import com.intellij.util.SVGLoader
import io.unthrottled.doki.icon.ColorPatcher
import io.unthrottled.doki.themes.DokiTheme
import java.util.Optional

typealias SVGL = SVGLoader
typealias PatcherProvider = SVGLoader.SvgElementColorPatcherProvider
typealias Patcher = SVGLoader.SvgElementColorPatcher

object SvgLoaderHacker {

  private lateinit var otherColorPatcher: PatcherProvider

  /**
   * Enables the ability to have more than one color patcher.
   */
  fun setSVGColorPatcher(dokiTheme: DokiTheme) {
    collectOtherPatcher()
      .ifPresent { otherPatcher ->
        val patcherKey = otherPatcher.javaClass.canonicalName
        if (otherPatchers.containsKey(patcherKey).not()) {
          otherPatchers[patcherKey]
        }
      }

    SVGLoader.setColorPatcherProvider(
      ColorPatcher(
        otherPatchers.values,
        dokiTheme
      )
    )
  }

  private val otherPatchers: Map<String, PatcherProvider> = mapOf()

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
        it as PatcherProvider
      }
}
