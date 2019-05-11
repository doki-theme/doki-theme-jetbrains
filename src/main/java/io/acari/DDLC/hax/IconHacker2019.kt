package io.acari.DDLC.hax

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.IconPathPatcher
import com.intellij.util.containers.stream
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors

class IconHacker2019 : IconHacker {
  private val mtIconReplacers = HashSet<IconPathPatcher>()
  private val ddlcIconReplacers = HashSet<IconPathPatcher>()
  private val iconBucket = HashSet<IconPathPatcher>()
  private val messageBus = ApplicationManager.getApplication().messageBus

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
        messageBus.syncPublisher(IconChangedTopic)
            .run()
      }
    }

  }

  private fun extractIcons(): MutableList<IconPathPatcher> {
    return try {
      val poopHead = Class.forName("com.intellij.openapi.util.IconLoader\$IconTransform")
      val myPatcherFields = poopHead.declaredFields.first { it.name == "myPatchers" }
      myPatcherFields.isAccessible = true
      val ourTransformField = IconLoader::class.java.getDeclaredField("ourTransform")
      ourTransformField.isAccessible = true
      val iconTransform = (ourTransformField.get(null) as AtomicReference<*>).get()
      val patchers = myPatcherFields.get(iconTransform)
      if(patchers is Array<*>){
        patchers.stream()
            .filter { it is IconPathPatcher }
            .map { it as IconPathPatcher }
            .collect(Collectors.toList())
      } else {
        mutableListOf()
      }
    } catch (e: Throwable) {
      mutableListOf()
    }
  }

}