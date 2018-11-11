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

package io.acari.DDLC;

import com.chrisrm.ideaddlc.config.BeforeConfigNotifier;
import com.chrisrm.ideaddlc.config.ConfigNotifier;
import com.chrisrm.ideaddlc.config.ui.ArrowsStyles;
import com.chrisrm.ideaddlc.config.ui.IndicatorStyles;
import com.chrisrm.ideaddlc.config.ui.MTForm;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@State(
    name = "DokiDokiThemeConfig",
    storages = @Storage("doki_dokit_theme.xml")
)
public class DDLCConfig implements PersistentStateComponent<DDLCConfig>, Cloneable {
  public static final String DEFAULT_BG =
      "https://github.com/cyclic-reference/jetbrains-theme/master/src/main/resources/themes/Doki_Doki_Literature_Club.png";

  // They are public so they can be serialized
  public String version;

  public String selectedTheme = DDLCThemes.MONIKA.getName();

  public boolean isWizardShown = false;
  public boolean isFirstTime = true;

  public DDLCConfig() {
  }

  @Override
  public Object clone() {
    return XmlSerializerUtil.createCopy(this);
  }

  /**
   * Get instance of the config from the ServiceManager
   *
   * @return the MTConfig instance
   */
  public static DDLCConfig getInstance() {
    return ServiceManager.getService(DDLCConfig.class);
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
    final DDLCThemeFacade themeFor = DDLCThemes.getThemeFor(selectedTheme);
    return ObjectUtils.notNull(themeFor, DDLCThemes.MONIKA);
  }

  public void setSelectedTheme(final DDLCThemeFacade selectedTheme) {
    this.selectedTheme = selectedTheme.getThemeId();
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

  /**
   * Fire an event to the application bus that the settings have changed
   *
   * @param form
   */
  public void fireBeforeChanged(final MTForm form) {
//    ApplicationManager.getApplication().getMessageBus()
//                      .syncPublisher(BeforeConfigNotifier.BEFORE_CONFIG_TOPIC)
//                      .beforeConfigChanged(this, form);
  }

  /**
   * Fire an event to the application bus that the settings have changed
   */
  public void fireChanged() {
//    ApplicationManager.getApplication().getMessageBus()
//                      .syncPublisher(ConfigNotifier.CONFIG_TOPIC)
//                      .configChanged(this);
  }

  @NotNull
  private Map getNativeProperties() {
    final HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", selectedTheme);
    hashMap.put("isFirstTime", isFirstTime);


    return hashMap;
  }

  private JSONObject getNativePropertiesAsJson() throws JSONException {
    final JSONObject hashMap = new JSONObject();
    hashMap.put("IDE", ApplicationNamesInfo.getInstance().getFullProductName());
    hashMap.put("IDEVersion", ApplicationInfo.getInstance().getBuild().getBaselineVersion());
    hashMap.put("version", version);
    hashMap.put("selectedTheme", selectedTheme);
    hashMap.put("isFirstTime", isFirstTime);


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

  public boolean isSelectedThemeChanged(final DDLCThemeFacade theme) {
    return !selectedTheme.equals(theme.getName());
  }

  public String getDefaultBackground() {
    return DEFAULT_BG;
  }


  public boolean getIsWizardShown() {
    return isWizardShown;
  }

  public void setIsWizardShown(final boolean isWizardShown) {
    this.isWizardShown = isWizardShown;
  }
}
