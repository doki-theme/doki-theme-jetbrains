package com.chrisrm.idea.actions

/**
 * Forged in the flames of battle by alex.
 */
object JoyManager {
    private var isOn = false

    fun isOn(): Boolean = isOn

    fun toggle() {
        isOn != isOn
    }

}