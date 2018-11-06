package io.acari.DDLC.chibi

import com.intellij.openapi.components.ApplicationComponent

class DDLCChibiComponent : ApplicationComponent {
    init {
        ChibiUtility.setChibis()
    }
}