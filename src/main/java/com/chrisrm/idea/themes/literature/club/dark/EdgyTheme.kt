package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.DokiDokiTheme
import java.util.stream.Stream

//todo: should probably rename these to something more expected :|
class EdgyTheme : DokiDokiTheme("yuri.dark", "Edgy", true, "Yuri") {

    override fun getBackgroundColorString(): String = "322A45"

    override fun getClubMember(): String = "yuri_dark.png"

    override fun joyfulClubMember(): String = "yuri_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "412D62"

    override fun getSecondaryForegroundColorString(): String = "9965BC"

    override fun getSelectionForegroundColorString(): String = "B39EFF"

    override fun getSelectionBackgroundColorString(): String = "442463"

    override fun getTreeSelectionBackgroundColorString(): String = "65318F"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "56009D"

    override fun getNotificationsColorString(): String = "36274E"

    override fun getContrastColorString(): String = "473960"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "7469C7"

    override fun getForegroundColorString(): String = "917DEA"

    override fun getDisabled(): String = "17C704"

    override fun getTextColorString(): String = "916ACE"

    override fun getNonProjectFileScopeColor(): String = "272132"

    override fun getTestScope(): String = "0c3118"

    override fun getSecondBorderColorString(): String = "50237C"

    override fun getDisabledColorString(): String = "000000"

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("yuri.dark.background", "3c3152"),
                Pair("Panel.background", "484c62"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "5E4174"),
                Pair("Menu.background", "5E4174"),
                Pair("MenuBar.background", "5E4174"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "d66fff"),
                Pair("MenuItem.selectionForeground", "ffffff"),
                Pair("MenuItem.foreground", "FFFFFF"),
                Pair("MenuItem.selectionBackground", "770E97"),
                Pair("Menu.selectionForeground", "ffffff"),
                Pair("Menu.selectionBackground", "770E97"),
                Pair("Notifications.background", "7F67A7"),
                Pair("Notifications.borderColor", "ffc4ff"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "987DB3"),
                Pair("Table.highlightOuter", "987DB3"),
                Pair("Autocomplete.selectionBackground", "532F7A"),
                Pair("Autocomplete.selectionForeground", "E298FF"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "493D5D"),
                Pair("Autocomplete.foreground", "D691F7"),
                Pair("Autocomplete.selectedGreyedForeground", "D072FF"),
                Pair("Autocomplete.prefixForeground", "D8A6FE"),
                Pair("Autocomplete.selectedPrefixForeground", "ECEBFF"),
                Pair("Autocomplete.selectionUnfocus", "4F3E86"),
                Pair("Button.mt.color2", "5B43B9"),
                Pair("Button.mt.primary.color", "573B7E"),
                Pair("Button.mt.selection.color1", "7861C7"),
                Pair("Button.mt.selection.color2", "D5BEF2"),
                Pair("Button.mt.background", "5B43B9"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "5B43B9"),
                Pair("ToolBar.background", "5B43B9"),
                Pair("EditorPane.caretForeground", "5B43B9"),
                Pair("SearchEverywhere.background", "988DCE"),
                Pair("SearchEverywhere.foreground", "AB4ADD"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("yuri.dark.textBackground", "3c3152"),
                Pair("yuri.dark.foreground", "7B71A7"),
                Pair("yuri.dark.textForeground", "7B71A7"),
                Pair("yuri.dark.caretForeground", "FFCC00"),
                Pair("yuri.dark.inactiveBackground", "3c3152"),
                Pair("yuri.dark.selectionForeground", "FFFFFF"),
                Pair("yuri.dark.selectionBackgroundInactive", "D2D4D5"),
                Pair("yuri.dark.selectionInactiveBackground", "D2D4D5"),
                Pair("yuri.dark.selectionForegroundInactive", "7B71A7"),
                Pair("yuri.dark.selectionInactiveForeground", "7B71A7")
        )
    }
}