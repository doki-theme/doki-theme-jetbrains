/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.idea.themes.literature.club;

import com.chrisrm.idea.MTAbstractTheme;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.stream.Stream;

public class DokiDokiTheme extends MTAbstractTheme {
  public static final String BACKGROUND = "fffcfc"; // 250, 250, 250
  public static final String FOREGROUND = "A7ADB0"; // 167, 173, 176
  public static final String CARET = "FFCC00"; // 255, 204, 0
  public static final String BORDER = "E6E6E6"; // 230, 230, 230
  public static final String TEXT = "A7ADB0"; // 167, 173, 176
  public static final String SELECTION_BACKGROUND = "FFFFFF"; // 84, 110, 122
  public static final String SELECTION_FOREGROUND = "000000";
  public static final String LABEL = "546E7A"; // 84, 110, 122
  public static final String SUB_LABEL = "B0BEC5"; // 176, 190, 197
  public static final String DISABLED = "81d7f7";//not really important

  public static final String SIDEBAR_HEADING = "CFD8DC"; // 207, 216, 220
  public static final String STATUS_LABEL = "90A4AE"; // 144, 164, 174
  public static final String INPUT_BORDER = "CFD8DC"; // 207, 216, 220

  public static final String BUTTON_BACKGROUND = "EAF3F2"; // 234, 243, 242
  public static final String BUTTON_FOREGROUND = "676E95"; // 103, 110, 149
  public static final String BUTTON_SELECTED = "CCEAE7"; // 204, 234, 231

  public static final String ACCENT_COLOR = "80CBC4"; // 128, 203, 196
  public static final String NON_PROJECT_FILES = "fdffce";
  public static final String TEST_FILES = "bbff7e";

  public DokiDokiTheme(@NotNull String id, String editorColorsScheme, boolean dark, String thingo) {
    super(id, editorColorsScheme, dark, thingo);
  }

  @Override
  public String getClubMember() {
    return "just_monika.png";
  }

  @NotNull
  @Override
  public String joyfulClubMember() {
    return "just_monika_joy.png";
  }

  @NotNull
  @Override
  public String getSelectionBackground() {
    return DokiDokiTheme.SELECTION_BACKGROUND;
  }

  @NotNull
  @Override
  public String getDisabled() {
    return DokiDokiTheme.DISABLED;
  }

  @Override
  public String getAccentColor() {
//    todo: this
    return "c6ff00";
  }

  @Override
  public String getExcludedColor() {
//    todo: this
    return "7E7E7E";
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public String getNonProjectFileScopeColor() {
    return MonikaTheme.Companion.getNON_PROJECT_FILES();
  }

  @Override
  public String getTestScope() {
    return MonikaTheme.Companion.getTEST_FILES();
  }

  @Override
  public String getEditorTabColorString() {
    return "def7a5";
  }

  //todo: important
  @Override
  public String getNotificationsColorString() {
    return "C3E88D";
  }

  //todo:important
  @Override
  public String getTreeSelectionBackgroundColorString() {
    return "546E50";
  }

  @Override
  public String getTreeSelectionForegroundColorString() {
    return "FFFFFF";
  }

  public String getButtonHighlightColorString() {
    return "F2F1F1";
  }

  @Override
  public String getHighlightColorString() {
    return "425B67";
  }

  @Override
  public String getTreeSelectionColorString() {
//    TODO: THIS
    return "546e50";
  }

  @Override
  public String getSecondBorderColorString() {
    return "d3e1e8";
  }

  @Override
  public String getTableSelectedColorString() {
    return "def7a5";
  }

  @Override
  public String getContrastColorString() {
    return "F4F4F4";
  }

  @Override
  public String getDisabledColorString() {
    return "000000";//TODO: IMPORTANT
  }

  @Override
  public String getSecondaryBackgroundColorString() {
    return "d8f26e";//TODO: IMPORTANT
  }


  public String getCaretColorString() {
    return "FFCC00";
  }

  @Override
  public String getInactiveColorString() {
    return "FFF4F2";
  }


  public String getButtonColorString() {
    return "FFF4F2";
  }

//  todo: imporant
  @Override
  public String getSelectionForegroundColorString() {
    return "447152";
  }

  //  todo: imporant
  @Override
  public String getMenuBarSelectionForegroundColorString() {
    return "7880a1";
  }

  //  todo: imporant
  @Override
  public String getMenuBarSelectionBackgroundColorString() {
    return "ffffff";
  }

  //todo: important
  @Override
  public String getSelectionBackgroundColorString() {
    return "99eb99";
  }

  @Override
  public String getTextColorString() {
    return "4d6e80";
  }

  //todo: this may be important
  @Override
  public String getForegroundColorString() {
    return "546E7A";
  }

  @Override
  public String getBackgroundColorString() {
    return "fffcfc";
  }

  @Override
  public String[] getTreeSelectionBackgroundResources() {
    return new String[] {
        "Tree.selectionBackground"
    };
  }

  @Override
  public String[] getTreeSelectionForegroundResources() {
    return new String[] {
        "Tree.selectionForeground"
    };
  }

  public String[] getButtonHighlightResources() {
    return new String[] {
        "Button.mt.color2",
        "Button.mt.selection.color2"
    };
  }

  @Override
  public String[] getHighlightResources() {
    return new String[] {
        "Focus.color",
        "TextField.separatorColor",
        "CheckBox.darcula.inactiveFillColor"
    };
  }

  @Override
  public String[] getSecondBorderResources() {
    return new String[] {
        "TabbedPane.highlight",
        "TabbedPane.selected",
        "TabbedPane.selectHighlight"
    };
  }

  @Override
  public String[] getTableSelectedResources() {
    return new String[] {
        "ProgressBar.halfColor",
        "MemoryIndicator.unusedColor"
    };
  }

  @Override
  public String[] getContrastResources() {
    return new String[] {
        "Table.stripedBackground",
        "ScrollBar.thumb",
        "Table.focusCellBackground",
        "ToolWindow.header.tab.selected.background",
        "ToolWindow.header.tab.selected.active.background",
        "material.contrast",
        "ActionToolbar.background",
        "Toolbar.background",
        "material.tab.backgroundColor",
        "TabbedPane.mt.tab.background",
        "TabbedPane.background",
        "OptionPane.background",
        "TabbedPane.highlight",
        "TabbedPane.darkShadow",
        "TabbedPane.shadow",
        "TabbedPane.borderColor",
        "Popup.Header.inactiveBackground",
        "Popup.Header.activeBackground",
    };
  }

  @Override
  public String[] getDisabledResources() {
    return new String[] {
        "MenuItem.disabledForeground",
        "ComboBox.disabledForeground"
    };
  }

  @Override
  public String[] getSecondaryBackgroundResources() {
    return new String[] {
        "Separator.foreground",
        "TextField.separatorColorDisabled",
        "TextField.inactiveForeground",
        "PasswordField.inactiveForeground",
        "Button.darcula.selection.color1",
        "Button.darcula.selection.color2",
        "Button.mt.selection.color1",
        "List.background",
        "ToolWindow.header.active.background",
        "ToolWindow.header.border.background",
        "material.disabled",
        "material.mergeCommits"
    };
  }

  public String[] getCaretResources() {
    return new String[] {
        "mt.monika.caretForeground"
    };
  }

  @Override
  public String[] getInactiveResources() {
    return new String[] {
        "Table.gridColor",
        "MenuBar.darcula.borderColor",
        "MenuBar.darcula.borderShadowColor",
        "CheckBox.darcula.disabledBorderColor1",
        "CheckBox.darcula.disabledBorderColor2"
    };
  }

  @Override
  public Stream<String> getSelectionForegroundResources() {
    return Stream.of(
            "Menu.selectionForeground",
            "Menu.acceleratorSelectionForeground",
            "MenuItem.selectionForeground",
            "MenuItem.acceleratorSelectionForeground",
            "Table.selectionForeground",
            "TextField.selectionForeground",
            "PasswordField.selectionForeground",
            "TextArea.selectionForeground",
            "Label.selectedForeground",
            "Button.darcula.selectedButtonForeground",
            "PopupMenu.background",
            "Separator.background",
            "Button.darcula.selectedButtonForeground",
            "PasswordField.selectionForeground",
            "TextField.selectionForeground",
            "TextArea.selectionForeground"

        );
  }

  @Override
  public String[] getSelectionBackgroundResources() {
    return new String[] {
        "inactiveCaption",
        "Menu.selectionBackground",
        "Menu.acceleratorSelectionBackground",
        "MenuItem.selectionBackground",
        "MenuItem.acceleratorSelectionBackground",
        "Table.selectionBackground",
        "TextField.selectionBackground",
        "PasswordField.selectionBackground",
        "Button.mt.selectedBackground",
        "TextArea.selectionBackground",
        "Label.selectedBackground",
        "Button.darcula.selectedButtonBackground",
        "PasswordField.selectionBackground",
        "TextField.selectionBackground",
        "TextArea.selectionBackground",
        "TabbedPane.selected"
    };
  }

  @Override
  public String[] getTextResources() {
    return new String[] {
        "Menu.acceleratorForeground",
        "MenuItem.acceleratorForeground",
        "material.tagColor",
        "material.primaryColor",
        "SearchEverywhere.shortcutForeground",
        "Tree.foreground"
    };
  }

  @Override
  public String[] getBackgroundResources() {
    return new String[] {
        "mt.monika.background",
        "mt.monika.textBackground",
        "mt.monika.inactiveBackground",
        "window",
        "activeCaption",
        "control",
        "PopupMenu.translucentBackground",
        "EditorPane.inactiveBackground",
        "Table.background",
        "MenuBar.disabledBackground",
        "MenuBar.shadow",
        "Desktop.background",
        "PopupMenu.background",
        "Separator.background",
        "MenuBar.background",
        "Separator.foreground",
        "TextField.background",
        "PasswordField.background",
        "FormattedTextField.background",
        "TextArea.background",
        "CheckBox.darcula.backgroundColor1",
        "CheckBox.darcula.backgroundColor2",
        "CheckBox.darcula.checkSignColor",
        "CheckBox.darcula.shadowColor",
        "CheckBox.darcula.shadowColorDisabled",
        "CheckBox.darcula.focusedArmed.backgroundColor1",
        "CheckBox.darcula.focusedArmed.backgroundColor2",
        "CheckBox.darcula.focused.backgroundColor1",
        "CheckBox.darcula.focused.backgroundColor2",
        "ComboBox.background",
        "ComboBox.disabledBackground",
        "RadioButton.darcula.selectionDisabledColor",
        "StatusBar.topColor",
        "StatusBar.top2Color",
        "StatusBar.bottomColor",
        "ToolTip.background",
        "Spinner.background",
        "SplitPane.highlight",
        "SearchEverywhere.background",
        "SidePanel.background",
        "DialogWrapper.southPanelDivider",
        "OnePixelDivider.background",
        "Dialog.titleColor",
        "SearchEverywhere.background",
        "RadioButton.background",
        "CheckBoxMenuItem.background",
        "RadioButtonMenuItem.background",
        "CheckBox.background",
        "ColorChooser.background",
        "Slider.background",
        "ToolWindow.header.background",
        "material.background"
    };
  }

  @Override
  public Stream<String> getButtonBackgroundResources() {
    return Stream.concat(super.getButtonBackgroundResources(), Stream.of(
        "Button.mt.color1",
        "Button.mt.background"
        ));
  }

  @NotNull
  public Color getBorderColor() {
    return Color.getHSBColor(62,91,149);
  }

  @Override
  public String[] getForegroundResources() {
    return new String[] {
        "mt.monika.foreground",
        "mt.monika.textForeground",
        "mt.monika.selectionForegroundInactive",
        "mt.monika.selectionInactiveForeground",
        "Label.foreground",
        "EditorPane.inactiveForeground",
        "CheckBox.foreground",
        "ComboBox.foreground",
        "RadioButton.foreground",
        "ColorChooser.foreground",
        "MenuBar.foreground",
        "RadioButtonMenuItem.foreground",
        "CheckBoxMenuItem.foreground",
        "PopupMenu.foreground",
        "Spinner.foreground",
        "TabbedPane.foreground",
        "TextField.foreground",
        "FormattedTextField.foreground",
        "PasswordField.foreground",
        "TextArea.foreground",
        "TextPane.foreground",
        "EditorPane.foreground",
        "ToolBar.foreground",
        "ToolTip.foreground",
        "List.foreground",
        "SearchEverywhere.foreground",
        "Label.foreground",
        "Label.disabledForeground",
        "Label.selectedDisabledForeground",
        "Table.foreground",
        "TableHeader.foreground",
        "ToggleButton.foreground",
        "Table.sortIconColor",
        "material.branchColor",
        "material.foreground",
        "TitledBorder.titleColor"
    };
  }

}
