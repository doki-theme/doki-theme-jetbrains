package io.acari.DDLC.actions

import com.intellij.ide.util.PropertiesComponent

/**
 * Forged in the flames of battle by alex.
 */
object JoyManager {

    private var isOn = false
    private const val SAVED_STATE = "JOY_MODE_ON"

    init {
        isOn = PropertiesComponent.getInstance()
                .getValue(SAVED_STATE)?.toBoolean() ?: false
    }

    @JvmStatic
    fun isOn(): Boolean = isOn

    fun toggle() {
        isOn = isOn.not()
        PropertiesComponent.getInstance()
                .setValue(SAVED_STATE, isOn)
    }

}