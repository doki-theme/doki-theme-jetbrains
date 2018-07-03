package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

/**
 * Forged in the flames of battle by alex.
 */
class DeletedCharacterTheme: MTDokiDokiTheme("deleted.character", "Deleted Character", true, "Deleted") {

    override fun getBackgroundColorString(): String = "0A1523"

    override fun getSecondaryBackgroundColorString(): String = "50A2C7"

    override fun getSecondaryForegroundColorString(): String = "237417"

    override fun getSelectionForegroundColorString(): String = "5FC652"

    override fun getSelectionBackgroundColorString(): String = "ACFFAF"

    override fun getTreeSelectionBackgroundColorString(): String = "65B564"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "2D9D04"

    override fun getNotificationsColorString(): String = "76B87B"

    override fun getContrastColorString(): String = "ADFF8D"


    override fun getEditorTabColorString(): String = contrastColorString
}