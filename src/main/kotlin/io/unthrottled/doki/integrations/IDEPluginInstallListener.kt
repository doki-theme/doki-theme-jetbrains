package io.unthrottled.doki.integrations

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import io.unthrottled.doki.icon.IconPathReplacementComponent
import io.unthrottled.doki.laf.LookAndFeelInstaller
import io.unthrottled.doki.service.DOKI_ICONS_PLUGIN_ID
import io.unthrottled.doki.util.Logging

class IDEPluginInstallListener : DynamicPluginListener, Logging {
  override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
  }

  override fun beforePluginUnload(
    pluginDescriptor: IdeaPluginDescriptor,
    isUpdate: Boolean,
  ) {
  }

  override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    if (isDokiIconPlugin(pluginDescriptor)) {
      IconPathReplacementComponent.removePatchers()
      LookAndFeelInstaller.installAllUIComponents()
    }
  }

  private fun isDokiIconPlugin(intellijIdeaPluginDescriptorParameterReferenceVariable: IdeaPluginDescriptor) =
    intellijIdeaPluginDescriptorParameterReferenceVariable.pluginId.idString == DOKI_ICONS_PLUGIN_ID

  override fun pluginUnloaded(
    pluginDescriptor: IdeaPluginDescriptor,
    isUpdate: Boolean,
  ) {
    if (isDokiIconPlugin(pluginDescriptor)) {
      IconPathReplacementComponent.installPatchers()
      LookAndFeelInstaller.installAllUIComponents()
    }
  }
}
