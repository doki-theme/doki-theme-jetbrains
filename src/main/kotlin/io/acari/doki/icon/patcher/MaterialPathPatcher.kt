package io.acari.doki.icon.patcher

import com.intellij.openapi.util.IconPathPatcher

class MaterialPathPatcher(
  private val patcherDefinition: PathPatcherDefinition
) : IconPathPatcher() {

  override fun getContextClassLoader(path: String, originalClassLoader: ClassLoader?): ClassLoader? =
    javaClass.classLoader

  override fun patchPath(path: String, classLoader: ClassLoader?): String? {
    val adjustedPath = path.replace(patcherDefinition.pathToRemove, "")
    val pathBoi = "/icons/material${patcherDefinition.pathToAppend}$adjustedPath"
    return if(javaClass.getResource(pathBoi) != null)
      pathBoi
    else
      null
  }
}