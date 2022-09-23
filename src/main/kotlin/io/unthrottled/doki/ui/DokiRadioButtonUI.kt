package io.unthrottled.doki.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaRadioButtonUI
import io.unthrottled.doki.icon.IconLookup.getIcon
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.AbstractButton
import javax.swing.JComponent
import javax.swing.plaf.ComponentUI

@Suppress("ACCIDENTAL_OVERRIDE")
class DokiRadioButtonUI : DarculaRadioButtonUI() {

  companion object {
    @JvmStatic
    @Suppress("UNUSED", "UNUSED_PARAMETER")
    fun createUI(c: JComponent?): ComponentUI = DokiRadioButtonUI()
  }

  override fun paintIcon(c: Graphics?, b: AbstractButton, iconRect: Rectangle) {
    val icon = getIcon("radio", b.isSelected, b.hasFocus(), b.isEnabled)
    icon.paintIcon(b, c, iconRect.x, iconRect.y)
  }
}
