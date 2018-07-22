package com.chrisrm.idea.themes.literature.club

class MTNatsukiTheme : MTDokiDokiTheme("mt.natsuki", "Material Natsuki", false, "Natsuki") {

    override fun getBackgroundColorString(): String = "fff3fc"

    override fun getClubMember(): String = "natsuki.png"

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
}
