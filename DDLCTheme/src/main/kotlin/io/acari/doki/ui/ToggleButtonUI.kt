/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 *
 */
package io.acari.doki.ui

import com.intellij.ui.ColorUtil
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.components.OnOffButton
import com.intellij.util.NotNullProducer
import com.intellij.util.ui.*
import java.awt.*
import javax.swing.JComponent
import javax.swing.border.Border
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicToggleButtonUI

class ToggleButtonUI : BasicToggleButtonUI() {
  override fun getPreferredSize(c: JComponent): Dimension {
    val size = Dimension(BUTTON_SIZE)
    JBInsets.addTo(size, BUTTON_BORDER.getBorderInsets(c))
    return size
  }

  override fun getMaximumSize(c: JComponent): Dimension = getPreferredSize(c)

  override fun getMinimumSize(c: JComponent): Dimension = getPreferredSize(c)

  override fun paint(g: Graphics, button: JComponent) {
    if (button is OnOffButton){
      val g2 = g.create() as Graphics2D
      val config = GraphicsUtil.setupAAPainting(g2)
      try {
        val insets = button.getInsets()
        val origin = Point((button.getWidth() - BUTTON_SIZE.width) / 2 + insets.left,
          (button.getHeight() - BUTTON_SIZE.height) / 2 + insets.top)

        val backgroundAlpha = if (button.isSelected) 0.65 else 0.2
        g2.color =  ColorUtil.withAlpha(ON_BACKGROUND, backgroundAlpha)
        g2.fillRoundRect(origin.x, origin.y, BUTTON_SIZE.width, BUTTON_SIZE.height, ARC, ARC)

        g2.color = BUTTON_COLOR
        val location = Point(
          (if (button.isSelected) JBUI.scale(20) else JBUI.scale(-2)) + origin.x,
          JBUI.scale(-2) + origin.y)
        g2.fillOval(location.x, location.y, TOGGLE_SIZE.width, TOGGLE_SIZE.height)
        config.restore()
      } finally {
        g2.dispose()
      }
    }
  }

  companion object {
    val BORDER_COLOR: Color = JBColor.namedColor(
      "ToggleButton.borderColor", JBColor(
        Gray._192, Gray._80
      )
    )
    val BUTTON_COLOR: Color = JBColor.namedColor(
      "ToggleButton.buttonColor", JBColor(
        Gray._200, Gray._100
      )
    )
    val ON_BACKGROUND: Color = JBColor.namedColor(
      "ToggleButton.onBackground", JBColor(
        Color(74, 146, 73), Color(77, 105, 76)
      )
    )
    val ON_FOREGROUND: Color = JBColor.namedColor(
      "ToggleButton.onForeground", JBColor(
        NotNullProducer { UIUtil.getListForeground(true, true) }
      )
    )

    val OFF_BACKGROUND: Color = JBColor.namedColor(
      "ToggleButton.offBackground", JBColor(
        NotNullProducer { UIUtil.getPanelBackground() }
      )
    )
    val OFF_FOREGROUND: Color = JBColor.namedColor(
      "ToggleButton.offForeground", JBColor(
        NotNullProducer { UIUtil.getLabelDisabledForeground() }
      )
    )

    private val TOGGLE_SIZE: Dimension = JBDimension(18, 18)
    private val BUTTON_SIZE: Dimension = JBDimension(32, 14)
    private val BUTTON_BORDER: Border = JBUI.Borders.empty(1, 10)
    private const val ARC = 16

    @Suppress("ACCIDENTAL_OVERRIDE","UNUSED", "UNUSED_PARAMETER")
    @JvmStatic
    fun createUI(component: JComponent): ComponentUI {
      return ToggleButtonUI()
    }
  }
}