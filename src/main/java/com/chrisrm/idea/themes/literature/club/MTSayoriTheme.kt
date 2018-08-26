package com.chrisrm.idea.themes.literature.club

class MTSayoriTheme : MTDokiDokiTheme("mt.sayori", "Material Sayori", false, "Sayori") {

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
}
