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

package com.chrisrm.idea.themes;

import com.chrisrm.idea.MTAbstractTheme;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class MTMonikaTheme extends MTAbstractTheme {
  public static final String BACKGROUND = "fffcfc"; // 250, 250, 250
  public static final String FOREGROUND = "A7ADB0"; // 167, 173, 176
  public static final String CARET = "FFCC00"; // 255, 204, 0
  public static final String BORDER = "E6E6E6"; // 230, 230, 230
  public static final String TEXT = "A7ADB0"; // 167, 173, 176
  public static final String SELECTION_BACKGROUND = "546E7A"; // 84, 110, 122
  public static final String SELECTION_FOREGROUND = "FFFFFF";
  public static final String LABEL = "546E7A"; // 84, 110, 122
  public static final String SUB_LABEL = "B0BEC5"; // 176, 190, 197
  public static final String DISABLED = "e20090";

  public static final String SIDEBAR_HEADING = "CFD8DC"; // 207, 216, 220
  public static final String STATUS_LABEL = "90A4AE"; // 144, 164, 174
  public static final String INPUT_BORDER = "CFD8DC"; // 207, 216, 220

  public static final String BUTTON_BACKGROUND = "EAF3F2"; // 234, 243, 242
  public static final String BUTTON_FOREGROUND = "676E95"; // 103, 110, 149
  public static final String BUTTON_SELECTED = "CCEAE7"; // 204, 234, 231

  public static final String ACCENT_COLOR = "80CBC4"; // 128, 203, 196

  public MTMonikaTheme() {
    super("mt.monika", "Material Monika", false);
  }

  @NotNull
  @Override
  public String getSelectionBackground() {
    return MTMonikaTheme.SELECTION_BACKGROUND;
  }

  @NotNull
  @Override
  public String getDisabled() {
    return MTMonikaTheme.DISABLED;
  }

  @Override
  protected String getNotificationsColorString() {
    return "C3E88D";
  }

  @Override
  protected String getTreeSelectionColorString() {
    return "546E7A50";
  }

  protected String getButtonHighlightColorString() {
    return "F2F1F1";
  }

  @Override
  protected String getHighlightColorString() {
    return "425B67";
  }

  @Override
  protected String getSecondBorderColorString() {
    return "d3e1e8";
  }

  @Override
  protected String getTableSelectedColorString() {
    return "BDE3DF";
  }

  @Override
  protected String getContrastColorString() {
    return "F4F4F4";
  }

  @Override
  protected String getDisabledColorString() {
    return "ff021a";
  }

  @Override
  protected String getSecondaryBackgroundColorString() {
    return "d8f26e";
  }


  protected String getCaretColorString() {
    return "FFCC00";
  }

  @Override
  protected String getInactiveColorString() {
    return "FFF4F2";
  }

  @Override
  protected String getSelectionForegroundColorString() {
    return "FFFFFF";
  }

  @Override
  protected String getSelectionBackgroundColorString() {
    return "D2D4D5";
  }

  @Override
  protected String getTextColorString() {
    return "7E939E";
  }

  @Override
  protected String getForegroundColorString() {
    return "546E7A";
  }

  @Override
  protected String getBackgroundColorString() {
    return "fffcfc";
  }

  @Override
  protected String[] getTreeSelectionResources() {
    return new String[] {
        "Tree.selectionBackground"
    };
  }

  protected String[] getButtonHighlightResources() {
    return new String[] {
        "Button.mt.color2",
        "Button.mt.selection.color2"
    };
  }

  @Override
  protected String[] getHighlightResources() {
    return new String[] {
        "Focus.color",
        "TextField.separatorColor",
        "CheckBox.darcula.inactiveFillColor"
    };
  }

  @Override
  protected String[] getSecondBorderResources() {
    return new String[] {
        "TabbedPane.highlight",
        "TabbedPane.selected",
        "TabbedPane.selectHighlight"
    };
  }

  @Override
  protected String[] getTableSelectedResources() {
    return new String[] {
        "ProgressBar.halfColor",
        "MemoryIndicator.unusedColor"
    };
  }

  @Override
  protected String[] getContrastResources() {
    return new String[] {
        "Table.stripedBackground",
        "ScrollBar.thumb",
        "Table.focusCellBackground",
        "ToolWindow.header.tab.selected.background",
        "ToolWindow.header.tab.selected.active.background",
        "material.contrast",
        "ActionToolbar.background",
        "Toolbar.background"
    };
  }

  @Override
  protected String[] getDisabledResources() {
    return new String[] {
        "MenuItem.disabledForeground",
        "ComboBox.disabledForeground"
    };
  }

  @Override
  protected String[] getSecondaryBackgroundResources() {
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

  protected String[] getCaretResources() {
    return new String[] {
        "mt.monika.caretForeground"
    };
  }

  @Override
  protected String[] getInactiveResources() {
    return new String[] {
        "Table.gridColor",
        "MenuBar.darcula.borderColor",
        "MenuBar.darcula.borderShadowColor",
        "CheckBox.darcula.disabledBorderColor1",
        "CheckBox.darcula.disabledBorderColor2"
    };
  }

  @Override
  protected String[] getSelectionForegroundResources() {
    return new String[] {
        "mt.monika.selectionForeground",
        "Menu.selectionForeground",
        "Menu.acceleratorSelectionForeground",
        "MenuItem.selectionForeground",
        "MenuItem.acceleratorSelectionForeground",
        "Table.selectionForeground",
        "TextField.selectionForeground",
        "PasswordField.selectionForeground",
        "Button.mt.selectedForeground",
        "TextArea.selectionForeground",
        "Label.selectedForeground",
        "Button.darcula.selectedButtonForeground"
    };
  }

  @Override
  protected String[] getSelectionBackgroundResources() {
    return new String[] {
        "mt.monika.selectionBackgroundInactive",
        "mt.monika.selectionInactiveBackground",
        "inactiveCaption",
        "Button.disabledText"
    };
  }

  @Override
  protected String[] getTextResources() {
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
  protected String[] getBackgroundResources() {
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
        "TabbedPane.highlight",
        "TabbedPane.darkShadow",
        "TabbedPane.shadow",
        "TabbedPane.borderColor",
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
        "Button.background",
        "Button.darcula.color1",
        "Button.darcula.color2",
        "Button.darcula.disabledText.shadow",
        "Button.mt.color1",
        "Button.mt.background",
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
        "TabbedPane.background",
        "OptionPane.background",
        "ToolWindow.header.background",
        "ToolWindow.header.closeButton.background",
        "material.tab.backgroundColor",
        "material.background"
    };
  }

  @NotNull
  @Override
  public Color getBorderColor() {
    return Color.getHSBColor(62,91,149);
  }

  @Override
  protected String[] getForegroundResources() {
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
