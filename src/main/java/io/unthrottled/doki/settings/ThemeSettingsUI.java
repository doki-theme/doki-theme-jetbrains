package io.unthrottled.doki.settings;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.DataManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.options.ex.Settings;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.ActionLink;
import com.intellij.util.ui.FontInfo;
import com.intellij.util.ui.UIUtil;
import io.unthrottled.doki.config.ThemeConfig;
import io.unthrottled.doki.icon.DokiIcons;
import io.unthrottled.doki.promotions.MessageBundle;
import io.unthrottled.doki.service.PluginService;
import io.unthrottled.doki.stickers.CurrentSticker;
import io.unthrottled.doki.stickers.StickerPaneService;
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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.HyperlinkEvent;
import java.util.Arrays;

public class ThemeSettingsUI implements SearchableConfigurable, Configurable.NoScroll, DumbAware {
  private final ThemeSettingsModel themeSettingsModel = ThemeSettings.createThemeSettingsModel();
  private ThemeSettingsModel initialThemeSettingsModel = ThemeSettings.createThemeSettingsModel();

  private JTabbedPane fontTabPane;
  private JPanel rootPane;
  private JComboBox currentThemeWomboComboBox;
  private JCheckBox showStickerCheckBox;
  private JCheckBox emptyEditorBackgroundCheckBox;
  private JRadioButton primaryRadioButton;
  private JRadioButton secondaryRadioButton;
  private JCheckBox allowPositioningCheckBox;
  private JCheckBox backgroundWallpaperCheckBox;
  private JLabel warningLabel;
  private JCheckBox nameInStatusBarCheckBox;
  private JCheckBox framelessModeMacOSOnlyCheckBox;
  private JButton chooseImageButton;
  private JCheckBox useCustomStickerCheckBox;
  private JTextPane generalLinks;
  private JSlider notificationOpacitySlider;
  private JCheckBox makeNotificationsTransparentCheckBox;
  private JCheckBox themeChangeAnimationCheckBox;
  private JSpinner customFontSize;
  private JCheckBox overrideEditorFontSizeCheckBox;
  private JComboBox consoleFontWomboComboBox;
  private JCheckBox overrideConsoleFont;
  private JCheckBox discreetModeCheckBox;
  private JCheckBox enableDimensionCappingCheckBox;
  private JSpinner maxHeightSpinner;
  private JSpinner maxWidthSpinner;
  private JSpinner smolMaxHeightSpinner;
  private JSpinner smolMaxWidthSpinner;
  private JCheckBox enableSmallStickers;
  private JButton resetStickerMarginsButton;
  private JLabel marginHelp;
  private JCheckBox ignoreScalingCheckBox;
  private com.intellij.ui.components.ActionLink randmizerInstallLink;
  private JSpinner hideDelayMsSpinner;
  private JCheckBox hideOnHoverCheck;
  private JCheckBox allowPromotionalContentCheckBox;
  private ActionLink iconLink;

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

    if (PluginService.INSTANCE.isRandomizerInstalled()) {
      randmizerInstallLink.setVisible(false);
    } else {
      randmizerInstallLink.setIcon(DokiIcons.Plugins.Randomizer.INSTANCE.getTOOL_WINDOW(), false);
      randmizerInstallLink.setText(MessageBundle.message("settings.general.randomizer.install"));
      randmizerInstallLink.addActionListener(e -> {
        final var settings = Settings.KEY.getData(DataManager.getInstance().getDataContext(randmizerInstallLink));
        if (settings != null) {
          settings.select(settings.find("preferences.pluginManager"), "/tag:\"Editor Color Schemes\" Theme Randomizer");
        }
      });
    }

    if (PluginService.INSTANCE.areIconsInstalled()) {
      iconLink.setVisible(false);
    } else {
      iconLink.setIcon(DokiIcons.Plugins.Icons.INSTANCE.getTOOL_WINDOW(), false);
      iconLink.setText(MessageBundle.message("settings.general.icons.install"));
      iconLink.addActionListener(e -> {
        final var settings = Settings.KEY.getData(DataManager.getInstance().getDataContext(iconLink));
        if (settings != null) {
          settings.select(settings.find("preferences.pluginManager"), "/tag:\"User Interface\" Doki Theme Icons");
        }
      });
    }

    useCustomStickerCheckBox.setSelected(initialThemeSettingsModel.isCustomSticker());
    useCustomStickerCheckBox.addChangeListener(e ->
      themeSettingsModel.setCustomSticker(useCustomStickerCheckBox.isSelected())
    );

    allowPromotionalContentCheckBox.setSelected(initialThemeSettingsModel.getAllowPromotionalContent());
    allowPromotionalContentCheckBox.addChangeListener(e ->
      themeSettingsModel.setAllowPromotionalContent(allowPromotionalContentCheckBox.isSelected())
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

    ignoreScalingCheckBox.setSelected(initialThemeSettingsModel.getIgnoreScaling());
    ignoreScalingCheckBox.addActionListener(e ->
      themeSettingsModel.setIgnoreScaling(ignoreScalingCheckBox.isSelected())
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

    discreetModeCheckBox.setSelected(initialThemeSettingsModel.getDiscreetMode());
    discreetModeCheckBox.addActionListener(e -> {
        themeSettingsModel.setDiscreetMode(discreetModeCheckBox.isSelected());
        toggleDiscreetModeStuff(discreetModeCheckBox.isSelected());
      }
    );

    framelessModeMacOSOnlyCheckBox.setSelected(initialThemeSettingsModel.isThemedTitleBar());
    framelessModeMacOSOnlyCheckBox.addActionListener(e ->
      themeSettingsModel.setThemedTitleBar(framelessModeMacOSOnlyCheckBox.isSelected())
    );

    overrideEditorFontSizeCheckBox.setSelected(initialThemeSettingsModel.isCustomFontSize());
    overrideEditorFontSizeCheckBox.addActionListener(e ->
      themeSettingsModel.setCustomFontSize(overrideEditorFontSizeCheckBox.isSelected()));

    overrideConsoleFont.setSelected(initialThemeSettingsModel.isOverrideConsoleFont());
    overrideConsoleFont.addActionListener(e ->
      themeSettingsModel.setOverrideConsoleFont(overrideConsoleFont.isSelected()));

    SpinnerNumberModel customFontSizeModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getCustomFontSizeValue(),
      1,
      Integer.MAX_VALUE,
      1
    );
    customFontSize.setModel(customFontSizeModel);
    customFontSize.addChangeListener(change ->
      themeSettingsModel.setCustomFontSizeValue(
        customFontSizeModel.getNumber().intValue()
      ));

    makeNotificationsTransparentCheckBox.setSelected(initialThemeSettingsModel.isSeeThroughNotifications());
    makeNotificationsTransparentCheckBox.addActionListener(e -> {
      notificationOpacitySlider.setEnabled(makeNotificationsTransparentCheckBox.isSelected());
      themeSettingsModel.setSeeThroughNotifications(makeNotificationsTransparentCheckBox.isSelected());
    });
    notificationOpacitySlider.setForeground(UIUtil.getContextHelpForeground());
    notificationOpacitySlider.setEnabled(initialThemeSettingsModel.isSeeThroughNotifications());
    notificationOpacitySlider.setValue(initialThemeSettingsModel.getNotificationOpacity());
    notificationOpacitySlider.addChangeListener(change ->
      themeSettingsModel.setNotificationOpacity(notificationOpacitySlider.getValue())
    );

    themeChangeAnimationCheckBox.setSelected(initialThemeSettingsModel.isLafAnimation());
    themeChangeAnimationCheckBox.addActionListener(e ->
      themeSettingsModel.setLafAnimation(themeChangeAnimationCheckBox.isSelected()));

    enableDimensionCappingCheckBox.setSelected(initialThemeSettingsModel.getCapStickerDimensions());
    enableDimensionCappingCheckBox.addActionListener(e -> {
      updatePrimaryStickerDimensionCapComponents();
      themeSettingsModel.setCapStickerDimensions(enableDimensionCappingCheckBox.isSelected());
    });

    SpinnerNumberModel maxPrimaryStickerHeightSpinnerModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getMaxStickerHeight(),
      -1,
      Integer.MAX_VALUE,
      1
    );
    maxHeightSpinner.setModel(maxPrimaryStickerHeightSpinnerModel);
    maxHeightSpinner.addChangeListener(change ->
      themeSettingsModel.setMaxStickerHeight(
        maxPrimaryStickerHeightSpinnerModel.getNumber().intValue()
      ));

    SpinnerNumberModel maxPrimaryStickerWidthSpinnerModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getMaxStickerWidth(),
      -1,
      Integer.MAX_VALUE,
      1
    );
    maxWidthSpinner.setModel(maxPrimaryStickerWidthSpinnerModel);
    maxWidthSpinner.addChangeListener(change ->
      themeSettingsModel.setMaxStickerWidth(
        maxPrimaryStickerWidthSpinnerModel.getNumber().intValue()
      ));

    updatePrimaryStickerDimensionCapComponents();

    enableSmallStickers.setSelected(initialThemeSettingsModel.getShowSmallStickers());
    enableSmallStickers.addActionListener(e -> {
      updateSmolStickerDimensionCapComponents();
      themeSettingsModel.setShowSmallStickers(enableSmallStickers.isSelected());
    });

    SpinnerNumberModel smallStickerMaxHeightSpinnerModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getSmallMaxStickerHeight(),
      -1,
      Integer.MAX_VALUE,
      1
    );
    smolMaxHeightSpinner.setModel(smallStickerMaxHeightSpinnerModel);
    smolMaxHeightSpinner.addChangeListener(change ->
      themeSettingsModel.setSmallMaxStickerHeight(
        smallStickerMaxHeightSpinnerModel.getNumber().intValue()
      ));

    SpinnerNumberModel smallStickerMaxWidthSpinnerModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getSmallMaxStickerWidth(),
      -1,
      Integer.MAX_VALUE,
      1
    );
    smolMaxWidthSpinner.setModel(smallStickerMaxWidthSpinnerModel);
    smolMaxWidthSpinner.addChangeListener(change ->
      themeSettingsModel.setSmallMaxStickerWidth(
        smallStickerMaxWidthSpinnerModel.getNumber().intValue()
      ));

    updateSmolStickerDimensionCapComponents();

    resetStickerMarginsButton.addActionListener((e) -> {
      StickerPaneService.Companion.getInstance().resetMargins();
    });
    marginHelp.setForeground(UIUtil.getContextHelpForeground());

    toggleDiscreetModeStuff(initialThemeSettingsModel.getDiscreetMode());

    hideOnHoverCheck.setSelected(initialThemeSettingsModel.getHideOnHover());
    hideOnHoverCheck.addActionListener(e -> {
      hideDelayMsSpinner.setEnabled(hideOnHoverCheck.isSelected());
      themeSettingsModel.setHideOnHover(hideOnHoverCheck.isSelected());
    });

    SpinnerNumberModel hideOnHoverDelaySpinnerModel = new SpinnerNumberModel(
      initialThemeSettingsModel.getHideDelayMS(),
      10,
      Integer.MAX_VALUE,
      1
    );
    hideDelayMsSpinner.setModel(hideOnHoverDelaySpinnerModel);
    hideDelayMsSpinner.addChangeListener(e ->
      themeSettingsModel.setHideDelayMS(hideOnHoverDelaySpinnerModel.getNumber().intValue()));
    hideDelayMsSpinner.setEnabled(hideOnHoverCheck.isSelected());

    initializeFontComboBox();
  }

  private void initializeFontComboBox() {
    Application app = ApplicationManager.getApplication();
    // loading fonts is an expensive operation on some
    // other folk's machines. So delegating font loading work
    // off of the AWT thread. Also setting the initial font is
    // dependent on the combo box loading all fonts, so once this
    // font is loaded, it's assumed the combo box is loaded.
    app.executeOnPooledThread(() -> {
      FontInfo initialFont = FontInfo.get(initialThemeSettingsModel.getConsoleFontValue());
      consoleFontWomboComboBox.setSelectedItem(initialFont);
    });
  }

  private void toggleDiscreetModeStuff(boolean discreetModeOn) {
    showStickerCheckBox.setEnabled(!discreetModeOn);
    ignoreScalingCheckBox.setEnabled(!discreetModeOn);
    allowPositioningCheckBox.setEnabled(!discreetModeOn);
    chooseImageButton.setEnabled(!discreetModeOn);
    useCustomStickerCheckBox.setEnabled(!discreetModeOn);
    resetStickerMarginsButton.setEnabled(!discreetModeOn);
    primaryRadioButton.setEnabled(!discreetModeOn);
    secondaryRadioButton.setEnabled(!discreetModeOn);
    enableDimensionCappingCheckBox.setEnabled(!discreetModeOn);
    enableSmallStickers.setEnabled(!discreetModeOn);
    maxHeightSpinner.setEnabled(!discreetModeOn);
    maxWidthSpinner.setEnabled(!discreetModeOn);
    smolMaxHeightSpinner.setEnabled(!discreetModeOn);
    smolMaxWidthSpinner.setEnabled(!discreetModeOn);
    backgroundWallpaperCheckBox.setEnabled(!discreetModeOn);
    emptyEditorBackgroundCheckBox.setEnabled(!discreetModeOn);
    nameInStatusBarCheckBox.setEnabled(!discreetModeOn);
    notificationOpacitySlider.setEnabled(!discreetModeOn);
    makeNotificationsTransparentCheckBox.setEnabled(!discreetModeOn);
  }

  private void updatePrimaryStickerDimensionCapComponents() {
    maxHeightSpinner.setEnabled(enableDimensionCappingCheckBox.isSelected());
    maxWidthSpinner.setEnabled(enableDimensionCappingCheckBox.isSelected());
  }

  private void updateSmolStickerDimensionCapComponents() {
    smolMaxHeightSpinner.setEnabled(enableSmallStickers.isSelected());
    smolMaxWidthSpinner.setEnabled(enableSmallStickers.isSelected());
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
    generalLinks = new JTextPane();
    String accentHex = ColorUtil.toHex(
      JBColor.namedColor("Link.activeForeground", JBColor.namedColor("link.foreground", 0x589DF6))
    );
    generalLinks.setEditable(false);
    generalLinks.setContentType("text/html");
    generalLinks.setBackground(UIUtil.getPanelBackground());
    generalLinks.setText(
      "<html>\n" +
        "<head>\n" +
        "    <style type='text/css'>\n" +
        "        body {\n" +
        "            font-family: \"Open Sans\", \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
        "        }\n" +
        "\n" +
        "        a {\n" +
        "            color: #" + accentHex + ";\n" +
        "            font-weight: bold;\n" +
        "        }\n" +
        "\n" +
        "        p {\n" +
        "            color: #" + ColorUtil.toHex(UIUtil.getLabelForeground()) + ";\n" +
        "        }\n" +
        "        .meme {\n" +
        "            margin-top: 5;\n" +
        "            text-align: center;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<a href='https://github.com/doki-theme/doki-theme-jetbrains#documentation'>View Documentation</a><br/><br/>\n" +
        "<a href='https://github.com/doki-theme/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md'>See Changelog</a><br/><br/>\n" +
        "<a href='https://github.com/doki-theme/doki-theme-jetbrains/issues'>Report Issue</a><br/><br/>\n" +
        "</div>\n" +
        "</html>"
    );
    generalLinks.addHyperlinkListener(h -> {
      if (HyperlinkEvent.EventType.ACTIVATED.equals(h.getEventType())) {
        BrowserUtil.browse(h.getURL());
      }
    });

    currentThemeWomboComboBox = ThemeSettings.INSTANCE.createThemeComboBoxModel(
      () -> this.themeSettingsModel == null ?
        ThemeSettings.createThemeSettingsModel() :
        themeSettingsModel
    );

    consoleFontWomboComboBox = ThemeSettings.INSTANCE.createConsoleFontComboBoxModel(
      () -> this.themeSettingsModel == null ?
        ThemeSettings.createThemeSettingsModel() :
        themeSettingsModel
    );
  }
}
