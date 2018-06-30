package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

/**
 * Forged in the flames of battle by alex.
 */
class JustMonikaTheme: MTDokiDokiTheme("just.monika", "Just Monika", true, "Monika") {

    override fun getBackgroundColorString(): String = "1C230D"

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