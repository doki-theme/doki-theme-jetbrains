package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class SyrenaTheme : AnthroTheme("syrena", "Syrena", true, "Syrena") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "362130"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "362839"

  override fun getSecondaryForegroundColorString(): String = "635959"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "F0E891"

  override fun getTreeSelectionBackgroundColorString(): String = "ECD95A"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "635959"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "C5BD68"

  override fun getNotificationsColorString(): String = "FFFBC7"

  override fun getHighlightColorString(): String = "FFEE96"

  override fun getContrastColorString(): String = "321f1f"

  override fun getBorderColorString(): String = "6c113a"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "635959"

  override fun getAccentColor(): String = MTAccents.TOMATO.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x321f1f)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x635959)

  override fun getTableSelectedColorString(): String = "FFFBA3"

  override fun getStartColor(): String = "6d4466"

  override fun getStopColor(): String = "ec1f62"

  override fun getNonProjectFileScopeColor(): String = "56412d"
}
