package io.unthrottled.doki.icon

import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor.namedColor
import io.ktor.utils.io.core.toByteArray
import io.unthrottled.doki.hax.Patcher
import io.unthrottled.doki.hax.PatcherProvider
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toHexString
import org.w3c.dom.Element
import java.awt.Color
import java.net.URL
import java.nio.charset.Charset

class ColorPatcher(
  private val otherColorPatcherProvider: PatcherProvider
) : PatcherProvider {

  companion object {
    private val patcherDigest = ColorPatcher::class.java.name
      .toByteArray(Charset.forName("UTF-8"))
  }

  override fun forPath(path: String?) =
    buildHackedPatcher(
      runSafelyWithResult({
        otherColorPatcherProvider.forPath(path)
      }) {
        null
      }
    )

  override fun forURL(url: URL?) =
    buildHackedPatcher(
      runSafelyWithResult({
        otherColorPatcherProvider.forURL(url)
      }) {
        null
      }
    )

  private fun buildHackedPatcher(
    otherPatcher: Patcher?
  ): Patcher {

    val self = this
    return object : Patcher {
      override fun patchColors(svg: Element) {
        self.patchColors(svg, otherPatcher)
      }

      override fun digest(): ByteArray {
        return patcherDigest
      }
    }
  }

  fun patchColors(
    svg: Element,
    otherPatcher: Patcher?
  ) {
    runSafely({
      otherPatcher?.patchColors(svg)
    })
    patchChildren(
      svg,
      otherPatcher
    )
  }

  private fun patchChildren(svg: Element, otherPatcher: Patcher?) {
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
        patchColors(item, otherPatcher)
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
}
