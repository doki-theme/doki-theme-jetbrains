package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.DokiDokiTheme
import java.util.stream.Stream

class OnlyPlayWithMeTheme : DokiDokiTheme("natsuki.dark", "Only Play With Me", true, "Natsuki") {

    override fun getBackgroundColorString(): String = "240E1E"

    override fun getClubMember(): String = "natsuki_dark.png"

    override fun joyfulClubMember(): String = "natsuki_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "330A2B                          "

    override fun getSecondaryForegroundColorString(): String = "BB47A8"

    override fun getSelectionForegroundColorString(): String = "D671B0"

    override fun getSelectionBackgroundColorString(): String = "5D2960"

    override fun getTreeSelectionBackgroundColorString(): String = "B54E8E"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "9D0064"

    override fun getNotificationsColorString(): String = "562447"

    override fun getContrastColorString(): String = "56324C"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "E85EDB"

    override fun getForegroundColorString(): String = "DC508F"

    override fun getTextColorString(): String = "FF78BA"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getNonProjectFileScopeColor(): String = "2a1b1f"

    override fun getTestScope(): String = "0f2a00"

    override fun getSecondBorderColorString(): String = "5A1943"

    override fun getDisabledColorString(): String = "232323"

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("natsuki.dark.background", "231921"),
                Pair("Panel.background", "2B2827"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "874C57"),
                Pair("Menu.background", "874C57"),
                Pair("MenuBar.background", "874C57"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "ff6fd5"),
                Pair("MenuItem.selectionForeground", "ffffff"),
                Pair("MenuItem.selectionBackground", "AA041A"),
                Pair("MenuItem.foreground", "FFFFFF"),
                Pair("Menu.selectionForeground", "ffffff"),
                Pair("Menu.selectionBackground", "AA041A"),
                Pair("Notifications.background", "B17FB1"),
                Pair("Notifications.borderColor", "ffc4ff"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "CB7BCA"),
                Pair("Table.highlightOuter", "FFD3F6"),
                Pair("Autocomplete.selectionBackground", "462325"),
                Pair("Autocomplete.selectionForeground", "FB67B7"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "401925"),
                Pair("Autocomplete.foreground", "FC99D0"),
                Pair("Autocomplete.selectedGreyedForeground", "F844D2"),
                Pair("Autocomplete.prefixForeground", "F4B1F8"),
                Pair("Autocomplete.selectedPrefixForeground", "FFEAFD"),
                Pair("Autocomplete.selectionUnfocus", "542A30"),
                Pair("Button.mt.color2", "9B3685"),
                Pair("Button.mt.primary.color", "7E436E"),
                Pair("Button.mt.selection.color1", "B9589F"),
                Pair("Button.mt.selection.color2", "B3B2B2"),
                Pair("Button.mt.background", "9B3685"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "9B3685"),
                Pair("ToolBar.background", "9B3685"),
                Pair("EditorPane.caretForeground", "9B3685"),
                Pair("SearchEverywhere.background", "CC91C9"),
                Pair("SearchEverywhere.foreground", "AE36B6"),
                Pair("SearchEverywhere.shortcutForeground", "C1B7C5"),
                Pair("natsuki.dark.textBackground", "4A1D3B"),
                Pair("natsuki.dark.foreground", "A76E9F"),
                Pair("natsuki.dark.textForeground", "A76E9F"),
                Pair("natsuki.dark.caretForeground", "FFCC00"),
                Pair("natsuki.dark.inactiveBackground", "4A1D3B"),
                Pair("natsuki.dark.selectionForeground", "FFFFFF"),
                Pair("natsuki.dark.selectionBackgroundInactive", "D2D4D5"),
                Pair("natsuki.dark.selectionInactiveBackground", "D2D4D5"),
                Pair("natsuki.dark.selectionForegroundInactive", "A76E9F"),
                Pair("natsuki.dark.selectionInactiveForeground", "A76E9F")
        )
    }
}