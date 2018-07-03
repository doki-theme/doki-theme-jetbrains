package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.MTDokiDokiTheme

/**
 * C̸̮͙̭̀h̷̯̯̋͊̅ä̶̠̟̝́ȑ̸̟̝̪̇a̵̞̙͝c̴̩͝t̵̳͉̆ẻ̴̫r̸̨̢͑ ̷̡̛̛͍͐n̶̡͚̻̽o̵͈͌̈͝t̴̻͚͑̏̽ ̴̰͌f̷̫͛ò̴̠̺̝ú̵̧̲̘̐̚ṅ̸̠̠̰d̵̦͎̲͠͠
 */
class DeletedCharacterTheme: MTDokiDokiTheme("deleted.character", "Š̸̘͚̼͎̯̙̣̱̎̋̐͒a̴̖̟̠̳̤͙̟͂̂͑̐͜ỷ̵̧̨̞̠̖̠o̴̧͍̗̬̎̓͆̔͝ͅr̴̡̮̟͈͠ͅi̴̡̨͓͈̬̗̺̍́̃̇̓", true, "D̶̯̘̻͖̅̔͝e̴̯͉̞̟͑͂͋͒l̴̛̪̆̓ȩ̶̼̖̲́͝t̶͙͉̉͐͊ë̷̞̍̈d̷͔̹͇̓͆̎̒ ̶̪͌͠͝Ć̵̱̲̓̾̌ḫ̶̀a̴̼̱̮̬͉͌̉̽r̶̳̥̮͎̐͒ȁ̶̝͈̖̤̀̇̈́c̶̹̳̖̞̫̿̆̐͆͝t̵͍͈͍͋ę̷̮̜̖͒r̵̭̠̽̀") {

    override fun getBackgroundColorString(): String = "0A1523"

    override fun getSecondaryBackgroundColorString(): String = "50A2C7"

    override fun getSecondaryForegroundColorString(): String = "237417"

    override fun getSelectionForegroundColorString(): String = "5FC652"

    override fun getSelectionBackgroundColorString(): String = "ACFFAF"

    override fun getTreeSelectionBackgroundColorString(): String = "65B564"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "2D9D04"

    override fun getNotificationsColorString(): String = "76B87B"

    override fun getContrastColorString(): String = "ADFF8D"


    override fun getEditorTabColorString(): String = contrastColorString
}