package io.acari.DDLC.chibi

import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.util.ui.JBSwingUtilities

object ChibiUtility {
    init {
        IdeBackgroundUtil.getIdeBackgroundColor()//load this transform first
        JBSwingUtilities.addGlobalCGTransform(DDLCChibiTransform())
    }
    fun setChibis(){

    }
}