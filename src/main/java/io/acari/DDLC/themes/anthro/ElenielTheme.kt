package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class ElenielTheme : AnthroTheme("eleniel", "Eleniel", true, "Eleniel") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "3b363e"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "413b42"

  override fun getSecondaryForegroundColorString(): String = "635959"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "897b89"

  override fun getTreeSelectionBackgroundColorString(): String = "ECD95A"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "635959"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "C5BD68"

  override fun getNotificationsColorString(): String = "FFFBC7"

  override fun getHighlightColorString(): String = "FFEE96"

  override fun getContrastColorString(): String = "3c363f"

  override fun getBorderColorString(): String = "423c42"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "635959"

  override fun getAccentColor(): String = MTAccents.SOFT_SPRINGS.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x3c363f)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x635959)

  override fun getTableSelectedColorString(): String = "FFFBA3"

  override fun getStartColor(): String = "6e6773"

  override fun getStopColor(): String = "76ffff"

  override fun getNonProjectFileScopeColor(): String = "56412d"
}
