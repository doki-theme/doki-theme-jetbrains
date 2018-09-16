package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.DokiDokiTheme
import java.util.stream.Stream

/**
 * Forged in the flames of battle by alex.
 */
class JustMonikaTheme : DokiDokiTheme("just.monika", "Just Monika", true, "Monika") {

    override fun getBackgroundColorString(): String = "1C230D"

    override fun getClubMember(): String = "only_monika.png"

    override fun joyfulClubMember(): String = "only_monika_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "1a1e12"

    override fun getSecondaryForegroundColorString(): String = "6f9a65"

    override fun getSelectionForegroundColorString(): String = "FFFFFF"

    override fun getSelectionBackgroundColorString(): String = "30432B"

    override fun getTreeSelectionBackgroundColorString(): String = "2A692C"

    override fun getMenuBarSelectionForegroundColorString(): String = "000000"

    override fun getMenuBarSelectionBackgroundColorString(): String = "B1B1B1"

    override fun getNotificationsColorString(): String = "214321"

    override fun getContrastColorString(): String = "244020"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "6f9a65"

    override fun getForegroundColorString(): String = "F9F9F9"

    override fun getTextColorString(): String = "F9F9F9"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getNonProjectFileScopeColor(): String = "262b18"

    override fun getTestScope(): String = "0f2a00"

    override fun getSecondBorderColorString(): String = "297C16"

    override fun getDisabledColorString(): String = "505050"

    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("just.monika.background", "1a1e12"),
                Pair("Panel.background", "302521"),
                Pair("Menu.foreground", "fffefd"),
                Pair("PopupMenu.background", "331d1b"),
                Pair("Menu.background", "331d1b"),
                Pair("MenuBar.background", "331d1b"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "fffefd"),
                Pair("Button.mt.foreground", "256f25"),
                Pair("MenuItem.selectionForeground", "000000"),
                Pair("MenuItem.selectionBackground", "fdfdfd"),
                Pair("MenuItem.foreground", "ffffff"),
                Pair("Menu.selectionForeground", "000000"),
                Pair("Menu.selectionBackground", "fdfdfd"),
                Pair("Notifications.background", "458C2D"),
                Pair("Notifications.borderColor", "BAFFAB"),
                Pair("Table.selectionForeground", "FFFFFF"),
                Pair("Table.selectionBackground", "2A692C"),
                Pair("Table.highlightOuter", "8DFC8E"),
                Pair("Autocomplete.selectionBackground", "367B34"),
                Pair("Autocomplete.selectionForeground", "E0FFD8"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "415a2f"),
                Pair("Autocomplete.foreground", "E0FFD8"),
                Pair("Autocomplete.selectedGreyedForeground", "33CE47"),
                Pair("Autocomplete.prefixForeground", "98DE78"),
                Pair("Autocomplete.selectedPrefixForeground", "79DE65"),
                Pair("Autocomplete.selectionUnfocus", "367B34"),
                Pair("Button.mt.primary.color", "4C7934"),
                Pair("ToolBar.background", "5a7053"),
                Pair("EditorPane.caretForeground", "83AB79"),
                Pair("SearchEverywhere.background", "c7b3ff"),
                Pair("SearchEverywhere.foreground", "c753ff"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("just.monika.background", "1a1e12"),
                Pair("just.monika.textBackground", "1a1e12"),
                Pair("just.monika.foreground", "6F9A65"),
                Pair("just.monika.textForeground", "6F9A65"),
                Pair("just.monika.caretForeground", "FFCC00"),
                Pair("just.monika.inactiveBackground", "1a1e12"),
                Pair("just.monika.selectionForeground", "333333"),
                Pair("just.monika.selectionBackgroundInactive", "D2D4D5"),
                Pair("just.monika.selectionInactiveBackground", "D2D4D5"),
                Pair("just.monika.selectionForegroundInactive", "6F9A65"),
                Pair("just.monika.selectionInactiveForeground", "6F9A65"),
                Pair("just.monika.selectionBackground", "20491F"),
                Pair("just.monika.selectionForeground", "AFD1A3")
        )
    }
}
