package io.acari.DDLC.hax

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.IconPathPatcher
import com.intellij.util.messages.Topic
import io.acari.DDLC.legacy.Runner
import java.util.stream.Collectors

val IconChangedTopic: Topic<Runner> = Topic.create("Icons Changed", Runner::class.java);

class LegacyIconHacker : IconHacker {
  private val mtIconReplacers = HashSet<IconPathPatcher>()
  private val ddlcIconReplacers = HashSet<IconPathPatcher>()
  private val iconBucket = HashSet<IconPathPatcher>()
  private lateinit var iconReplacements: MutableList<IconPathPatcher>
  private val eventBus = ApplicationManager.getApplication().messageBus

  override fun extractPatchersOnActivation() {
    iconReplacements = extractPatchersReference()
    handleMaterialThemeActivated(true) //assuming that material ui loads first
  }

  override fun handleMaterialThemeActivated(materialThemeActive: Boolean) {
    harvestIconPatchers()
    iconReplacements.addAll(iconBucket)
    when (materialThemeActive && mtIconReplacers.isNotEmpty()) {
      true -> {
        iconReplacements.addAll(mtIconReplacers)
      }
      false -> {
        iconReplacements.addAll(ddlcIconReplacers)
        eventBus.syncPublisher(IconChangedTopic)
            .run()
      }
    }
  }

  private fun extractPatchersReference(): MutableList<IconPathPatcher> {
    return try {
      val ourPatchersField = DDLCIconHackComponent.fetchPatches()
      ourPatchersField.isAccessible = true
      val patchers = ourPatchersField.get(null)
      if(patchers is MutableList<*>) {
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
}