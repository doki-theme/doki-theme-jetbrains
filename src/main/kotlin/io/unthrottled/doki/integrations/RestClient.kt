package io.unthrottled.doki.integrations

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.io.HttpRequests
import io.unthrottled.doki.integrations.RestTools.performRequest
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.readAllTheBytes
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toOptional
import java.io.InputStream
import java.util.Optional
import java.util.concurrent.Callable

object RestClient : Logging {
  fun performGet(url: String): Optional<String> =
    ApplicationManager.getApplication().executeOnPooledThread(
      Callable {
        runSafelyWithResult({
          performRequest(url) { responseBody ->
            String(responseBody.readAllTheBytes())
          }
        }) {
          logger().warn("Unable to complete request for $url for raisins.", it)
          Optional.empty()
        }
      },
    ).get()
}

object RestTools {
  private val log = Logger.getInstance(this::class.java)

  fun <T> performRequest(
    url: String,
    bodyExtractor: (InputStream) -> T,
  ): Optional<T> {
    log.info("Attempting to download remote asset: $url")
    return runSafelyWithResult({
      HttpRequests.request(url)
        .connect { request ->
          runSafelyWithResult({
            val body = bodyExtractor(request.inputStream)
            log.info("Asset has responded for remote asset: $url")
            body.toOptional()
          }) {
            log.warn("Unable to get remote asset: $url for raisins", it)
            Optional.empty<T>()
          }
        }
    }) {
      log.warn("Unable to perform request for url $url", it)
      Optional.empty()
    }
  }
}
