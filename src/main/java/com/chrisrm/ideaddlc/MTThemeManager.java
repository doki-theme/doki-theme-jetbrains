/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.chrisrm.ideaddlc;

import com.chrisrm.ideaddlc.icons.IconManager;
import com.chrisrm.ideaddlc.lafs.MTDarkLaf;
import com.chrisrm.ideaddlc.lafs.MTLafInstaller;
import com.chrisrm.ideaddlc.listeners.MTTopics;
import com.chrisrm.ideaddlc.messages.MaterialThemeBundle;
import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.chrisrm.ideaddlc.themes.lists.AccentResources;
import com.chrisrm.ideaddlc.themes.lists.ContrastResources;
import com.chrisrm.ideaddlc.themes.lists.FontResources;
import com.chrisrm.ideaddlc.utils.*;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.laf.IntelliJLaf;
import com.intellij.ide.ui.laf.LafManagerImpl;
import com.intellij.ide.ui.laf.darcula.DarculaInstaller;
import com.intellij.ide.ui.laf.darcula.DarculaLaf;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.AppEditorFontOptions;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.Consumer;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import io.acari.DDLC.*;
import io.acari.DDLC.integrations.NormandyThemeIntegration;
import org.jetbrains.annotations.NonNls;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Manages appearance settings
 */
@SuppressWarnings( {"ClassWithTooManyMethods",
    "DuplicateStringLiteralInspection",
    "UtilityClassCanBeEnum",
    "OverlyComplexClass",
    "UtilityClass"})
public final class MTThemeManager {

  private static final String WE_USING_DDLC_BOIS = "WE USING DDLC BOIS";
  private static final String WE_AINT_USING_DDLC_BOIS = "WE_AINT USING DDLC BOIS";
  private static final int DEFAULT_SIDEBAR_HEIGHT = 28;
  private static final int DEFAULT_INDENT = 6;
  private static final int DEFAULT_FONT_SIZE = JBUI.scale(13);
  private static final String DEFAULT_FONT = "Roboto";
  private static final String DEFAULT_MONO_FONT = "Fira Code";
  @NonNls
  private static final String RETINA = "@2x.css";
  @NonNls
  private static final String NON_RETINA = ".css";
  @NonNls
  private static final String DARCULA = "darcula";
  private static String MATERIAL_THEME_PROP = "io.acari.ddlc.DDLCTheme.active.status";


  private MTThemeManager() {

  }

  private Consumer<Boolean> ddlcIsActiveConsumer = ddlcActive ->{
    if (!ddlcActive) {
      PropertiesComponent.getInstance().setValue(MATERIAL_THEME_PROP, WE_AINT_USING_DDLC_BOIS);
      mtUIActivationListeners.forEach(consumer -> consumer.consume(true));
    } else {
      mtUIActivationListeners.forEach(consumer -> consumer.consume(false));
    }
  };

  public Runnable createListener(){
    return () -> {
      this.ddlcIsActiveConsumer.consume(isDDLCActive());
      LafManager.getInstance().addLafManagerListener(lafManager -> {
        boolean ddlcActive = UIManager.getLookAndFeel().getDescription().contains("DDLC");
        this.ddlcIsActiveConsumer.consume(ddlcActive);
      });
    };
  }

  private static List<Consumer<Boolean>> mtUIActivationListeners = new LinkedList<>();
  public static void addMaterialThemeActivatedListener(Consumer<Boolean> callback){
    mtUIActivationListeners.add(callback);
  }

  public static boolean isDDLCActive(){
    PropertiesComponent instance = PropertiesComponent.getInstance();
    return instance != null &&
        instance.getValue(MATERIAL_THEME_PROP, WE_USING_DDLC_BOIS)
            .equals(WE_USING_DDLC_BOIS);
  }

  public static MTThemeManager getInstance() {
    return ServiceManager.getService(MTThemeManager.class);
  }

  //region Action Toggles

  /**
   * Toggle material design.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleMaterialDesign() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setIsMaterialDesign(!mtConfig.isMaterialDesign());

    askForRestart();
  }

  /**
   * Toggle project view decorators.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleProjectViewDecorators() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setUseProjectViewDecorators(!mtConfig.isUseProjectViewDecorators());
    updateFileIcons();
  }

  /**
   * Toggle material theme.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleMaterialTheme() {
    MTConfig.getInstance().setIsMaterialTheme(!MTConfig.getInstance().isMaterialTheme());
    activate();
  }

  /**
   * Set contrast and reactivate theme
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleContrast() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setIsContrastMode(!mtConfig.isContrastMode());

    applyContrast(true);
  }


  /**
   * Toggle compact status bar.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleCompactStatusBar() {
    final boolean compactStatusBar = MTConfig.getInstance().isCompactStatusBar();
    MTConfig.getInstance().setIsCompactStatusBar(!compactStatusBar);

    applyCompactToolWindowHeaders();
  }

  private static void applyCompactToolWindowHeaders() {
    if (MTConfig.getInstance().isCompactStatusBar()) {
      UIManager.put("ToolWindow.tab.verticalPadding", JBUI.scale(0));
    } else {
      UIManager.put("ToolWindow.tab.verticalPadding", JBUI.scale(5));
    }
  }

  /**
   * Toggle hide file icons.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleHideFileIcons() {
    final boolean hideFileIcons = MTConfig.getInstance().isHideFileIcons();
    MTConfig.getInstance().setHideFileIcons(!hideFileIcons);

    updateFileIcons();
  }

  /**
   * Toggle monochrome icons.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleMonochromeIcons() {
    final boolean monochromeIcons = MTConfig.getInstance().isMonochromeIcons();
    MTConfig.getInstance().setMonochromeIcons(!monochromeIcons);

    IconManager.applyFilter();
    updateFileIcons();
  }

  /**
   * Toggle compact sidebar.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleCompactSidebar() {
    final boolean isCompactSidebar = MTConfig.getInstance().isCompactSidebar();
    MTConfig.getInstance().setCompactSidebar(!isCompactSidebar);

    applyCompactSidebar(true);
  }

  /**
   * Toggle compact dropdowns.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleCompactDropdowns() {
    final boolean isCompactDropdowns = MTConfig.getInstance().isCompactDropdowns();
    MTConfig.getInstance().setCompactDropdowns(!isCompactDropdowns);

    UIReplacer.patchUI();
  }

  /**
   * Toggle compact menus.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleCompactMenus() {
    final boolean isCompact = MTConfig.getInstance().isCompactMenus();
    MTConfig.getInstance().setIsCompactMenus(!isCompact);

    applyMenusHeight();
    UIReplacer.patchUI();
  }

  /**
   * Compact table cells
   */
  @SuppressWarnings({"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleCompactTableCells() {
    final boolean isCompact = MTConfig.getInstance().isCompactTables();
    MTConfig.getInstance().setIsCompactTables(!isCompact);

    reloadUI();
  }

  /**
   * Toggle material icons.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleMaterialIcons() {
    final boolean useMaterialIcons = MTConfig.getInstance().isUseMaterialIcons();
    MTConfig.getInstance().setUseMaterialIcons(!useMaterialIcons);

    updateFileIcons();
  }


  /**
   * Toggle material file icons.
   */
  @SuppressWarnings("BooleanVariableAlwaysNegated")
  public static void toggleMaterialFileIcons() {
    final boolean useMaterialFileIcons = MTConfig.getInstance().isFileIcons();
    MTConfig.getInstance().setFileIcons(!useMaterialFileIcons);

    updateFileIcons();
  }


  /**
   * Toggle material fonts.
   */
  @SuppressWarnings( {"FeatureEnvy",
      "BooleanVariableAlwaysNegated"})
  public static void toggleMaterialFonts() {
    final boolean useMaterialFonts = MTConfig.getInstance().isUseMaterialFont();
    MTConfig.getInstance().setUseMaterialFont(!useMaterialFonts);

    applyFonts();
  }

  /**
   * Toggle upper case tabs.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleUpperCaseTabs() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setIsUpperCaseTabs(!mtConfig.isUpperCaseTabs());
    mtConfig.fireChanged();
  }

  /**
   * Toggle status bar indicator.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleStatusBarIndicator() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setIsStatusBarTheme(!mtConfig.isStatusBarTheme());
    mtConfig.fireChanged();
  }

  /**
   * Toggle dark title bar.
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleDarkTitleBar() {
    final MTConfig mtConfig = MTConfig.getInstance();
    mtConfig.setDarkTitleBar(!mtConfig.isDarkTitleBar());
    themeTitleBar();
  }

  /**
   * Toggle override accent color
   */
  @SuppressWarnings("FeatureEnvy")
  public static void toggleOverrideAccent() {
    MTConfig.getInstance().setOverrideAccentColor(!MTConfig.getInstance().isOverrideAccentColor());
    MTConfig.getInstance().fireChanged();
  }
  //endregion

  //region File Icons support

  /**
   * Update file icons.
   */
  static void updateFileIcons() {
    ApplicationManager.getApplication().runWriteAction(() -> {
      final FileTypeManagerEx instanceEx = FileTypeManagerEx.getInstanceEx();
      instanceEx.fireFileTypesChanged();
      ActionToolbarImpl.updateAllToolbarsImmediately();
    });
  }
  //endregion


  //region Theme activation and deactivation

  /**
   * Activate selected theme or deactivate current
   */
  @SuppressWarnings("FeatureEnvy")
  public static void activate() {
    final DDLCThemeFacade mtTheme = DDLCConfig.getInstance().getSelectedTheme();
    if (!MTConfig.getInstance().isMaterialTheme()) {
      removeTheme(mtTheme);
      return;
    }

    activate(mtTheme, false);
  }

  public static void activate(final String themeId) {
    final DDLCThemeFacade themeFor = DDLCThemes.getThemeFor(themeId);
    if (themeFor != null) {
      activate(themeFor, false);
    }
  }

  /**
   * Activate theme and switch color scheme
   *
   * @param mtTheme           the mt theme
   * @param switchColorScheme whether to switch color scheme
   */
  public static void activate(final DDLCThemeFacade mtTheme, final boolean switchColorScheme) {
    DDLCThemeFacade newTheme = mtTheme;
    if (newTheme == null){
      newTheme = DDLCThemes.MONIKA;
    }

    DDLCConfig.getInstance().setSelectedTheme(newTheme);
    PropertiesComponent.getInstance().setValue(MATERIAL_THEME_PROP, WE_USING_DDLC_BOIS);

    newTheme.getTheme().activate();

    // Change color scheme to the theme's
    switchScheme(newTheme, switchColorScheme);

    // Save a reference to the theme
    IconLoader.clearCache();

    // apply different settings

    applyContrast(false);
    applyCompactSidebar(false);
    applyCustomTreeIndent();
    applyMenusHeight();
    applyAccents(true);
    applyFonts();
    themeTitleBar();
    applyCompactToolWindowHeaders();

    // Documentation styles
    patchStyledEditorKit();

    // Monochrome filter and co
    IconManager.applyFilter();
    LafManager.getInstance().updateUI();

    // Custom UI Patches
    UIReplacer.patchUI();

    fireThemeChanged(newTheme);
  }

  /**
   * New way of switching themes
   */
  public static void setLookAndFeel(final DDLCThemeFacade selectedTheme) {
    // Find LAF theme and trigger a theme change
    final LafManager lafManager = LafManagerImpl.getInstance();
    final UIManager.LookAndFeelInfo lafInfo =
        ContainerUtil.find(lafManager.getInstalledLookAndFeels(),
        lookAndFeelInfo -> lookAndFeelInfo.getName().equals(selectedTheme.getThemeName()));

    MTChangeLAFAnimator.showSnapshot();
    if (lafInfo != null) {
      lafManager.setCurrentLookAndFeel(lafInfo);
    } else {
      // good ol' shit
      activate(selectedTheme, true);
    }
    SwingUtilities.invokeLater(MTChangeLAFAnimator::hideSnapshotWithAnimation);
  }

  /**
   * Switch the color scheme to the current theme's
   *
   * @param mtTheme           the current theme
   * @param switchColorScheme whether to switch color scheme
   */
  private static void switchScheme(final DDLCThemeFacade mtTheme, final boolean switchColorScheme) {
    final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
    if (switchColorScheme) {
      final EditorColorsScheme themeScheme = editorColorsManager.getScheme(mtTheme.getThemeColorScheme());
      if (themeScheme != null) {
        editorColorsManager.setGlobalScheme(themeScheme);
      }
    }
    // Need to trigger a change otherwise the ui will get stuck. Yes this sucks
    final EditorColorsScheme globalScheme = editorColorsManager.getGlobalScheme();
    editorColorsManager.setGlobalScheme(editorColorsManager.getScheme(MTUiUtils.DARCULA));
    editorColorsManager.setGlobalScheme(globalScheme);
  }

  /**
   * Apply accents.
   */
  @SuppressWarnings("MagicNumber")
  public static void applyAccents(final boolean fireEvent) {
    final String accentColor = MTConfig.getInstance().getAccentColor();
    final Color accentColorColor = ColorUtil.fromHex(accentColor);
    for (final String resource : AccentResources.ACCENT_RESOURCES) {
      UIManager.put(resource, accentColorColor);
    }

    final Color transparentAccentColor = ColorUtil.toAlpha(accentColorColor, 70);
    for (final String resource : AccentResources.ACCENT_TRANSPARENT_RESOURCES) {
      UIManager.put(resource, transparentAccentColor);
    }

    // Scrollbars management
    applyScrollbars(ColorUtil.fromHex(accentColor));

    patchStyledEditorKit();

    if (fireEvent) {
      fireAccentChanged(accentColorColor);
    }
  }

  private static void applyScrollbars(Color accentColor) {
    final Color transColor = ColorUtil.toAlpha(accentColor, 50);
    final Color hoverColor = ColorUtil.toAlpha(accentColor, 75);

    // IDE scrollbars
    final Couple<Color> scrollbarColors = getScrollbarColors(accentColor, transColor, hoverColor);
    if (scrollbarColors != null) { //null unless accent scrollbars is on
      final Color scrollbarColor = scrollbarColors.getFirst();
      final Color scrollbarHoverColor = scrollbarColors.getSecond();

      for (final String resource : AccentResources.SCROLLBAR_RESOURCES) {
        UIManager.put(resource, scrollbarColor);
      }
      for (final String resource : AccentResources.SCROLLBAR_HOVER_RESOURCES) {
        UIManager.put(resource, scrollbarHoverColor);
      }
    }
  }

  public static Couple<Color> getScrollbarColors(final Color accentColor, final Color transColor, final Color hoverColor) {
    // Scrollbars
    if (MTConfig.getInstance().isAccentScrollbars()) {
      return MTConfig.getInstance().isThemedScrollbars() ?
          new Couple<>(transColor, hoverColor) :
          new Couple<>(hoverColor, accentColor);
    }
    return null;
  }

  /**
   * Ask for restart.
   */
  @SuppressWarnings("Duplicates")
  private static void askForRestart() {
    final String title = MaterialThemeBundle.message("mt.restartDialog.title");
    final String message = MaterialThemeBundle.message("mt.restartDialog.content");

    final int answer = Messages.showYesNoDialog(message, title, Messages.getQuestionIcon());
    if (answer == Messages.YES) {
      MTUiUtils.restartIde();
    }
  }

  /**
   * Remove the Material Theme and install the native themes
   *
   * @param mtTheme current material theme
   */
  private static void removeTheme(final DDLCThemeFacade mtTheme) {
    try {
      resetContrast();

      // Still create the MT Look and Feels in order to retrieve some of the components
      if (UIUtil.isUnderDarcula()) {
        UIManager.setLookAndFeel(new DarculaLaf());
      } else {
        UIManager.setLookAndFeel(new IntelliJLaf());
      }
      final MTLafInstaller mtLafInstaller = new MTLafInstaller();
      mtLafInstaller.installMTDefaults(UIManager.getDefaults());

      JBColor.setDark(mtTheme.isDark());
      IconLoader.setUseDarkIcons(mtTheme.isDark());

      // We need this to update parts of the UI that do not change
      if (UIUtil.isUnderDarcula()) {
        DarculaInstaller.uninstall();
        DarculaInstaller.install();
      } else {
        DarculaInstaller.uninstall();
      }

      // Reset custom properties
      UIManager.put("material.primaryColor", null);
      UIManager.put("material.background", null);
      UIManager.put("material.foreground", null);
      UIManager.put("material.tab.borderColor", null);
      UIManager.put("material.tab.borderThickness", null);
      UIManager.put("material.contrast", null);

      // Apply other settings
      themeTitleBar();
      applyCompactSidebar(false);
      applyCustomTreeIndent();
      applyAccents(false);

      // Finally reapply Icon filters and UIReplacer patches
      LafManager.getInstance().updateUI();
      IconManager.applyFilter();
      UIReplacer.patchUI();
    } catch (final UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  private static void fireThemeChanged(final DDLCThemeFacade newTheme) {
    MessageBus messageBus = ApplicationManager.getApplication().getMessageBus();
    messageBus
        .syncPublisher(MTTopics.THEMES)
        .themeChanged(newTheme);
    NormandyThemeIntegration.INSTANCE.themeChanged(messageBus, newTheme);
  }

  private static void fireAccentChanged(final Color accentColor) {
    MessageBus messageBus = ApplicationManager.getApplication().getMessageBus();
    messageBus
        .syncPublisher(MTTopics.ACCENTS)
        .accentChanged(accentColor);
    NormandyThemeIntegration.INSTANCE.accentChanged(messageBus, accentColor);
  }

  //endregion

  // region Fonts

  /**
   * Apply custom fonts
   *
   * @param uiDefaults the defaults to override
   * @param fontFace   the font face
   * @param fontSize   the font size
   */
  @SuppressWarnings("Duplicates")
  private static void applySettingsFont(@NonNls final UIDefaults uiDefaults, final String fontFace, final int fontSize) {
    uiDefaults.put("Tree.ancestorInputMap", null);
    final FontUIResource font = UIUtil.getFontWithFallback(fontFace, Font.PLAIN, fontSize);

    final String editorFontName = AppEditorFontOptions.getInstance().getFontPreferences().getFontFamily();
    final String monospaceFont = ObjectUtils.notNull(editorFontName, DEFAULT_MONO_FONT);
    final FontUIResource monoFont = new FontUIResource(monospaceFont, Font.PLAIN, fontSize);

    // Keep old style and size
    for (final String fontResource : FontResources.FONT_RESOURCES) {
      final Font curFont = ObjectUtils.notNull(uiDefaults.getFont(fontResource), font);
      uiDefaults.put(fontResource, font.deriveFont(curFont.getStyle(), curFont.getSize()));
    }

    uiDefaults.put("PasswordField.font", monoFont);
    uiDefaults.put("TextArea.font", monoFont);
    uiDefaults.put("TextPane.font", font);
    uiDefaults.put("EditorPane.font", font);
  }

  @SuppressWarnings("Duplicates")
  private static void applyMaterialFonts(@NonNls final UIDefaults uiDefaults) {
    uiDefaults.put("Tree.ancestorInputMap", null);

    @NonNls final String language = Locale.getDefault().getLanguage();
    final boolean cjkLocale =
        (Locale.CHINESE.getLanguage().equals(language) ||
         Locale.JAPANESE.getLanguage().equals(language) ||
         Locale.KOREAN.getLanguage().equals(language));

    FontUIResource font = UIUtil.getFontWithFallback(DEFAULT_FONT, Font.PLAIN, DEFAULT_FONT_SIZE);
    if (cjkLocale) {
      font = UIUtil.getFontWithFallback(MTUiUtils.NOTO_SANS, Font.PLAIN, DEFAULT_FONT_SIZE);
    }

    final FontUIResource uiFont = font;
    final FontUIResource textFont = font;

    final String editorFontName = AppEditorFontOptions.getInstance().getFontPreferences().getFontFamily();
    final String monospaceFont = ObjectUtils.notNull(editorFontName, DEFAULT_MONO_FONT);
    final FontUIResource monoFont = new FontUIResource(monospaceFont, Font.PLAIN, DEFAULT_FONT_SIZE);

    // Keep old style and size
    for (final String fontResource : FontResources.FONT_RESOURCES) {
      final Font curFont = ObjectUtils.notNull(uiDefaults.getFont(fontResource), uiFont);
      uiDefaults.put(fontResource, uiFont.deriveFont(curFont.getStyle(), (float) curFont.getSize()));
    }

    uiDefaults.put("PasswordField.font", monoFont);
    uiDefaults.put("TextArea.font", monoFont);
    uiDefaults.put("TextPane.font", textFont);
    uiDefaults.put("EditorPane.font", textFont);
  }

  /**
   * Apply fonts according to settings
   */
  @SuppressWarnings("FeatureEnvy")
  private static void applyFonts() {
    final UISettings uiSettings = UISettings.getInstance();
    @NonNls final UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
    final int treeFontSize = JBUI.scale(MTConfig.getInstance().getTreeFontSize());

    final boolean useMaterialFont = MTConfig.getInstance().isUseMaterialFont();

    if (uiSettings.getOverrideLafFonts()) {
      applySettingsFont(lookAndFeelDefaults, uiSettings.getFontFace(), uiSettings.getFontSize());
    } else if (useMaterialFont) {
      applyMaterialFonts(lookAndFeelDefaults);
    } else {
      if (SystemInfo.isMacOSYosemite) {
        LafManagerImpl.installMacOSXFonts(lookAndFeelDefaults);
      }
    }

    if (MTConfig.getInstance().isTreeFontSizeEnabled()) {
      // Tree font size
      final Font font = lookAndFeelDefaults.getFont("Tree.font");
      lookAndFeelDefaults.put("Tree.font", font.deriveFont((float) treeFontSize));
      LafManager.getInstance().updateUI();
    }
  }
  //endregion

  //region Contrast support

  /**
   * Apply contrast
   *
   * @param reloadUI if true, reload the ui
   */
  @SuppressWarnings("FeatureEnvy")
  private static void applyContrast(final boolean reloadUI) {
    final boolean apply = MTConfig.getInstance().isContrastMode();
    final MTThemeable mtTheme = DDLCConfig.getInstance().getSelectedTheme().getTheme();
    for (final String resource : ContrastResources.CONTRASTED_RESOURCES) {
      final Color contrastedColor = apply ? mtTheme.getContrastColor() : mtTheme.getBackgroundColor();
      UIManager.put(resource, contrastedColor);
    }

    if (reloadUI) {
      reloadUI();
    }
  }

  /**
   * Remove all contrast properties
   */
  private static void resetContrast() {
    for (final String resource : ContrastResources.CONTRASTED_RESOURCES) {
      UIManager.put(resource, null);
    }
  }
  //endregion

  //region Custom tree indents support

  /**
   * Apply custom tree indent
   */
  @SuppressWarnings("FeatureEnvy")
  private static void applyCustomTreeIndent() {
    final MTConfig mtConfig = MTConfig.getInstance();

    if (mtConfig.isCustomTreeIndent()) {
      UIManager.put("Tree.leftChildIndent", mtConfig.getLeftTreeIndent());
      UIManager.put("Tree.rightChildIndent", mtConfig.getRightTreeIndent());
    } else {
      UIManager.put("Tree.leftChildIndent", (DEFAULT_INDENT / 2) + JBUI.scale(7));
      UIManager.put("Tree.rightChildIndent", (DEFAULT_INDENT / 2) + JBUI.scale(4));
    }
  }
  //endregion

  //region Compact Menus support

  /**
   * Apply custom tree indent
   */
  private static void applyMenusHeight() {
    final MTConfig mtConfig = MTConfig.getInstance();

    if (mtConfig.isCompactMenus()) {
      UIManager.put("PopupMenuSeparator.height", 3);
      UIManager.put("PopupMenuSeparator.stripeIndent", 1);
    } else {
      UIManager.put("PopupMenuSeparator.height", 10);
      UIManager.put("PopupMenuSeparator.stripeIndent", 5);
    }
  }
  //endregion

  //region Compact Sidebar support

  /**
   * Use compact sidebar option
   */
  @SuppressWarnings("FeatureEnvy")
  private static void applyCompactSidebar(final boolean reloadUI) {
    final boolean isCustomSidebarHeight = MTConfig.getInstance().isCompactSidebar();
    final int customSidebarHeight = MTConfig.getInstance().getCustomSidebarHeight();
    final int rowHeight = isCustomSidebarHeight ? JBUI.scale(customSidebarHeight) : JBUI.scale(DEFAULT_SIDEBAR_HEIGHT);
    UIManager.put("Tree.rowHeight", rowHeight);

    if (reloadUI) {
      reloadUI();
    }
  }

  //endregion

  //region Accents supports

  /**
   * Override patch style editor kit for custom accent support
   */
  @SuppressWarnings( {"FeatureEnvy",
      "StringConcatenation",
      "OverlyBroadCatchBlock"})
  private static void patchStyledEditorKit() {
    @NonNls final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
    final MTConfig mtConfig = MTConfig.getInstance();
    final MTThemeable selectedTheme = DDLCConfig.getInstance().getSelectedTheme().getTheme();

    // Load css
    final URL url = selectedTheme.getClass().getResource(selectedTheme.getId() + (JBUI.isUsrHiDPI() ? RETINA : NON_RETINA));
    StyleSheet styleSheet = UIUtil.loadStyleSheet(url);
    if (styleSheet == null) {
      final URL fallbackUrl = DarculaLaf.class.getResource(DARCULA + (JBUI.isUsrHiDPI() ? RETINA : NON_RETINA));
      styleSheet = UIUtil.loadStyleSheet(fallbackUrl);
    }

    // Add custom accent color
    assert styleSheet != null;
    final String accentColor = ObjectUtils.notNull(mtConfig.getAccentColor(), MTAccents.TURQUOISE.getHexColor());

    @NonNls final String css = "a, address, b { color: #%s; }";
    styleSheet.addRule(String.format(css, accentColor));
    UIManager.put("StyledEditorKit.JBDefaultStyle", styleSheet);
    defaults.put("StyledEditorKit.JBDefaultStyle", styleSheet);

    try {
      final Field keyField = HTMLEditorKit.class.getDeclaredField("DEFAULT_STYLES_KEY");
      keyField.setAccessible(true);
      AppContext.getAppContext().put(keyField.get(null), styleSheet);
    } catch (final Exception ignored) {
    }
  }
  //endregion

  //region Tabs Height support

  /**
   * Sets tabs height.
   *
   * @param newTabsHeight the new tabs height
   */
  public static void setTabsHeight(final int newTabsHeight) {
    MTConfig.getInstance().setTabsHeight(newTabsHeight);
  }
  //endregion

  /**
   * Trigger a reloadUI event
   */
  private static void reloadUI() {
    try {
      UIManager.setLookAndFeel(new MTDarkLaf(DDLCConfig.getInstance().getSelectedTheme().getTheme()));

      applyFonts();

      DarculaInstaller.uninstall();
      if (UIUtil.isUnderDarcula()) {
        DarculaInstaller.install();
      }
      LafManager.getInstance().updateUI();
    } catch (final UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  //region Title bar support

  /**
   * Theme title bar.
   */
  static void themeTitleBar() {
    final boolean isDarkTitleOn = MTConfig.getInstance().isDarkTitleBar();
    if (SystemInfo.isWin10OrNewer && isDarkTitleOn) {
      // Write in the registry
      themeWindowsTitleBar();
    }
  }

  /**
   * Theme windows title bar.
   */
  private static void themeWindowsTitleBar() {
    final Color backgroundColor = MTConfig.getInstance().getSelectedTheme().getTheme().getBackgroundColor();

    WinRegistry.writeTitleColor(backgroundColor);
  }

  //endregion
}
