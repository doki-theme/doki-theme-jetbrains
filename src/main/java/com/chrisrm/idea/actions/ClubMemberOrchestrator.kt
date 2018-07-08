package com.chrisrm.idea.actions

import com.chrisrm.idea.MTThemes
import com.intellij.ide.util.PropertiesComponent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Forged in the flames of battle by alex.
 */
object ClubMemberOrchestrator {
    private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
    private const val SAVED_THEME = "CLUB_MEMBER_THEME_PROPERTY"
    private val isOn = AtomicBoolean(true)
    private var currentTheme = getSavedTheme()

    private fun getSavedTheme(): MTThemes =
            MTThemes.getTheme(PropertiesComponent.getInstance().getValue(SAVED_THEME))

    fun toggleWeebShit() {

    }

    fun activate(theme: MTThemes) {

    }

}