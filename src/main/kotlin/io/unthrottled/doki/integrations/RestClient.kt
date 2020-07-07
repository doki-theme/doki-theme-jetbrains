package io.unthrottled.doki.integrations

import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.doki.util.readAllTheBytes
import io.unthrottled.doki.util.toOptional
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.util.Optional
import java.util.concurrent.TimeUnit

object RestClient {
  private val httpClient = HttpClients.createMinimal()
  private val log = Logger.getInstance(this::class.java)

  fun performGet(url: String): Optional<String> {
    log.info("Attempting to fetch remote asset: $url")
    val request = createGetRequest(url)
    return try {
      val response = httpClient.execute(request)
      val statusCode = response.statusLine.statusCode
      log.info("Asset has responded for remote asset: $url with status code $statusCode")
      if (statusCode == 200) {
        response.entity.content.use { responseBody ->
          String(responseBody.readAllTheBytes())
        }.toOptional()
      } else {
        Optional.empty()
      }
    } catch (e: Exception) {
      log.warn("Unable to get remote asset: $url for raisins", e)
      Optional.empty<String>()
    }
  }

  private fun createGetRequest(remoteUrl: String): HttpGet {
    val remoteAssetRequest = HttpGet(remoteUrl)
    remoteAssetRequest.addHeader("User-Agent", "One-Dark")
    remoteAssetRequest.config = RequestConfig.custom()
      .setConnectTimeout(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS).toInt())
      .build()
    return remoteAssetRequest
  }
}