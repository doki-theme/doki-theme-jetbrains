package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

class NeeraTheme : DokiDokiTheme("neera", "Neera", false, "Neera") {

  override fun getBackgroundColorString(): String = "D7FEFC"

  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "c7f2ff"

  override fun getSecondaryForegroundColorString(): String = "256fe2"

  override fun getSelectionForegroundColorString(): String = "256fe2"

  override fun getSelectionBackgroundColorString(): String = "99ebf0"

  override fun getTreeSelectionBackgroundColorString(): String = "67C1EC"

  override fun getInactiveColorString(): String = "C2D2FF"

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "de0a22"

  override fun getNotificationsColorString(): String = "c3e8ff"

  override fun getHighlightColorString(): String = "aafffe"

  override fun getContrastColorString(): String = "D3FCFF"

  override fun getBorderColorString(): String = "B8E7FF"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "00559A"

  override fun getAccentColor(): String = MTAccents.CYAN.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xD4FCFE)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x5A75EA)

  override fun getTableSelectedColorString(): String = "A7CFFF"

  override fun getStartColor(): String = "0D96F1"

  override fun getStopColor(): String = "3DFFF3"

  override fun getNonProjectFileScopeColor(): String = "dcffe9"
}
