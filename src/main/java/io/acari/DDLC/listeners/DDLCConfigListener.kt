package io.acari.DDLC.listeners

import com.chrisrm.ideaddlc.config.ui.MTForm
import com.intellij.util.messages.Topic
import io.acari.DDLC.DDLCConfig

interface DDLCConfigListener {
  companion object {
    val DDLC_CONFIG_TOPIC: Topic<DDLCConfigListener> = Topic.create("Doki-Doki Theme Configuration Changes", DDLCConfigListener::class.java)
  }

  fun configurationChanged(ddlcConfig: DDLCConfig)
  fun beforeConfigurationChanged(ddlcConfig: DDLCConfig, mtForm: MTForm)
}