package io.unthrottled.doki.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

public class ThemeSettingsUI implements SearchableConfigurable, Configurable.NoScroll, DumbAware {
  private ThemeSettingsModel themeSettingsModel = ThemeSettings.createThemeSettingsModel();
  private ThemeSettingsModel initialThemeSettingsModel = ThemeSettings.createThemeSettingsModel();

  private JTabbedPane tabbedPane1;
  private JPanel rootPane;
  private JComboBox currentThemeWomboComboBox;
  private JCheckBox showStickerCheckBox;
  private JCheckBox emptyEditorBackgroundCheckBox;
  private JRadioButton primaryRadioButton;
  private JRadioButton secondaryRadioButton;
  private JCheckBox allowPositioningCheckBox;
  private JCheckBox backgroundWallpaperCheckBox;
  private DialogPanel materialIconPane;
  private JLabel warningLabel;
  private JCheckBox nameInStatusBarCheckBox;
  private JCheckBox framelessModeMacOSOnlyCheckBox;


  @Override
  public @NotNull
  @NonNls String getId() {
    return ThemeSettings.INSTANCE.getId();
  }

  @Override
  public @NlsContexts.ConfigurableName String getDisplayName() {
    return ThemeSettings.THEME_SETTINGS_DISPLAY_NAME;
  }

  @Override
  public @Nullable JComponent createComponent() {
    initializeComponents();
    return rootPane;
  }

  private void initializeComponents() {
    warningLabel.setForeground(UIUtil.getContextHelpForeground());
  }

  @Override
  public boolean isModified() {
    return !initialThemeSettingsModel.equals(themeSettingsModel);
  }

  @Override
  public void apply() throws ConfigurationException {
    ThemeSettings.INSTANCE.apply(themeSettingsModel);
  }

  private void createUIComponents() {
    try {
      materialIconPane = ThemeSettings.INSTANCE.createMaterialIconsPane(() -> this.themeSettingsModel);
    } catch (RuntimeException ignored) {
      materialIconPane = new DialogPanel();
    }
  }
}
