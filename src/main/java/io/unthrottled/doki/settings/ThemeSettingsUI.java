package io.unthrottled.doki.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

public class ThemeSettingsUI implements SearchableConfigurable, Configurable.NoScroll, DumbAware {
  private ThemeSettingsModel themeSettingsModel = ThemeSettings.createThemeSettingsModel();
  private ThemeSettingsModel initialThemeSettingsModel = ThemeSettings.createThemeSettingsModel();

  private JTabbedPane tabbedPane1;
  private JPanel rootPane;
  private JComboBox comboBox1;
  private JCheckBox checkBox1;
  private JCheckBox checkBox2;
  private JRadioButton radioButton1;
  private JRadioButton radioButton2;
  private JCheckBox checkBox3;
  private JCheckBox checkBox4;
  private JCheckBox checkBox5;
  private DialogPanel materialIconPane;


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
