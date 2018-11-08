package io.acari.DDLC.chibi

import com.intellij.openapi.wm.impl.IdeGlassPaneImpl
import com.intellij.util.PairFunction
import com.intellij.util.containers.stream
import io.acari.DDLC.actions.ClubMemberOrchestrator.DDLC_BACKGROUND_PROP
import io.acari.DDLC.actions.ClubMemberOrchestrator.DDLC_CHIBI_PROP
import java.awt.Graphics2D
import javax.swing.JComponent

/**
 * Forged in the flames of battle by alex.
 */
class DDLCChibiTransform : PairFunction<JComponent, Graphics2D, Graphics2D> {
    private val painters = listOf(DDLC_CHIBI_PROP, DDLC_BACKGROUND_PROP)
    private var initialized = false
    override fun `fun`(c: JComponent, g: Graphics2D): Graphics2D {
        val glassPane = c.rootPane.glassPane
        if (glassPane is IdeGlassPaneImpl && !initialized) {
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
                                initialized = true
                            }
                        }
                    }

        }
        return g
    }
}

