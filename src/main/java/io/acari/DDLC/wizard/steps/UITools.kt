package io.acari.DDLC.wizard.steps

import java.awt.Cursor
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

fun createMouseListener(function: () -> Unit): MouseListener {
  return object : MouseListener {
    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent) {
      e.component.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    }

    override fun mouseClicked(e: MouseEvent?) {
      function()
    }

    override fun mouseExited(e: MouseEvent) {
      e.component.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    override fun mousePressed(e: MouseEvent?) {
    }

  }
}
