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

package com.chrisrm.ideaddlc.ui.indicators

import com.intellij.util.ui.JBUI

import java.awt.*

class DokiHeartSelectedTreeIndicator : MTSelectedTreeIndicatorImpl() {

  override fun paintBorder(component: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
    val oldColor = g.color
    val thickness = JBUI.scale(thickness) * 3

    val g2 = g.create() as Graphics2D
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)
    g2.color = highlightColor
    drawHeart(g2, x, y + height / 2 - thickness / 2, thickness, thickness)
    g2.color = oldColor
    g2.dispose()
  }

  private fun drawHeart(g: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
    val triangleX = intArrayOf(x - 2 * width / 19, x + width + 2 * width / 19, (x - 2 * width / 19 + x + width + 2 * width / 19) / 2)
    val triangleY = intArrayOf(y + height - 2 * height / 3, y + height - 2 * height / 3, y + height - 2 * height / 9)
    g.fillOval(
        x - width / 12,
        y,
        width / 2 + width / 6,
        height / 2)
    g.fillOval(
        x + width / 2 - width / 12,
        y,
        width / 2 + width / 6,
        height / 2)
    g.fillPolygon(triangleX, triangleY, triangleX.size)
  }
}
