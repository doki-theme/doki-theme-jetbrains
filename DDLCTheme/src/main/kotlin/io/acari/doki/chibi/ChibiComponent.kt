package io.acari.doki.chibi

import com.intellij.ide.ui.laf.LafManagerImpl
import io.acari.doki.themes.DokiThemes

class ChibiComponent {

    init {
        val currentLaf = LafManagerImpl.getInstance().currentLookAndFeel
        DokiThemes.processLaf(currentLaf)
            .ifPresent {
                ChibiService.instance.activateForTheme(it)
            }
    }
}