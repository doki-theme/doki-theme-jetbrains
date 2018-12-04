package io.acari.DDLC.themes.light

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

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

    override fun getAccentColor(): String {
        return MTAccents.CYAN.hexColor
    }

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xf4fbfe)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x5A75EA)

    override fun getTableSelectedColorString(): String = "485FC3"
}
