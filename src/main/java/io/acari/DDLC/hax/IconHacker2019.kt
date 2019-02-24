package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.icons.MTIconReplacerComponent
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import java.util.concurrent.atomic.AtomicReference

class IconHacker2019 : IconHacker {
  private val mtIconReplacers = HashSet<IconPathPatcher>()
  private val ddlcIconReplacers = HashSet<IconPathPatcher>()
  private val iconBucket = HashSet<IconPathPatcher>()

  override fun extractPatchersOnActivation() {
    val iconPatchers = extractIcons()
    sortIntoBuckets(iconPatchers)
    handleMaterialThemeActivated(true)
  }

  private fun sortIntoBuckets(iconPatchers: MutableList<IconPathPatcher>) {
    iconPatchers.forEach {
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
    }
  }

  override fun handleMaterialThemeActivated(materialThemeActive: Boolean) {
    val patchers = extractIcons()
    sortIntoBuckets(patchers)
    patchers.forEach { IconLoader.removePathPatcher(it) }

    val installer: (IconPathPatcher) -> Unit = { IconLoader.installPathPatcher(it) }
    iconBucket.forEach(installer)
    when (materialThemeActive && mtIconReplacers.isNotEmpty()) {
      true -> {
        mtIconReplacers.forEach(installer)
      }
      false -> {
        ddlcIconReplacers.forEach(installer)
        MTIconReplacerComponent.useDDLCIcons()
      }
    }

  }

  fun extractIcons(): MutableList<IconPathPatcher> {
    return try {
      val poopHead = Class.forName("com.intellij.openapi.util.IconLoader\$IconTransform")
      val myPatcherFields = poopHead.declaredFields.first { it.name == "myPatchers" }
      myPatcherFields.isAccessible = true
      val ourTransformField = IconLoader::class.java.getDeclaredField("ourTransform")
      ourTransformField.isAccessible = true
      val iconTransform = (ourTransformField.get(null) as AtomicReference<*>).get()
      val patchers = myPatcherFields.get(iconTransform) as Array<IconPathPatcher>
      patchers.toMutableList()
    } catch (e: Throwable) {
      mutableListOf()
    }
  }

}