package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class SyrenaTheme : AnthroTheme("syrena", "Syrena", true, "Syrena") {

  override fun getChibi(): String = "anthro/syrena.png"

  override fun getNormalChibi(): String = "anthro/syrena.png"

  override fun getBackgroundColorString(): String = "342222"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "392D2B"

  override fun getSecondaryForegroundColorString(): String = "B9AA9F"

  override fun getSelectionForegroundColorString(): String = "ffffff"

  override fun getSelectionBackgroundColorString(): String = "824747"

  override fun getTreeSelectionBackgroundColorString(): String = "824747"

  override fun getInactiveColorString(): String = "543939"

  override fun getMenuItemForegroundColor(): String = "B9AA9F"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "824747"

  override fun getNotificationsColorString(): String = "321f1f"

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

  override fun getNonProjectFileScopeColor(): String = "393229"

  override fun getTestScope(): String = "29392C"

  override fun getDisabledColorString(): String = "7F818F"

  override fun getSelectedButtonForegroundColor(): String = selectionForegroundColorString

  override fun getCompletionPopupBackgroundColor(): String = "432F2E"

  override fun getMenuBarColorString(): String = "321f1f"
}
