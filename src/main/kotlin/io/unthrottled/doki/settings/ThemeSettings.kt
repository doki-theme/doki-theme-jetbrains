package io.unthrottled.doki.settings

import com.intellij.ide.BrowserUtil.browse
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.layout.panel
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.LafAnimationActor
import io.unthrottled.doki.settings.actors.MaterialIconsActor
import io.unthrottled.doki.settings.actors.ShowReadmeActor
import io.unthrottled.doki.settings.actors.StickerActor
import io.unthrottled.doki.settings.actors.ThemeActor
import io.unthrottled.doki.settings.actors.ThemeStatusBarActor
import io.unthrottled.doki.settings.actors.ThemedTitleBarActor
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.themes.ThemeManager
import java.net.URI
import java.util.Vector
import javax.swing.DefaultComboBoxModel
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var isLafAnimation: Boolean,
  var currentTheme: String,
  var isThemedTitleBar: Boolean,
  var showThemeStatusBar: Boolean,
  var isSwappedSticker: Boolean,
  var isMaterialDirectories: Boolean,
  var isMaterialFiles: Boolean,
  var isMaterialPSIIcons: Boolean,
  var isNotShowReadmeAtStartup: Boolean
)

class ThemeSettings : SearchableConfigurable {

  companion object {
    const val THEME_SETTINGS_DISPLAY_NAME = "Doki Theme Settings"
    val CHANGELOG_URI =
      URI("https://github.com/doki-theme/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md")
    private const val REPOSITORY = "https://github.com/doki-theme/doki-theme-jetbrains"
    const val ULTIMATE_INSTRUCTIONS = "$REPOSITORY/wiki/Ultimate-Theme-Setup"
    val ISSUES_URI = URI("$REPOSITORY/issues")
    val REVIEW_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-theme/reviews")
  }

  override fun getId(): String = "io.unthrottled.doki.settings.ThemeSettings"

  override fun getDisplayName(): String =
    THEME_SETTINGS_DISPLAY_NAME

  private val initialThemeSettingsModel = ThemeSettingsModel(
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
    ThemeConfig.instance.isLafAnimation,
    ThemeManager.instance.currentTheme.map { it.name }.orElseGet { ThemeManager.DEFAULT_THEME_NAME },
    ThemeConfig.instance.isThemedTitleBar,
    ThemeConfig.instance.showThemeStatusBar,
    ThemeConfig.instance.currentSticker == CurrentSticker.SECONDARY,
    ThemeConfig.instance.isMaterialDirectories,
    ThemeConfig.instance.isMaterialFiles,
    ThemeConfig.instance.isMaterialPSIIcons,
    ThemeConfig.instance.isNotShowReadmeAtStartup
  )

  private val themeSettingsModel = initialThemeSettingsModel.copy()

  override fun isModified(): Boolean {
    return initialThemeSettingsModel != themeSettingsModel
  }

  override fun apply() {
    LafAnimationActor.enableAnimation(themeSettingsModel.isLafAnimation)
    ShowReadmeActor.dontShowReadmeOnStartup(themeSettingsModel.isNotShowReadmeAtStartup)
    StickerActor.enableStickers(themeSettingsModel.areStickersEnabled, false)
    StickerActor.swapStickers(themeSettingsModel.isSwappedSticker, false)
    ThemedTitleBarActor.enableThemedTitleBar(themeSettingsModel.isThemedTitleBar)
    ThemeActor.applyTheme(themeSettingsModel.currentTheme)
    ThemeStatusBarActor.applyConfig(themeSettingsModel.showThemeStatusBar)
    MaterialIconsActor.enableDirectoryIcons(themeSettingsModel.isMaterialDirectories)
    MaterialIconsActor.enableFileIcons(themeSettingsModel.isMaterialFiles)
    MaterialIconsActor.enablePSIIcons(themeSettingsModel.isMaterialPSIIcons)
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeConfig.instance)
  }

  override fun createComponent(): JComponent? {
    return try {
      val tabbedPanel = JBTabbedPane()
      tabbedPanel.add("Main", createSettingsPane())
      tabbedPanel.add("Material Icons", createMaterialIconsPane())
      tabbedPanel
    } catch (e: Throwable) {
      val outOfServicePanel = JPanel()
      val outOfServiceTextPan = JTextPane()
      outOfServiceTextPan.text =
        """
          Doki Theme Settings Menu currently unavailable in the 
          2020 EAP Builds. All functionality is available in the 
          Doki Theme Options in the toolbar and tool menu.
        """.trimIndent()

      outOfServicePanel.add(outOfServiceTextPan)
      outOfServicePanel
    }
  }

  private fun createMaterialIconsPane(): DialogPanel {
    val directoryIcon = JLabel()
    directoryIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/directoryIcon.png"))
    val fileIcon = JLabel()
    fileIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/fileIcon.png"))
    val psiIcon = JLabel()
    psiIcon.icon = ImageIcon(javaClass.getResource("/icons/settings/psiIcon.png"))
    return panel {
      titledRow("Doki Themed Material Icons") {
        row {
          cell {
            directoryIcon()
            checkBox(
              "Directory Icons",
              themeSettingsModel.isMaterialDirectories,
              actionListener = { _, component ->
                themeSettingsModel.isMaterialDirectories = component.isSelected
              }
            )
          }
        }
        row {
          cell {
            fileIcon()
            checkBox(
              "File Icons",
              themeSettingsModel.isMaterialFiles,
              actionListener = { _, component ->
                themeSettingsModel.isMaterialFiles = component.isSelected
              }
            )
          }
        }
        row {
          cell {
            psiIcon()
            checkBox(
              "PSI Icons",
              themeSettingsModel.isMaterialPSIIcons,
              actionListener = { _, component ->
                themeSettingsModel.isMaterialPSIIcons = component.isSelected
              }
            )
          }
        }
      }
    }
  }

  @Suppress("LongMethod")
  private fun createSettingsPane(): DialogPanel {
    val themeComboBox = ComboBox(
      DefaultComboBoxModel(
        Vector(
          ThemeManager.instance.allThemes
            .groupBy { it.groupName }
            .entries
            .flatMap { it.value.sortedBy { theme -> theme.name } }
            .map { it.name }
        )
      )
    )
    themeComboBox.model.selectedItem = themeSettingsModel.currentTheme
    themeComboBox.addActionListener {
      themeSettingsModel.currentTheme = themeComboBox.model.selectedItem as String
    }
    val settingsPane = panel {
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
            "Theme Name in Status Bar",
            themeSettingsModel.showThemeStatusBar,
            actionListener = { _, component ->
              themeSettingsModel.showThemeStatusBar = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Themed Title Bar",
            themeSettingsModel.isThemedTitleBar,
            comment = "Feature only works on MacOS and Jetbrains Products",
            actionListener = { _, component ->
              themeSettingsModel.isThemedTitleBar = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Theme Transition Animation",
            themeSettingsModel.isLafAnimation,
            comment =
              """The animations will remain in your IDE after uninstalling the plugin.
            |To remove them, un-check this action or toggle the action at 
            |"Help -> Find Action -> ide.intellij.laf.enable.animation". 
            """.trimMargin(),
            actionListener = { _, component ->
              themeSettingsModel.isLafAnimation = component.isSelected
            }
          )
        }
        row {
          checkBox(
            "Don't show README on project startup",
            themeSettingsModel.isNotShowReadmeAtStartup,
            comment =
              """Anytime you open a new project, don't automatically open the README.
              |That way you can admire your theme's background art instead!
            |This will stay even after you uninstall the plugin.
|To re-enable it, un-check this action or toggle the action at "Help -> Find Action -> ide.open.readme.md.on.startup". 
            """.trimMargin(),
            actionListener = { _, component ->
              themeSettingsModel.isNotShowReadmeAtStartup = component.isSelected
            }
          )
        }
      }
      titledRow("Miscellaneous Items") {
        row {
          cell {
            button("View Issues") {
              browse(ISSUES_URI)
            }
            button("View Changelog") {
              browse(CHANGELOG_URI)
            }
            button("Leave a Review") {
              browse(REVIEW_URI)
            }
          }
        }
      }
    }
    return settingsPane
  }
}
