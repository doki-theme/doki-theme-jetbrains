package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class SanyaTheme : AnthroTheme("sanya", "Sanya", true, "Sanya") {

  override fun getChibi(): String = "athro/neera_temp.png"

  override fun getNormalChibi(): String = "athro/neera_temp.png"

  override fun getBackgroundColorString(): String = "362130"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "362839"

  override fun getSecondaryForegroundColorString(): String = "cebcb8"

  override fun getSelectionForegroundColorString(): String = "E2CB14"

  override fun getSelectionBackgroundColorString(): String = "F0E891"

  override fun getTreeSelectionBackgroundColorString(): String = "ECD95A"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "cebcb8"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "C5BD68"

  override fun getNotificationsColorString(): String = "FFFBC7"

  override fun getHighlightColorString(): String = "403046"

  override fun getContrastColorString(): String = "341F2D"

  override fun getBorderColorString(): String = "5C3141"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getForegroundColorString(): String = "f8f8f8"

  override fun getTextColorString(): String = "f8f8f8"

  override fun getButtonForegroundColor(): String = "cebcb8"

  override fun getAccentColor(): String = MTAccents.ACID_LIME.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x332638)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xcebcb8)

  override fun getTableSelectedColorString(): String = "FFFBA3"

  override fun getStartColor(): String = "6d4466"

  override fun getStopColor(): String = "ec1f62"

  override fun getNonProjectFileScopeColor(): String = "56412d"
}
