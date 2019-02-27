package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.util.IconLoader
import io.acari.DDLC.DDLCApplicationInitializationComponent
import java.lang.reflect.Field

object DDLCIconHackComponent {

  private val iconHacker: IconHacker =
      if (hasOldMethod()) LegacyIconHacker()
      else IconHacker2019()

  init {
    DDLCApplicationInitializationComponent.addInitializationListener(Runnable {
      iconHacker.extractPatchersOnActivation()
    })

    MTThemeManager.addMaterialThemeActivatedListener {
      this.iconHacker.handleMaterialThemeActivated(it)
    }
  }

  fun fetchPatches(): Field = IconLoader::class.java.getDeclaredField("ourPatchers")

  private fun hasOldMethod() =
      try {
        fetchPatches()
        true
      } catch (e: Throwable) {
        false
      }
}