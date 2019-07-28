package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import com.intellij.ui.JBColor
import io.acari.DDLC.themes.AnthroTheme
import java.util.stream.Stream
import javax.swing.UIManager
import javax.swing.plaf.ColorUIResource

class NeeraTheme : AnthroTheme("neera", "Neera", false, "Neera") {

  override fun getChibi(): String = "athro/neera.png"

  override fun getNormalChibi(): String = "athro/neera.png"

  override fun getBackgroundColorString(): String = "fffffb"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "fffff7"

  override fun getSecondaryForegroundColorString(): String = "546E7A"

  override fun getSelectionForegroundColorString(): String = "A8930A"

  override fun getTreeSelectionForegroundColorString(): String = selectionForegroundColorString

  override fun getSelectionBackgroundColorString(): String = "F0E891"

  override fun getTreeSelectionBackgroundColorString(): String = "fff3b4"

  override fun getInactiveColorString(): String = "FFFFBC"

  override fun getMenuItemForegroundColor(): String = "546E7A"

  override fun getMenuBarSelectionForegroundColorString(): String = selectionForegroundColorString

  override fun getMenuBarSelectionBackgroundColorString(): String = "fff3b4"

  override fun getNotificationsColorString(): String = "fefefe"

  override fun getHighlightColorString(): String = "fff3b4"

  override fun getContrastColorString(): String = "fffef7"

  override fun getBorderColorString(): String = "dadada"

  override fun getForegroundColorString(): String = "546E7A"

  override fun getTextColorString(): String = "546E7A"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "546E7A"

  override fun getDisabledColorString(): String = "7F818F"

  override fun getAccentColor(): String = MTAccents.DAISY.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xfffffb)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0xEAA900)

  override fun getTableSelectedColorString(): String = "FFFBA3"

  override fun getStartColor(): String = "EBDA50"

  override fun getStopColor(): String = "FCFFA5"

  override fun getNonProjectFileScopeColor(): String = "FFF7DF"

  override fun getTestScope(): String = "D7FFDC"
}
