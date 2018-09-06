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

package com.chrisrm.idea;

import com.chrisrm.idea.themes.MTThemeable;
import com.chrisrm.idea.themes.literature.club.MonikaTheme;
import com.chrisrm.idea.utils.MTUiUtils;
import com.chrisrm.idea.utils.PropertiesParser;
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo;
import com.intellij.ide.ui.laf.LafManagerImpl;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.IconUtil;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.Serializable;
import java.util.stream.Stream;

public abstract class MTAbstractTheme implements Serializable, MTThemeable {
  public static final ColorUIResource DEFAULT_BORDER_COLOR = new ColorUIResource(0x80cbc4);
  public static final ColorUIResource DEFAULT_CONTRAST = new ColorUIResource(0x1E272C);
  public static final ColorUIResource DEFAULT_FOREGROUND = new ColorUIResource(0xB0BEC5);
  public static final ColorUIResource DEFAULT_BACKGROUND = new ColorUIResource(0x263238);
  public static final ColorUIResource DEFAULT_PRIMARY = new ColorUIResource(0x263238);
  public static final int HC_FG_TONES = 4;
  public static final int HC_BG_TONES = 2;

  private final String id;
  private final String editorColorsScheme;
  private final boolean dark;
  private String name;
  private String icon;

  public MTAbstractTheme(final boolean dark) {
    this(getDefaultID(dark), getDefaultColorScheme(dark), dark);
  }

  protected MTAbstractTheme(@NotNull final String id,
                            final String editorColorsScheme,
                            final boolean dark) {
    this.id = id;
    this.editorColorsScheme = editorColorsScheme;
    this.dark = dark;
    name = id;
  }

  protected MTAbstractTheme(@NotNull final String id,
                            final String editorColorsScheme,
                            final boolean dark,
                            final String name,
                            final String icon) {
    this(id, editorColorsScheme, dark, name);
    this.icon = icon;
  }

  protected MTAbstractTheme(@NotNull final String id, final String editorColorsScheme, final boolean dark, final String name) {
    this(id, editorColorsScheme, dark);
    this.name = name;
  }

  @NotNull
  private static String getDefaultID(final boolean dark) {
    return dark ? "mt.monika" : "mt.monika";
  }

  @NotNull
  private static String getDefaultColorScheme(final boolean dark) {
    return dark ? "Darcula" : "Default";
  }

  /**
   * Get the theme id
   */
  @Override
  public String toString() {
    return getId();
  }

  /**
   * Activate the theme by overriding UIManager with the theme resources and by setting the relevant Look and feel
   */
  @Override
  public final void activate() {
    try {
      if (isDark()) {
        LafManagerImpl.getTestInstance().setCurrentLookAndFeel(new DarculaLookAndFeelInfo());
      } else {
        LafManagerImpl.getTestInstance().setCurrentLookAndFeel(new IntelliJLookAndFeelInfo());
      }
      JBColor.setDark(isDark());
      IconLoader.setUseDarkIcons(isDark());
      buildResources(getBackgroundResources(), contrastifyBackground(getBackgroundColorString()));
      buildResources(getButtonBackgroundResources(), getButtonBackgroundColor());
      buildResources(getButtonForegroundResources(), getButtonForegroundColor());
      buildResources(getForegroundResources(), getForegroundColorString());
      buildResources(getMenuItemForegroundResources(), getMenuItemForegroundColor());
      buildResources(getTextResources(), contrastifyForeground(getTextColorString()));
      buildResources(getSelectionBackgroundResources(), getSelectionBackgroundColorString());
      buildResources(getSelectionForegroundResources(), getSelectionForegroundColorString());

      buildResources(getInactiveResources(), getInactiveColorString());
      buildResources(getSecondaryBackgroundResources(), getSecondaryBackgroundColorString());
      buildResources(getSecondaryForegroundResources(), getSecondaryForegroundColorString());
      buildResources(getDisabledResources(), getDisabledColorString());
      buildResources(getContrastResources(), contrastifyBackground(getContrastColorString()));
      buildResources(getTableSelectedResources(), getTableSelectedColorString());
      buildResources(getSecondBorderResources(), getSecondBorderColorString());
      buildResources(getHighlightResources(), getHighlightColorString());

      buildResources(getMenuItemSelectionBackgroundResources(), getMenuBarSelectionBackgroundColorString());
      buildResources(getMenuItemSelectionForegroundResources(), getMenuBarSelectionForegroundColorString());

      buildResources(getTreeSelectionBackgroundResources(), getTreeSelectionBackgroundColorString());
      buildResources(getTreeSelectionForegroundResources(), getTreeSelectionForegroundColorString());
      buildResources(getNotificationsResources(), getNotificationsColorString());
      buildNotificationsColors();

      // Apply theme accent color if said so
      if (MTConfig.getInstance().isOverrideAccentColor()) {
        MTConfig.getInstance().setAccentColor(getAccentColor());
        MTThemeManager.getInstance().applyAccents();
      }

      if (isDark()) {
        UIManager.setLookAndFeel(new MTDarkLaf(this));
      } else {
        UIManager.setLookAndFeel(new MTLightLaf(this));
      }
      JBColor.setDark(isDark());
      IconLoader.setUseDarkIcons(isDark());
    } catch (final UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  public String getButtonForegroundColor() {
    return "C700A5";
  }

  //region Getters/Setters

  /**
   * The theme name
   */
  @NotNull
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set the theme name
   *
   * @param name
   */
  @Override
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * Get the editor color scheme
   */
  @Override
  public String getEditorColorsScheme() {
    return editorColorsScheme;
  }

  /**
   * The theme id
   */
  @Override
  @NotNull
  public String getId() {
    return id;
  }

  /**
   * Whether the theme is a dark one
   */
  @Override
  public boolean isDark() {
    return dark;
  }

  /**
   * Get Theme ID
   */
  @NotNull
  @Override
  public String getThemeId() {
    return getId();
  }

  @NotNull
  @Override
  public final Icon getIcon() {
    return icon != null ? IconLoader.getIcon(icon) : IconUtil.getEmptyIcon(true);
  }

  public final void setIcon(final String icon) {
    this.icon = icon;
  }

  private Stream<String> getMenuItemSelectionForegroundResources() {
    return Stream.of("MenuItem.selectionForeground",
        "Menu.acceleratorSelectionForeground",
        "Menu.selectionForeground",
        "MenuItem.acceleratorSelectionForeground");
  }

  private Stream<String> getMenuItemSelectionBackgroundResources() {
    return Stream.of("Menu.selectionBackground",
        "Menu.acceleratorSelectionBackground",
        "MenuItem.acceleratorSelectionBackground",
        "MenuItem.selectionBackground");
  }

  protected String getMenuItemForegroundColor() {
    return "FFFFFF";
  }

  protected abstract String getEditorTabColorString();

  /**
   * Whether the theme is a custom or external one
   */
  @Override
  public boolean isCustom() {
    return false;
  }
  //endregion

  //region Theme methods

  /**
   * Get the default selection background
   */
  @NotNull
  @Override
  public String getSelectionBackground() {
    //todo: when dark theme comes in!!!!!
    return dark ? MonikaTheme.SELECTION_BACKGROUND : MonikaTheme.SELECTION_BACKGROUND;
  }

  /**
   * Get disabled color
   */
  @NotNull
  @Override
  public String getDisabled() {
    //todo: when dark theme comes in!!!!!
    return dark ? MonikaTheme.DISABLED : MonikaTheme.DISABLED;
  }

  /**
   * Get background color custom property
   */
  @Override
  @NotNull
  public Color getBackgroundColor() {
    final Color defaultValue = MTUiUtils.getColor(
        UIManager.getColor("material.background"),
        ObjectUtils.notNull(UIManager.getColor("darcula.background"), new ColorUIResource(0x3c3f41)),
        ObjectUtils.notNull(UIManager.getColor("intellijlaf.background"), new ColorUIResource(0xe8e8e8)));
    return ObjectUtils.notNull(defaultValue, DEFAULT_BACKGROUND);
  }

  /**
   * Get contrast color custom property
   */
  @Override
  @NotNull
  public Color getContrastColor() {
    final Color defaultValue = MTUiUtils.getColor(
        UIManager.getColor("material.contrast"),
        ObjectUtils.notNull(UIManager.getColor("darcula.contrastColor"), new ColorUIResource(0x262626)),
        ObjectUtils.notNull(UIManager.getColor("intellijlaf.contrastColor"), new ColorUIResource(0xeeeeee)));
    return ObjectUtils.notNull(defaultValue, DEFAULT_CONTRAST);
  }

  /**
   * Get foreground color custom property
   */
  @Override
  @NotNull
  public Color getForegroundColor() {
    final Color defaultValue = MTUiUtils.getColor(
        UIManager.getColor("material.foreground"),
        ObjectUtils.notNull(UIManager.getColor("darcula.foreground"), new ColorUIResource(0x3c3f41)),
        ObjectUtils.notNull(UIManager.getColor("intellijlaf.foreground"), new ColorUIResource(0xe8e8e8)));
    return ObjectUtils.notNull(defaultValue, DEFAULT_FOREGROUND);
  }

  /**
   * Get background color custom property
   */
  @Override
  @NotNull
  public Color getPrimaryColor() {
    final Color defaultValue = MTUiUtils.getColor(
        UIManager.getColor("material.primaryColor"),
        ObjectUtils.notNull(UIManager.getColor("darcula.primary"), new ColorUIResource(0x3c3f41)),
        ObjectUtils.notNull(UIManager.getColor("intellijlaf.primary"), new ColorUIResource(0xe8e8e8)));
    return ObjectUtils.notNull(defaultValue, DEFAULT_PRIMARY);
  }

  private String contrastifyForeground(final String colorString) {
    final boolean isHighContrast = MTConfig.getInstance().getIsHighContrast();
    if (!isHighContrast) {
      return colorString;
    }

    if (isDark()) {
      return ColorUtil.toHex(ColorUtil.brighter(ColorUtil.fromHex(colorString), HC_FG_TONES));
    } else {
      return ColorUtil.toHex(ColorUtil.darker(ColorUtil.fromHex(colorString), HC_FG_TONES));
    }
  }

  private Color contrastifyForeground(final Color color) {
    final boolean isHighContrast = MTConfig.getInstance().getIsHighContrast();
    if (!isHighContrast) {
      return color;
    }

    if (isDark()) {
      return ColorUtil.brighter(color, HC_FG_TONES);
    } else {
      return ColorUtil.darker(color, HC_FG_TONES);
    }
  }

  private String contrastifyBackground(final String colorString) {
    final boolean isHighContrast = MTConfig.getInstance().getIsHighContrast();
    if (!isHighContrast) {
      return colorString;
    }

    if (isDark()) {
      return ColorUtil.toHex(ColorUtil.darker(ColorUtil.fromHex(colorString), HC_BG_TONES));
    } else {
      return ColorUtil.toHex(ColorUtil.brighter(ColorUtil.fromHex(colorString), HC_BG_TONES));
    }
  }

  private Color contrastifyBackground(final Color color) {
    final boolean isHighContrast = MTConfig.getInstance().getIsHighContrast();
    if (!isHighContrast) {
      return color;
    }

    if (isDark()) {
      return ColorUtil.darker(color, HC_BG_TONES);
    } else {
      return ColorUtil.brighter(color, HC_BG_TONES);
    }
  }

  //endregion

  //region MTThemeable methods

  /**
   * Get resources using the background color
   */
  protected String[] getBackgroundResources() {
    return new String[]{
        "window",
        "activeCaption",
        "control",
        "PopupMenu.translucentBackground",
        "EditorPane.inactiveBackground",
        "Table.background",
        "Table.gridColor",
        "Desktop.background",
        "PopupMenu.background",
        "Separator.background",
        "MenuBar.background",
        "MenuBar.disabledBackground",
        "MenuBar.shadow",
        "TabbedPane.background",
        "TabbedPane.borderColor",
        "TextField.background",
        "PasswordField.background",
        "FormattedTextField.background",
        "TextArea.background",
        "CheckBox.background",
        "OptionPane.background",
        "ColorChooser.background",
        "Slider.background",
        "TabbedPane.mt.tab.background",
        "TextPane.background",
        "RadioButton.background",
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
        "ComboBox.arrowFillColor",
        "ComboBox.darcula.arrowButtonBackground",
        "RadioButton.darcula.selectionDisabledColor",
        "StatusBar.topColor",
        "StatusBar.top2Color",
        "StatusBar.bottomColor",
        "ToolTip.background",
        "Spinner.background",
        "SplitPane.highlight",
        "Tree.background",
        "Popup.Border.inactiveColor",
        "Popup.inactiveBorderColor",
        "Popup.preferences.background",
        "Popup.preferences.borderColor",
        "HelpTooltip.backgroundColor",
        "SidePanel.background",
        "DialogWrapper.southPanelDivider",
        "Dialog.titleColor",
        "SearchEverywhere.background",
        "CheckBoxMenuItem.background",
        "ToolWindow.header.background",
        "ToolWindow.header.closeButton.background",
        "material.tab.backgroundColor",
        "TextField.borderColor",
        "TextField.hoverBorderColor",
        "SearchEverywhere.Dialog.background",
        "SearchEverywhere.SearchField.Border.color",
        "TextField.focusedBorderColor",
        "ComboBox.darcula.nonEditableBackground",
        "darcula.background",
        "intellijlaf.background",
        "material.background"
    };
  }

  /**
   * Get the hex code for the background color
   */
  public abstract String getBackgroundColorString();

  /**
   * Get resources using the foreground color
   */
  protected String[] getForegroundResources() {
    return new String[]{
        "text",
        "textText",
        "textInactiveText",
        "infoText",
        "controlText",
        "OptionPane.messageForeground",
        "Menu.foreground",
        "MenuItem.foreground",
        "Label.foreground",
        "Label.selectedDisabledForeground",
        "CheckBox.foreground",
        "ComboBox.foreground",
        "RadioButton.foreground",
        "ColorChooser.foreground",
        "MenuBar.foreground",
        "RadioButtonMenuItem.foreground",
        "CheckBoxMenuItem.foreground",
        "MenuItem.foreground",
        //        "OptionPane.foreground",
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
        "Table.foreground",
        "TableHeader.foreground",
        "ToggleButton.foreground",
        "Table.sortIconColor",
        "material.branchColor",
        "material.foreground",
        "CheckBox.darcula.borderColor1",
        "RadioButton.darcula.borderColor1",
        "HelpTooltip.textColor",
        "darcula.foreground",
        "intellijlaf.foreground",
        "TitledBorder.titleColor"
    };
  }

  protected String[] getMenuItemForegroundResources() {
    return new String[]{
        "Menu.foreground",
        "MenuItem.foreground",
        "PopupMenu.foreground",
    };
  }

  protected Stream<String> getButtonBackgroundResources() {
    return Stream.of(
        "Button.background",
        "Button.darcula.color1",
        "Button.darcula.color2",
        "Button.darcula.disabledText.shadow",
        "ToolWindow.header.closeButton.background");
  }

  protected Stream<String> getButtonForegroundResources() {
    return Stream.of(
        "Button.foreground",
        "Button.mt.foreground",
        "Button.mt.selectedButtonForeground",
        "ToolWindow.header.closeButton.foreground");
  }


  protected String getButtonBackgroundColor() {
    return "fbffeb";
  }

  /**
   * Get the hex code for the foreground color
   */
  public abstract String getForegroundColorString();

  /**
   * Get resources using the label color
   */
  protected String[] getTextResources() {
    return new String[]{
        "Menu.acceleratorForeground",
        "text",
        "textText",
        "textInactiveText",
        "infoText",
        "controlText",
        "OptionPane.messageForeground",
        "MenuItem.acceleratorForeground",
        "TextField.separatorColorDisabled",
        "Table.sortIconColor",
        "material.tagColor",
        "material.primaryColor",
        "SearchEverywhere.shortcutForeground",
        "HelpTooltip.shortcutTextColor",
        "Tree.foreground"
    };
  }

  /**
   * Get the hex code for the text color
   */
  public abstract String getTextColorString();

  /**
   * Get resources using the selection background color
   */
  protected String[] getSelectionBackgroundResources() {
    return new String[]{
        "Menu.selectionBackground",
        "MenuItem.selectionBackground",
        "RadioButtonMenuItem.selectionBackground",
        "CheckBoxMenuItem.selectionBackground",
        "EditorPane.selectionBackground",
        "Autocomplete.selectionBackground",
        "TabbedPane.selectHighlight",
        "List.selectionBackground",
        "TabbedPane.selected",
    };
  }

  /**
   * Get the hex code for the selection background color
   */
  public abstract String getSelectionBackgroundColorString();

  /**
   * Get resources using the selection foreground color
   */
  protected Stream<String> getSelectionForegroundResources() {
    return Stream.of(
        "Table.selectionForeground",
        "TextField.selectionForeground",
        "PasswordField.selectionForeground",
        "Button.mt.selectedForeground",
        "TextArea.selectionForeground",
        "List.selectionForeground",
        "ComboBox.selectionForeground",
        "FormattedTextField.selectionForeground",
        "CheckBoxMenuItem.selectionForeground",
        "TextPane.selectionForeground",
        "EditorPane.selectionForeground",
        "Tree.selectionForeground",
        "TableHeader.focusCellForeground",
        "TabbedPane.selectedForeground",
        "Button.darcula.selectedButtonForeground"
    );
  }

  /**
   * Get the hex code for the selection foreground color
   */
  public abstract String getSelectionForegroundColorString();

  protected abstract String getMenuBarSelectionForegroundColorString();

  protected abstract String getMenuBarSelectionBackgroundColorString();

  /**
   * Get resources using the inactive color
   */
  protected String[] getInactiveResources() {
    return new String[]{
        "MenuBar.darcula.borderColor",
        "MenuBar.darcula.borderShadowColor",
        "Button.mt.color1",
        "Button.mt.color2",
        "Button.mt.background",
        "material.disabled",
        "material.mergeCommits"
    };
  }

  /**
   * Get the hex code for the inactive color
   */
  protected abstract String getInactiveColorString();

  /**
   * Get resources using the secondary background color
   */
  protected String[] getSecondaryBackgroundResources() {
    return new String[]{
        "inactiveCaption",
        "ToolWindow.header.active.background",
        "ToolWindow.header.border.background",
        "MemoryIndicator.unusedColor",
        "List.background"
    };
  }

  /**
   * Get resources using the secondary background color
   */
  protected String[] getSecondaryForegroundResources() {
    return new String[]{
        "ToolWindow.header.active.foreground",
        "ToolWindow.header.border.foreground",
        "List.foreground"
    };
  }

  /**
   * Get the hex code for the secondary background color
   */
  public abstract String getSecondaryBackgroundColorString();

  //TODO: MAKE ME DEFAULT
  protected String getSecondaryForegroundColorString() {
    return "256f25";
  }

  /**
   * Get resources using the disabled color
   */
  protected String[] getDisabledResources() {
    return new String[]{
        "MenuItem.disabledForeground",
        "ComboBox.disabledForeground",
        "CheckBox.darcula.disabledBorderColor1",
        "CheckBox.darcula.disabledBorderColor2",
        "TextField.inactiveForeground",
        "FormattedTextField.inactiveForeground",
        "PasswordField.inactiveForeground",
        "TextArea.inactiveForeground",
        "TextPane.inactiveForeground",
        "EditorPane.inactiveForeground",
        "Button.disabledText",
        "TabbedPane.selectedDisabledColor",
        "Menu.disabledForeground",
        "Label.disabledForeground",
        "RadioButtonMenuItem.disabledForeground",
        "Outline.disabledColor",
        "CheckBoxMenuItem.disabledForeground",
        "CheckBox.darcula.checkSignColorDisabled"
    };
  }

  /**
   * Get the hex code for the disabled color
   */
  public abstract String getDisabledColorString();

  /**
   * Get resources using the contrast color
   */
  protected String[] getContrastResources() {
    return new String[]{
        "Table.stripedBackground",
        "ToolWindow.header.tab.selected.background",
        "ToolWindow.header.tab.selected.active.background",
        "Table.focusCellBackground",
        "ScrollBar.thumb",
        "EditorPane.background",
        "ToolBar.background",
        "Popup.Header.inactiveBackground",
        "Popup.Toolbar.background",
        "Popup.Border.color",
        "Popup.Toolbar.Border.color",
        "SearchEverywhere.SearchField.background",
        "material.contrast"
    };
  }

  /**
   * Get the hex code for the contrast color
   */
  public abstract String getContrastColorString();

  /**
   * Get resources using the table/button selection color
   */
  protected String[] getTableSelectedResources() {
    return new String[]{
        "Table.selectionBackground",
        "TextField.selectionBackground",
        "PasswordField.selectionBackground",
        "FormattedTextField.selectionBackground",
        "ComboBox.selectionBackground",
        "TextArea.selectionBackground",
        "TextPane.selectionBackground",
        "Button.darcula.selection.color1",
        "Button.darcula.selection.color2",
        "Button.darcula.focusedBorderColor",
        "Button.darcula.defaultFocusedBorderColor",
        "Button.mt.selection.color2",
        "Button.mt.selection.color1"
    };
  }

  /**
   * Get the hex code for the table selected color
   */
  public abstract String getTableSelectedColorString();

  /**
   * Get resources using the second border color
   */
  protected String[] getSecondBorderResources() {
    return new String[]{
        "MenuBar.darcula.borderShadowColor",
        "CheckBox.darcula.disabledBorderColor1",
        "CheckBox.darcula.disabledBorderColor2",
        "TabbedPane.highlight",
        "TabbedPane.darkShadow",
        "OnePixelDivider.background",
        "Button.darcula.disabledOutlineColor",
        "HelpTooltip.borderColor",
        "SearchEverywhere.List.Separator.Color",
        "TabbedPane.shadow"
    };
  }

  /**
   * Get the hex code for the second border color
   */
  public abstract String getSecondBorderColorString();

  /**
   * Get resources using the highlight color
   */
  protected String[] getHighlightResources() {
    return new String[]{
        "Focus.color",
        "TextField.separatorColor",
        "ProgressBar.halfColor",
        "Autocomplete.selectionUnfocus",
        "CheckBox.darcula.inactiveFillColor",
        "TabbedPane.selectHighlight",
        "TabbedPane.selectedColor",
        "TabbedPane.hoverColor",
        "TabbedPane.contentAreaColor",
        "SearchEverywhere.Tab.selected.background",
        "TableHeader.borderColor",
        "Outline.focusedColor",
        "MemoryIndicator.usedColor"
    };
  }

  /**
   * Get the hex code for the highlight color
   */
  public abstract String getHighlightColorString();

  /**
   * Get resources using the tree selected row color
   */
  protected String[] getTreeSelectionBackgroundResources() {
    return new String[]{
        "Tree.selectionBackground"

    };
  }

  protected String[] getTreeSelectionForegroundResources() {
    return new String[]{
        "Tree.selectionForeground"

    };
  }

  /**
   * Get the hex code for the tree selection color
   */
  public abstract String getTreeSelectionBackgroundColorString();

  public abstract String getTreeSelectionForegroundColorString();

  /**
   * Get notifications colors resources
   */
  protected String[] getNotificationsResources() {
    return new String[]{
        "Notifications.background",
        "Notifications.borderColor"
    };
  }

  /**
   * Get the hex code for the notifications color
   */
  public abstract String getNotificationsColorString();
  //endregion

  /**
   * Iterate over theme resources and fill up the UIManager
   *
   * @param resources
   * @param color
   */
  private void buildResources(final String[] resources, final String color) {
    for (final String resource : resources) {
      UIManager.getDefaults().put(resource, PropertiesParser.parseColor(color));
    }
  }

  private void buildResources(final Stream<String> resources, final String color) {
    Color o1 = PropertiesParser.parseColor(color);
    resources.forEach(resource -> UIManager.getDefaults().put(resource, o1));
  }

  private void buildNotificationsColors() {
    UIManager.put("Notifications.errorBackground", new JBColor(new ColorUIResource(0xef5350), new ColorUIResource(0x4E0C05)));
    UIManager.put("Notifications.warnBackground", new JBColor(new ColorUIResource(0xFFD54F), new ColorUIResource(0x4E3C0D)));
    UIManager.put("Notifications.infoBackground", new JBColor(new ColorUIResource(0x66BB6A), new ColorUIResource(0x113215)));
  }
}
