package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class CleoTheme : AnthroTheme("cleo", "Cleo", false, "Cleo") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "e6e6f1"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "dcdbe9"

  override fun getSecondaryForegroundColorString(): String = "36363a"

  override fun getSelectionForegroundColorString(): String = "2e509f"

  override fun getSelectionBackgroundColorString(): String = "55545d"

  override fun getTreeSelectionBackgroundColorString(): String = "857b81"

  override fun getInactiveColorString(): String = "b0acb7"

  override fun getMenuItemForegroundColor(): String = "252529"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "443f42"

  override fun getNotificationsColorString(): String = "295369"

  override fun getHighlightColorString(): String = "fdfeff"

  override fun getContrastColorString(): String = "e7e7ed"

  override fun getBorderColorString(): String = "d0cdd5"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "252427"

  override fun getAccentColor(): String = MTAccents.ABYSS.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xe7e7ed)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x0F111A)

  override fun getTableSelectedColorString(): String = "857b81"

  override fun getStartColor(): String = "252d33"

  override fun getStopColor(): String = "6accfc"

  override fun getNonProjectFileScopeColor(): String = "DADBC5"
}
