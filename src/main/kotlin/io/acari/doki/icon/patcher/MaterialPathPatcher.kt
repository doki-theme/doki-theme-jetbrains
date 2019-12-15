package io.acari.doki.icon.patcher

import com.intellij.openapi.util.IconPathPatcher

class MaterialPathPatcher(
  private val patcherDefinition: PathPatcherDefinition
) : IconPathPatcher() {

  private val cache: MutableMap<String, String?> = HashMap(512)

  override fun getContextClassLoader(path: String, originalClassLoader: ClassLoader?): ClassLoader? =
    javaClass.classLoader

  override fun patchPath(path: String, classLoader: ClassLoader?): String? {
    return if (cache.containsKey(path)) {
      cache[path]
    } else {
      val adjustedPath = path.replace(patcherDefinition.pathToRemove, "")
      val pathBoi = "/icons/material${patcherDefinition.pathToAppend}$adjustedPath"
      val toCache = if (javaClass.getResource(pathBoi) != null)
        pathBoi
      else
        null
      cache[path] = toCache
      toCache
    }
  }
}