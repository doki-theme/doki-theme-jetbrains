package io.acari.DDLC.chibi

import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorComponentImpl
import com.intellij.openapi.fileEditor.impl.EditorsSplitters
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.TARGET_PROP
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.withNamedPainters
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl
import com.intellij.openapi.wm.impl.ToolWindowHeader
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.tabs.JBTabs
import com.intellij.util.PairFunction
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.containers.stream
import io.acari.DDLC.actions.ClubMemberOrchestrator.DDLC_BACKGROUND_PROP
import io.acari.DDLC.actions.ClubMemberOrchestrator.DDLC_CHIBI_PROP
import java.awt.Graphics2D
import javax.swing.*

/**
 * Forged in the flames of battle by alex.
 */
class DDLCChibiTransform : PairFunction<JComponent, Graphics2D, Graphics2D> {
    private val initializedComponents = HashSet<IdeGlassPaneImpl>()
    private val painters = listOf(DDLC_CHIBI_PROP, DDLC_BACKGROUND_PROP)
    override fun `fun`(c: JComponent, g: Graphics2D): Graphics2D {
        val glassPane = c.rootPane.glassPane
        if (glassPane is IdeGlassPaneImpl && !initializedComponents.contains(glassPane)) {
            IdeGlassPaneImpl::class.java.declaredMethods.stream()
                    .filter { it.name == "getNamedPainters" }.findFirst()
                    .ifPresent { getNamedPaintersMethod ->
                        getNamedPaintersMethod.isAccessible = true
                        val myNamedPaintersField = IdeGlassPaneImpl::class.java
                                .getDeclaredField("myNamedPainters")
                        myNamedPaintersField.isAccessible = true
                        painters.forEach { painterName ->
                            val painter = getNamedPaintersMethod.invoke(glassPane, painterName)
                            if (painter != null) {
                                val paintersHelper =
                                        Class.forName("com.intellij.openapi.wm.impl.PaintersHelper")
                                paintersHelper.declaredMethods.stream()
                                        .filter { it.name == "initWallpaperPainter" }
                                        .findFirst()
                                        .ifPresent { initWallpaperPainterMethod ->
                                            initWallpaperPainterMethod.isAccessible = true
                                            initWallpaperPainterMethod.invoke(null, painterName, painter)//this is stupid ._.
                                        }
                                initializedComponents.add(glassPane)
                            }
                        }
                    }

        }
        return when (getComponentType(c)) {
            "frame" -> withFrameBackground(g, c)
            null -> g
            "editor" -> {
                val editor = (c as? EditorComponentImpl)?.editor
                        ?: if (c is EditorGutterComponentEx) CommonDataKeys.EDITOR.getData(c as DataProvider) else null
                if (editor != null && g::class.java.name != "MyGraphics" && java.lang.Boolean.TRUE == EditorTextField.SUPPLEMENTARY_KEY.get(editor)) g
                else withEditorBackground(g, c)
            }
            else -> withEditorBackground(g, c)
        }
    }

    private fun withFrameBackground(g: Graphics2D, c: JComponent): Graphics2D =
            withNamedPainters(g, DDLC_BACKGROUND_PROP, c)

    private fun withEditorBackground(g: Graphics2D, c: JComponent): Graphics2D =
            if (suppressBackground(c)) g
            else withNamedPainters(g, DDLC_CHIBI_PROP, c)
}

private fun suppressBackground(component: JComponent): Boolean {
    val type = getComponentType(component) ?: return false
    val spec = System.getProperty(TARGET_PROP, "*")
    val allInclusive = spec.startsWith("*")
    return allInclusive && spec.contains("-$type") || !allInclusive && !spec.contains(type)
}

private val ourKnownNames = ContainerUtil.newHashSet("navbar", "terminal")

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
        is JPanel -> if (ourKnownNames.contains(component.name)) component.name else null
        else -> if (component.javaClass.name == "Stripe") "stripe" else null
    }
}

