package io.acari.doki.themes

import com.intellij.openapi.components.ServiceManager

interface ThemeManager {
    companion object {
        val instance: ThemeManager
            get() = ServiceManager.getService(ThemeManager::class.java)
    }
}