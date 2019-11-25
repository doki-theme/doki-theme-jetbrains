package io.acari.doki.ui.tree

import com.intellij.util.ui.JBUI.insets
import java.awt.Component
import java.awt.Graphics
import java.awt.Insets
import javax.swing.border.Border

class TreeIndicator(private val indicatorPainter: TreeIndicatorPainter) : Border {

  override fun getBorderInsets(c: Component): Insets =
    insets(0)

  override fun isBorderOpaque(): Boolean = false

  override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
    indicatorPainter.paint(c, g, x, y, width, height)
  }

}