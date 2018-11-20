package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import io.acari.DDLC.DDLCApplicationInitializationComponent
import java.util.*

object DDLCIconHackComponent {

  init {
    DDLCApplicationInitializationComponent.addInitializationListener(Runnable {
      enableMaterialCoexistence()
    })
    MTThemeManager.addMaterialThemeActivatedListener {
      activatePatchers(it)
    }
  }

  private fun activatePatchers(materialThemeActive: Boolean?) {

  }

  private val mtIconReplacers = LinkedList<IconPathPatcher>()
  private lateinit var iconReplacements: List<IconPathPatcher>

  private fun enableMaterialCoexistence() {
    iconReplacements = extractPatchersReference()
    println(iconReplacements)
  }

  private fun extractPatchersReference(): List<IconPathPatcher> {
    val ourPatchersField = IconLoader::class.java.getDeclaredField("ourPatchers")
    ourPatchersField.isAccessible = true
    return ourPatchersField.get(null) as List<IconPathPatcher>

  }


}