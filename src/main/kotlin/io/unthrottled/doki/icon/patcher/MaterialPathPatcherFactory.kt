package io.unthrottled.doki.icon.patcher

import com.google.gson.Gson
import com.intellij.util.ResourceUtil
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object MaterialPathPatcherFactory {

  private val gson = Gson()

  val materialPathPatchers: MaterialPathPatchers = try {
    ResourceUtil.getResource(
      MaterialPathPatcherFactory::class.java,
      "/patcher/",
      "patcher.json"
    ).openStream().use {
      val def = gson.fromJson(
        InputStreamReader(it, StandardCharsets.UTF_8),
        PathPatchersDefinition::class.java
      )
      MaterialPathPatchers(
        def.glyphs.map { patcherDef -> MaterialPathPatcher(patcherDef) }
      )
    }
  } catch (t: Throwable) {
    t.printStackTrace()
    MaterialPathPatchers()
  }
}
