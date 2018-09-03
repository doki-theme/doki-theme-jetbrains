package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

/**
 * Forged in the flames of battle by alex.
 */
class JustMonikaTheme: MTDokiDokiTheme("just.monika", "Just Monika", true, "Monika") {

    override fun getBackgroundColorString(): String = "1C230D"

    override fun getClubMember(): String = "only_monika.png"

    override fun joyfulClubMember(): String = "only_monika_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "1a1e12"

    override fun getSecondaryForegroundColorString(): String = "6f9a65"

    override fun getSelectionForegroundColorString(): String = "FFFFFF"

    override fun getSelectionBackgroundColorString(): String = "30432B"

    override fun getTreeSelectionBackgroundColorString(): String = "3D7B3D"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "2D9D04"

    override fun getNotificationsColorString(): String = "214321"

    override fun getContrastColorString(): String = "244020"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "6f9a65"

    override fun getForegroundColorString(): String = "F9F9F9"

    override fun getTextColorString(): String = "F9F9F9"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getNonProjectFileScopeColor(): String = "262b18"

    override fun getTestScope(): String = "0f2a00"
}
