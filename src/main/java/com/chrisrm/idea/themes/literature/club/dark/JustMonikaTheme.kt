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
                Pair("MenuBar.foreground", "fffefd")
        )
    }
}
