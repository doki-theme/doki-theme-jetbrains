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
import com.intellij.ui.ColorUtil.*
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.components.OnOffButton
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
    if (button is OnOffButton) {
      val g2 = g.create() as Graphics2D
      val config = GraphicsUtil.setupAAPainting(g2)
      try {
        val insets = button.getInsets()
        val origin = Point(
          (button.getWidth() - BUTTON_SIZE.width) / 2 + insets.left,
          (button.getHeight() - BUTTON_SIZE.height) / 2 + insets.top
        )

        g2.color =
          if(button.isSelected) withAlpha(ON_BACKGROUND, 0.45)
          else {
            val parentBackground = button.parent.background
            if(isDark(parentBackground))  brighter(parentBackground, 2)
            else darker(parentBackground.darker(), 2)
          }


        g2.fillRoundRect(origin.x, origin.y, BUTTON_SIZE.width, BUTTON_SIZE.height, ARC, ARC)

        g2.color = BUTTON_COLOR
        val halfWay = BUTTON_SIZE.width / 2
        val location = Point(
          (if (button.isSelected) JBUI.scale(halfWay) else JBUI.scale(0)) + origin.x,
          origin.y
        )
        g2.fillRoundRect(location.x, location.y, halfWay, BUTTON_SIZE.height, ARC, ARC)
        config.restore()
      } finally {
        g2.dispose()
      }
    }
  }

  companion object {
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

    private val TOGGLE_SIZE: Dimension = JBDimension(18, 12)
    private val BUTTON_SIZE: Dimension = JBDimension(42, 14)
    private val BUTTON_BORDER: Border = JBUI.Borders.empty(1, 10)
    private const val ARC = 16

    @Suppress("ACCIDENTAL_OVERRIDE", "UNUSED", "UNUSED_PARAMETER")
    @JvmStatic
    fun createUI(component: JComponent): ComponentUI = ToggleButtonUI()
  }
}