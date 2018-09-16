package com.chrisrm.idea.themes.literature.club

import java.util.stream.Stream

class NatsukiTheme : DokiDokiTheme("natsuki", "Natsuki", false, "Natsuki") {

    override fun getBackgroundColorString(): String = "fff3fc"

    override fun getClubMember(): String = "natsuki.png"

    override fun joyfulClubMember(): String = "natsuki_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "ffceeb"

    override fun getSecondaryForegroundColorString(): String = "b9198d"

    override fun getSelectionForegroundColorString(): String = "fa6fe2"

    override fun getSelectionBackgroundColorString(): String = "ffd5f5"

    override fun getTreeSelectionBackgroundColorString(): String = "ff6eec"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "d9031a"

    override fun getNotificationsColorString(): String = "ffc7ec"

    override fun getContrastColorString(): String = "fdceff"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("natsuki.background", "FFDDF3"),
                Pair("Panel.background", "ffede0"),
                Pair("Menu.foreground", "ffffff"),
                Pair("PopupMenu.background", "fdafbb"),
                Pair("Menu.background", "fdafbb"),
                Pair("MenuBar.background", "fdafbb"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "ffffff"),
                Pair("Button.mt.foreground", "ff6fd5"),
                Pair("MenuItem.selectionForeground", "ffffff"),
                Pair("MenuItem.selectionBackground", "d9031a"),
                Pair("MenuItem.foreground", "FFFFFF"),
                Pair("Menu.selectionForeground", "ffffff"),
                Pair("Menu.selectionBackground", "d9031a"),
                Pair("Notifications.background", "ffc4ff"),
                Pair("Notifications.borderColor", "ffc4ff"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "fda0fc"),
                Pair("Table.highlightOuter", "ee68ff"),
                Pair("Autocomplete.selectionBackground", "FCA3CD"),
                Pair("Autocomplete.selectionForeground", "7B3A58"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "FDDCFF"),
                Pair("Autocomplete.foreground", "6F0058"),
                Pair("Autocomplete.selectedGreyedForeground", "E13FBD"),
                Pair("Autocomplete.prefixForeground", "6C4D6E"),
                Pair("Autocomplete.selectedPrefixForeground", "FFEAFD"),
                Pair("Autocomplete.selectionUnfocus", "ffbbf2"),
                Pair("Button.mt.color2", "FCE8F7"),
                Pair("Button.mt.primary.color", "FFB4FC"),
                Pair("Button.mt.selection.color1", "ffceeb"),
                Pair("Button.mt.selection.color2", "F2F1F1"),
                Pair("Button.mt.background", "FCE8F7"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "FCE8F7"),
                Pair("ToolBar.background", "FCE8F7"),
                Pair("EditorPane.caretForeground", "FCE8F7"),
                Pair("SearchEverywhere.background", "FFAEFB"),
                Pair("SearchEverywhere.foreground", "F643FF"),
                Pair("SearchEverywhere.shortcutForeground", "C1B7C5"),
                Pair("natsuki.textBackground", "FFDDF3"),
                Pair("natsuki.foreground", "546E7A"),
                Pair("natsuki.textForeground", "546E7A"),
                Pair("natsuki.caretForeground", "FFCC00"),
                Pair("natsuki.inactiveBackground", "FFDDF3"),
                Pair("natsuki.selectionForeground", "FFFFFF"),
                Pair("natsuki.selectionBackgroundInactive", "D2D4D5"),
                Pair("natsuki.selectionInactiveBackground", "D2D4D5"),
                Pair("natsuki.selectionForegroundInactive", "546E7A"),
                Pair("natsuki.selectionInactiveForeground", "546E7A")
        )
    }
}
