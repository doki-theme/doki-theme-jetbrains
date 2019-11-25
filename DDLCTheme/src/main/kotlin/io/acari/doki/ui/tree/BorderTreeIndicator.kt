package io.acari.doki.ui.tree

import com.intellij.ui.JBColor.namedColor
import io.acari.doki.ui.DEFAULT_COLOR
import java.awt.Component
import java.awt.Graphics

class BorderTreeIndicator : TreeIndicatorPainter {
  override fun paint(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
    val thiccness = 5 // todo: this can be configurable
    val previousColor = g.color
    g.color = namedColor("doki.doki.accent.color", DEFAULT_COLOR)
    g.fillRect(x, y, thiccness, height)
    g.color = previousColor
  }
}

interface TreeIndicatorPainter {
  fun paint(
    c: Component,
    g: Graphics,
    x: Int,
    y: Int,
    width: Int,
    height: Int
  )
}