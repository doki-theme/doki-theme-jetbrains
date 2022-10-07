package io.unthrottled.doki.icon

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeBasedLookAndFeelInfo
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor.namedColor
import com.intellij.util.SVGLoader
import com.intellij.util.io.DigestUtil
import io.unthrottled.doki.hax.Patcher
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

object NoOptPatcher : Patcher {
  override fun patchColors(svg: Element) {}
  val byteArray = ByteArray(0)
  override fun digest(): ByteArray {
    return byteArray
  }
}

val emptyByteArray = ByteArray(0)

val noOptPatcherProvider = object : PatcherProvider {

  override fun forPath(path: String?): SVGLoader.SvgElementColorPatcher {
    return NoOptPatcher
  }
}

@Suppress("TooManyFunctions")
object ColorPatcher : PatcherProvider {

  private var otherColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private var uiColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private lateinit var dokiTheme: DokiTheme

  private val patcherProviderCache = HashSet<String>()

  override fun forPath(path: String?): Patcher {
    val safeKey = path ?: "ayyLmao"
    return if (patcherProviderCache.add(safeKey)) {
      val hackedPatcher = buildHackedPatcher(
        listOf(otherColorPatcherProvider, uiColorPatcherProvider)
          .distinct()
          .mapNotNull { patcherProvider ->
            runSafelyWithResult({
              patcherProvider.forPath(path)
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

  private val patcherCache: Cache<String, Patcher> =
    CacheBuilder.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(1))
      .build()

  private fun buildHackedPatcher(
    otherPatchers: List<Patcher>,
    patcherKey: String,
  ): Patcher {
    val cachedPatcher = patcherCache.getIfPresent(patcherKey)
    if (cachedPatcher !== null) {
      return cachedPatcher
    }

    val self = this
    val recursionResistentPatcher = object : Patcher {
      private val svgCache = HashSet<Element>()

      override fun patchColors(svg: Element) {
        if (svgCache.add(svg)) {
          try {
            runSafely({
              self.patchColors(svg, otherPatchers)
            })
          } finally {
            svgCache.remove(svg)
          }
        }
      }

      override fun digest(): ByteArray? {
        val shaDigest = DigestUtil.sha512()
        otherPatchers.forEach { otherPatcher ->
          shaDigest.update(otherPatcher.digest() ?: emptyByteArray)
        }
        if (ColorPatcher::dokiTheme.isInitialized) {
          shaDigest.update((dokiTheme.id + dokiTheme.version).toByteArray(Charsets.UTF_8))
        } else {
          shaDigest.update(this.toString().toByteArray(Charsets.UTF_8))
        }
        return shaDigest.digest()
      }
    }

    patcherCache.put(patcherKey, recursionResistentPatcher)

    return recursionResistentPatcher
  }

  fun patchColors(
    svg: Element,
    otherPatchers: List<Patcher>
  ) {
    otherPatchers.forEach { otherPatcher ->
      runSafely({
        otherPatcher.patchColors(svg)
      })
    }
    patchChildren(
      svg,
      otherPatchers
    )
  }

  private fun patchChildren(
    element: Element,
    otherPatchers: List<Patcher>
  ) {
    patchAccent(element.getAttribute("accentTint"), element) {
      it.toHexString()
    }
    patchAccent(element.getAttribute("accentTintDarker"), element) {
      ColorUtil.darker(it, 1).toHexString()
    }
    patchAccent(element.getAttribute("accentContrastTint"), element) {
      getIconAccentContrastColor().toHexString()
    }
    patchAccent(element.getAttribute("stopTint"), element) {
      getThemedStopColor()
    }
    patchAccent(element.getAttribute("editorAccentTint"), element) {
      ThemeManager.instance.currentTheme
        .map { it.editorAccentColor.toHexString() }
        .orElseGet { Color.CYAN.toHexString() }
    }

    val themedStartAttr = element.getAttribute("themedStart")
    val themedStopAttr = element.getAttribute("themedStop")
    val themedFillAttr = element.getAttribute("themedFill")
    when {
      "true" == themedStartAttr -> {
        val themedStart = getThemedStartColor()
        element.setAttribute("stop-color", themedStart)
        element.setAttribute("fill", themedStart)
      }
      "true" == themedStopAttr -> {
        val themedStop = getThemedStopColor()
        element.setAttribute("stop-color", themedStop)
        element.setAttribute("fill", themedStop)
      }
      "true" == themedFillAttr -> {
        val themedStart = getThemedStartColor()
        element.setAttribute("fill", themedStart)
        element.setAttribute("stroke", themedStart)
      }
    }

    val nodes = element.childNodes
    val length = nodes.length
    for (i in 0 until length) {
      val item = nodes.item(i)
      if (item is Element) {
        patchColors(item, otherPatchers)
      }
    }
  }

  private fun patchAccent(attribute: String?, svg: Element, colorDecorator: (Color) -> String) {
    when (attribute) {
      "fill" -> svg.setAttribute("fill", colorDecorator(getAccentColor()))
      "stroke" -> svg.setAttribute("stroke", colorDecorator(getAccentColor()))
      "both", "partialFill" -> {
        val accentColor = colorDecorator(getAccentColor())
        svg.setAttribute("stroke", accentColor)
        svg.setAttribute("stroke-opacity", if (attribute == "both") "1" else "0.25")
        svg.setAttribute("fill", accentColor)
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

  fun setDokiTheme(dokiTheme: DokiTheme) {
    this.dokiTheme = dokiTheme
    LafManager.getInstance()
      .currentLookAndFeel.toOptional()
      .filter { it is UIThemeBasedLookAndFeelInfo }
      .map { it as UIThemeBasedLookAndFeelInfo }
      .ifPresent {
        this.uiColorPatcherProvider = it.theme.colorPatcher
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
}
