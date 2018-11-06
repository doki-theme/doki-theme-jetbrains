package io.acari.DDLC.chibi

import com.intellij.util.PairFunction
import java.awt.Graphics2D
import javax.swing.JComponent

/**
 * Forged in the flames of battle by alex.
 */
class DDLCChibiTransform: PairFunction<JComponent, Graphics2D, Graphics2D> {
    override fun `fun`(t: JComponent?, v: Graphics2D?): Graphics2D {
        return v!!
    }

}

