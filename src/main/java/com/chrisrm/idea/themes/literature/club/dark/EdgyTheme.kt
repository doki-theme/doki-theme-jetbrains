package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

//todo: should probably rename these to something more expected :|
class EdgyTheme: MTDokiDokiTheme("yuri.dark", "Edgy", true, "Yuri") {

    override fun getBackgroundColorString(): String = "322A45"

    override fun getClubMember(): String = "yuri_dark.png"

    override fun joyfulClubMember(): String = "yuri.png"

    override fun getSecondaryBackgroundColorString(): String = "9A5DC7"

    override fun getSecondaryForegroundColorString(): String = "543674"

    override fun getSelectionForegroundColorString(): String = "B39EFF"

    override fun getSelectionBackgroundColorString(): String = "7C6DC6"

    override fun getTreeSelectionBackgroundColorString(): String = "7D6CB5"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "56009D"

    override fun getNotificationsColorString(): String = "8E56B8"

    override fun getContrastColorString(): String = "5A456D"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "8262E9"

    override fun getForegroundColorString(): String = "8A77DE"

    override fun getTextColorString(): String = "916ACE"


}