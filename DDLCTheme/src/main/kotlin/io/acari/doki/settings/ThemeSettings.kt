package io.acari.doki.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.layout.panel
import io.acari.doki.settings.actors.FileColorActor
import io.acari.doki.config.ThemeConfig
import io.acari.doki.settings.actors.StickerActor
import io.acari.doki.settings.actors.ThemeActor
import io.acari.doki.settings.actors.ThemedTitleBarActor
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.themes.ThemeManager
import java.net.URI
import java.util.*
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var currentTheme: String,
  var isThemedTitleBar: Boolean,
  var isFileColors: Boolean,
  var isSwappedSticker: Boolean
)

class ThemeSettings : SearchableConfigurable {

  companion object {
    const val THEME_SETTINGS_DISPLAY_NAME = "Doki Theme Settings"
    val CHANGELOG_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/docs/CHANGELOG.md")
    val ISSUES_URI = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/issues")
    val MARKETPLACE_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-doki-theme")
  }

  override fun getId(): String = "io.acari.doki.settings.ThemeSettings"

  override fun getDisplayName(): String =
    THEME_SETTINGS_DISPLAY_NAME

  private val initialThemeSettingsModel = ThemeSettingsModel(
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
    ThemeManager.instance.currentTheme.map { it.name }.orElseGet { ThemeManager.MONIKA_LIGHT },
    ThemeConfig.instance.isThemedTitleBar,
    ThemeConfig.instance.isDokiFileColors,
    ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY
  )

  private val themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    FileColorActor.enableFileColors(themeSettingsModel.isFileColors)
    StickerActor.enableStickers(themeSettingsModel.areStickersEnabled)
    StickerActor.swapStickers(themeSettingsModel.isSwappedSticker)
    ThemedTitleBarActor.enableThemedTitleBar(themeSettingsModel.isThemedTitleBar)
    ThemeActor.applyTheme(themeSettingsModel.currentTheme)
  }

  override fun createComponent(): JComponent? {
    val themeComboBox = ComboBox(DefaultComboBoxModel(
      Vector(ThemeManager.instance.allThemes
        .groupBy { it.groupName }
        .entries
        .flatMap { it.value.sortedBy { theme -> theme.name } }
        .map { it.name }
      ))
    )
    themeComboBox.model.selectedItem = themeSettingsModel.currentTheme
    themeComboBox.addActionListener {
      themeSettingsModel.currentTheme = themeComboBox.model.selectedItem as String
    }
    return panel {
      titledRow("Main Settings") {
        row {
          cell {
            label("Current Theme")
            themeComboBox()
          }
        }
        row {
          checkBox(
            "Enable Stickers",
            themeSettingsModel.areStickersEnabled,
            actionListener = { _, component ->
              themeSettingsModel.areStickersEnabled = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Swap Sticker",
            themeSettingsModel.isSwappedSticker,
            actionListener = { _, component ->
              themeSettingsModel.isSwappedSticker = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Themed Title Bar",
            themeSettingsModel.isThemedTitleBar,
            actionListener = { _, component ->
              themeSettingsModel.isThemedTitleBar = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Enable File Colors",
            themeSettingsModel.isFileColors,
            actionListener = { _, component ->
              themeSettingsModel.isFileColors = component.isSelected
            }
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