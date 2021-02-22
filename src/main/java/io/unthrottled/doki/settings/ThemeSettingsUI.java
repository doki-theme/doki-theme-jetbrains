package io.unthrottled.doki.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ui.UIUtil;
import io.unthrottled.doki.config.ThemeConfig;
import io.unthrottled.doki.stickers.CurrentSticker;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import java.util.Arrays;

public class ThemeSettingsUI implements SearchableConfigurable, Configurable.NoScroll, DumbAware {
  private final ThemeSettingsModel themeSettingsModel = ThemeSettings.createThemeSettingsModel();
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
  private JButton chooseImageButton;
  private JCheckBox useCustomStickerCheckBox;


  @Override
  public @NotNull
  @NonNls
  String getId() {
    return ThemeSettings.SETTINGS_ID;
  }

  @Override
  public @NlsContexts.ConfigurableName String getDisplayName() {
    return ThemeSettings.THEME_SETTINGS_DISPLAY_NAME;
  }

  @Override
  public @Nullable JComponent createComponent() {
    initializeAutoCreatedComponents();
    return rootPane;
  }

  private void initializeAutoCreatedComponents() {
    chooseImageButton.addActionListener(e -> {
      CustomStickerChooser dialog = new CustomStickerChooser(
        Arrays.stream(ProjectManager.getInstance().getOpenProjects()).findFirst().orElse(
          ProjectManager.getInstance().getDefaultProject()
        ),
        themeSettingsModel.getCustomStickerPath()
      );

      dialog.showAndGet();

      if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
        themeSettingsModel.setCustomStickerPath(dialog.getPath());
      }
    });

    useCustomStickerCheckBox.setSelected(initialThemeSettingsModel.isCustomSticker());
    useCustomStickerCheckBox.addChangeListener(e ->
      themeSettingsModel.setCustomSticker(useCustomStickerCheckBox.isSelected())
    );

    warningLabel.setForeground(UIUtil.getContextHelpForeground());

    showStickerCheckBox.setSelected(initialThemeSettingsModel.getAreStickersEnabled());
    showStickerCheckBox.addActionListener(e ->
      themeSettingsModel.setAreStickersEnabled(showStickerCheckBox.isSelected())
    );

    allowPositioningCheckBox.setSelected(initialThemeSettingsModel.isMoveableStickers());
    allowPositioningCheckBox.addActionListener(e ->
      themeSettingsModel.setMoveableStickers(allowPositioningCheckBox.isSelected())
    );

    primaryRadioButton.setSelected(ThemeConfig.Companion.getInstance().getCurrentSticker() == CurrentSticker.DEFAULT);
    primaryRadioButton.addActionListener(e ->
      themeSettingsModel.setCurrentSticker(
        primaryRadioButton.isSelected() ? CurrentSticker.DEFAULT : CurrentSticker.SECONDARY
      )
    );
    secondaryRadioButton.setSelected(ThemeConfig.Companion.getInstance().getCurrentSticker() == CurrentSticker.SECONDARY);
    secondaryRadioButton.addActionListener(e ->
      themeSettingsModel.setCurrentSticker(
        secondaryRadioButton.isSelected() ? CurrentSticker.SECONDARY : CurrentSticker.DEFAULT
      )
    );

    backgroundWallpaperCheckBox.setSelected(initialThemeSettingsModel.isDokiBackground());
    backgroundWallpaperCheckBox.addActionListener(e ->
      themeSettingsModel.setDokiBackground(backgroundWallpaperCheckBox.isSelected())
    );

    emptyEditorBackgroundCheckBox.setSelected(initialThemeSettingsModel.isEmptyFrameBackground());
    emptyEditorBackgroundCheckBox.addActionListener(e ->
      themeSettingsModel.setEmptyFrameBackground(emptyEditorBackgroundCheckBox.isSelected())
    );

    nameInStatusBarCheckBox.setSelected(initialThemeSettingsModel.getShowThemeStatusBar());
    nameInStatusBarCheckBox.addActionListener(e ->
      themeSettingsModel.setShowThemeStatusBar(nameInStatusBarCheckBox.isSelected())
    );

    framelessModeMacOSOnlyCheckBox.setSelected(initialThemeSettingsModel.isThemedTitleBar());
    framelessModeMacOSOnlyCheckBox.addActionListener(e ->
      themeSettingsModel.setThemedTitleBar(framelessModeMacOSOnlyCheckBox.isSelected())
    );
  }

  @Override
  public boolean isModified() {
    return !initialThemeSettingsModel.equals(themeSettingsModel);
  }

  @Override
  public void apply() {
    ThemeSettings.INSTANCE.apply(themeSettingsModel);
    initialThemeSettingsModel = themeSettingsModel.duplicate();
  }

  private void createUIComponents() {
    currentThemeWomboComboBox = ThemeSettings.INSTANCE.createThemeComboBoxModel(
      () -> this.themeSettingsModel == null ?
        ThemeSettings.createThemeSettingsModel() :
        themeSettingsModel
    );

    try {
      materialIconPane = ThemeSettings.INSTANCE.createMaterialIconsPane(
        () -> this.themeSettingsModel == null ?
          ThemeSettings.createThemeSettingsModel() :
          themeSettingsModel
      );
    } catch (RuntimeException ignored) {
      materialIconPane = new DialogPanel();
    }
  }
}
