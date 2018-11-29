package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.laf.MTDarkLaf
import com.chrisrm.ideaddlc.laf.MTLightLaf
import com.chrisrm.ideaddlc.themes.models.MTSerializedTheme
import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.chrisrm.ideaddlc.utils.PropertiesParser
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.JBColor
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.legacy.Runner
import java.awt.Color
import java.io.Serializable
import java.util.stream.Stream
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.ColorUIResource

abstract class DokiDokiTheme(val ddlcThemeId: String,
                             val isDarkTheme: Boolean,
                             val colorScheme: String,
                             val clubMemberIcon: String,
                             val clubMemberName: String
): Serializable, MTThemeable, MTSerializedTheme {

  init {

  }

  override fun getThemeId() = this.ddlcThemeId
  override fun isDark() = this.isDarkTheme
  override fun getThemeColorScheme() = this.colorScheme
  override fun getThemeName() = this.clubMemberName
  override fun getThemeIcon() = this.clubMemberIcon

  override fun activate() {
    try {
      if (isDark) {
        LegacySupportUtility.invokeVoidMethodSafely(
            LafManagerImpl::class.java,
            "getTestInstance",
            Runner{ LafManagerImpl.getTestInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo()) },
            Runner{
              LafManager.getInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo())
              UIManager.setLookAndFeel(MTDarkLaf(this))
            }
        )

      } else {
        LegacySupportUtility.invokeVoidMethodSafely(
            LafManagerImpl::class.java,
            "getTestInstance",
            Runner{ LafManagerImpl.getTestInstance().setCurrentLookAndFeel(IntelliJLookAndFeelInfo()) },
            Runner{
              LafManager.getInstance().setCurrentLookAndFeel(IntelliJLookAndFeelInfo())
              UIManager.setLookAndFeel(MTLightLaf(this))
            }
        )

      }
      JBColor.setDark(isDark)
      IconLoader.setUseDarkIcons(isDark)
      buildResources(getBackgroundResources(), backgroundColorString)
      buildResources(getButtonBackgroundResources(), getButtonBackgroundColor())
      buildResources(getButtonForegroundResources(), getButtonForegroundColor())
      buildResources(getForegroundResources(), foregroundColorString)
      buildResources(getMenuItemForegroundResources(), getMenuItemForegroundColor())
      buildResources(getTextResources(), textColorString)
      buildResources(getSelectionBackgroundResources(), selectionBackgroundColorString)
      buildResources(getSelectionForegroundResources(), selectionForegroundColorString)

      buildResources(getInactiveResources(), getInactiveColorString())
      buildResources(getSecondaryBackgroundResources(), secondaryBackgroundColorString)
      buildResources(getSecondaryForegroundResources(), getSecondaryForegroundColorString())
      buildResources(getDisabledResources(), disabledColorString)
      buildResources(getContrastResources(), contrastColorString)
      buildResources(getTableSelectedResources(), tableSelectedColorString)
      buildResources(getSecondBorderResources(), secondBorderColorString)
      buildResources(getHighlightResources(), highlightColorString)

      buildResources(getMenuItemSelectionBackgroundResources(), getMenuBarSelectionBackgroundColorString())
      buildResources(getMenuItemSelectionForegroundResources(), getMenuBarSelectionForegroundColorString())

      buildResources(getTreeSelectionBackgroundResources(), getTreeSelectionBackgroundColorString())
      buildResources(getTreeSelectionForegroundResources(), getTreeSelectionForegroundColorString())
      buildResources(getNotificationsResources(), notificationsColorString)
      buildNotificationsColors()

      // Apply theme accent color if said so
      val instance = MTConfig.getInstance()
      if (instance.isOverrideAccentColor) {
        instance.accentColor = accentColor
        MTThemeManager.applyAccents(true)
      }

      if (isDark) {
        UIManager.setLookAndFeel(MTDarkLaf(this))
      } else {
        UIManager.setLookAndFeel(MTLightLaf(this))
      }
      JBColor.setDark(isDark)
      IconLoader.setUseDarkIcons(isDark)
    } catch (e: UnsupportedLookAndFeelException) {
      e.printStackTrace()
    }

  }

  override fun isCustom(): Boolean = false

  override fun getBackgroundColor(): Color = ColorUIResource(backgroundColorResource)

  override fun getContrastColor(): Color = ColorUIResource(contrastColorResource)

  override fun getForegroundColor(): Color = foregroundColorResource

  override fun getSelectionBackgroundColor(): Color = secondaryBackgroundColorResource

  override fun getSelectionForegroundColor(): Color = selectionForegroundColorResource

  override fun getExcludedColor(): Color = excludedColorResource

  override fun getPrimaryColor(): Color = textColorResource

  fun getTreeSelectionBackgroundResources(): Stream<String> {
    return Stream.of("Tree.selectionBackground")
  }

  fun getTreeSelectionForegroundResources(): Stream<String> {
    return Stream.of("Tree.selectionForeground")
  }

  fun getHighlightResources(): Stream<String> {
    return Stream.of(
            "Focus.color",
            "TextField.separatorColor",
            "CheckBox.darcula.inactiveFillColor")
  }

  fun getSecondBorderResources(): Stream<String> {
    return Stream.of(
            "TabbedPane.highlight",
            "TabbedPane.selected",
            "TabbedPane.selectHighlight")
  }

  fun getTableSelectedResources(): Stream<String> {
    return Stream.of(
            "ProgressBar.halfColor",
            "MemoryIndicator.unusedColor")
  }

  fun getContrastResources(): Stream<String> {
    return Stream.of(
            "Table.stripedBackground",
            "ScrollBar.thumb",
            "Table.focusCellBackground",
            "ToolWindow.header.tab.selected.background",
            "ToolWindow.header.tab.selected.active.background",
            "material.contrast",
            "ActionToolbar.background",
            "Toolbar.background",
            "material.tab.backgroundColor",
            "TabbedPane.mt.tab.background",
            "TabbedPane.background",
            "OptionPane.background",
            "TabbedPane.highlight",
            "TabbedPane.darkShadow",
            "TabbedPane.shadow",
            "TabbedPane.borderColor",
            "Popup.Header.inactiveBackground",
            "Popup.Header.activeBackground")
  }

  fun getDisabledResources(): Stream<String> {
    return Stream.of(
            "MenuItem.disabledForeground",
            "ComboBox.disabledForeground")
  }

  fun getSecondaryBackgroundResources(): Stream<String> {
    return Stream.of(
            "Separator.foreground",
            "TextField.separatorColorDisabled",
            "PasswordField.inactiveForeground",
            "Button.darcula.selection.color1",
            "Button.darcula.selection.color2",
            "Button.mt.selection.color1",
            "List.background",
            "ToolWindow.header.active.background",
            "ToolWindow.header.border.background",
            "material.disabled",
            "material.mergeCommits")
  }

  fun getInactiveResources(): Stream<String> {
    return Stream.of(
            "Table.gridColor",
            "MenuBar.darcula.borderColor",
            "MenuBar.darcula.borderShadowColor",
            "CheckBox.darcula.disabledBorderColor1",
            "CheckBox.darcula.disabledBorderColor2")
  }

  fun getSelectionForegroundResources(): Stream<String> {
    return Stream.of(
            "Menu.selectionForeground",
            "Menu.acceleratorSelectionForeground",
            "MenuItem.selectionForeground",
            "MenuItem.acceleratorSelectionForeground",
            "Table.selectionForeground",
            "TextField.selectionForeground",
            "PasswordField.selectionForeground",
            "TextArea.selectionForeground",
            "Label.selectedForeground",
            "Button.darcula.selectedButtonForeground",
            "Button.darcula.selectedButtonForeground",
            "PasswordField.selectionForeground",
            "TextField.selectionForeground",
            "TextArea.selectionForeground"
    )
  }

  fun getSelectionBackgroundResources(): Stream<String> {
    return Stream.of(
            "inactiveCaption",
            "Menu.selectionBackground",
            "Menu.acceleratorSelectionBackground",
            "MenuItem.selectionBackground",
            "MenuItem.acceleratorSelectionBackground",
            "Table.selectionBackground",
            "TextField.selectionBackground",
            "PasswordField.selectionBackground",
            "Button.mt.selectedBackground",
            "TextArea.selectionBackground",
            "SearchEverywhere.Tab.selected.background",
            "Label.selectedBackground",
            "Button.darcula.selectedButtonBackground",
            "PasswordField.selectionBackground",
            "TextField.selectionBackground",
            "TextArea.selectionBackground",
            "TabbedPane.selected")
  }

  fun getTextResources(): Stream<String> {
    return Stream.of(
            "Menu.acceleratorForeground",
            "MenuItem.acceleratorForeground",
            "TextField.inactiveForeground",
            "material.tagColor",
            "material.primaryColor",
            "SearchEverywhere.shortcutForeground",
            "Tree.foreground")
  }

  fun getBackgroundResources(): Stream<String> {
    return Stream.of(
            "monika.background",
            "monika.textBackground",
            "monika.inactiveBackground",
            "window",
            "activeCaption",
            "control",
            "PopupMenu.translucentBackground",
            "EditorPane.inactiveBackground",
            "Table.background",
            "MenuBar.disabledBackground",
            "MenuBar.shadow",
            "Desktop.background",
            "Separator.background",
            "MenuBar.background",
            "Separator.foreground",
            "TextField.background",
            "PasswordField.background",
            "FormattedTextField.background",
            "TextArea.background",
            "CheckBox.darcula.backgroundColor1",
            "CheckBox.darcula.backgroundColor2",
            "CheckBox.darcula.checkSignColor",
            "CheckBox.darcula.shadowColor",
            "CheckBox.darcula.shadowColorDisabled",
            "CheckBox.darcula.focusedArmed.backgroundColor1",
            "CheckBox.darcula.focusedArmed.backgroundColor2",
            "CheckBox.darcula.focused.backgroundColor1",
            "CheckBox.darcula.focused.backgroundColor2",
            "ComboBox.background",
            "ComboBox.disabledBackground",
            "RadioButton.darcula.selectionDisabledColor",
            "StatusBar.topColor",
            "StatusBar.top2Color",
            "StatusBar.bottomColor",
            "ToolTip.background",
            "Spinner.background",
            "SplitPane.highlight",
            "SearchEverywhere.background",
            "SidePanel.background",
            "DialogWrapper.southPanelDivider",
            "OnePixelDivider.background",
            "Dialog.titleColor",
            "SearchEverywhere.background",
            "RadioButton.background",
            "CheckBoxMenuItem.background",
            "RadioButtonMenuItem.background",
            "CheckBox.background",
            "ColorChooser.background",
            "Slider.background",
            "ToolWindow.header.background",
            "material.background")
  }

  fun getButtonBackgroundResources(): Stream<String> {
    return Stream.of(
            "Button.background",
            "Button.darcula.color1",
            "Button.darcula.color2",
            "Button.darcula.disabledText.shadow",
            "ToolWindow.header.closeButton.background",
            "Button.mt.color1",
            "Button.mt.background"
    )
  }

  fun getButtonForegroundResources(): Stream<String> {
    return Stream.of(
            "Button.foreground",
            "Button.mt.foreground",
            "Button.mt.selectedButtonForeground",
            "ToolWindow.header.closeButton.foreground")
  }


  fun getBorderColor(): Color {
    return Color.getHSBColor(62f, 91f, 149f)
  }

  fun getForegroundResources(): Stream<String> {
    return Stream.of(
            "monika.foreground",
            "monika.textForeground",
            "monika.selectionForegroundInactive",
            "monika.selectionInactiveForeground",
            "Label.foreground",
            "EditorPane.inactiveForeground",
            "CheckBox.foreground",
            "ComboBox.foreground",
            "RadioButton.foreground",
            "ColorChooser.foreground",
            "MenuBar.foreground",
            "RadioButtonMenuItem.foreground",
            "CheckBoxMenuItem.foreground",
            "PopupMenu.foreground",
            "Spinner.foreground",
            "TabbedPane.foreground",
            "TextField.foreground",
            "FormattedTextField.foreground",
            "PasswordField.foreground",
            "TextArea.foreground",
            "TextPane.foreground",
            "EditorPane.foreground",
            "ToolBar.foreground",
            "ToolTip.foreground",
            "List.foreground",
            "SearchEverywhere.foreground",
            "Label.foreground",
            "Label.disabledForeground",
            "Label.selectedDisabledForeground",
            "Table.foreground",
            "TableHeader.foreground",
            "ToggleButton.foreground",
            "Table.sortIconColor",
            "material.branchColor",
            "material.foreground",
            "TitledBorder.titleColor")
  }

  fun getMenuItemSelectionForegroundResources(): Stream<String> {
    return Stream.of("MenuItem.selectionForeground",
            "Menu.acceleratorSelectionForeground",
            "Menu.selectionForeground",
            "MenuItem.acceleratorSelectionForeground")
  }

  fun getMenuItemSelectionBackgroundResources(): Stream<String> {
    return Stream.of("Menu.selectionBackground",
            "Menu.acceleratorSelectionBackground",
            "MenuItem.acceleratorSelectionBackground",
            "MenuItem.selectionBackground")
  }

  fun getNotificationsResources(): Stream<String> {
    return Stream.of("Notifications.background",
            "Notifications.borderColor")
  }

  fun getButtonBackgroundColor(): String {
    return "fbffeb"
  }


  abstract fun getMenuBarSelectionForegroundColorString(): String

  abstract fun getMenuBarSelectionBackgroundColorString(): String

  abstract fun getInactiveColorString(): String

  abstract fun getTreeSelectionBackgroundColorString(): String

  abstract fun getTreeSelectionForegroundColorString(): String

  fun getSecondaryForegroundResources(): Stream<String> {
    return Stream.of("ToolWindow.header.active.foreground",
            "ToolWindow.header.border.foreground",
            "List.foreground")
  }

  fun getSecondaryForegroundColorString(): String {
    return "256f25"
  }


  fun getButtonForegroundColor(): String {
    return "C700A5"
  }


  fun getMenuItemForegroundResources(): Stream<String> {
    return Stream.of(
            "Menu.foreground",
            "MenuItem.foreground",
            "PopupMenu.foreground")
  }

  fun getMenuItemForegroundColor(): String {
    return "FFFFFF"
  }


  /**
   * Get the hex code for the notifications color
   */
  abstract override fun getNotificationsColorString(): String

  private fun buildNotificationsColors() {
    val errorColor = JBColor(ColorUIResource(0xef5350), ColorUIResource(0xb71c1c))
    UIManager.put("Notification.ToolWindowError.background", errorColor)
    UIManager.put("Notification.ToolWindowError.borderColor", errorColor)

    val warnColor = JBColor(ColorUIResource(0xFFD54F), ColorUIResource(0x5D4037))
    UIManager.put("Notification.ToolWindowWarning.background", warnColor)
    UIManager.put("Notification.ToolWindowWarning.borderColor", warnColor)

    val infoColor = JBColor(ColorUIResource(0x66BB6A), ColorUIResource(0x1B5E20))
    UIManager.put("Notification.ToolWindowInfo.borderColor", infoColor)
    UIManager.put("Notification.ToolWindowInfo.background", infoColor)
  }

  private fun buildResources(resources: Stream<String>, color: String) {
    val o1 = PropertiesParser.parseColor(color)
    resources.forEach { resource -> UIManager.getDefaults()[resource] = o1 }
  }

}