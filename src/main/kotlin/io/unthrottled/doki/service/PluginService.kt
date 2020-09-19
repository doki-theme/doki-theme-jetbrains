package io.unthrottled.doki.service

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

const val MOTIVATOR_PLUGIN_ID = "zd.zero.waifu-motivator-plugin"

object PluginService {

  fun isMotivatorInstalled(): Boolean = PluginManagerCore.isPluginInstalled(
    PluginId.getId(MOTIVATOR_PLUGIN_ID)
  )
}