package io.acari.DDLC.ui

import com.chrisrm.idea.ui.MTRootPaneUI
import javax.swing.JComponent

/**
 * Forged in the flames of battle by alex.
 */
class DDLCRootPaneUI : MTRootPaneUI() {



    override fun installUI(c: JComponent?) {
        println()
        super.installUI(c)
        println(c)
    }
}