package io.unthrottled.doki.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.IconManager
import com.intellij.ui.layout.panel
import io.unthrottled.doki.config.THEME_CONFIG_TOPIC
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.BackgroundActor
import io.unthrottled.doki.settings.actors.EmptyFrameBackgroundActor
import io.unthrottled.doki.settings.actors.LafAnimationActor
import io.unthrottled.doki.settings.actors.MaterialIconsActor
import io.unthrottled.doki.settings.actors.MoveableStickerActor
import io.unthrottled.doki.settings.actors.ShowReadmeActor
import io.unthrottled.doki.settings.actors.StickerActor
import io.unthrottled.doki.settings.actors.ThemeActor
import io.unthrottled.doki.settings.actors.ThemeStatusBarActor
import io.unthrottled.doki.settings.actors.ThemedTitleBarActor
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.CustomStickerService
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.themes.ThemeManager
import java.net.URI
import java.util.Vector
import javax.swing.DefaultComboBoxModel
import javax.swing.JLabel

data class ThemeSettingsModel(
  var areStickersEnabled: Boolean,
  var isLafAnimation: Boolean,
  var currentTheme: String,
  var isThemedTitleBar: Boolean,
  var showThemeStatusBar: Boolean,
  var currentSticker: CurrentSticker,
  var isMaterialDirectories: Boolean,
  var isMaterialFiles: Boolean,
  var isMaterialPSIIcons: Boolean,
  var isNotShowReadmeAtStartup: Boolean,
  var isMoveableStickers: Boolean,
  var isDokiBackground: Boolean,
  var isEmptyFrameBackground: Boolean,
  var isCustomSticker: Boolean,
  var customStickerPath: String,
) {

  fun duplicate(): ThemeSettingsModel = copy()
}

object ThemeSettings {

  const val SETTINGS_ID = "io.unthrottled.doki.settings.ThemeSettings"
  const val THEME_SETTINGS_DISPLAY_NAME = "Doki Theme Settings"
  val CHANGELOG_URI =
    URI("https://github.com/doki-theme/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md")
  private const val REPOSITORY = "https://github.com/doki-theme/doki-theme-jetbrains"
  const val ULTIMATE_INSTRUCTIONS = "$REPOSITORY/wiki/Ultimate-Theme-Setup"
  val ISSUES_URI = URI("$REPOSITORY/issues")
  val REVIEW_URI = URI("https://plugins.jetbrains.com/plugin/10804-the-doki-theme/reviews")

  @JvmStatic
  fun createThemeSettingsModel(): ThemeSettingsModel =
    ThemeSettingsModel(
      areStickersEnabled = ThemeConfig.instance.currentStickerLevel == StickerLevel.ON,
      isLafAnimation = ThemeConfig.instance.isLafAnimation,
      currentTheme = ThemeManager.instance.currentTheme.map { it.name }.orElseGet { ThemeManager.DEFAULT_THEME_NAME },
      isThemedTitleBar = ThemeConfig.instance.isThemedTitleBar,
      showThemeStatusBar = ThemeConfig.instance.showThemeStatusBar,
      currentSticker = ThemeConfig.instance.currentSticker,
      isMaterialDirectories = ThemeConfig.instance.isMaterialDirectories,
      isMaterialFiles = ThemeConfig.instance.isMaterialFiles,
      isMaterialPSIIcons = ThemeConfig.instance.isMaterialPSIIcons,
      isNotShowReadmeAtStartup = ThemeConfig.instance.isNotShowReadmeAtStartup,
      isMoveableStickers = ThemeConfig.instance.isMoveableStickers,
      isDokiBackground = ThemeConfig.instance.isDokiBackground,
      isEmptyFrameBackground = ThemeConfig.instance.isEmptyFrameBackground,
      isCustomSticker = ThemeConfig.instance.isCustomSticker,
      customStickerPath = CustomStickerService.getCustomStickerPath().orElse("")
    )

  fun apply(themeSettingsModel: ThemeSettingsModel) {
    LafAnimationActor.enableAnimation(themeSettingsModel.isLafAnimation)
    ShowReadmeActor.dontShowReadmeOnStartup(themeSettingsModel.isNotShowReadmeAtStartup)
    StickerActor.enableStickers(themeSettingsModel.areStickersEnabled, false)
    StickerActor.swapStickers(themeSettingsModel.currentSticker, false)
    StickerActor.setCustomSticker(
      themeSettingsModel.customStickerPath,
      themeSettingsModel.isCustomSticker,
      false
    )
    ThemedTitleBarActor.enableThemedTitleBar(themeSettingsModel.isThemedTitleBar)
    ThemeActor.applyTheme(themeSettingsModel.currentTheme)
    ThemeStatusBarActor.applyConfig(themeSettingsModel.showThemeStatusBar)
    MaterialIconsActor.enableDirectoryIcons(themeSettingsModel.isMaterialDirectories)
    MaterialIconsActor.enableFileIcons(themeSettingsModel.isMaterialFiles)
    MaterialIconsActor.enablePSIIcons(themeSettingsModel.isMaterialPSIIcons)
    MoveableStickerActor.moveableStickers(themeSettingsModel.isMoveableStickers)
    BackgroundActor.handleBackgroundUpdate(themeSettingsModel.isDokiBackground)
    EmptyFrameBackgroundActor.handleBackgroundUpdate(themeSettingsModel.isEmptyFrameBackground)
    ApplicationManager.getApplication().messageBus.syncPublisher(
      THEME_CONFIG_TOPIC
    ).themeConfigUpdated(ThemeConfig.instance)
  }

  fun createMaterialIconsPane(settingsSupplier: () -> ThemeSettingsModel): DialogPanel {
    val directoryIcon = JLabel()
    directoryIcon.icon = IconManager.getInstance().getIcon("icons/doki/settings/directoryIcon.png", javaClass)
    val fileIcon = JLabel()
    fileIcon.icon = IconManager.getInstance().getIcon("icons/doki/settings/fileIcon.png", javaClass)
    val psiIcon = JLabel()
    psiIcon.icon = IconManager.getInstance().getIcon("icons/doki/settings/psiIcon.png", javaClass)
    return panel {
      titledRow("Doki Themed Material Icons") {
        row {
          cell {
            directoryIcon()
            checkBox(
              "Directory Icons",
              settingsSupplier().isMaterialDirectories,
              actionListener = { _, component ->
                settingsSupplier().isMaterialDirectories = component.isSelected
              }
            )
          }
        }
        row {
          cell {
            fileIcon()
            checkBox(
              "File Icons",
              settingsSupplier().isMaterialFiles,
              actionListener = { _, component ->
                settingsSupplier().isMaterialFiles = component.isSelected
              }
            )
          }
        }
        row {
          cell {
            psiIcon()
            checkBox(
              "PSI Icons",
              settingsSupplier().isMaterialPSIIcons,
              actionListener = { _, component ->
                settingsSupplier().isMaterialPSIIcons = component.isSelected
              }
            )
          }
        }
      }
    }
  }

  fun createThemeComboBoxModel(settingsSupplier: () -> ThemeSettingsModel): ComboBox<String> {
    val themeComboBox = ComboBox(
      DefaultComboBoxModel(
        Vector(
          ThemeManager.instance.allThemes
            .sortedBy { theme -> theme.name }
            .map { it.name }
        )
      )
    )
    themeComboBox.model.selectedItem = settingsSupplier().currentTheme
    themeComboBox.addActionListener {
      settingsSupplier().currentTheme = themeComboBox.model.selectedItem as String
    }
    return themeComboBox
  }
}
