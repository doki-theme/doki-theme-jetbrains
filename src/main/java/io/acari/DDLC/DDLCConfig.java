/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
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

package io.acari.DDLC;

import com.chrisrm.ideaddlc.config.ui.MTForm;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.acari.DDLC.actions.DarkMode;
import io.acari.DDLC.chibi.ChibiLevel;
import io.acari.DDLC.chibi.ChibiOrchestrator;
import io.acari.DDLC.listeners.DDLCConfigListener;
import io.acari.DDLC.wizard.DDLCWizardDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.acari.DDLC.chibi.ChibiOrchestrator.SAVED_THEME;

/**
 * Hey! If you think about changing all of the <code>public</code> access modifiers,
 * please think again. Doing so will prevent any of the configurations to be deserialized from the
 * settings xml file, thanks!
 */
@SuppressWarnings("UnusedReturnValue")
@State(
    name = "DokiDokiThemeConfig",
    storages = @Storage("doki_doki_theme.xml")
)
public class DDLCConfig implements PersistentStateComponent<DDLCConfig>, Cloneable {
  public static final String DEFAULT_BG =
      "https://github.com/cyclic-reference/jetbrains-theme/master/src/main/resources/themes/Doki_Doki_Literature_Club.png";

  // They are public so they can be serialized
  public String version;
  public String chibiLevel = ChibiLevel.ON.name();

  public String selectedTheme = "";

  public boolean isWizardShown = false;
  public boolean isFirstTime = true;
  public boolean isDarkMode = false;
  public boolean isDokiFileColors = false;

  public DDLCConfig() {
  }

  /**
   * Get instance of the config from the ServiceManager
   *
   * @return the MTConfig instance
   */
  public static DDLCConfig getInstance() {
    return ServiceManager.getService(DDLCConfig.class);
  }

  @Override
  public Object clone() {
    return XmlSerializerUtil.createCopy(this);
  }

  public boolean isFirstTime() {
    return isFirstTime;
  }

  public void setFirstTime(boolean firstTime) {
    isFirstTime = firstTime;
  }

  public Map asProperties() {
    return getNativeProperties();
  }

  public JSONObject asJson() throws JSONException {
    return getNativePropertiesAsJson();
  }

  public void copyFrom(final DDLCConfig configCopy) {
    XmlSerializerUtil.copyBean(configCopy, this);
  }


  /**
   * Convenience method to reset settings
   */
  public void resetSettings() {
    selectedTheme = DDLCThemes.MONIKA.getName();
  }

  public boolean needsRestart(final MTForm form) {

    return false;
  }

  /**
   * Return the selected theme by eventually loads it if not loaded yet
   *
   * @return
   */
  public DDLCThemeFacade getSelectedTheme() {
    final DDLCThemeFacade themeFor = DokiThemes.getThemeById(selectedTheme);
    return Optional.of(themeFor)
        .orElseGet(() -> Optional.ofNullable(PropertiesComponent.getInstance().getValue(SAVED_THEME))
            .map(DokiThemes::getThemeById)
            .orElse(DDLCThemes.MONIKA));
  }

  public void setSelectedTheme(final DDLCThemeFacade selectedTheme) {
    this.selectedTheme = selectedTheme.getThemeId();
  }

  public void setIsDarkMode(final boolean isDarkMode) {
    this.isDarkMode = isDarkMode;

    // Love me some tech debt <3
    if (this.isDarkMode) {
      DarkMode.turnOn();
    } else {
      DarkMode.turnOff();
    }
  }

  /**
   * Get the state of MTConfig
   */
  @Nullable
  @Override
  public DDLCConfig getState() {
    return this;
  }

  /**
   * Load the state from XML
   *
   * @param state the MTConfig instance
   */
  @Override
  public void loadState(@NotNull final DDLCConfig state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public ChibiLevel getChibiLevel() {
    return ChibiLevel.valueOf(chibiLevel);
  }

  public void setChibiLevel(final ChibiLevel chibiLevel) {
    this.chibiLevel = chibiLevel.name();
  }


  public void setAndActivateChibiLevel(final ChibiLevel chibiLevel) {
    this.chibiLevel = chibiLevel.name();
    ChibiOrchestrator.INSTANCE.setChibiLevel(chibiLevel);
  }


  @NotNull
  private Map getNativeProperties() {
    final HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", getSelectedTheme());
    hashMap.put("isFirstTime", isFirstTime);
    hashMap.put("chibiLevel", chibiLevel);
    hashMap.put("isDokiFileColors", isDokiFileColors);


    return hashMap;
  }

  private JSONObject getNativePropertiesAsJson() throws JSONException {
    final JSONObject hashMap = new JSONObject();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", getSelectedTheme());
    hashMap.put("isFirstTime", isFirstTime);
    hashMap.put("chibiLevel", chibiLevel);
    hashMap.put("isDokiFileColors", isDokiFileColors);


    return hashMap;
  }

  public String getVersion() {
    return version;
  }

  /**
   * Quick doc
   *
   * @param version
   */
  public void setVersion(final String version) {
    this.version = version;
  }

  public String getDefaultBackground() {
    return DEFAULT_BG;
  }


  public boolean isWizardShown() {
    return isWizardShown || legacyWizardShown();
  }

  private boolean legacyWizardShown() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    boolean legacyWizardShown = propertiesComponent
        .getBoolean(DDLCWizardDialog.MT_IS_SHOWN_WIZARD, false);
    if (legacyWizardShown) {
      setIsWizardShown(true);
      propertiesComponent.unsetValue(DDLCWizardDialog.MT_IS_SHOWN_WIZARD);

    }
    return legacyWizardShown;
  }

  public void setIsWizardShown(final boolean isWizardShown) {
    this.isWizardShown = isWizardShown;
  }

  public boolean isSelectedThemeChanged(DDLCThemeFacade theme) {
    return !Objects.equals(getSelectedTheme(), theme);
  }

  public void applySettings(MTForm form) {
    setSelectedTheme(form.getTheme());
    setIsDarkMode(form.isDarkMode());
    setAndActivateChibiLevel(form.getChibiLevel());

    fireChanged();
  }

  public void fireChanged() {
    ApplicationManager.getApplication().getMessageBus()
        .syncPublisher(DDLCConfigListener.Companion.getDDLC_CONFIG_TOPIC())
        .configurationChanged(this);
  }

  public boolean isDarkMode() {
    return isDarkMode;
  }

  public boolean isDarkModeChanged(boolean darkMode) {
    return darkMode != DarkMode.isOn();
  }

  public boolean isChibiLevelChanged(ChibiLevel chibiLevel) {
    return !Objects.equals(chibiLevel.name(), this.chibiLevel);
  }
}
