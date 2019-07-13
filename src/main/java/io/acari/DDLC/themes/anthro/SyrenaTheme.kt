package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class SyrenaTheme : AnthroTheme("syrena", "Syrena", true, "Syrena") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "392525"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "392D2B"

  override fun getSecondaryForegroundColorString(): String = "B9AA9F"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "824747"

  override fun getTreeSelectionBackgroundColorString(): String = "824747"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "B9AA9F"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "C5BD68"

  override fun getNotificationsColorString(): String = "fd985d"

  override fun getHighlightColorString(): String = "593E3D"

  override fun getContrastColorString(): String = "392525"

  override fun getBorderColorString(): String = "49181c"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "B9AA9F"

  override fun getForegroundColorString(): String = "dac8bb"

  override fun getTextColorString(): String = "dac8bb"

  override fun getAccentColor(): String = MTAccents.MAGMA.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x392525)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xB9AA9F)

  override fun getTableSelectedColorString(): String = "824747"

  override fun getStartColor(): String = "972e26"

  override fun getStopColor(): String = "fd7c4b"

  override fun getNonProjectFileScopeColor(): String = "39342C"
}
