package io.acari.doki.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.layout.panel
import java.net.URI
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent


class ThemeSettings : SearchableConfigurable {

  companion object {
    val CHANGELOG_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/docs/CHANGELOG.md")
    val ISSUES_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/issues")
    val MARKETPLACE_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-doki-theme")
  }

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
      titledRow("Main Settings") {
        row {
          cell {
            label("Current Theme")
            comboBox(DefaultComboBoxModel(
              arrayOf(1, 2, 3)
            ), { 3 }, { _ -> })
          }
        }
        row {
          row {
            checkBox("Enable Stickers")
          }
          row {
            checkBox("Swap Sticker")
          }
          row {
            checkBox("Themed Title Bar")
          }
        }
      }
      titledRow("Miscellaneous") {
        row {
          cell {
            button("View Issues") {
              browse(ISSUES_URI)
            }
            button("View Changelog") {
              browse(CHANGELOG_URI)
            }
            button("Marketplace Homepage") {
              browse(MARKETPLACE_URI)
            }
          }
        }
      }
    }
}