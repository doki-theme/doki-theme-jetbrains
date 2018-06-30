package com.chrisrm.idea.actions

/**
 * Forged in the flames of battle by alex.
 */

class DarkMode {

    companion object {
        private var isOn = false

        @JvmStatic
        fun isOn() = isOn

        fun toggle() {
            isOn = isOn.not()
        }
    }
}