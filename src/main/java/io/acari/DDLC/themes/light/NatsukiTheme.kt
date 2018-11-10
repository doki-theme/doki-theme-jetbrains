package io.acari.DDLC.themes.light

import io.acari.DDLC.themes.DokiDokiTheme

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
}
