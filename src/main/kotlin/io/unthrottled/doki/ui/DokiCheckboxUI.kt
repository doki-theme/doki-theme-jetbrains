package io.unthrottled.doki.ui

import com.intellij.ide.ui.laf.darcula.DarculaUIUtil
import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxUI
import com.intellij.ui.scale.JBUIScale.scale
import com.intellij.util.ui.MacUIUtil
import io.unthrottled.doki.icon.IconLookup
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.Path2D
import java.awt.geom.RoundRectangle2D
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.plaf.ComponentUI

@Suppress("ACCIDENTAL_OVERRIDE")
class DokiCheckboxUI : DarculaCheckBoxUI() {

  companion object {
    @JvmStatic
    @Suppress("UNUSED", "UNUSED_PARAMETER")
    fun createUI(c: JComponent?): ComponentUI = DokiCheckboxUI()
  }

  override fun drawCheckIcon(
    c: JComponent?,
    g: Graphics2D?,
    b: AbstractButton?,
    iconRect: Rectangle?,
    selected: Boolean,
    enabled: Boolean
  ) {
    val g2 = g!!.create() as Graphics2D
    try {
      val iconName = if (isIndeterminate(b)) "checkBoxIndeterminate" else "checkBox"
      val op = b!!.getClientProperty("JComponent.outline")
      val hasFocus = op == null && b.hasFocus()
      val icon = IconLookup.getIcon(iconName, selected || isIndeterminate(b), hasFocus, b.isEnabled)
      icon.paintIcon(b, g2, iconRect!!.x, iconRect.y)
      if (op != null) {
        DarculaUIUtil.Outline.valueOf(op.toString()).setGraphicsColor(g2, b.hasFocus())
        val outline: Path2D = Path2D.Float(Path2D.WIND_EVEN_ODD)
        outline.append(
          RoundRectangle2D.Float(
            (iconRect.x + scale(1)).toFloat(),
            iconRect.y.toFloat(),
            scale(18).toFloat(),
            scale(18).toFloat(),
            scale(8).toFloat(),
            scale(8).toFloat()
          ),
          false
        )
        outline.append(
          RoundRectangle2D.Float(
            (iconRect.x + scale(4)).toFloat(),
            (iconRect.y + scale(3)).toFloat(),
            scale(12).toFloat(),
            scale(12).toFloat(),
            scale(3).toFloat(),
            scale(3).toFloat()
          ),
          false
        )
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(
          RenderingHints.KEY_STROKE_CONTROL,
          if (MacUIUtil.USE_QUARTZ) RenderingHints.VALUE_STROKE_PURE else RenderingHints.VALUE_STROKE_NORMALIZE
        )
        g2.fill(outline)
      }
    } finally {
      g2.dispose()
    }
  }
}
