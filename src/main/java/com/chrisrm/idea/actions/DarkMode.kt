package com.chrisrm.idea.actions

import com.intellij.ide.util.PropertiesComponent

/**
 * Forged in the flames of battle by alex.
 */

class DarkMode {

    companion object {

        private var isOn = false
        private const val SAVED_STATE = "DARK_MODE_ON"

        init {
            isOn = PropertiesComponent.getInstance().getValue(SAVED_STATE)?.toBoolean() ?: false
        }

        @JvmStatic
        fun isOn() = isOn

        fun toggle() {
            isOn = isOn.not()
            PropertiesComponent.getInstance()
                    .setValue(SAVED_STATE, isOn)
        }
    }
}