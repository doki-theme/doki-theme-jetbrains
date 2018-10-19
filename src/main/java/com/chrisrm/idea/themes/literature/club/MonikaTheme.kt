/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.idea.themes.literature.club

import java.awt.Color
import java.util.stream.Stream

class MonikaTheme : DokiDokiTheme("monika", "Monika", false, "Monika") {

    override fun getClubMember(): String = "just_monika.png"

    override fun getSelectionBackground(): String = MonikaTheme.SELECTION_BACKGROUND

    override fun getButtonForegroundColor(): String = "14610D"
    override fun getDisabled(): String = MonikaTheme.DISABLED

    override fun getNotificationsColorString(): String = "C3E88D"

    override fun getTreeSelectionBackgroundColorString(): String = "546E50"

    override fun getButtonHighlightColorString(): String = "F2F1F1"

    override fun getHighlightColorString(): String = "425B67"

    override fun getSecondBorderColorString(): String = "d3e1e8"

    override fun getTableSelectedColorString(): String = "def7a5"

    override fun getContrastColorString(): String = selectionBackgroundColorString

    override fun getDisabledColorString(): String = "000000"

    override fun getSecondaryBackgroundColorString(): String = "d8f26e"

    override fun getCaretColorString(): String = "FFCC00"

    override fun getInactiveColorString(): String = "FFF4F2"

    override fun getButtonColorString(): String = "FFF4F2"

    //  todo: imporant
    override fun getSelectionForegroundColorString(): String = "447152"

    //todo: important
    override fun getSelectionBackgroundColorString(): String = "99eb99"

    override fun getTextColorString(): String = "4d6e80"

    //todo: this may be important
    override fun getForegroundColorString(): String = "546E7A"

    override fun getBackgroundColorString(): String = "f2fadf"

    override fun getTreeSelectionBackgroundResources(): Array<String> = arrayOf("Tree.selectionBackground", "TextField.inactiveForeground")

    override fun getButtonHighlightResources(): Array<String> = arrayOf("Button.mt.color2", "Button.mt.selection.color2")

    override fun getHighlightResources(): Array<String> = arrayOf("Focus.color", "TextField.separatorColor", "CheckBox.darcula.inactiveFillColor")

    override fun getSecondBorderResources(): Array<String> = arrayOf("TabbedPane.highlight", "TabbedPane.selected", "TabbedPane.selectHighlight")

    override fun getTableSelectedResources(): Array<String> = arrayOf("ProgressBar.halfColor", "MemoryIndicator.unusedColor")

    override fun getDisabledResources(): Array<String> = arrayOf("MenuItem.disabledForeground", "ComboBox.disabledForeground")

    override fun getSecondaryBackgroundResources(): Array<String> = arrayOf("Separator.foreground", "TextField.separatorColorDisabled", "PasswordField.inactiveForeground", "Button.darcula.selection.color1", "Button.darcula.selection.color2", "Button.mt.selection.color1", "List.background", "ToolWindow.header.active.background", "ToolWindow.header.border.background", "material.disabled", "material.mergeCommits")

    override fun getCaretResources(): Array<String> = arrayOf("monika.caretForeground")

    override fun getInactiveResources(): Array<String> = arrayOf("Table.gridColor", "MenuBar.darcula.borderColor", "MenuBar.darcula.borderShadowColor", "CheckBox.darcula.disabledBorderColor1", "CheckBox.darcula.disabledBorderColor2")

    override fun getSelectionForegroundResources(): Stream<String> = Stream.of(
            "monika.selectionForeground",
            "Menu.selectionForeground",
            "Menu.acceleratorSelectionForeground",
            "MenuItem.selectionForeground",
            "MenuItem.acceleratorSelectionForeground",
            "Table.selectionForeground",
            "TextField.selectionForeground",
            "PasswordField.selectionForeground",
            "Button.mt.selectedForeground",
            "TextArea.selectionForeground",
            "Label.selectedForeground",
            "Button.darcula.selectedButtonForeground",
            "Separator.background"

    )


    override fun getSelectionBackgroundResources(): Array<String> = arrayOf("monika.selectionBackgroundInactive", "monika.selectionInactiveBackground", "inactiveCaption", "Button.disabledText", "monika.selectionBackground", "Menu.selectionBackground", "Menu.acceleratorSelectionBackground", "MenuItem.selectionBackground", "MenuItem.acceleratorSelectionBackground", "Table.selectionBackground", "TextField.selectionBackground", "PasswordField.selectionBackground", "Button.mt.selectedBackground", "TextArea.selectionBackground", "Label.selectedBackground", "Button.darcula.selectedButtonBackground")

    override fun getTextResources(): Array<String> = arrayOf("Menu.acceleratorForeground", "MenuItem.acceleratorForeground", "material.tagColor", "material.primaryColor", "SearchEverywhere.shortcutForeground", "Tree.foreground")

    override fun getBackgroundResources(): Array<String> = arrayOf("monika.background", "monika.textBackground", "monika.inactiveBackground", "window", "activeCaption", "control", "PopupMenu.translucentBackground", "EditorPane.inactiveBackground", "Table.background", "MenuBar.disabledBackground", "MenuBar.shadow", "TabbedPane.highlight", "TabbedPane.darkShadow", "TabbedPane.shadow", "TabbedPane.borderColor", "Desktop.background", "Separator.background", "MenuBar.background", "Separator.foreground", "TextField.background", "PasswordField.background", "FormattedTextField.background", "TextArea.background", "CheckBox.darcula.backgroundColor1", "CheckBox.darcula.backgroundColor2", "CheckBox.darcula.checkSignColor", "CheckBox.darcula.shadowColor", "CheckBox.darcula.shadowColorDisabled", "CheckBox.darcula.focusedArmed.backgroundColor1", "CheckBox.darcula.focusedArmed.backgroundColor2", "CheckBox.darcula.focused.backgroundColor1", "CheckBox.darcula.focused.backgroundColor2", "ComboBox.background", "ComboBox.disabledBackground", "RadioButton.darcula.selectionDisabledColor", "StatusBar.topColor", "StatusBar.top2Color", "StatusBar.bottomColor", "ToolTip.background", "Spinner.background", "SplitPane.highlight", "SearchEverywhere.background", "SidePanel.background", "DialogWrapper.southPanelDivider", "OnePixelDivider.background", "Dialog.titleColor", "SearchEverywhere.background", "RadioButton.background", "CheckBoxMenuItem.background", "RadioButtonMenuItem.background", "CheckBox.background", "ColorChooser.background", "Slider.background", "TabbedPane.background", "OptionPane.background", "ToolWindow.header.background", "material.tab.backgroundColor", "material.background")

    override fun getButtonBackgroundResources(): Stream<String> = Stream.concat(super.getButtonBackgroundResources(), Stream.of(
            "Button.mt.color1",
            "Button.mt.background"
    ))


    override fun getBorderColor(): Color = Color.getHSBColor(62f, 91f, 149f)

    override fun getForegroundResources(): Array<String> = arrayOf("monika.foreground", "monika.textForeground", "monika.selectionForegroundInactive", "monika.selectionInactiveForeground", "Label.foreground", "EditorPane.inactiveForeground", "CheckBox.foreground", "ComboBox.foreground", "RadioButton.foreground", "ColorChooser.foreground", "MenuBar.foreground", "RadioButtonMenuItem.foreground", "CheckBoxMenuItem.foreground", "PopupMenu.foreground", "Spinner.foreground", "TabbedPane.foreground", "TextField.foreground", "FormattedTextField.foreground", "PasswordField.foreground", "TextArea.foreground", "TextPane.foreground", "EditorPane.foreground", "ToolBar.foreground", "ToolTip.foreground", "List.foreground", "SearchEverywhere.foreground", "Label.foreground", "Label.disabledForeground", "Label.selectedDisabledForeground", "Table.foreground", "TableHeader.foreground", "ToggleButton.foreground", "Table.sortIconColor", "material.branchColor", "material.foreground", "TitledBorder.titleColor")

    companion object {
        val BACKGROUND = "fffcfc"
        val FOREGROUND = "A7ADB0" // 167, 173, 176
        val TEXT = "A7ADB0" // 167, 173, 176
        val SELECTION_BACKGROUND = "FFFFFF" // 84, 110, 122
        val SELECTION_FOREGROUND = "000000"
        val LABEL = "546E7A" // 84, 110, 122
        val DISABLED = "81d7f7"//not really important
        val NON_PROJECT_FILES = "fdffce"
        val TEST_FILES = "bbff7e"
    }
}
