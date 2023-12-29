package io.unthrottled.doki.icon

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfoImpl
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor.namedColor
import com.intellij.ui.svg.SvgAttributePatcher
import com.intellij.util.io.DigestUtil
import io.unthrottled.doki.hax.PatcherProvider
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toHexString
import io.unthrottled.doki.util.toOptional
import org.w3c.dom.Element
import java.awt.Color
import java.time.Duration

object NoOptPatcher : SvgAttributePatcher {
  override fun patchColors(attributes: MutableMap<String, String>) {
  }
}

val noOptPatcherProvider = object : PatcherProvider {
  val longArray = longArrayOf(0)
  override fun digest(): LongArray {
    return longArray
  }
}

object DokiColorPatcher : PatcherProvider {

  private var otherColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private var uiColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private lateinit var dokiTheme: DokiTheme
  private val patcherProviderCache = HashSet<String>()


  fun setDokiTheme(dokiTheme: DokiTheme) {
    this.dokiTheme = dokiTheme
    LafManager.getInstance()
      ?.currentUIThemeLookAndFeel.toOptional()
      .filter { it is UIThemeLookAndFeelInfoImpl }
      .map { it as UIThemeLookAndFeelInfoImpl }
      .ifPresent {
        this.uiColorPatcherProvider = it.theme.colorPatcher ?: noOptPatcherProvider
      }
    clearCaches()
  }


  fun setOtherPatcher(otherPatcher: PatcherProvider) {
    this.otherColorPatcherProvider = otherPatcher
    clearCaches()
  }

  private fun clearCaches() {
    this.patcherCache.invalidateAll()
    this.patcherProviderCache.clear()
  }

  override fun digest(): LongArray {
    val shaDigest = DigestUtil.sha512()
//        otherPatchers.forEach { otherPatcher ->
//          shaDigest.update(otherPatcher.digest() ?: emptyByteArray)
//        }
    if (DokiColorPatcher::dokiTheme.isInitialized) {
      shaDigest.update((dokiTheme.id + dokiTheme.version).toByteArray(Charsets.UTF_8))
    } else {
      shaDigest.update(this.toString().toByteArray(Charsets.UTF_8))
    }
    val digest = shaDigest.digest()
    val longArray = LongArray(digest.size)
    digest.forEachIndexed { index, byteMe -> longArray[index] = byteMe.toLong() }
    return longArray
  }

  private val patcherCache: Cache<String, SvgAttributePatcher> =
    CacheBuilder.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(1))
      .build()

  override fun attributeForPath(path: String): SvgAttributePatcher? {
    val safeKey = path ?: "ayyLmao"
    return if (patcherProviderCache.add(safeKey)) {
      val hackedPatcher = buildHackedPatcher(
        listOf(otherColorPatcherProvider, uiColorPatcherProvider)
          .distinct()
          .mapNotNull { patcherProvider ->
            runSafelyWithResult({
              patcherProvider.attributeForPath(path)
            }) {
              null
            }
          },
        safeKey
      )
      patcherProviderCache.remove(safeKey)
      hackedPatcher
    } else {
      NoOptPatcher
    }
  }

  private fun buildHackedPatcher(
    otherPatchers: List<SvgAttributePatcher>,
    patcherKey: String
  ): SvgAttributePatcher {
    val cachedPatcher = patcherCache.getIfPresent(patcherKey)
    if (cachedPatcher !== null) {
      return cachedPatcher
    }

    val self = this
    val recursionResistentPatcher = object : SvgAttributePatcher {
      private val svgCache = HashSet<Element>()
      override fun patchColors(attributes: MutableMap<String, String>) {
//        if (svgCache.add(svg)) {
//          try {
//            runSafely({
//              self.patchColors(svg, otherPatchers)
//            })
//          } finally {
//            svgCache.remove(svg)
//          }
//        }
        otherPatchers.forEach { otherPatcher ->
          runSafely({
            otherPatcher.patchColors(attributes)
          })
        }

        patchAccent("accentTint", attributes) {
          it.toHexString()
        }
        patchAccent("accentTintDarker", attributes) {
          ColorUtil.darker(it, 1).toHexString()
        }
        patchAccent("accentContrastTint", attributes) {
          getIconAccentContrastColor().toHexString()
        }
        patchAccent("stopTint", attributes) {
          getThemedStopColor()
        }
        patchAccent("editorAccentTint", attributes) {
          ThemeManager.instance.currentTheme
            .map { it.editorAccentColor.toHexString() }
            .orElseGet { Color.CYAN.toHexString() }
        }

        val themedStartAttr = attributes.get("themedStart")
        val themedStopAttr = attributes.get("themedStop")
        val themedFillAttr = attributes.get("themedFill")
        when {
          "true" == themedStartAttr -> {
            val themedStart = getThemedStartColor()
            attributes.set("stop-color", themedStart)
            attributes.set("fill", themedStart)
          }

          "true" == themedStopAttr -> {
            val themedStop = getThemedStopColor()
            attributes.set("stop-color", themedStop)
            attributes.set("fill", themedStop)
          }

          "true" == themedFillAttr -> {
            val themedStart = getThemedStartColor()
            attributes.set("fill", themedStart)
            attributes.set("stroke", themedStart)
          }
        }
      }
    }

    patcherCache.put(patcherKey, recursionResistentPatcher)

    return recursionResistentPatcher
  }

  private fun patchAccent(
    attribute: String,
    attributes: MutableMap<String, String>,
    colorDecorator: (Color) -> String
  ) {
    when (attribute) {
      "fill" -> attributes.set("fill", colorDecorator(getAccentColor()))
      "stroke" -> attributes.set("stroke", colorDecorator(getAccentColor()))
      "both", "partialFill" -> {
        val accentColor = colorDecorator(getAccentColor())
        attributes.set("stroke", accentColor)
        attributes.set("stroke-opacity", if (attribute == "both") "1" else "0.25")
        attributes.set("fill", accentColor)
      }
    }
  }

  private fun getAccentColor() =
    namedColor("Doki.Accent.color", Color.CYAN)

  private fun getIconAccentContrastColor() =
    namedColor("Doki.Icon.Accent.Contrast.color", Color.WHITE)

  private fun getThemedStartColor() =
    namedColor("Doki.startColor", Color.CYAN).toHexString()

  private fun getThemedStopColor() =
    namedColor("Doki.stopColor", Color.CYAN).toHexString()


}