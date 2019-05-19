package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.ide.IconProvider
import com.intellij.openapi.extensions.impl.ExtensionPointImpl
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.containers.stream
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.legacy.Runner
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors

object DokiExtensionHacker {
  private lateinit var cacheFeildToHack: Field
  private var materialExtensions: MutableList<Any?> = ArrayList()
  private var nonMaterialExtensions: MutableList<Any?> = ArrayList()
  private var dokiExtensions: MutableList<Any?> = ArrayList()

  init{
    MTThemeManager.addMaterialThemeActivatedListener {
      handleMaterialThemeActivated(it)
    }
  }

  private fun handleMaterialThemeActivated(materialThemeActive: Boolean) {
      if(materialThemeActive){
        putBackMaterialExtensions()
      } else {
        removeMaterialExtensions()
      }
  }

  private fun removeMaterialExtensions() {
    if(!dokiExtensions.isEmpty()){
      val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
      val listToAddTo = ArrayList(dokiExtensions)
      ContainerUtil.addAll(listToAddTo, nonMaterialExtensions)
      cacheFeildToHack.set(point, ContainerUtil.immutableList(listToAddTo))
    }
  }

  private fun putBackMaterialExtensions() {
    if(!materialExtensions.isEmpty()){
      val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
      val listToAddTo = ArrayList(materialExtensions)
      ContainerUtil.addAll(listToAddTo, nonMaterialExtensions)
      cacheFeildToHack.set(point, ContainerUtil.immutableList(listToAddTo))
    }

  }

  fun hackExtensions() {
    LegacySupportUtility.invokeClassSafely("com.intellij.ide.ui.laf.darcula.ui.DarculaSeparatorUI", Runner {
      mutilateExtensionPoints()
    })
  }

  private fun mutilateExtensionPoints() {
    val classToHack = ExtensionPointImpl::class.java
    cacheFeildToHack = classToHack.declaredFields.stream().filter { it.name == "myExtensionsCache" }.findFirst().get()
    cacheFeildToHack.isAccessible = true
    val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
    point.extensionList
    val cache = cacheFeildToHack.get(point)
    if (cache is List<*>) {
      val partitionedIconProviders: Map<String, List<Any?>> = cache.stream().collect(
          Collectors.groupingBy {
            val name = it?.javaClass?.name ?: ""
            val containsMaterial = name.contains("com.chrisrm")
            val containsDDLC = name.contains("ddlc")
            if(!containsDDLC && containsMaterial) "MATERIAL"
            else if(containsDDLC && containsMaterial) "DOKI-DOKI"
            else "OTHER"
          }
      )

      this.nonMaterialExtensions = ContainerUtil.immutableList(partitionedIconProviders["OTHER"] ?: Collections.emptyList())
      this.materialExtensions = ContainerUtil.immutableList(partitionedIconProviders["MATERIAL"] ?: Collections.emptyList())
      this.dokiExtensions = ContainerUtil.immutableList(partitionedIconProviders["DOKI-DOKI"] ?: Collections.emptyList())

      val arrayList = ArrayList(dokiExtensions)
      ContainerUtil.addAll(arrayList, nonMaterialExtensions)
      cacheFeildToHack.set(point, ContainerUtil.immutableList(arrayList))
    }

  }
}