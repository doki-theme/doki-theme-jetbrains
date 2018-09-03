package com.chrisrm.idea

import com.chrisrm.idea.actions.MTAddFileColorsAction
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project

/**
 * Forged in the flames of battle by alex.
 */
class DDLCProjectInitializationComponent(project: Project?) : AbstractProjectComponent(project) {

    private val mtAddFileColorsAction = MTAddFileColorsAction()

    override fun getComponentName(): String {
        return "DDLCProjectInitializationComponent"
    }

    override fun projectOpened() {
        mtAddFileColorsAction.setFileScopes(this.myProject)
    }
}