package io.acari.DDLC

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.ide.GeneralSettings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.TipDialog
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import io.acari.DDLC.actions.DDLCAddFileColorsAction
import java.time.Instant
import java.util.*

/**
 * Forged in the flames of battle by alex.
 */
class DDLCProjectInitializationComponent(private val project: Project) : ProjectComponent {
  private val ranbo = Random(Instant.now().epochSecond / 100)
  private val dokiFileColors = DDLCAddFileColorsAction()

  override fun getComponentName(): String {
    return "DDLCProjectInitializationComponent"
  }

  override fun projectOpened() {
    if (MTThemeManager.isDDLCActive()) {
      MTThemeManager.activate()
      if(DDLCConfig.getInstance().isDokiFileColors){
        dokiFileColors.setFileScopes(this.project)
      }
      if (!GeneralSettings.getInstance().isShowTipsOnStartup) {
        val timesTipsChosen = PropertiesComponent.getInstance().getInt(WRITING_TIP_OF_THE_DAY, 0)
        if (timesTipsChosen < 1) {
          GeneralSettings.getInstance().isShowTipsOnStartup = true
          showMonikasWritingTipOfTheDay(timesTipsChosen)
        } else if (timesTipsChosen < 2 && shouldShowAgain()) {
          showMonikasWritingTipOfTheDay(timesTipsChosen)
        }
      }
    } else {
      //todo: should probably check to see if the material file scopes are installed (when somebody complains)
      dokiFileColors.removeFileScopes(this.project)
    }

  }

  private fun shouldShowAgain() = ranbo.nextLong() % 42 == 0L

  @Suppress("DEPRECATION")
  private fun showMonikasWritingTipOfTheDay(timesTipsChosen: Int) {
    TipDialog.createForProject(this.project).show()
    PropertiesComponent.getInstance().setValue(WRITING_TIP_OF_THE_DAY, (timesTipsChosen + 1).toString())
  }

}


const val WRITING_TIP_OF_THE_DAY: String = "WRITING_TIP_OF_THE_DAY"
