package io.acari.doki.chibi

import com.intellij.openapi.components.ServiceManager
import io.acari.doki.themes.DokiTheme

interface ChibiService {

    fun activateForTheme(dokiTheme: DokiTheme)

    companion object {
        val instance: ChibiService
            get() = ServiceManager.getService(ChibiService::class.java)
    }
}