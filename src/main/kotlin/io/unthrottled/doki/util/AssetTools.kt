package io.unthrottled.doki.util

import com.google.gson.Gson
import com.intellij.util.ResourceUtil
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.Optional

object AssetTools : Logging {
  private val gson = Gson()

  // todo: common
  fun <T : Any> readJsonFromResources(
    basePath: String,
    filePath: String,
    type: Type
  ): Optional<T> {
    return runSafelyWithResult({
      ResourceUtil.getResourceAsStream(
        AssetTools::class.java.classLoader,
        basePath,
        filePath
      ).use { inputStream ->
        gson.fromJson<T>(
          InputStreamReader(inputStream, StandardCharsets.UTF_8),
          type
        ).toOptional()
      }
    }) {
      logger().error("Unable to read JSON from resources for file $basePath/$filePath", it)
      Optional.empty()
    }
  }
}
