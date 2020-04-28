package io.acari.doki.icon

import com.intellij.ui.JBColor.namedColor
import com.intellij.util.SVGLoader
import io.acari.doki.util.toHexString
import org.w3c.dom.Element
import java.awt.Color
import java.net.URL

class ColorPatcher(
  private val otherColorPatcherProvider: (URL?) -> (Element) -> Unit = { {} }
) : SVGLoader.SvgElementColorPatcherProvider {
  override fun forURL(url: URL?): SVGLoader.SvgElementColorPatcher {
    val self = this
    val otherPatcher = otherColorPatcherProvider(url)
    return object : SVGLoader.SvgElementColorPatcher {
      override fun patchColors(svg: Element) {
        self.patchColors(svg, otherPatcher)
      }

      override fun digest(): ByteArray? {
        return null
      }
    }
  }

  fun patchColors(
    svg: Element,
    otherPatcher: (Element) -> Unit
  ) {
    otherPatcher(svg)
    patchChildren(
      svg,
      otherPatcher
    )
  }

  private fun patchChildren(svg: Element, otherPatcher: (Element) -> Unit) {
    when (val accentTintAttribute = svg.getAttribute("accentTint")) {
      "fill" -> svg.setAttribute("fill", getAccentColor())
      "stroke" -> svg.setAttribute("stroke", getAccentColor())
      "both", "partialFill" -> {
        val accentColor = getAccentColor()
        svg.setAttribute("stroke", accentColor)
        svg.setAttribute("stroke-opacity", if (accentTintAttribute == "both") "1" else "0.25")
        svg.setAttribute("fill", accentColor)
      }
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

  private fun getAccentColor() =
    namedColor("Doki.Accent.color", Color.CYAN).toHexString()

  private fun getThemedStartColor() =
    namedColor("Doki.startColor", Color.CYAN).toHexString()

  private fun getThemedStopColor() =
    namedColor("Doki.stopColor", Color.CYAN).toHexString()
}