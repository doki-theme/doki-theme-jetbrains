package io.acari.DDLC.themes.dark

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.plaf.ColorUIResource

/**
 * C̸̮͙̭̀h̷̯̯̋͊̅ä̶̠̟̝́ȑ̸̟̝̪̇a̵̞̙͝c̴̩͝t̵̳͉̆ẻ̴̫r̸̨̢͑ ̷̡̛̛͍͐n̶̡͚̻̽o̵͈͌̈͝t̴̻͚͑̏̽ ̴̰͌f̷̫͛ò̴̠̺̝ú̵̧̲̘̐̚ṅ̸̠̠̰d̵̦͎̲͠͠
 */
class DeletedCharacterTheme : DokiDokiTheme("deleted.character", "Deleted Character", true, "Š̸̘͚̼͎̯̙̣̱̎̋̐͒a̴̖̟̠̳̤͙̟͂̂͑̐͜ỷ̵̧̨̞̠̖̠o̴̧͍̗̬̎̓͆̔͝ͅr̴̡̮̟͈͠ͅi̴̡̨͓͈̬̗̺̍́̃̇̓") {

    override fun getCompletionPopupBackgroundColor(): String = "233A5B"

    override fun getBackgroundColorString(): String = "0E1D31"

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0x0e1d31)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x2687C6)

    override fun getTableSelectedColorString(): String = "26374C"

    override fun getAccentColor(): String {
        return MTAccents.CYAN.hexColor
    }

    override fun getClubMember(): String = "sayori_dark.png"

    override fun joyfulClubMember(): String = "sayori_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "111B28"

    override fun getSecondaryForegroundColorString(): String = "157174"

    override fun getSelectionForegroundColorString(): String = "9CF5FF"

    override fun getSelectionBackgroundColorString(): String = "324964"

    override fun getTreeSelectionBackgroundColorString(): String = "1D358C"

    override fun getInactiveColorString(): String = "101C4B"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "7C081A"

    override fun getNotificationsColorString(): String = "335063"

    override fun getContrastColorString(): String = "1B323F"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "26A8E8"

    override fun getForegroundColorString(): String = "2687C6"

    override fun getSelectedButtonForegroundColor(): String = "ffffff"

    override fun getTextColorString(): String = "6BB2CE"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getNonProjectFileScopeColor(): String = "102121"

    override fun getTestScope(): String = "0c3118"

    override fun getSecondBorderColorString(): String = "1B467C"

    override fun getDisabledColorString(): String = "232323"

    override fun getHighlightColorString(): String = "07356A"

    override fun getStartColor(): String = "0A74BB"

    override fun getStopColor(): String = "11FFFD"

    override fun getMenuBarColorString(): String = "8B4646"
}