package com.chrisrm.idea.actions

/**
 * Forged in the flames of battle by alex.
 */
object JoyManager {

    // todo: need to save state
    private var isOn = false

    @JvmStatic
    fun isOn(): Boolean = isOn

    fun toggle() {
        isOn = !isOn
    }

}