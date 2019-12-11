package io.acari.doki.stickers

import com.intellij.openapi.components.ServiceManager
import io.acari.doki.themes.DokiTheme

interface StickerService {

    fun activateForTheme(dokiTheme: DokiTheme)
    fun remove()

    companion object {
        val instance: StickerService
            get() = ServiceManager.getService(StickerService::class.java)
    }
}