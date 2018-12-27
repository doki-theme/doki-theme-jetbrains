package io.acari.DDLC.themes.dark

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

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

    override fun getInactiveColorString(): String = "122D13"

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

    override fun getAccentColor(): String {
        return MTAccents.BREAKING_BAD.hexColor
    }

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x1a1e12)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xF9F9F9)

    override fun getTableSelectedColorString(): String = "224C1C"

    override fun getHighlightColorString(): String = "2f491d"
}
