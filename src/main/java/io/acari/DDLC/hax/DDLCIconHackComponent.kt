package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.icons.MTIconReplacerComponent
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import io.acari.DDLC.DDLCApplicationInitializationComponent
import java.util.concurrent.atomic.AtomicReference

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
    iconReplacements.addAll(iconBucket)
    when (materialThemeActive && mtIconReplacers.isNotEmpty()) {
      true -> {
        iconReplacements.addAll(mtIconReplacers)
      }
      false -> {
        iconReplacements.addAll(ddlcIconReplacers)
        MTIconReplacerComponent.useDDLCIcons()
      }
    }
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
    return try{
      val ourPatchersField = IconLoader::class.java.getDeclaredField("ourPatchers")
      ourPatchersField.isAccessible = true
      ourPatchersField.get(null) as MutableList<IconPathPatcher>
    } catch (e: Throwable){
        tryForOther()
    }
  }

  fun tryForOther(): MutableList<IconPathPatcher> {
    return try{
      val poopHead = Class.forName("com.intellij.openapi.util.IconLoader\$IconTransform")
      val myPatcherFields = poopHead.declaredFields.first { it.name == "myPatchers" }
      myPatcherFields.isAccessible = true

      val ourTransformField = IconLoader::class.java.getDeclaredField("ourTransform")
      ourTransformField.isAccessible = true
      val iconTransform = (ourTransformField.get(null) as AtomicReference<*>).get()
      val patchers = myPatcherFields.get(iconTransform) as Array<IconPathPatcher>
      patchers.toMutableList()
    }catch (e: Throwable){
      mutableListOf()
    }
  }


}