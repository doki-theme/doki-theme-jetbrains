package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class WylaTheme : AnthroTheme("wyla", "Wyla", true, "Wyla") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "2f2329"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "2e2b32"

  override fun getSecondaryForegroundColorString(): String = "635959"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "40434c"

  override fun getTreeSelectionBackgroundColorString(): String = "40434c"

  override fun getInactiveColorString(): String = "2a272e"

  override fun getMenuItemForegroundColor(): String = "635959"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "40434c"

  override fun getNotificationsColorString(): String = "4a5b75"

  override fun getHighlightColorString(): String = "4e393e"

  override fun getContrastColorString(): String = "2f2329"

  override fun getBorderColorString(): String = "2b2a32"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "635959"

  override fun getAccentColor(): String = MTAccents.MERRIGOLD.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x312428)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x635959)

  override fun getTableSelectedColorString(): String = "40434c"

  override fun getStartColor(): String = "6c341d"

  override fun getStopColor(): String = "eeb416"

  override fun getNonProjectFileScopeColor(): String = "312F20S"
}
