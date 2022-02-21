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
import java.net.URL
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
  override fun forURL(url: URL?): SVGLoader.SvgElementColorPatcher? {
    return NoOptPatcher
  }

  override fun forPath(path: String?): SVGLoader.SvgElementColorPatcher? {
    return NoOptPatcher
  }
}

object ColorPatcher : PatcherProvider {

  private var otherColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private var uiColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private lateinit var dokiTheme: DokiTheme

  private val patcherProviderCache = HashSet<String>()

  override fun forPath(path: String?): Patcher {
    val safeKey = path ?: "ayyLmao"
    return if (patcherProviderCache.add(safeKey)) {
      val buildHackedPatcher = buildHackedPatcher(
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
      buildHackedPatcher
    } else {
      NoOptPatcher
    }
  }

  override fun forURL(url: URL?) =
    buildHackedPatcher(
      listOf(otherColorPatcherProvider, uiColorPatcherProvider)
        .distinct()
        .mapNotNull { patcherProvider ->
          runSafelyWithResult({
            patcherProvider.forURL(url)
          }) {
            null
          }
        },
      url?.toString() ?: "ayyLmao"
    )

  private val patcherCache: Cache<String, Patcher> = CacheBuilder.newBuilder()
    .expireAfterWrite(Duration.ofMinutes(1))
    .build()

  private fun buildHackedPatcher(
    otherPatchers: List<Patcher>,
    patcherKey: String,
  ): Patcher {
    val ifPresent = patcherCache.getIfPresent(patcherKey)
    if (ifPresent !== null) {
      return ifPresent
    }

    val self = this
    val value = object : Patcher {
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
        } else {
          println("already patching bruv")
        }
      }

      override fun digest(): ByteArray? {
        val shaDigest = DigestUtil.sha512()
        otherPatchers.forEach { otherPatcher ->
          shaDigest.update(otherPatcher.digest() ?: emptyByteArray)
        }
        if (ColorPatcher::dokiTheme.isInitialized) {
          shaDigest.update((dokiTheme.id + dokiTheme.version + "hax5").toByteArray(Charsets.UTF_8))
        } else {
          shaDigest.update(this.toString().toByteArray(Charsets.UTF_8))
        }
        return shaDigest.digest()
      }
    }

    patcherCache.put(patcherKey, value)

    return value
  }

  fun patchColors(
    svg: Element,
    otherPatchers: List<Patcher>
  ) {
    otherPatchers.forEach { otherPatcher ->
      runSafely({
        otherPatcher?.patchColors(svg)
      })
    }
    patchChildren(
      svg,
      otherPatchers
    )
  }

  private fun patchChildren(svg: Element, otherPatchers: List<Patcher>) {
    patchAccent(svg.getAttribute("accentTint"), svg) {
      it.toHexString()
    }
    patchAccent(svg.getAttribute("accentTintDarker"), svg) {
      ColorUtil.darker(it, 1).toHexString()
    }
    patchAccent(svg.getAttribute("accentContrastTint"), svg) {
      getIconAccentContrastColor().toHexString()
    }
    patchAccent(svg.getAttribute("stopTint"), svg) {
      getThemedStopColor()
    }
    patchAccent(svg.getAttribute("editorAccentTint"), svg) {
      ThemeManager.instance.currentTheme
        .map { it.editorAccentColor.toHexString() }
        .orElseGet { Color.CYAN.toHexString() }
    }

    val themedStartAttr = svg.getAttribute("themedStart")
    val themedStopAttr = svg.getAttribute("themedStop")
    val themedFillAttr = svg.getAttribute("themedFill")
    when {
      "true" == themedStartAttr -> {
        val themedStart = getThemedStartColor()
        svg.setAttribute("stop-color", themedStart)
        svg.setAttribute("fill", themedStart)
      }
      "true" == themedStopAttr -> {
        val themedStop = getThemedStopColor()
        svg.setAttribute("stop-color", themedStop)
        svg.setAttribute("fill", themedStop)
      }
      "true" == themedFillAttr -> {
        val themedStart = getThemedStartColor()
        svg.setAttribute("fill", themedStart)
        svg.setAttribute("stroke", themedStart)
      }
    }

    val nodes = svg.childNodes
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
    this.patcherCache.invalidateAll()
  }

  fun setOtherPatcher(otherPatcher: PatcherProvider) {
    this.otherColorPatcherProvider = otherPatcher
    this.patcherCache.invalidateAll()
  }
}
