package com.chrisrm.idea

import com.chrisrm.idea.actions.MTAddFileColorsAction
import com.intellij.ide.GeneralSettings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.TipDialog
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.WindowManager
import java.time.Instant
import java.util.*

/**
 * Forged in the flames of battle by alex.
 */
class DDLCProjectInitializationComponent(project: Project?) : AbstractProjectComponent(project) {
    val random = Random(Instant.now().epochSecond / 100)
    private val mtAddFileColorsAction = MTAddFileColorsAction()

    override fun getComponentName(): String {
        return "DDLCProjectInitializationComponent"
    }

    override fun projectOpened() {
        mtAddFileColorsAction.setFileScopes(this.myProject)
        if(!GeneralSettings.getInstance().isShowTipsOnStartup()){
            val timesTipsChosen = PropertiesComponent.getInstance().getInt(WRITING_TIP_OF_THE_DAY, 0)
            if (timesTipsChosen < 1) {
                GeneralSettings.getInstance().setShowTipsOnStartup(true)
                showMonikasWritingTipOfTheDay(timesTipsChosen)
            } else if (timesTipsChosen < 2 && shouldShowAgain()) {
                showMonikasWritingTipOfTheDay(timesTipsChosen)
            }
        }

        val frame = WindowManager.getInstance().getFrame(this.myProject)
        val ideFrame = WindowManager.getInstance().getIdeFrame(this.myProject)

    }

    private fun shouldShowAgain() = random.nextLong() % 42 == 0L

    fun showMonikasWritingTipOfTheDay(timesTipsChosen: Int) {
        TipDialog.createForProject(this.myProject).show()
        PropertiesComponent.getInstance().setValue(WRITING_TIP_OF_THE_DAY, (timesTipsChosen + 1).toString())
    }

}


val WRITING_TIP_OF_THE_DAY = "WRITING_TIP_OF_THE_DAY"
