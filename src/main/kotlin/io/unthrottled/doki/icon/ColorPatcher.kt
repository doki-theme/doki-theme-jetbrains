package io.unthrottled.doki.icon

import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor.namedColor
import com.intellij.util.SVGLoader
import io.unthrottled.doki.util.toHexString
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
    patchAccent(svg.getAttribute("accentTint"), svg) {
      it.toHexString()
    }
    patchAccent(svg.getAttribute("accentTintDarker"), svg) {
      ColorUtil.darker(it, 1).toHexString()
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
    when(attribute)  {
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

  private fun getThemedStartColor() =
    namedColor("Doki.startColor", Color.CYAN).toHexString()

  private fun getThemedStopColor() =
    namedColor("Doki.stopColor", Color.CYAN).toHexString()
}