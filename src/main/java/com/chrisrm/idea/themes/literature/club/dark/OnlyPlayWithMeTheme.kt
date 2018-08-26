package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

class OnlyPlayWithMeTheme: MTDokiDokiTheme("natsuki.dark", "Only Play With Me", true, "Natsuki") {

    override fun getBackgroundColorString(): String = "24071D"

    override fun getClubMember(): String = "natsuki_dark.png"

    override fun joyfulClubMember(): String = "natsuki_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "330A2B                          "

    override fun getSecondaryForegroundColorString(): String = "157174"

    override fun getSelectionForegroundColorString(): String = "984C7C"

    override fun getSelectionBackgroundColorString(): String = "5D2960"

    override fun getTreeSelectionBackgroundColorString(): String = "B54E8E"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "9D0064"

    override fun getNotificationsColorString(): String = "9F4584"

    override fun getContrastColorString(): String = "5D3652"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "E85EDB"

    override fun getForegroundColorString(): String = "C64B7D"

    override fun getTextColorString(): String = "C566BE"

    override fun getEditorTabColorString(): String = contrastColorString
}