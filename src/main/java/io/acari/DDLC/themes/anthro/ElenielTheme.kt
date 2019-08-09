package io.acari.DDLC.themes.anthro

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.AnthroTheme
import java.util.stream.Stream
import javax.swing.plaf.ColorUIResource

class ElenielTheme : AnthroTheme("eleniel", "Eleniel", true, "Eleniel") {

  override fun getChibi(): String = "anthro/eleniel.png"

  override fun getNormalChibi(): String = "anthro/eleniel.png"

  override fun getBackgroundColorString(): String = "3b363e"

  //todo: remove these
  override fun getClubMember(): String = "sayori.png"

  override fun joyfulClubMember(): String = "sayori_joy.png"

  override fun getSecondaryBackgroundColorString(): String = "413b42"

  override fun getSecondaryForegroundColorString(): String = "9e9199"

  override fun getSelectionForegroundColorString(): String = "ffffff"

  override fun getSelectionBackgroundColorString(): String = "897b89"

  override fun getTreeSelectionBackgroundColorString(): String = "48414B"

  override fun getInactiveColorString(): String = "423C45"

  override fun getDisabledColorString(): String = "525C6F"

  override fun getMenuItemForegroundColor(): String = "9e9199"//48414B

  override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

  override fun getMenuBarSelectionBackgroundColorString(): String = "807484"

  override fun getNotificationsColorString(): String = "3B3541"

  override fun getHighlightColorString(): String = "62576b"

  override fun getContrastColorString(): String = "3c363f"

  override fun getBorderColorString(): String = "423c42"

  override fun getEditorTabColorString(): String = contrastColorString

  override fun getButtonBackgroundColor(): String = contrastColorString

  override fun getButtonForegroundColor(): String = "80747B"

  override fun getAccentColor(): String = MTAccents.SOFT_SPRINGS.hexColor

  override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x3c363f)

  override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x9e9199)

  override fun getTableSelectedColorString(): String = "48414B"

  override fun getStartColor(): String = "6e6773"

  override fun getStopColor(): String = "76ffff"

  override fun getForegroundColorString(): String = "aca3a7"

  override fun getTextColorString(): String = "aca3a7"

  override fun getNonProjectFileScopeColor(): String = "483d3e"

  override fun getTestScope(): String = "3A5841"

  override fun getSelectedButtonForegroundColor(): String = selectionForegroundColorString

  override fun getCompletionPopupBackgroundColor(): String = "FFFFF9"

  override fun getMenuBarColorString(): String = "3B3541"

  private val naughtyBackgroundSet: Set<String> = setOf(
      "Button.select"
  )


  override fun getSelectionBackgroundResources(): Stream<String> {
    return super.getSelectionBackgroundResources()
        .filter { !naughtyBackgroundSet.contains(it) }
  }

  override fun getBackgroundResources(): Stream<String> {
    return Stream.concat(super.getBackgroundResources(),
        naughtyBackgroundSet.stream())
  }
}
