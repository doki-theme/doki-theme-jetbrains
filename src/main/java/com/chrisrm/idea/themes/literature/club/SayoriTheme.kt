package com.chrisrm.idea.themes.literature.club

import java.util.stream.Stream

class SayoriTheme : DokiDokiTheme("sayori", "Sayori", false, "Sayori") {

    override fun getBackgroundColorString(): String = "f4fbfe"

    override fun getClubMember(): String = "sayori.png"

    override fun joyfulClubMember(): String = "sayori_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "c7f2ff"

    override fun getSecondaryForegroundColorString(): String = "256fe2"

    override fun getSelectionForegroundColorString(): String = "256fe2"

    override fun getSelectionBackgroundColorString(): String = "99ebf0"

    override fun getTreeSelectionBackgroundColorString(): String = "546eec"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "de0a22"

    override fun getNotificationsColorString(): String = "c3e8ff"

    override fun getContrastColorString(): String = "d8f2ff"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "00559A"

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("sayori.background", "f4fbfe"),
                Pair("Panel.background", "fffbf1"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "efa597"),
                Pair("Menu.background", "efa597"),
                Pair("MenuBar.background", "efa597"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "256fd5"),
                Pair("MenuItem.selectionForeground", "ffffff"),
                Pair("MenuItem.foreground", "ffffff"),
                Pair("MenuItem.selectionBackground", "e33d57"),
                Pair("Menu.selectionForeground", "ffffff"),
                Pair("Menu.selectionBackground", "e33d57"),
                Pair("Notifications.background", "c3e8ff"),
                Pair("Notifications.borderColor", "c3e8ff"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "a5f7fc"),
                Pair("Table.highlightOuter", "44dfff"),
                Pair("Autocomplete.selectionBackground", "50B9CB"),
                Pair("Autocomplete.selectionForeground", "38557B"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "C7F5FF"),
                Pair("Autocomplete.foreground", "00066F"),
                Pair("Autocomplete.selectedGreyedForeground", "2191FA"),
                Pair("Autocomplete.prefixForeground", "2F656E"),
                Pair("Autocomplete.selectedPrefixForeground", "A6F3FF"),
                Pair("Autocomplete.selectionUnfocus", "aceff2"),
                Pair("Button.mt.color2", "effcfb"),
                Pair("Button.mt.primary.color", "B0E4FF"),
                Pair("Button.mt.selection.color1", "c7f2ff"),
                Pair("Button.mt.selection.color2", "0A73AB"),
                Pair("Button.mt.background", "effcfb"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "effcfb"),
                Pair("ToolBar.background", "effcfb"),
                Pair("EditorPane.caretForeground", "effcfb"),
                Pair("SearchEverywhere.background", "aaddff"),
                Pair("SearchEverywhere.foreground", "497de7"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("sayori.textBackground", "f4fbfe"),
                Pair("sayori.foreground", "546E7A"),
                Pair("sayori.textForeground", "546E7A"),
                Pair("sayori.caretForeground", "FFCC00"),
                Pair("sayori.inactiveBackground", "f4fbfe"),
                Pair("sayori.selectionForeground", "FFFFFF"),
                Pair("sayori.selectionBackgroundInactive", "D2D4D5"),
                Pair("sayori.selectionInactiveBackground", "D2D4D5"),
                Pair("sayori.selectionForegroundInactive", "546E7A"),
                Pair("sayori.selectionInactiveForeground", "546E7A")
                )


    }
}
