package io.acari.DDLC.ui.chibi

import com.intellij.openapi.ui.AbstractPainter
import com.intellij.openapi.ui.Painter
import com.intellij.util.containers.ContainerUtil
import java.awt.*
import java.util.stream.Collectors
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Forged in the flames of battle by alex.
 */
class DDLCChibiPane(private val rootPane: JFrame) : JPanel() {
    companion object {
        val EDITOR = "EDITOR"
        val EMPTY_FRAME = "EMPTY_FRAME"
    }

    private lateinit var namedPainters: Map<String, DDLCPaintersHelper>

    init {
        isOpaque = false
        isVisible = false
        layout = null
        namedPainters = listOf(EDITOR, EMPTY_FRAME)
                .stream()
                .collect(Collectors.toMap({ paneKey ->
                    paneKey
                }, { DDLCPaintersHelper(this) }))

        DDLCChibiPainters.initEditorPainters(this, namedPainters[EDITOR]!!)
        DDLCChibiPainters.initFramePainters(this)
    }

    override fun paintComponent(p0: Graphics?) {

    }
}

object DDLCChibiPainters {
    fun initFramePainters(ddlcChibiPane: DDLCChibiPane) {

    }

    fun initEditorPainters(ddlcChibiPane: DDLCChibiPane, ddlcPaintersHelper: DDLCPaintersHelper) {
//        ddlcPaintersHelper.addPainter(ImagePainter())
    }
}

class DDLCPaintersHelper(private val rootComponent: JComponent) : Painter.Listener {
    private val myPainters = ContainerUtil.newLinkedHashSet<Painter>()
    private val myPainter2Component = ContainerUtil.newLinkedHashMap<Painter, Component>()

    override fun onNeedsRepaint(painter: Painter?, dirtyComponent: JComponent?) =
            if (dirtyComponent != null && dirtyComponent.isShowing) {
                val rec = SwingUtilities.convertRectangle(dirtyComponent, dirtyComponent.bounds, rootComponent)
                rootComponent.repaint(rec)
            } else {
                rootComponent.repaint()
            }

    fun addPainter(painter: Painter) {
        myPainters.add(painter)
        myPainter2Component[painter] = rootComponent
        painter.addListener(this)
    }
}