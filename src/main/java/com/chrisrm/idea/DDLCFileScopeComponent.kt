package com.chrisrm.idea

import com.chrisrm.idea.status.MTStatusBarManager
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project

/**
 * Forged in the flames of battle by alex.
 */
class DDLCFileScopeComponent(project: Project?) : AbstractProjectComponent(project) {


    override fun initComponent() {
//        statusBarWidget = MTStatusBarManager.create(myProject)
    }

    override fun disposeComponent() {
//        statusBarWidget.dispose()
    }

    override fun getComponentName(): String {
        return "DDLCFileScopeComponent"
    }

    override fun projectOpened() {

    }

    override fun projectClosed() {
//        statusBarWidget.uninstall()
    }
}