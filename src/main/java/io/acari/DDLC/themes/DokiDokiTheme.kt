package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.lafs.MTDarkLaf
import com.chrisrm.ideaddlc.lafs.MTLightLaf
import com.chrisrm.ideaddlc.themes.models.MTSerializedTheme
import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.chrisrm.ideaddlc.utils.MTUI
import com.chrisrm.ideaddlc.utils.PropertiesParser
import com.google.common.collect.Sets
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
import icons.DDLCIcons
import io.acari.DDLC.themes.light.MonikaTheme
import java.awt.Color
import java.io.Serializable
import java.util.*
import java.util.stream.Stream
import javax.swing.Icon
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.ColorUIResource

abstract class DokiDokiTheme(private val ddlcThemeId: String,
                             private val colorScheme: String,
                             private val isDarkTheme: Boolean,
                             private val clubMemberName: String
) : Serializable, MTThemeable, MTSerializedTheme {

    init {

    }

    override fun getThemeId(): String = this.ddlcThemeId
    override fun getId(): String = this.ddlcThemeId
    override fun isDark(): Boolean = this.isDarkTheme
    override fun getEditorColorsScheme(): String = this.colorScheme
    override fun getName(): String = this.clubMemberName
    override fun getIcon(): Icon = IconLoader.getIcon(iconPath())

    open fun iconPath(): String =
        "/icons/ddlc/ddlcTheme.svg"

    override fun getHighlightColorString(): String {
        return "425B67"
    }

    override fun activate() {
        try {
            JBColor.setDark(isDark)
            IconLoader.setUseDarkIcons(isDark)
            buildResources(getBackgroundResources(), backgroundColorString)
            buildResources(getButtonBackgroundResources(), getButtonBackgroundColor())
            buildResources(getButtonForegroundResources(), getButtonForegroundColor())
            buildResources(getSelectedButtonForegroundResources(), getSelectedButtonForegroundColor())
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
            buildResources(getCompletionBackgroundResources(), completionPopupBackgroundColor)
            buildResources(getTableSelectedResources(), tableSelectedColorString)
            buildResources(getSecondBorderResources(), secondBorderColorString)
            buildResources(getHighlightResources(), highlightColorString)

            buildResources(getMenuItemSelectionBackgroundResources(), getMenuBarSelectionBackgroundColorString())
            buildResources(getMenuItemSelectionForegroundResources(), getMenuBarSelectionForegroundColorString())

            buildResources(getTreeSelectionBackgroundResources(), treeSelectionBackgroundColorString)
            buildResources(getTreeSelectionForegroundResources(), getTreeSelectionForegroundColorString())
            buildResources(getNotificationsResources(), notificationsColorString)
            buildResources(getBorderResources(), borderColorString)
            buildResources(getMenuBarResources(), menuBarColorString)
            buildNotificationsColors()
            buildFlameChartColors()
            buildTransparentColors()
            buildTreeSelectionInactiveColors()

            MTThemeManager.applyAccents(true)

            applyOneOffs()

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
        return "A2B6CB"
    }

    override fun getBorderColorString(): String =
        contrastColorString

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
        return "b4fbae"
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
        return "b4fbae"
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
            "ActionButton.pressedBackground",
            "ActionButton.pressedBorderColor",
            "Autocomplete.selectionUnfocus",
            "CheckBox.darcula.inactiveFillColor",
            "CompletionPopup.selectionInactiveBackground", // todo: why is this here??
            "Component.borderColor",
            "Component.focusedBorderColor",
            "DebuggerTabs.active.background",
            "DebuggerTabs.selectedBackground",
            "DefaultTabs.hoverColor",
            "DefaultTabs.hoverMaskColor",
            "DebuggerTabs.underlinedTabBackground",
            "DefaultTabs.hoverBackground",
            "DefaultTabs.underlinedTabBackground",
            "EditorTabs.underlinedTabBackground",
            "EditorTabs.active.background", // deprecated
            "EditorTabs.selectedBackground",
            "EditorTabs.underlinedTabBackground",
            "EditorTabs.active.background", // deprecated
            "EditorTabs.selectedBackground",
            "EditorTabs.hoverColor",
            "EditorTabs.hoverMaskColor",
            "Focus.color",
            "Github.List.tallRow.selectionBackground.unfocused",
            "MemoryIndicator.usedBackground",
            "MemoryIndicator.usedColor",
            "Outline.focusedColor",
            "Plugins.Button.installFocusedBackground",
            "Plugins.eapTagBackground",
            "Plugins.tagBackground",
            "Plugins.Tab.hover.background",
            "ProgressBar.halfColor",
            "ProgressBar.selectionBackground",
            "ProgressBar.trackColor",
            "SearchEverywhere.Tab.active.background",
            "SearchEverywhere.Tab.selected.background",
            "SearchEverywhere.Tab.selectedBackground",
            "Slider.trackDisabled",
            "SpeedSearch.background",
            "TabbedPane.contentAreaColor",
            "TabbedPane.hoverColor",
            "TabbedPane.selectedColor",
            "TabbedPane.selectHighlight",
            "TableHeader.borderColor",
            "TextField.separatorColor",
            "TitlePane.Button.hoverBackground",
            "ToolWindow.HeaderTab.hoverBackground",
            "VersionControl.Log.Commit.currentBranchBackground",
            "VersionControl.Ref.backgroundBase",
            "VersionControl.RefLabel.backgroundBase"
        )
    }

    open fun getSecondBorderResources(): Stream<String> {
        return Stream.of(
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
            "SearchEverywhere.List.separatorColor",
            "SpeedSearch.borderColor",
            "TabbedPane.darkShadow",
            "TabbedPane.highlight",
            "TabbedPane.selected",
            "TabbedPane.selectHighlight",
            "TabbedPane.shadow",
            "Tree.hash",
            "WelcomeScreen.separatorColor",
            "windowBorder"
        ).distinct()
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
                "EditorTabs.selectedBackground",
                "FormattedTextField.selectionBackground",
                "ParameterInfo.borderColor",
                "PasswordField.selectionBackground",
                "Plugins.selectionBackground",
                "Plugins.Tab.active.background",
                "TabbedPane.focusColor",
                "Table.focusCellBackground",
                "Table.selectionBackground",
                "TextArea.selectionBackground",
                "TextPane.selectionBackground",
                "ToolWindow.Button.hoverBackground",
                "Button.default.startBackground",
                "Button.default.endBackground",
                "Button.focus", // deprecated
                "DebuggerTabs.underlinedTabBackground",
                "DefaultTabs.underlinedTabBackground",
                "EditorTabs.underlinedTabBackground",
                "ToolWindow.Button.hoverBackground"

        ).distinct()
    }

    open fun getCompletionBackgroundResources(): Stream<String> =
        Stream.of(
            "CompletionPopup.background"
        ).distinct()

    open fun getBorderResources(): Stream<String> =
        Stream.of("Borders.color",
            "Borders.ContrastBorderColor",
            "ComboPopup.border",
            "Component.borderColor",
            "DebuggerPopup.borderColor",
            "DefaultTabs.borderColor",
            "EditorTabs.borderColor",
            "HelpTooltip.borderColor",
            "InformationHint.borderColor",
            "InplaceRefactoringPopup.borderColor",
            "Menu.borderColor",
            "MenuBar.borderColor",
            "NavBar.borderColor",
            "Notification.borderColor",
            "Notification.errorBorderColor",
            "Notification.MoreButton.innerBorderColor",
            "Notification.ToolWindow.errorBorderColor",
            "Notification.ToolWindow.informativeBorderColor",
            "Notification.ToolWindow.warningBorderColor",
            "ParameterInfo.borderColor",
            "Plugins.Button.installBorderColor",
            "Plugins.Button.updateBorderColor",
            "Plugins.SearchField.borderColor",
            "Popup.Advertiser.borderColor",
            "Popup.Advertiser.borderInsets",
            "Popup.Border.color",
            "Popup.Border.inactiveColor",
            "Popup.borderColor",
            "Popup.inactiveBorderColor",
            "Popup.innerBorderColor",
            "Popup.paintBorder",
            "Popup.preferences.borderColor",
            "Popup.Toolbar.borderColor",
            "SearchEverywhere.Advertiser.borderInsets",
            "SearchEverywhere.SearchField.borderColor",
            "SpeedSearch.borderColor",
            "StatusBar.borderColor",
            "TableHeader.cellBorder",
            "ToggleButton.borderColor",
            "ToolWindow.Header.borderColor",
            "ValidationTooltip.errorBorderColor",
            "ValidationTooltip.warningBorderColor",
            "WelcomeScreen.borderColor",
            "WelcomeScreen.groupIconBorderColor",
            "Window.border").distinct()

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
                "NewClass.SearchField.background",
                "material.tab.backgroundColor",
                "TabbedPane.mt.tab.background",
                "TabbedPane.background",
                "OptionPane.background",
                "TabbedPane.highlight",
                "TabbedPane.darkShadow",
                "TabbedPane.shadow",
                "TabbedPane.borderColor",
                "Popup.Header.inactiveBackground",
                "Popup.Header.activeBackground",
                "ToolBar.background",
                "ToolWindow.Button.selectedBackground",
                "ToolWindow.header.tab.selected.active.background", // deprecated
                "ToolWindow.header.tab.selected.background", // deprecated
                "ToolWindow.HeaderTab.selectedInactiveBackground",
                "ToolWindow.HeaderTab.selectedBackground",
                "ToolWindow.inactive.HeaderTab.background", // deprecated
                "ToolWindow.active.HeaderTab.background", // deprecated
                "ToolWindow.HeaderTab.underlinedTabInactiveBackground",
                "WelcomeScreen.captionBackground",
                "WelcomeScreen.footerBackground"
        )
    }

    open fun getMenuBarResources(): Stream<String> =
        Stream.of(
            "Menu.background",
            "MenuItem.background",
            "MenuBar.background",
            "PopupMenu.background",
            "TitlePane.background"
        )

    open fun getDisabledResources(): Stream<String> {
        return Stream.of(
                "MenuItem.disabledForeground",
                "ToggleButton.off.background",
                "ComboBox.disabledForeground",
                "Button.disabledText",
                "CheckBox.darcula.checkSignColorDisabled", // deprecated
                "CheckBox.darcula.disabledBorderColor1", // deprecated
                "CheckBox.darcula.disabledBorderColor2", // deprecated
                "CheckBox.disabledText",
                "CheckBoxMenuItem.disabledForeground",
                "ComboBox.ArrowButton.disabledIconColor",
                "ComboBox.darcula.arrowButtonDisabledForeground", // deprecated
                "ComboBox.disabledForeground",
                "Component.disabledBorderColor",
                "EditorPane.inactiveForeground",
                "FormattedTextField.inactiveForeground",
                "Label.disabledForeground",
                "Label.disabledForegroundColor", // deprecated
                "Label.disabledShadow", // deprecated
                "Label.disabledText",
                "Menu.disabledForeground",
                "MenuBar.disabledForeground",
                "MenuItem.disabledForeground",
                "Outline.disabledColor", // deprecated
                "ParameterInfo.disabledColor", //deprecated
                "ParameterInfo.disabledForeground",
                "PasswordField.inactiveForeground",
                "Plugins.disabledForeground",
                "RadioButton.disabledText",
                "RadioButtonMenuItem.disabledForeground",
                "SearchEverywhere.SearchField.grayForeground", // deprecated
                "TabbedPane.disabledForeground",
                "TabbedPane.disabledText", // deprecated
                "TabbedPane.disabledUnderlineColor",
                "TabbedPane.selectedDisabledColor",
                "TextArea.inactiveForeground",
                "TextField.inactiveForeground",
                "TextPane.inactiveForeground",
                "TitlePane.inactiveInfoForeground",
                "ToggleButton.disabledText",
                "VersionControl.HgLog.closedBranchIconColor"
        ).distinct()
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
                "material.mergeCommits",
    //            "List.background", todo: dis
                "MemoryIndicator.allocatedBackground",
                "MemoryIndicator.unusedColor", // deprecated
                "ParameterInfo.background",
                "Plugins.SectionHeader.background",
                "Popup.separatorColor",
                "Slider.tickColor",
                "Table.lightSelectionInactiveBackground",

    //            todo: do I really want this here?
                "ToolWindow.active.Header.background",
                "ToolWindow.Header.background",
                "ToolWindow.header.active.background", //deprecated

                "WelcomeScreen.Projects.background",
                "WelcomeScreen.Projects.selectionInactiveBackground"
        ).distinct()
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
            "CompletionPopup.selectionForeground",
            "CompletionPopup.selectionInfoForeground",
            "Label.selectedForeground",
            "List.selectionForeground",
            "Menu.acceleratorSelectionForeground",
            "Menu.selectionForeground",
            "MenuItem.acceleratorSelectionForeground",
            "MenuItem.selectionForeground",
            "PasswordField.selectionForeground",
            "Plugins.selectionForeground", // deprecated
            "Plugins.Tab.active.foreground", // deprecated
            "Plugins.Tab.selectedForeground",
            "SearchEverywhere.Tab.selectedForeground",
            "TabbedPane.selectedForeground", // deprecated
            "Table.selectionForeground",
            "TextArea.selectionForeground",
            "TextField.selectionForeground"
        )
    }

    open fun getSelectionBackgroundResources(): Stream<String> {
        return Stream.of(
                "inactiveCaption",
                "List.selectionBackground",
                "Table.selectionBackground",
                "CompletionPopup.selectionBackground",
                "Plugins.Tab.selectedBackground",
                "PasswordField.selectionBackground",
                "Button.mt.selectedBackground",
                "TextArea.selectionBackground",
                "SearchEverywhere.Tab.selected.background",
                "Label.selectedBackground",
            "Plugins.selectionBackground", // deprecated
            "Plugins.lightSelectionBackground",
                "Button.darcula.selectedButtonBackground",
                "Button.select",
                "PasswordField.selectionBackground",
                "TextField.selectionBackground",
                "TextArea.selectionBackground",
                "TabbedPane.selected"
        )
    }

    open fun getTextResources(): Stream<String> {
        return Stream.of(
            "CompletionPopup.infoForeground",
            "CompletionPopup.selectionInactiveInfoForeground",
            "Label.infoForeground",
            "material.primaryColor",
            "material.tagColor",
            "Menu.acceleratorForeground",
            "MenuItem.acceleratorForeground",
            "SearchEverywhere.shortcutForeground",
            "TextField.inactiveForeground",
            "Tree.foreground"
        )
    }

    open fun getBackgroundResources(): Stream<String> {
        return Stream.of(
            "activeCaption",
            "CheckBox.background",
            "CheckBox.darcula.backgroundColor1",
            "CheckBox.darcula.backgroundColor2",
            "CheckBox.darcula.checkSignColor",
            "CheckBox.darcula.focused.backgroundColor1",
            "CheckBox.darcula.focused.backgroundColor2",
            "CheckBox.darcula.focusedArmed.backgroundColor1",
            "CheckBox.darcula.focusedArmed.backgroundColor2",
            "CheckBox.darcula.shadowColor",
            "CheckBox.darcula.shadowColorDisabled",
            "CheckBoxMenuItem.background",
            "ColorChooser.background",
            "ComboBox.background",
            "ComboBox.disabledBackground",

            "control",
            "DebuggerPopup.borderColor",
            "DefaultTabs.background",
            "DefaultTabs.borderColor",
            "Desktop.background",
            "Dialog.titleColor", // deprecated
            "DialogWrapper.southPanelBackground",
            "DialogWrapper.southPanelDivider",
            "DragAndDrop.backgroundBorderColor", // deprecated
            "DragAndDrop.backgroundColor", //deprecated
            "DragAndDrop.areaBackground",
            "Editor.background",
            "EditorPane.inactiveBackground",
            "EditorTabs.background",
            "EditorTabs.inactive.maskColor",
            "EditorTabs.inactiveColoredFileBackground",
            "EditorTabs.inactiveMaskColor",
            "FormattedTextField.background",
            "GutterTooltip.lineSeparatorColor",
            "HeaderColor.active", // deprecated
            "HelpTooltip.background",
            "HelpTooltip.backgroundColor", // deprecated
            "inactiveCaptionBorder",
            "intellijlaf.background", // deprecated
            "InplaceRefactoringPopup.borderColor",
            "InternalFrame.inactiveTitleBackground",
            "material.background",
            "MenuBar.disabledBackground",
            "MenuBar.shadow",
            "monika.background",
            "monika.inactiveBackground",
            "monika.textBackground",
            "NewClass.Panel.background",
            "OptionPane.background",
            "OnePixelDivider.background",
            "PasswordField.background",
            "Plugins.background",
            "Plugins.SearchField.background",
            "Popup.Advertiser.background",
            "Popup.Header.activeBackground",
            "Popup.preferences.background", // deprecated
            "PopupMenu.background",
            "PopupMenu.translucentBackground",
            "RadioButton.background",
            "RadioButton.darcula.selectionDisabledColor",
            "RadioButtonMenuItem.background",
            "ScrollBar.background",
            "SearchEverywhere.Advertiser.background",
            "SearchEverywhere.Advertiser.foreground",
            "SearchEverywhere.background",
            "SearchEverywhere.Dialog.background",
            "SearchEverywhere.Header.background",
            "SearchEverywhere.List.separatorColor",
            "SearchEverywhere.SearchField.background",
            "SearchEverywhere.SearchField.Border.color",
            "SearchEverywhere.SearchField.borderColor",
            "SearchEverywhere.SearchField.grayForeground",
            "Separator.background",
            "Separator.foreground",
            "SidePanel.background",
            "Slider.background",
            "Spinner.background",
            "SplitPane.highlight",
            "StatusBar.bottomColor",
            "StatusBar.hoverBackground",
            "StatusBar.top2Color",
            "StatusBar.topColor",
            "TabbedPane.background",
            "Table.background",
            "Table.gridColor",
            "TableHeader.background",
            "TextArea.background",
            "TextField.background",
            "TextPane.background",
            "ToolWindow.HeaderTab.underlinedTabBackground",
            "ToolTip.actions.background", // deprecated
            "ToolTip.Actions.background",
            "ToolTip.background",
            "ToolWindow.header.background",
            "ToolWindow.header.closeButton.background",
            "ToolWindow.Header.inactiveBackground",
            "ToolWindow.HeaderCloseButton.background",
            "ToolWindow.inactive.Header.background",
            "UiDesigner.Panel.background",
            "UiDesigner.Preview.background",
            "VersionControl.FileHistory.Commit.otherBranchBackground", // deprecated
            "VersionControl.FileHistory.Commit.selectedBranchBackground",
            "WelcomeScreen.background",
            "WelcomeScreen.headerBackground",
            "window"
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
                "Button.mt.selectedButtonForeground",
                "ToolWindow.header.closeButton.foreground")
    }


    open fun getBorderColor(): Color {
        return Color.getHSBColor(62f, 91f, 149f)
    }

    open fun getForegroundResources(): Stream<String> {
        return Stream.of(
            "CheckBox.foreground",
            "CheckBoxMenuItem.acceleratorForeground",
            "CheckBoxMenuItem.acceleratorSelectionForeground",
            "CheckBoxMenuItem.foreground",
            "ColorChooser.foreground",
            "ComboBox.foreground",
            "CompletionPopup.foreground",
            "EditorPane.foreground",
            "EditorPane.inactiveForeground",
            "FormattedTextField.foreground",
            "Label.disabledForeground",
            "Label.foreground",
            "Label.selectedDisabledForeground",
            "List.foreground",
            "material.branchColor",
            "material.foreground",
            "Menu.acceleratorForeground",
            "MenuBar.foreground",
            "MenuItem.acceleratorForeground",
            "monika.foreground",
            "monika.selectionForegroundInactive",
            "monika.selectionInactiveForeground",
            "monika.textForeground",
            "Notification.foreground",
            "Notification.MoreButton.foreground",
            "Notification.ToolWindow.errorForeground",
            "Notification.ToolWindow.infoForeground", // deprecated
            "Notification.ToolWindow.informativeForeground",
            "Notification.ToolWindow.warningForeground",
            "Notification.ToolWindowError.foreground", // deprecated
            "Notification.ToolWindowInfo.foreground", // deprecated
            "Notification.ToolWindowWarning.foreground", // deprecated
            "PasswordField.foreground",
            "Plugins.Button.installForeground",
            "Plugins.Button.installFillForeground",
            "Plugins.Button.updateForeground",
            "Plugins.SectionHeader.foreground",
            "PopupMenu.foreground",
            "RadioButton.foreground",
            "RadioButtonMenuItem.acceleratorForeground",
            "RadioButtonMenuItem.acceleratorSelectionForeground",
            "RadioButtonMenuItem.foreground",
            "SearchEverywhere.foreground",
            "SearchEverywhere.List.Separator.foreground",
            "SearchEverywhere.List.separatorForeground",
            "SearchEverywhere.SearchField.infoForeground",
            "Spinner.foreground",
            "TabbedPane.foreground",
            "Table.foreground",
            "Table.sortIconColor",
            "TableHeader.foreground",
            "TextArea.foreground",
            "TextField.foreground",
            "TextPane.foreground",
            "TitledBorder.titleColor",
            "ToggleButton.foreground",
            "ToolBar.foreground",
            "ToolTip.foreground"
        )
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

    open fun getSecondaryForegroundResources(): Stream<String> {
        return Stream.of(
            "CompletionPopup.infoForeground",
            "Component.infoForeground",
            "Debugger.Variables.collectingDataForeground",
            "Debugger.Variables.evaluatingExpressionForeground",
            "Editor.shortcutForeground",
            "Git.Log.Ref.Other",
            "Git.Log.Ref.Tag",
            "Github.List.tallRow.secondary.foreground",
            "HelpTooltip.infoForeground",
            "HelpTooltip.shortcutForeground",
            "HelpTooltip.shortcutTextColor",
            "Hg.Log.Ref.LocalTag",
            "Hg.Log.Ref.MqTag",
            "Hg.Log.Ref.Tag",
            "inactiveCaptionText",
            "infoText",
            "InternalFrame.inactiveTitleForeground",
            "Link.secondaryForeground",
            "List.foreground",
            "material.primaryColor",
            "material.tagColor",
            "ParameterInfo.ContextHelp.foreground",
            "ParameterInfo.infoForeground",
            "Plugins.Button.installFillForeground",
            "SearchEverywhere.shortcutForeground",
            "Table.lightSelectionInactiveForeground",
            "TitlePane.infoForeground",
            "ToolBar.borderHandleColor",
            "ToolBar.floatingForeground",
            "ToolTip.Actions.grayForeground",
            "ToolTip.Actions.infoForeground",
            "ToolTip.infoForeground",
            "tooltips.actions.keymap.text.color",
            "ToolWindow.Button.selectedForeground",
            "ToolWindow.header.active.foreground",
            "ToolWindow.header.border.foreground",
            "VersionControl.GitLog.otherIconColor",
            "VersionControl.GitLog.tagIconColor",
            "VersionControl.HgLog.localTagIconColor",
            "VersionControl.HgLog.mqTagIconColor",
            "VersionControl.HgLog.tagIconColor",
            "VersionControl.HgLog.tipIconColor",
            "VersionControl.Log.Commit.unmatchedForeground"
        )
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

  open fun getSelectedButtonForegroundColor(): String = getButtonForegroundColor()

  open fun getSelectedButtonForegroundResources(): Stream<String> =
      Stream.of("Button.darcula.selectedButtonForeground",
          "Button.default.startBackground")


    open fun getMenuItemForegroundResources(): Stream<String> {
        return Stream.of(
                "Menu.foreground",
                "MenuItem.foreground",
                "PopupMenu.foreground")
    }

    open fun getMenuItemForegroundColor(): String {
        return "FFFFFF"
    }


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

    private fun buildFlameChartColors() {
        UIManager.put("FlameGraph.JVMBackground", MTUI.MTColor.CYAN)
        UIManager.put("FlameGraph.JVMFocusBackground", MTUI.MTColor.BLUE)
        UIManager.put("FlameGraph.JVMSearchNotMatchedBackground", MTUI.MTColor.RED)
        UIManager.put("FlameGraph.JVMFocusSearchNotMatchedBackground", MTUI.MTColor.BROWN)

        UIManager.put("FlameGraph.nativeBackground", MTUI.MTColor.YELLOW)
        UIManager.put("FlameGraph.nativeFocusBackground", MTUI.MTColor.ORANGE)
        UIManager.put("FlameGraph.nativeSearchNotMatchedBackground", MTUI.MTColor.PURPLE)
        UIManager.put("FlameGraph.nativeFocusSearchNotMatchedBackground", MTUI.MTColor.PINK)
    }

    private fun buildTreeSelectionInactiveColors() {
        val colors = Collections.unmodifiableSet(
            Sets.newHashSet(
                "Tree.selectionInactiveBackground",
                "List.selectionInactiveBackground",
                "Table.selectionInactiveBackground",
                "TitlePane.inactiveBackground"
            ))

        val transparentBackground = getTreeSelectionInactiveColor()
        for (color in colors) {
            UIManager.put(color, transparentBackground)
        }
    }

    open fun getTreeSelectionInactiveColor(): Color?= MTUI.Tree.getSelectionInactiveBackground()

    private fun buildTransparentColors() {
        val colors = Collections.unmodifiableSet(
            Sets.newHashSet(
                "CompletionPopup.nonFocusedState",
                "CompletionPopup.nonFocusedMask",
                "ScrollBar.hoverTrackColor",
                "ScrollBar.trackColor",
                "ScrollBar.Mac.hoverTrackColor",
                "ScrollBar.Mac.trackColor",
                "ScrollBar.Transparent.hoverTrackColor",
                "ScrollBar.Transparent.trackColor",
                "ScrollBar.Mac.Transparent.hoverTrackColor",
                "ScrollBar.Mac.Transparent.trackColor"
            ))

        val transparentBackground = getTransparentBackgroundColor()
        for (color in colors) {
            UIManager.put(color, transparentBackground)
        }
    }

    open fun getTransparentBackgroundColor(): Color = MTUI.Panel.getTransparentBackground()

    open fun getOneOffResources(): Stream<Pair<Stream<String>, String>> = Stream.empty()

    private fun applyOneOffs() {
        getOneOffResources().forEach { (properties, colorString) ->
            buildResources(properties, colorString)
        }
    }
}
