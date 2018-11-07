package io.acari.DDLC.chibi

import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorComponentImpl
import com.intellij.openapi.fileEditor.impl.EditorsSplitters
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.impl.ToolWindowHeader
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.tabs.JBTabs
import com.intellij.util.PairFunction
import java.awt.Graphics2D
import javax.swing.*

/**
 * Forged in the flames of battle by alex.
 */
class DDLCChibiTransform : PairFunction<JComponent, Graphics2D, Graphics2D> {
    override fun `fun`(c: JComponent, g: Graphics2D): Graphics2D =
            when (getComponentType(c)) {
                "frame" -> withFrameBackground(g, c)
                "editor" -> withEditorBackground(g, c)
                else -> g
            }

    fun withFrameBackground(g: Graphics2D, c: JComponent): Graphics2D = g
    fun withEditorBackground(g: Graphics2D, c: JComponent): Graphics2D = g
}

fun getComponentType(component: JComponent): String? {
    return when (component) {
        is JTree -> "tree"
        is JList<*> -> "list"
        is JTable -> "table"
        is JViewport -> "viewport"
        is JTabbedPane -> "tabs"
        is JButton -> "button"
        is ActionToolbar -> "toolbar"
        is StatusBar -> "statusbar"
        is EditorsSplitters -> "frame"
        is EditorComponentImpl -> "editor"
        is EditorGutterComponentEx -> "editor"
        is JBLoadingPanel -> "loading"
        is JBTabs -> "tabs"
        is ToolWindowHeader -> "title"
        is JBPanelWithEmptyText -> "panel"
        else -> null
    }
}

