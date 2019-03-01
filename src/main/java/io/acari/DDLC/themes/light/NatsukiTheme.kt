package io.acari.DDLC.themes.light

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

class NatsukiTheme : DokiDokiTheme("natsuki", "Natsuki", false, "Natsuki") {

    override fun getBackgroundColorString(): String = "ffeafa"

    override fun getClubMember(): String = "natsuki.png"

    override fun joyfulClubMember(): String = "natsuki_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "ffe0f2"

    override fun getSecondaryForegroundColorString(): String = "b9198d"

    override fun getSelectionForegroundColorString(): String = "fa6fe2"

    override fun getSelectionBackgroundColorString(): String = "ffd5f5"

    override fun getTreeSelectionForegroundColorString(): String = "546E7A"

    override fun getTreeSelectionBackgroundColorString(): String = "ffd5fa"

    override fun getInactiveColorString(): String = "ffc8de"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "d9031a"

    override fun getNotificationsColorString(): String = "ffc7ec"

    override fun getContrastColorString(): String = "fedfff"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getAccentColor(): String {
        return MTAccents.FUCHSIA.hexColor
    }

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xffebfa)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xEA427E)

    override fun getTableSelectedColorString(): String = "EDB9DF"

    override fun getHighlightColorString(): String = "ffa7ff"

    override fun getStartColor(): String = "F25AAF"

    override fun getStopColor(): String = "fbadff"
}
