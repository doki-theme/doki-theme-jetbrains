package io.acari.DDLC.themes.dark

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

class OnlyPlayWithMeTheme : DokiDokiTheme("natsuki.dark", "Only Play With Me", true, "Natsuki") {

  override fun getCompletionPopupBackgroundColor(): String = "5B1D44"

  override fun getBackgroundColorString(): String = "240E1E"

  override fun getClubMember(): String = "natsuki_dark.png"

  override fun joyfulClubMember(): String = "natsuki_dark_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "330A2B"

  override fun getSecondaryForegroundColorString(): String = "BB47A8"

  override fun getSelectionForegroundColorString(): String = "D671B0"

  override fun getSelectionBackgroundColorString(): String = "5D2960"

  override fun getTreeSelectionBackgroundColorString(): String = "B54E8E"

  override fun getInactiveColorString(): String = "6F3654"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "9D0064"

  override fun getNotificationsColorString(): String = "562447"

  override fun getContrastColorString(): String = "56324C"

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "E85EDB"

  override fun getSelectedButtonForegroundColor(): String = "ffffff"

  override fun getForegroundColorString(): String = "DC508F"

  override fun getTextColorString(): String = "FF78BA"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getNonProjectFileScopeColor(): String = "2a1b1f"

  override fun getTestScope(): String = "0f2a00"

  override fun getSecondBorderColorString(): String = "5A1943"

  override fun getDisabledColorString(): String = "232323"

  override fun getAccentColor(): String {
    return MTAccents.FUCHSIA.hexColor
  }

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x231921)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xDC508F)

  override fun getTableSelectedColorString(): String = "4C1A45"

  override fun getHighlightColorString(): String = "550D3B"

  override fun getStartColor(): String = "BB0A69"

  override fun getStopColor(): String = "F650FF"

  override fun getMenuBarColorString(): String = "874C57"
}