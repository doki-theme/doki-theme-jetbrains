package io.acari.doki.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.layout.panel
import javax.swing.JComponent


class ThemeSettings : SearchableConfigurable {
  override fun getId(): String = "io.acari.doki.settings.ThemeSettings"

  override fun getDisplayName(): String = "Doki Theme Settings"

  override fun isModified(): Boolean {
    return false
  }

  override fun apply() {
    // apply settings
  }

  override fun createComponent(): JComponent? =
    panel {
      row("Ayy lemons") {
        checkBox("Hello")
      }
    }
}