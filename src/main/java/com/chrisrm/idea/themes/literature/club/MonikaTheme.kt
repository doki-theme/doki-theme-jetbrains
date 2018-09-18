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

    override fun getTreeSelectionBackgroundResources(): Array<String> = arrayOf("Tree.selectionBackground")

    override fun getButtonHighlightResources(): Array<String> = arrayOf("Button.mt.color2", "Button.mt.selection.color2")

    override fun getHighlightResources(): Array<String> = arrayOf("Focus.color", "TextField.separatorColor", "CheckBox.darcula.inactiveFillColor")

    override fun getSecondBorderResources(): Array<String> = arrayOf("TabbedPane.highlight", "TabbedPane.selected", "TabbedPane.selectHighlight")

    override fun getTableSelectedResources(): Array<String> = arrayOf("ProgressBar.halfColor", "MemoryIndicator.unusedColor")

    override fun getDisabledResources(): Array<String> = arrayOf("MenuItem.disabledForeground", "ComboBox.disabledForeground")

    override fun getSecondaryBackgroundResources(): Array<String> = arrayOf("Separator.foreground", "TextField.separatorColorDisabled", "TextField.inactiveForeground", "PasswordField.inactiveForeground", "Button.darcula.selection.color1", "Button.darcula.selection.color2", "Button.mt.selection.color1", "List.background", "ToolWindow.header.active.background", "ToolWindow.header.border.background", "material.disabled", "material.mergeCommits")

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

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("monika.background", "f2fadf"),
                Pair("Panel.background", "fceee3"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "eea588"),
                Pair("Menu.background", "eea588"),
                Pair("MenuBar.background", "eea588"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "256f25"),
                Pair("MenuItem.selectionForeground", "000000"),
                Pair("MenuItem.selectionBackground", "ffffff"),
                Pair("MenuItem.foreground", "ffffff"),
                Pair("Menu.selectionForeground", "000000"),
                Pair("Menu.selectionBackground", "ffffff"),
                Pair("Notifications.background", "C3E88D"),
                Pair("Notifications.borderColor", "C3E88D"),
                Pair("Autocomplete.selectionBackground", "8BCB6E"),
                Pair("Autocomplete.selectionForeground", "467B6C"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "E0FFD8"),
                Pair("Autocomplete.foreground", "276F01"),
                Pair("Autocomplete.selectedGreyedForeground", "2EA240"),
                Pair("Autocomplete.prefixForeground", "256E25"),
                Pair("Autocomplete.selectedPrefixForeground", "DFFF8B"),
                Pair("Autocomplete.selectionUnfocus", "b6f3b3"),
                Pair("Button.mt.color2", "fbffeb"),
                Pair("Button.mt.primary.color", "d4ee6c"),
                Pair("Button.mt.selection.color1", "d4ee6c"),
                Pair("Button.mt.selection.color2", "F2F1F1"),
                Pair("Button.mt.background", "fbffeb"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "fbffeb"),
                Pair("ToolBar.background", "fbffeb"),
                Pair("EditorPane.caretForeground", "fbffeb"),
                Pair("SearchEverywhere.background", "99eb99"),
                Pair("SearchEverywhere.foreground", "006d09"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("monika.textBackground", "f2fadf"),
                Pair("monika.foreground", "546E7A"),
                Pair("monika.textForeground", "546E7A"),
                Pair("monika.caretForeground", "FFCC00"),
                Pair("monika.inactiveBackground", "f2fadf"),
                Pair("monika.selectionForeground", "000000"),
                Pair("monika.selectionBackgroundInactive", "D2D4D5"),
                Pair("monika.selectionInactiveBackground", "D2D4D5"),
                Pair("monika.selectionForegroundInactive", "546E7A"),
                Pair("monika.selectionInactiveForeground", "546E7A")
        )
    }
}
