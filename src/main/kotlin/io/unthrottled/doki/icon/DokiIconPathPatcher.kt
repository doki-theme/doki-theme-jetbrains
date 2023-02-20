package io.unthrottled.doki.icon

import com.google.gson.reflect.TypeToken
import com.intellij.openapi.util.IconPathPatcher
import io.unthrottled.doki.util.AssetTools.readJsonFromResources
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger

data class PathMapping(
  val originalPath: String,
  val replacementPath: String
)

class DokiIconPathPatcher(mappingFile: String) : IconPathPatcher(), Logging {

  private val pathMappings: Map<String, String> =
    readJsonFromResources<List<PathMapping>>(
      "/mappings",
      mappingFile,
      object : TypeToken<List<PathMapping>>() {}.type
    )
      .map { def ->
        def.associate {
          it.originalPath to it.replacementPath
        }
      }.orElseGet {
        logger().warn("Unable to read path mappings")
        emptyMap()
      }

  override fun patchPath(
    path: String,
    classLoader: ClassLoader?
  ): String? = pathMappings[path]

  override fun getContextClassLoader(
    path: String,
    originalClassLoader: ClassLoader?
  ): ClassLoader? =
    javaClass.classLoader
}
