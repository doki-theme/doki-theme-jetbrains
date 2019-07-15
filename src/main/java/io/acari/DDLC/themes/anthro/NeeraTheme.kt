package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class NeeraTheme : AnthroTheme("neera", "Neera", false, "Neera") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "fbfff5"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "FFFDF4"

  override fun getSecondaryForegroundColorString(): String = "546E7A"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "F0E891"

  override fun getTreeSelectionBackgroundColorString(): String = "ECD95A"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "546E7A"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "C5BD68"

  override fun getNotificationsColorString(): String = "FFFBC7"

  override fun getHighlightColorString(): String = "FFEE96"

  override fun getContrastColorString(): String = "fffef7"

  override fun getBorderColorString(): String = "FFFED4"

  override fun getForegroundColorString(): String = "546E7A"

  override fun getTextColorString(): String = "546E7A"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "546E7A"

  override fun getAccentColor(): String = MTAccents.DAISY.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xfefff5)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xEAA900)

  override fun getTableSelectedColorString(): String = "FFFBA3"

  override fun getStartColor(): String = "EBDA50"

  override fun getStopColor(): String = "FCFFA5"

  override fun getNonProjectFileScopeColor(): String = "FFF8D3"
}