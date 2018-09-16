package com.chrisrm.idea.themes.literature.club

import java.util.stream.Stream

class YuriTheme : DokiDokiTheme("yuri", "Yuri", false, "Yuri") {

    override fun getBackgroundColorString(): String = "e8e4ff"

    override fun getClubMember(): String = "yuri.png"

    override fun joyfulClubMember(): String = "yuri_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "bbb8ff"

    override fun getSecondaryForegroundColorString(): String = "562474"

    override fun getSelectionForegroundColorString(): String = "c06fff"

    override fun getSelectionBackgroundColorString(): String = "e4caff"

    override fun getTreeSelectionBackgroundColorString(): String = "d3a5fa"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "7c0e9d"

    override fun getNotificationsColorString(): String = "d3ceff"

    override fun getContrastColorString(): String = "ccaaff"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "6B1C9A"

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("yuri.background", "e8e4ff"),
                Pair("Panel.background", "fff6e0"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "6f4f86"),
                Pair("Menu.background", "6f4f86"),
                Pair("MenuBar.background", "6f4f86"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "d66fff"),
                Pair("MenuItem.selectionForeground", "ffffff"),
                Pair("MenuItem.foreground", "FFFFFF"),
                Pair("MenuItem.selectionBackground", "7c0e9d"),
                Pair("Menu.selectionForeground", "ffffff"),
                Pair("Menu.selectionBackground", "7c0e9d"),
                Pair("Notifications.background", "ffc4ff"),
                Pair("Notifications.borderColor", "ffc4ff"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "cea3fc"),
                Pair("Table.highlightOuter", "cea3fc"),
                Pair("Autocomplete.selectionBackground", "C1A7FC"),
                Pair("Autocomplete.selectionForeground", "683F7B"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "DEC6FF"),
                Pair("Autocomplete.foreground", "32006F"),
                Pair("Autocomplete.selectedGreyedForeground", "8A32C8"),
                Pair("Autocomplete.prefixForeground", "5A476E"),
                Pair("Autocomplete.selectedPrefixForeground", "ECEBFF"),
                Pair("Autocomplete.selectionUnfocus", "d3b3ff"),
                Pair("Button.mt.color2", "EDEDFF"),
                Pair("Button.mt.primary.color", "ACA5F5"),
                Pair("Button.mt.selection.color1", "b4b1f5"),
                Pair("Button.mt.selection.color2", "F2F1F1"),
                Pair("Button.mt.background", "EDEDFF"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "EDEDFF"),
                Pair("ToolBar.background", "EDEDFF"),
                Pair("EditorPane.caretForeground", "EDEDFF"),
                Pair("SearchEverywhere.background", "c7b3ff"),
                Pair("SearchEverywhere.foreground", "c753ff"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("yuri.background", "e8e4ff"),
                Pair("yuri.textBackground", "e8e4ff"),
                Pair("yuri.foreground", "546E7A"),
                Pair("yuri.textForeground", "546E7A"),
                Pair("yuri.caretForeground", "FFCC00"),
                Pair("yuri.inactiveBackground", "e8e4ff"),
                Pair("yuri.selectionForeground", "FFFFFF"),
                Pair("yuri.selectionBackgroundInactive", "D2D4D5"),
                Pair("yuri.selectionInactiveBackground", "D2D4D5"),
                Pair("yuri.selectionForegroundInactive", "546E7A"),
                Pair("yuri.selectionInactiveForeground", "546E7A")
        )
    }
}
