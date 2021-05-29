package io.unthrottled.doki.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.plugins.marketplace.IdeCompatibleUpdate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.Urls
import com.intellij.util.io.HttpRequests
import io.unthrottled.doki.util.Logging
import java.util.Collections
import java.util.concurrent.Callable

const val MOTIVATOR_PLUGIN_ID = "zd.zero.waifu-motivator-plugin"
const val AMII_PLUGIN_ID = "io.unthrottled.amii"

object PluginService : Logging {

  private val objectMapper by lazy { ObjectMapper() }

  fun isMotivatorInstalled(): Boolean = PluginManagerCore.isPluginInstalled(
    PluginId.getId(MOTIVATOR_PLUGIN_ID)
  )

  fun isAmiiInstalled(): Boolean = PluginManagerCore.isPluginInstalled(
    PluginId.getId(AMII_PLUGIN_ID)
  )

  private val PLUGIN_MANAGER_URL = ApplicationInfoImpl.getInstanceEx()
    .pluginManagerUrl
    .trimEnd('/')

  private val COMPATIBLE_UPDATE_URL = "$PLUGIN_MANAGER_URL/api/search/compatibleUpdates"

  private data class CompatibleUpdateRequest(
    val build: String,
    val pluginXMLIds: List<String>,
  )

  private fun getLastCompatiblePluginUpdate(
    ids: Set<PluginId>
  ): List<IdeCompatibleUpdate> {
    return try {
      if (ids.isEmpty()) {
        return emptyList()
      }

      val data = objectMapper.writeValueAsString(
        CompatibleUpdateRequest(
          ApplicationInfoImpl.getInstanceEx()
            .build.asString(), ids.map { it.idString }
        )
      )
      HttpRequests
        .post(Urls.newFromEncoded(COMPATIBLE_UPDATE_URL).toExternalForm(), HttpRequests.JSON_CONTENT_TYPE)
        .productNameAsUserAgent()
        .throwStatusCodeException(false)
        .connect {
          it.write(data)
          objectMapper.readValue(it.inputStream, object : TypeReference<List<IdeCompatibleUpdate>>() {})
        }
    } catch (e: Exception) {
      emptyList()
    }
  }

  fun canAmiiBeInstalled(): Boolean {
    return ApplicationManager.getApplication().executeOnPooledThread(
      Callable {
        val pluginId = PluginId.getId(AMII_PLUGIN_ID)
        getLastCompatiblePluginUpdate(
          Collections.singleton(pluginId)
        ).firstOrNull { pluginNode ->
          pluginNode.pluginId == AMII_PLUGIN_ID
        } != null
      }
    ).get()
  }
}
