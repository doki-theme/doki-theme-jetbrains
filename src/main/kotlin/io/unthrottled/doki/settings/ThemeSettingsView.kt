package io.unthrottled.doki.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.FontComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.not
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.stickers.CurrentSticker
import org.jetbrains.annotations.NonNls
import javax.swing.JComponent

class ThemeSettingsView(private val model: ThemeSettingsModel = ThemeSettings.createThemeSettingsModel()) :
  SearchableConfigurable, Configurable.NoMargin, DumbAware {
  private lateinit var discreetCheckBox: Cell<JBCheckBox>
  private lateinit var hideCheckbox: Cell<JBCheckBox>
  private lateinit var dimensionCheckbox: Cell<JBCheckBox>
  private lateinit var smallStickerCheckBox: Cell<JBCheckBox>
  private lateinit var generalPanel: DialogPanel
  private lateinit var stickerPanel: DialogPanel
  private lateinit var fontPanel: DialogPanel
  override fun getDisplayName(): @NlsContexts.ConfigurableName String = ThemeSettings.THEME_SETTINGS_DISPLAY_NAME
  override fun getId(): @NonNls String = ThemeSettings.SETTINGS_ID

  private fun createGeneralPanel(): DialogPanel {
    return panel {
      row {
        discreetCheckBox =
          checkBox(MessageBundle.message("settings.checkbox.discreet.mode")).bindSelected(model::discreetMode)
        checkBox(MessageBundle.message("settings.checkbox.theme.name.in.statusbar"))
          .bindSelected(model::showThemeStatusBar)
          .enabledIf(discreetCheckBox.selected.not())
      }
      rowsRange {
        group(MessageBundle.message("settings.border.title.background.images")) {
          row {
            checkBox(MessageBundle.message("settings.checkbox.background.wallpaper")).bindSelected(model::isDokiBackground)
              .comment(MessageBundle.message("settings.text.icon.src.allicons.general.warning.nbsp.wallpaper.will.remain.after.removing.plugin.if.left.enabled"))
            checkBox(MessageBundle.message("settings.checkbox.empty.editor.background")).bindSelected(model::isEmptyFrameBackground)
          }
        }
        group(MessageBundle.message("settings.border.title.notifications")) {
          row {
            slider(0, 100, 1, 10).label(
              MessageBundle.message("settings.label.notifications.opacity"),
              LabelPosition.TOP
            ).resizableColumn()
              .bindValue(model::notificationOpacity)
            checkBox(MessageBundle.message("settings.checkbox.make.transparent")).bindSelected(model::isSeeThroughNotifications)
          }
        }
      }.enabledIf(discreetCheckBox.selected.not())
    }
  }

  private fun createStickerPanel(): DialogPanel {
    return panel {
      rowsRange {
        group(MessageBundle.message("settings.sticker.title.general")) {
          row {
            checkBox(MessageBundle.message("settings.checkbox.show.sticker")).bindSelected(model::areStickersEnabled)
            checkBox(MessageBundle.message("settings.checkbox.ignore.scaling")).bindSelected(model::ignoreScaling)
          }
        }
        group(MessageBundle.message("settings.border.title.position")) {
          row {
            comment(MessageBundle.message("settings.text.icon.src.allicons.actions.help.nbsp.double.click.stickers.to.save.current.position"))
          }
          row {
            checkBox(MessageBundle.message("settings.checkbox.allow.positioning")).bindSelected(model::isMoveableStickers)
          }
          row {
            button(MessageBundle.message("settings.button.reset.position")) {
              ThemeSettings.resetStickerPosition()
            }
          }
        }
        group(MessageBundle.message("settings.border.title.sticker.image")) {
          buttonsGroup(MessageBundle.message("settings.label.content.type")) {
            row {
              radioButton(MessageBundle.message("settings.radio.primary"), CurrentSticker.SECONDARY)
            }
            row {
              radioButton(MessageBundle.message("settings.radio.secondary"), CurrentSticker.DEFAULT)
            }
          }.bind(model::currentSticker)
          group("Custom") {
            row {
              val customStickerCheckBox =
                checkBox(MessageBundle.message("settings.checkbox.use.custom.sticker")).bindSelected(model::isCustomSticker)
              @Suppress("UnstableApiUsage")
              textFieldWithBrowseButton(
                fileChooserDescriptor = FileChooserDescriptorFactory.singleFile(),
                project = null
              ) { virtualFile ->
                virtualFile.path
              }
                .bindText(model::customStickerPath)
                .resizableColumn()
                .enabledIf(customStickerCheckBox.selected)
            }
          }
        }
        group(MessageBundle.message("settings.border.title.visibility")) {
          row {
            hideCheckbox =
              checkBox(MessageBundle.message("settings.checkbox.hide.on.hover")).bindSelected(model::hideOnHover)
          }
          row {
            spinner(10..Integer.MAX_VALUE, 1).bindIntValue(model::hideDelayMS)
              .label(MessageBundle.message("settings.label.hide.delay.ms"))
          }.enabledIf(hideCheckbox.selected)
        }
        group(MessageBundle.message("settings.border.title.primary.sticker")) {
          row {
            dimensionCheckbox =
              checkBox(MessageBundle.message("settings.checkbox.enable.dimension.capping")).bindSelected(model::capStickerDimensions)
          }
          rowsRange {
            row {
              spinner(-1..Int.MAX_VALUE, 1).bindIntValue(model::maxStickerWidth)
                .label(MessageBundle.message("settings.label.max.width.px"))
            }
            row {
              spinner(-1..Int.MAX_VALUE, 1).bindIntValue(model::maxStickerHeight)
                .label(MessageBundle.message("settings.label.max.height.px"))
            }
          }.enabledIf(dimensionCheckbox.selected)
        }
        group(MessageBundle.message("settings.border.title.small.stickers")) {
          row {
            smallStickerCheckBox =
              checkBox(MessageBundle.message("settings.checkbox.enable.small.stickers")).bindSelected(model::showSmallStickers)
          }
          rowsRange {
            row {
              spinner(-1..Int.MAX_VALUE, 1).label(MessageBundle.message("settings.label.max.width.px"))
                .bindIntValue(model::smallMaxStickerWidth)
            }
            row {
              spinner(-1..Int.MAX_VALUE, 1).label(MessageBundle.message("settings.label.max.height.px"))
                .bindIntValue(model::smallMaxStickerHeight)
            }
          }.enabledIf(smallStickerCheckBox.selected)
        }
      }.enabledIf(discreetCheckBox.selected.not())
    }
  }

  private fun createFontPanel(): DialogPanel {
    return panel {
      row {
        spinner(1..Int.MAX_VALUE, 1).label(MessageBundle.message("settings.label.global.editor.font.size"))
          .bindIntValue(model::customFontSizeValue)
        checkBox(MessageBundle.message("settings.checkbox.override.editor.font.size")).bindSelected(model::isCustomFontSize)
      }
      row {
        cell(FontComboBox()).bind(
          { combo -> combo.fontName as String },
          { combo, value -> combo.fontName = value },
          model::consoleFontValue.toMutableProperty()
        )
        checkBox(MessageBundle.message("settings.checkbox.override.console.font")).bindSelected(model::isOverrideConsoleFont)
      }
    }
  }

  private fun createTabs(): JBTabbedPane {
    val tabs = JBTabbedPane()
    generalPanel = createGeneralPanel()
    stickerPanel = createStickerPanel()
    fontPanel = createFontPanel()
    with(tabs) {
      this.addTab(MessageBundle.message("settings.tab.general"), generalPanel)
      this.addTab(MessageBundle.message("settings.tab.sticker"), stickerPanel)
      this.addTab(MessageBundle.message("settings.tab.font"), fontPanel)
    }
    return tabs
  }

  override fun createComponent(): JComponent {
    return panel {
      row {
        cell(createTabs())
      }
    }
  }

  override fun isModified(): Boolean =
    isPanelsInitialized() && (generalPanel.isModified() || stickerPanel.isModified() || fontPanel.isModified())

  private fun isPanelsInitialized(): Boolean =
    ::generalPanel.isInitialized && ::generalPanel.isInitialized && ::fontPanel.isInitialized

  override fun apply() {
    safelyExecute {
      generalPanel.apply()
      stickerPanel.apply()
      fontPanel.apply()
      ThemeSettings.apply(model)
    }
  }

  private fun safelyExecute(block: () -> Unit) {
    if (isPanelsInitialized()) {
      block()
    }
  }

  override fun reset() {
    safelyExecute {
      generalPanel.reset()
      stickerPanel.reset()
      fontPanel.reset()
    }
  }
}