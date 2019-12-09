package io.acari.doki.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxUI
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.layout.panel
import com.intellij.util.ui.CheckBox
import io.acari.doki.config.ThemeConfig
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.themes.ThemeManager
import java.net.URI
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var currentTheme: String,
  var isThemedTitleBar: Boolean,
  var isSwappedSticker: Boolean
)


class ThemeSettings : SearchableConfigurable {

  companion object {
    val CHANGELOG_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/docs/CHANGELOG.md")
    val ISSUES_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/issues")
    val MARKETPLACE_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-doki-theme")
  }

  override fun getId(): String = "io.acari.doki.settings.ThemeSettings"

  override fun getDisplayName(): String = "Doki Theme Settings"

  private var initialThemeSettingsModel = ThemeSettingsModel(
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
    ThemeManager.instance.currentTheme.map { it.name }.orElseGet { ThemeManager.MONIKA_LIGHT },
    ThemeConfig.instance.isThemedTitleBar,
    ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY
  )

  private var themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    themeSettingsModel = initialThemeSettingsModel.copy()
    println(initialThemeSettingsModel)
    // dispetch deltas.
  }

  override fun createComponent(): JComponent? {
    return panel {
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
          checkBox(
            "Enable Stickers",
            themeSettingsModel::areStickersEnabled
          )
        }
        row {
          checkBox(
            "Swap Sticker",
            themeSettingsModel::isSwappedSticker
          )
        }
        row {
          checkBox(
            "Themed Title Bar",
            themeSettingsModel::isThemedTitleBar
          )
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
}