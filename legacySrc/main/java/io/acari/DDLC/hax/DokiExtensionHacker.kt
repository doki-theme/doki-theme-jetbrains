package io.acari.DDLC.hax

import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.ide.IconProvider
import com.intellij.openapi.extensions.impl.ExtensionComponentAdapter
import com.intellij.openapi.extensions.impl.ExtensionPointImpl
import com.intellij.openapi.util.IconLoader
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.containers.ImmutableList
import com.intellij.util.containers.stream
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.legacy.Runner
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors

object DokiExtensionHacker {
  private lateinit var cacheFeildToHack: Field
  private lateinit var adapterFeildToHack: Field
  private var materialExtensions: MutableList<Any?> = ArrayList()
  private var nonMaterialExtensions: MutableList<Any?> = ArrayList()
  private var dokiExtensions: MutableList<Any?> = ArrayList()
  private var materialAdapters: MutableList<Any?> = ArrayList()
  private var nonMaterialAdapters: MutableList<Any?> = ArrayList()
  private var dokiAdapters: MutableList<Any?> = ArrayList()

//  init{
//    MTThemeManager.addMaterialThemeActivatedListener {
//      handleMaterialThemeActivated(it)
//    }
//  }
//
//  private fun handleMaterialThemeActivated(materialThemeActive: Boolean) {
//      if(materialThemeActive){
//        putBackMaterialExtensions()
//      } else {
//        removeMaterialExtensions()
//      }
//  }
//
//  private fun removeMaterialExtensions() {
//    if(!dokiExtensions.isEmpty()){
//      val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
//      val listToAddTo = ArrayList(dokiExtensions)
//      listToAddTo.addAll(nonMaterialExtensions)
//      cacheFeildToHack.set(point, ContainerUtil.immutableList(listToAddTo))
//    }
//  }
//
//  private fun putBackMaterialExtensions() {
//    if(!materialExtensions.isEmpty()){
//      val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
//      val listToAddTo = ArrayList(materialExtensions)
//      listToAddTo.addAll(nonMaterialExtensions)
//      cacheFeildToHack.set(point, ContainerUtil.immutableList(listToAddTo))
//    }
//
//  }

  fun hackExtensions() {
//    LegacySupportUtility.invokeClassSafely("com.intellij.ide.ui.laf.darcula.ui.DarculaSeparatorUI", Runner {
//      mutilateExtensionPoints()
//    })
  }

//  private fun mutilateExtensionPoints() {
//    val classToHack = ExtensionPointImpl::class.java
//    cacheFeildToHack = classToHack.declaredFields.stream().filter { it.name == "myExtensionsCache" }.findFirst().get()
//
//    val (dokiExtractedExtensions, materialExtractedExtensions, nonMaterialExtractedExtensions) =
//        mutilateField(cacheFeildToHack) { it?.javaClass?.name ?: ""}
//    dokiExtensions = dokiExtractedExtensions
//    materialExtensions = materialExtractedExtensions
//    nonMaterialExtensions = nonMaterialExtractedExtensions
//
//    adapterFeildToHack = classToHack.declaredFields.stream().filter { it.name == "myAdapters" }.findFirst().get()
//    val (dokiExtractedAdapters, materialExtractedAdapters, nonMaterialExtractedAdapters) =
//        mutilateField(adapterFeildToHack) { (it as? ExtensionComponentAdapter)?.assignableToClassName ?: ""}
//    dokiAdapters = dokiExtractedAdapters
//    materialAdapters = materialExtractedAdapters
//    nonMaterialAdapters = nonMaterialExtractedAdapters
//  }
//
//  private fun mutilateField(cacheFeildToHack: Field, nameExtractor: (Any?)->String): Triple<ImmutableList<Any?>, ImmutableList<Any?>, ImmutableList<Any?>> {
//    cacheFeildToHack.isAccessible = true
//    val point = IconProvider.EXTENSION_POINT_NAME.getPoint(null)
//    point.extensionList
//    val cache = cacheFeildToHack.get(point)
//    if (cache is List<*>) {
//      val partitionedIconProviders: Map<String, List<Any?>> = cache.stream().collect(
//          Collectors.groupingBy {
//            val name = nameExtractor(it)
//            val containsMaterial = name.contains("com.chrisrm")
//            val containsDDLC = name.contains("ddlc")
//            if (!containsDDLC && containsMaterial) "MATERIAL"
//            else if (containsDDLC && containsMaterial) "DOKI-DOKI"
//            else "OTHER"
//          }
//      )
//
//      val nonMaterialExtensions_2 = ContainerUtil.immutableList(partitionedIconProviders["OTHER"]
//          ?: Collections.emptyList())
//      val materialExtensions_2 = ContainerUtil.immutableList(partitionedIconProviders["MATERIAL"]
//          ?: Collections.emptyList())
//      val dokiExtensions_2 = ContainerUtil.immutableList(partitionedIconProviders["DOKI-DOKI"]
//          ?: Collections.emptyList())
//
//      val arrayList = ArrayList(dokiExtensions_2)
//      arrayList.addAll(nonMaterialExtensions_2)
//      cacheFeildToHack.set(point, ContainerUtil.immutableList(arrayList))
//      return Triple(dokiExtensions_2,
//          materialExtensions_2,
//          nonMaterialExtensions_2)
//    }
//    val emptyList = ContainerUtil.immutableList(mutableListOf<Any?>())
//    return Triple(emptyList,emptyList,emptyList)
//  }
}