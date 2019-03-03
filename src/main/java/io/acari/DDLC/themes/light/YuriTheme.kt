package io.acari.DDLC.themes.light

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

class YuriTheme : DokiDokiTheme("yuri", "Yuri", false, "Yuri") {

    override fun getBackgroundColorString(): String = "efe4ff"

    override fun getClubMember(): String = "yuri.png"

    override fun joyfulClubMember(): String = "yuri_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "dcc5ff"

    override fun getSecondaryForegroundColorString(): String = "b36cdc"

    override fun getSelectionForegroundColorString(): String = "9921FF"

    override fun getSelectionBackgroundColorString(): String = "C89EFF"

    override fun getTreeSelectionBackgroundColorString(): String = "d3a5fa"

    override fun getInactiveColorString(): String = "DEC5FA"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "7c0e9d"

    override fun getNotificationsColorString(): String = "d3ceff"

    override fun getContrastColorString(): String = "ccaaff"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "6B1C9A"

    override fun getAccentColor(): String = MTAccents.AMETHYST.hexColor

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xe9d6ff)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x7A65EA)

    override fun getTableSelectedColorString(): String = "B6A2DC"

    override fun getHighlightColorString(): String = "caa7ff"

    override fun getStartColor(): String = "CB87FB"

    override fun getStopColor(): String = "F595FF"

    override fun getNonProjectFileScopeColor(): String = "efe6fa"
}
