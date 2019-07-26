package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import javax.swing.plaf.ColorUIResource

class SanyaTheme : AnthroTheme("sanya", "Sanya", true, "Sanya") {

  override fun getChibi(): String = "athro/sanya.png"

  override fun getNormalChibi(): String = "athro/sanya.png"

  override fun getBackgroundColorString(): String = "362130"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "362839"

  override fun getSecondaryForegroundColorString(): String = "ffffff"

  override fun getSelectionForegroundColorString(): String = "ffffff"

  override fun getSelectionBackgroundColorString(): String = "af1f51"

  override fun getTreeSelectionBackgroundColorString(): String = "af1f51"

  override fun getInactiveColorString(): String = "883552"

  override fun getMenuItemForegroundColor(): String = "cebcb8"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "af1f51"

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

  override fun getDisabledColorString(): String = "525C6F"

  override fun getTableSelectedColorString(): String = "af1f51"

  override fun getStartColor(): String = "6d4466"

  override fun getStopColor(): String = "ec1f62"

  override fun getNonProjectFileScopeColor(): String = "38352F"

  override fun getTestScope(): String = "28382D"


}
