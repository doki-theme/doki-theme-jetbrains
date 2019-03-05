package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.lafs.MTDarkLaf
import com.chrisrm.ideaddlc.lafs.MTLightLaf
import com.chrisrm.ideaddlc.themes.models.MTSerializedTheme
import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.chrisrm.ideaddlc.utils.PropertiesParser
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
import icons.DDLCIcons
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.legacy.Runner
import io.acari.DDLC.themes.light.MonikaTheme
import java.awt.Color
import java.io.Serializable
import java.util.stream.Stream
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.ColorUIResource

abstract class DokiDokiTheme(val ddlcThemeId: String,
                             val colorScheme: String,
                             val isDarkTheme: Boolean,
                             val clubMemberName: String
) : Serializable, MTThemeable, MTSerializedTheme {

    init {

    }

    override fun getThemeId() = this.ddlcThemeId
    override fun getId() = this.ddlcThemeId
    override fun isDark() = this.isDarkTheme
    override fun getEditorColorsScheme(): String = this.colorScheme
    override fun getName() = this.clubMemberName
    override fun getIcon() = DDLCIcons.EXCLUDED

    override fun getHighlightColorString(): String {
        return "425B67"
    }

    override fun activate() {
        try {
            if (isDark) {
                LegacySupportUtility.invokeVoidMethodSafely(
                        LafManagerImpl::class.java,
                        "getTestInstance",
                        Runner { LafManagerImpl.getTestInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo()) },
                        Runner {
                            LafManager.getInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo())
                            UIManager.setLookAndFeel(MTDarkLaf(this))
                        }
                )

            } else {
                LegacySupportUtility.invokeVoidMethodSafely(
                        LafManagerImpl::class.java,
                        "getTestInstance",
                        Runner { LafManagerImpl.getTestInstance().setCurrentLookAndFeel(IntelliJLookAndFeelInfo()) },
                        Runner {
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


    override fun getDisabled(): String = disabledColorString

    override fun getDisabledColorString(): String {
        return "000000"
    }

    override fun getForegroundColorString(): String {
        return "546E7A"
    }

    override fun getTableSelectedColorString(): String {
        return "def7a5"
    }

    override fun getNonProjectFileScopeColor(): String {
        return MonikaTheme.NON_PROJECT_FILES
    }

    override fun isCustom(): Boolean = false

    override fun getBackgroundColor(): Color = ColorUIResource(backgroundColorResource)

    override fun getContrastColor(): Color = ColorUIResource(contrastColorResource)

    override fun getForegroundColor(): Color = foregroundColorResource

    override fun getSelectionBackgroundColor(): Color = secondaryBackgroundColorResource

    override fun getSelectionForegroundColor(): Color = selectionForegroundColorResource

    override fun getExcludedColor(): Color = excludedColorResource

    override fun getTintedIconColor(): Color = ColorUtil.fromHex(getAdjustedPrimaryColor())

    private fun getAdjustedPrimaryColor(): String = if (isDarkTheme) selectionForegroundColorString else selectionBackgroundColorString

    override fun getStopColor(): String {
        return "FFFFFF"
    }

    override fun getStartColor(): String {
        return "000000"
    }

    open fun getInactiveColorString(): String {
        return "FFF4F2"
    }

    override fun getTestScope(): String {
        return MonikaTheme.TEST_FILES
    }

    override fun getSecondBorderColorString(): String {
        return "d3e1e8"
    }

    open fun getTreeSelectionForegroundColorString(): String {
        return "FFFFFF"
    }


    override fun getSelectionForegroundColorString(): String {
        return "447152"
    }

    override fun getSelectionBackground(): String {
        return "FFFFFF"
    }

    open fun getMenuBarSelectionForegroundColorString(): String {
        return "7880a1"
    }

    open fun getMenuBarSelectionBackgroundColorString(): String {
        return "ffffff"
    }

    override fun getSelectionBackgroundColorString(): String {
        return "99eb99"
    }

    override fun getTextColorString(): String {
        return "4d6e80"
    }

    override fun getButtonColorString(): String {
        return "FFF4F2"
    }

    override fun getTreeSelectionColorString(): String {
        return "546e50"
    }

    open fun getTreeSelectionBackgroundResources(): Stream<String> {
        return Stream.of("Tree.selectionBackground")
    }

    open fun getTreeSelectionForegroundResources(): Stream<String> {
        return Stream.of("Tree.selectionForeground")
    }

    open fun getHighlightResources(): Stream<String> {
        return Stream.of(
                "Focus.color",
                "TextField.separatorColor",
                "SearchEverywhere.Tab.active.background",
                "SearchEverywhere.Tab.selected.background",
                "CheckBox.darcula.inactiveFillColor",
                "ActionButton.pressedBackground",
                "ActionButton.pressedBorderColor",
                "Autocomplete.selectionUnfocus",
                "CheckBox.darcula.inactiveFillColor",
                "CompletionPopup.selectionInactiveBackground",
                "Component.focusedBorderColor",
                "DebuggerTabs.active.background",
                "Focus.color",
                "Github.List.tallRow.selectionBackground.unfocused",
                "MemoryIndicator.usedColor",
                "Outline.focusedColor",
                "Plugins.Button.installFocusedBackground",
                "Plugins.eapTagBackground",
                "Plugins.tagBackground",
                "ProgressBar.halfColor",
                "ProgressBar.selectionBackground",
                "SearchEverywhere.Tab.active.background",
                "SearchEverywhere.Tab.selectedBackground",
                "SearchEverywhere.Tab.selected.background",
                "SpeedSearch.background",
                "TabbedPane.contentAreaColor",
                "TabbedPane.hoverColor",
                "TabbedPane.selectHighlight",
                "TabbedPane.selectedColor",
                "TabbedPane.underlineColor",
                "TableHeader.borderColor",
                "TextField.separatorColor",
                "VersionControl.Ref.backgroundBase"
        ).distinct()
    }

    open fun getSecondBorderResources(): Stream<String> {
        return Stream.of(
                "TabbedPane.highlight",
                "TabbedPane.selected",
                "SearchEverywhere.List.Separator.Color",
                "SearchEverywhere.List.Separator.foreground",
                "SearchEverywhere.List.separatorColor",
                "Borders.color",
                "Button.darcula.disabledOutlineColor",
                "Button.darcula.shadowColor",
                "Button.disabledBorderColor",
                "Button.shadowColor",
                "HelpTooltip.borderColor",
                "Menu.separatorColor",
                "OnePixelDivider.background",
                "Plugins.SearchField.borderColor",
                "Popup.Separator.color",
                "SearchEverywhere.List.Separator.Color",
                "SearchEverywhere.List.Separator.foreground",
                "SearchEverywhere.List.separatorColor",
                "SpeedSearch.borderColor",
                "TabbedPane.darkShadow",
                "TabbedPane.highlight",
                "TabbedPane.shadow",
                "WelcomeScreen.separatorColor",
                "windowBorder",
                "TabbedPane.selectHighlight").distinct()
    }

    open fun getTableSelectedResources(): Stream<String> {
        return Stream.of(
                "ProgressBar.halfColor",
                "MemoryIndicator.unusedColor",
                "Button.darcula.defaultFocusedBorderColor",
                "Button.darcula.focusedBorderColor",
                "Button.darcula.selection.color1",
                "Button.darcula.selection.color2",
                "Button.mt.selection.color1",
                "Button.mt.selection.color2",
                "ComboBox.selectionBackground",
                "EditorTabs.active.background",
                "FormattedTextField.selectionBackground",
                "ParameterInfo.borderColor",
                "PasswordField.selectionBackground",
                "Plugins.selectionBackground",
                "Plugins.Tab.active.background",
                "Plugins.Tab.hover.background",
                "TabbedPane.focusColor",
                "Table.focusCellBackground",
                "Table.selectionBackground",
                "TextArea.selectionBackground",
                "TextField.selectionBackground",
                "TextPane.selectionBackground",
                "ToolWindow.Button.hoverBackground"
        ).distinct()
    }

    open fun getContrastResources(): Stream<String> {
        return Stream.of(
                "Table.stripedBackground",
                "Table.stripeColor",
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

    open fun getDisabledResources(): Stream<String> {
        return Stream.of(
                "MenuItem.disabledForeground",
                "ToggleButton.off.background",
                "ComboBox.disabledForeground")
    }

    open fun getSecondaryBackgroundResources(): Stream<String> {
        return Stream.of(
                "Separator.foreground",
                "ToggleButton.off.foreground",
                "ToggleButton.on.foreground",
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

    open fun getInactiveResources(): Stream<String> {
        return Stream.of(
                "Table.gridColor",
                "MenuBar.darcula.borderColor",
                "MenuBar.darcula.borderShadowColor",
                "List.selectionInactiveBackground",
                "Tree.selectionInactiveBackground",
                "Table.selectionInactiveBackground",
                "CheckBox.darcula.disabledBorderColor1",
                "CheckBox.darcula.disabledBorderColor2")
    }

    open fun getSelectionForegroundResources(): Stream<String> {
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
                "PasswordField.selectionForeground",
                "TextField.selectionForeground",
                "TextArea.selectionForeground"
        )
    }

    open fun getSelectionBackgroundResources(): Stream<String> {
        return Stream.of(
                "inactiveCaption",
                "List.selectionBackground",
                "Table.selectionBackground",
                "Table.selectionBackground",
                "TextField.selectionBackground",
                "PasswordField.selectionBackground",
                "Button.mt.selectedBackground",
                "TextArea.selectionBackground",
                "SearchEverywhere.Tab.selected.background",
                "Label.selectedBackground",
                "Button.darcula.selectedButtonBackground",
                "Button.select",
                "PasswordField.selectionBackground",
                "TextField.selectionBackground",
                "TextArea.selectionBackground",
                "TabbedPane.selected")
    }

    open fun getTextResources(): Stream<String> {
        return Stream.of(
                "Menu.acceleratorForeground",
                "MenuItem.acceleratorForeground",
                "TextField.inactiveForeground",
                "material.tagColor",
                "material.primaryColor",
                "SearchEverywhere.shortcutForeground",
                "Tree.foreground")
    }

    open fun getBackgroundResources(): Stream<String> {
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
                "SearchEverywhere.background",
                "SearchEverywhere.Dialog.background",
                "SearchEverywhere.Header.background",
                "SearchEverywhere.SearchField.Border.color",
                "SearchEverywhere.SearchField.borderColor",
                "SearchEverywhere.Advertiser.background",
                "SearchEverywhere.Advertiser.foreground",
                "SearchEverywhere.Header.background",
                "SearchEverywhere.List.Separator.foreground",
                "SearchEverywhere.List.separatorColor",
                "SearchEverywhere.SearchField.background",
                "SearchEverywhere.SearchField.borderColor",
                "SearchEverywhere.SearchField.grayForeground",
                "ToolWindow.header.background",
                "material.background"


        ).distinct()
    }

    open fun getButtonBackgroundResources(): Stream<String> {
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

    open fun getButtonForegroundResources(): Stream<String> {
        return Stream.of(
                "Button.foreground",
                "Button.mt.foreground",
                "Button.darcula.selectedButtonForeground",
                "Button.mt.selectedButtonForeground",
                "ToolWindow.header.closeButton.foreground")
    }


    open fun getBorderColor(): Color {
        return Color.getHSBColor(62f, 91f, 149f)
    }

    open fun getForegroundResources(): Stream<String> {
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

    open fun getMenuItemSelectionForegroundResources(): Stream<String> {
        return Stream.of("MenuItem.selectionForeground",
                "Menu.acceleratorSelectionForeground",
                "Menu.selectionForeground",
                "MenuItem.acceleratorSelectionForeground")
    }

    open fun getMenuItemSelectionBackgroundResources(): Stream<String> {
        return Stream.of(
                "Menu.selectionBackground",
                "Menu.acceleratorSelectionBackground",
                "MenuItem.acceleratorSelectionBackground",
                "MenuItem.selectionBackground")
    }

    open fun getNotificationsResources(): Stream<String> {
        return Stream.of("Notifications.background",
                "Notifications.borderColor",
                "Notification.background",
                "Notification.borderColor",
                "Notifications.background",
                "Notifications.borderColor",
                "ValidationTooltip.errorBackground",
                "ValidationTooltip.errorBackgroundColor",
                "ValidationTooltip.errorBorderColor",
                "ValidationTooltip.warningBackground",
                "ValidationTooltip.warningBackgroundColor",
                "ValidationTooltip.warningBorderColor"
        ).distinct()
    }

    open fun getButtonBackgroundColor(): String {
        return "fbffeb"
    }


    abstract fun getTreeSelectionBackgroundColorString(): String

    open fun getSecondaryForegroundResources(): Stream<String> {
        return Stream.of("ToolWindow.header.active.foreground",
                "ToolWindow.header.border.foreground",
                "ToolWindow.Button.selectedForeground",
                "List.foreground")
    }

    open fun getSecondaryForegroundColorString(): String {
        return "256f25"
    }

    open fun getEditorTabColorString(): String {
        return "def7a5"
    }


    open fun getButtonForegroundColor(): String {
        return "C700A5"
    }


    open fun getMenuItemForegroundResources(): Stream<String> {
        return Stream.of(
                "Menu.foreground",
                "MenuItem.foreground",
                "PopupMenu.foreground")
    }

    open fun getMenuItemForegroundColor(): String {
        return "FFFFFF"
    }

    open fun getInactiveColor() = getInactiveColorString()


    override fun getBackgroundColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getForegroundColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getTextColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getSelectionBackgroundColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getSelectionForegroundColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getButtonColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getSecondaryBackgroundColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getDisabledColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getContrastColorResource(): ColorUIResource = this.backgroundColorResource

    override fun getTableSelectedColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getSecondBorderColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getHighlightColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getTreeSelectionColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getNotificationsColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getAccentColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }

    override fun getExcludedColorResource(): ColorUIResource {
        return ColorUIResource(0xFfffff)
    }


    /**
     * Get the hex code for the notifications color
     */
    abstract override fun getNotificationsColorString(): String

    private fun buildNotificationsColors() {
        val errorColor = JBColor(ColorUIResource(0xef5350), ColorUIResource(0x4E0C05))
        UIManager.put("Notification.ToolWindowError.background", errorColor)
        UIManager.put("Notification.ToolWindowError.borderColor", errorColor)

        val warnColor = JBColor(ColorUIResource(0xFFD54F), ColorUIResource(0x4E3C0D))
        UIManager.put("Notification.ToolWindowWarning.background", warnColor)
        UIManager.put("Notification.ToolWindowWarning.borderColor", warnColor)

        val infoColor = JBColor(ColorUIResource(0x66BB6A), ColorUIResource(0x113215))
        UIManager.put("Notification.ToolWindowInfo.borderColor", infoColor)
        UIManager.put("Notification.ToolWindowInfo.background", infoColor)
    }

    private fun buildResources(resources: Stream<String>, color: String) {
        val o1 = PropertiesParser.parseColor(color)
        resources.forEach { resource -> UIManager.getDefaults()[resource] = o1 }
    }

}