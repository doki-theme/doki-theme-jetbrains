package io.unthrottled.doki.service

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.plugins.marketplace.MarketplaceRequests
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import java.util.Collections
import java.util.concurrent.Callable

const val MOTIVATOR_PLUGIN_ID = "zd.zero.waifu-motivator-plugin"
const val AMII_PLUGIN_ID = "io.unthrottled.amii"

object PluginService {

  fun isMotivatorInstalled(): Boolean = PluginManagerCore.isPluginInstalled(
    PluginId.getId(MOTIVATOR_PLUGIN_ID)
  )

  fun isAmiiInstalled(): Boolean = PluginManagerCore.isPluginInstalled(
    PluginId.getId(AMII_PLUGIN_ID)
  )

  fun canAmiiBeInstalled(): Boolean {
    return ApplicationManager.getApplication().executeOnPooledThread(Callable {
      val ids = AMII_PLUGIN_ID
      val pluginId = PluginId.getId(ids)
      MarketplaceRequests.getInstance().loadLastCompatiblePluginDescriptors(
        Collections.singletonList(ids)
      ).firstOrNull { pluginNode ->
        pluginNode.pluginId == pluginId
      } != null
    }).get()
  }
}
