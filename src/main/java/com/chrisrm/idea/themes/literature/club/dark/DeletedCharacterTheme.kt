package com.chrisrm.idea.themes.literature.club.dark

import com.chrisrm.idea.themes.literature.club.DokiDokiTheme
import java.util.stream.Stream

/**
 * C̸̮͙̭̀h̷̯̯̋͊̅ä̶̠̟̝́ȑ̸̟̝̪̇a̵̞̙͝c̴̩͝t̵̳͉̆ẻ̴̫r̸̨̢͑ ̷̡̛̛͍͐n̶̡͚̻̽o̵͈͌̈͝t̴̻͚͑̏̽ ̴̰͌f̷̫͛ò̴̠̺̝ú̵̧̲̘̐̚ṅ̸̠̠̰d̵̦͎̲͠͠
 */
class DeletedCharacterTheme : DokiDokiTheme("deleted.character", "Deleted Character", true, "Š̸̘͚̼͎̯̙̣̱̎̋̐͒a̴̖̟̠̳̤͙̟͂̂͑̐͜ỷ̵̧̨̞̠̖̠o̴̧͍̗̬̎̓͆̔͝ͅr̴̡̮̟͈͠ͅi̴̡̨͓͈̬̗̺̍́̃̇̓") {

    override fun getBackgroundColorString(): String = "0E1D31"

    override fun getClubMember(): String = "sayori_dark.png"

    override fun joyfulClubMember(): String = "sayori_dark_joy.png"

    override fun getSecondaryBackgroundColorString(): String = "111B28"

    override fun getSecondaryForegroundColorString(): String = "157174"

    override fun getSelectionForegroundColorString(): String = "9CF5FF"

    override fun getSelectionBackgroundColorString(): String = "324964"

    override fun getTreeSelectionBackgroundColorString(): String = "1D358C"

    override fun getMenuBarSelectionForegroundColorString(): String = "ffffff"

    override fun getMenuBarSelectionBackgroundColorString(): String = "7C081A"

    override fun getNotificationsColorString(): String = "335063"

    override fun getContrastColorString(): String = "1B323F"

    override fun getButtonBackgroundColor(): String = contrastColorString

    override fun getButtonForegroundColor(): String = "26A8E8"

    override fun getForegroundColorString(): String = "2687C6"

    override fun getTextColorString(): String = "6BB2CE"

    override fun getEditorTabColorString(): String = contrastColorString

    override fun getNonProjectFileScopeColor(): String = "102121"

    override fun getTestScope(): String = "0c3118"

    override fun getSecondBorderColorString(): String = "1B467C"

    override fun getDisabledColorString(): String = "232323"

    /**
     * TODO: DO NOT NEED TO DO THIS, THE INCLUSION OF A _window.properties file fixes this :|
     */
    override fun getPropertyStream(): Stream<Pair<String, String>> {
        return Stream.of(
                Pair("deleted.character.background", "12131f"),
                Pair("Panel.background", "222a2f"),
                Pair("Menu.foreground", "fffefd"),
                Pair("PopupMenu.background", "8B4646"),
                Pair("Menu.background", "8B4646"),
                Pair("MenuBar.background", "8B4646"),
                Pair("Menu.acceleratorForeground", "ffffff"),
                Pair("MenuBar.foreground", "fffefd"),
                Pair("Button.mt.foreground", "B2EDEE"),
                Pair("MenuItem.selectionForeground", "FFFFFF"),
                Pair("MenuItem.selectionBackground", "aa122a"),
                Pair("MenuItem.foreground", "ffffff"),
                Pair("Menu.selectionForeground", "FFFFFF"),
                Pair("Menu.selectionBackground", "aa122a"),
                Pair("Notifications.background", "5873A5"),
                Pair("Notifications.borderColor", "8DD4FF"),
                Pair("Table.selectionForeground", "000000"),
                Pair("Table.selectionBackground", "a5f7fc"),
                Pair("Table.highlightOuter", "44dfff"),
                Pair("Autocomplete.selectionBackground", "274E5E"),
                Pair("Autocomplete.selectionForeground", "A2E0EA"),
                Pair("Autocomplete.selectionForegroundGreyed", "FFFFFF"),
                Pair("Autocomplete.background", "242E5A"),
                Pair("Autocomplete.foreground", "8DABFE"),
                Pair("Autocomplete.selectedGreyedForeground", "2191FA"),
                Pair("Autocomplete.prefixForeground", "67D0FF"),
                Pair("Autocomplete.selectedPrefixForeground", "A6F3FF"),
                Pair("Autocomplete.selectionUnfocus", "274E5E"),
                Pair("Button.mt.color2", "3B4C77"),
                Pair("Button.mt.primary.color", "35627C"),
                Pair("Button.mt.selection.color1", "376BA8"),
                Pair("Button.mt.selection.color2", "0A73AB"),
                Pair("Button.mt.background", "3B4C77"),
                Pair("Button.mt.selectedForeground", "FFFFFF"),
                Pair("Button.mt.color1", "3B4C77"),
                Pair("ToolBar.background", "3B4C77"),
                Pair("EditorPane.caretForeground", "3B4C77"),
                Pair("SearchEverywhere.background", "aaddff"),
                Pair("SearchEverywhere.foreground", "497de7"),
                Pair("SearchEverywhere.shortcutForeground", "B0BEC5"),
                Pair("deleted.character.textBackground", "12131f"),
                Pair("deleted.character.foreground", "6787A7"),
                Pair("deleted.character.textForeground", "6787A7"),
                Pair("deleted.character.caretForeground", "FFCC00"),
                Pair("deleted.character.inactiveBackground", "12131f"),
                Pair("deleted.character.selectionForeground", "FFFFFF"),
                Pair("deleted.character.selectionBackgroundInactive", "D2D4D5"),
                Pair("deleted.character.selectionInactiveBackground", "D2D4D5"),
                Pair("deleted.character.selectionForegroundInactive", "6787A7"),
                Pair("deleted.character.selectionInactiveForeground", "6787A7")
        )
    }
}