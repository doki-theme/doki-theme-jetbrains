package com.chrisrm.idea.themes.literature.club

class MTYuriTheme : MTDokiDokiTheme("mt.yuri", "Material Yuri", false, "Yuri") {

    override fun getBackgroundColorString(): String = "e8e4ff"

    override fun getClubMember(): String = "yuri.png"

    override fun getSecondaryBackgroundColorString(): String = "bbb8ff"

    override fun getSecondaryForegroundColorString(): String = "562474"

    override fun getSelectionForegroundColorString(): String = "c06fff"

    override fun getSelectionBackgroundColorString(): String = "e4caff"

    override fun getTreeSelectionBackgroundColorString(): String = "d3a5fa"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "7c0e9d"

    override fun getNotificationsColorString(): String = "d3ceff"

    override fun getContrastColorString(): String = "ccaaff"

    override fun getEditorTabColorString(): String = contrastColorString
}
