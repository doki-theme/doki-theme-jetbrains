package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

/**
 * C̸̮͙̭̀h̷̯̯̋͊̅ä̶̠̟̝́ȑ̸̟̝̪̇a̵̞̙͝c̴̩͝t̵̳͉̆ẻ̴̫r̸̨̢͑ ̷̡̛̛͍͐n̶̡͚̻̽o̵͈͌̈͝t̴̻͚͑̏̽ ̴̰͌f̷̫͛ò̴̠̺̝ú̵̧̲̘̐̚ṅ̸̠̠̰d̵̦͎̲͠͠
 */
class DeletedCharacterTheme: MTDokiDokiTheme("deleted.character", "Deleted Character", true, "Š̸̘͚̼͎̯̙̣̱̎̋̐͒a̴̖̟̠̳̤͙̟͂̂͑̐͜ỷ̵̧̨̞̠̖̠o̴̧͍̗̬̎̓͆̔͝ͅr̴̡̮̟͈͠ͅi̴̡̨͓͈̬̗̺̍́̃̇̓") {

    override fun getBackgroundColorString(): String = "0A1523"

    override fun getClubMember(): String = "sayori_dark.png"

    override fun joyfulClubMember(): String = "sayori.png"

    override fun getSecondaryBackgroundColorString(): String = "50A2C7"

    override fun getSecondaryForegroundColorString(): String = "157174"

    override fun getSelectionForegroundColorString(): String = "9CF5FF"

    override fun getSelectionBackgroundColorString(): String = "5C9AC6"

    override fun getTreeSelectionBackgroundColorString(): String = "5E72B5"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "0A039D"

    override fun getNotificationsColorString(): String = "5E94B8"

    override fun getContrastColorString(): String = "9ABBFF"


    override fun getEditorTabColorString(): String = contrastColorString
}