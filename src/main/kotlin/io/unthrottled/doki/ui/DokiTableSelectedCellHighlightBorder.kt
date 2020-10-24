package io.unthrottled.doki.ui

import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Color
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import javax.swing.plaf.UIResource

open class DokiTableSelectedCellHighlightBorder : CompoundBorder(), UIResource {
  protected fun createInsideBorder(): Border = JBUI.Borders.empty(1, 2)

  protected fun createFocusBorderColor(): Color = UIUtil.getFocusedBorderColor()

  init {
    outsideBorder = JBUI.Borders.customLine(createFocusBorderColor(), 2)
    insideBorder = createInsideBorder()
  }
}
