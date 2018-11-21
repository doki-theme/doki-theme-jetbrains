package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import io.acari.DDLC.DDLCApplicationInitializationComponent

object DDLCIconHackComponent {

  init {
    DDLCApplicationInitializationComponent.addInitializationListener(Runnable {
      enableMaterialCoexistence()
    })

    MTThemeManager.addMaterialThemeActivatedListener {
      activatePatchers(it)
    }
  }

  private fun activatePatchers(materialThemeActive: Boolean) {
    harvestIconPatchers()
    when (materialThemeActive && mtIconReplacers.isNotEmpty()) {
      true -> {
        iconReplacements.addAll(mtIconReplacers)
      }
      false -> {
        iconReplacements.addAll(ddlcIconReplacers)
      }
    }
    iconReplacements.addAll(iconBucket)
  }

  private val mtIconReplacers = HashSet<IconPathPatcher>()
  private val ddlcIconReplacers = HashSet<IconPathPatcher>()
  private val iconBucket = HashSet<IconPathPatcher>()
  private lateinit var iconReplacements: MutableList<IconPathPatcher>

  private fun enableMaterialCoexistence() {
    iconReplacements = extractPatchersReference()
    activatePatchers(true) //assuming that material ui loads first
  }

  private fun harvestIconPatchers() {
    iconReplacements.removeAll {
      val name = it.javaClass.name
      val containsMaterial = name.contains("com.chrisrm")
      val containsDDLC = name.contains("ddlc")
      if (containsMaterial && !containsDDLC) {
        mtIconReplacers.add(it)
      } else if (containsMaterial && containsDDLC) {
        ddlcIconReplacers.add(it)
      } else {
        iconBucket.add(it)
      }
      true
    }
  }

  private fun extractPatchersReference(): MutableList<IconPathPatcher> {
    val ourPatchersField = IconLoader::class.java.getDeclaredField("ourPatchers")
    ourPatchersField.isAccessible = true
    return ourPatchersField.get(null) as MutableList<IconPathPatcher>

  }


}