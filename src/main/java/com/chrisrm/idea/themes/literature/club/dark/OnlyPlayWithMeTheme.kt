package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

class OnlyPlayWithMeTheme: MTDokiDokiTheme("natsuki.dark", "Only Play With Me", true, "Natsuki") {

    override fun getBackgroundColorString(): String = "0A1523"

    override fun getClubMember(): String = "natsuki_dark.png"

    override fun joyfulClubMember(): String = "natsuki_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "50A2C7"

    override fun getSecondaryForegroundColorString(): String = "157174"

    override fun getSelectionForegroundColorString(): String = "9CF5FF"

    override fun getSelectionBackgroundColorString(): String = "5C9AC6"

    override fun getTreeSelectionBackgroundColorString(): String = "5E72B5"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "0A039D"

    override fun getNotificationsColorString(): String = "5E94B8"

    override fun getContrastColorString(): String = "5D3652"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "26A8E8"

    override fun getForegroundColorString(): String = "2687C6"

    override fun getTextColorString(): String = "6BB2CE"

    override fun getEditorTabColorString(): String = contrastColorString
}