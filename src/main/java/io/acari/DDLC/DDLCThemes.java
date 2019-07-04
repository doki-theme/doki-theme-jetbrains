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

import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.intellij.ui.ColorUtil;
import io.acari.DDLC.actions.DarkMode;
import io.acari.DDLC.actions.JoyManager;
import io.acari.DDLC.themes.DokiDokiTheme;
import io.acari.DDLC.themes.light.MonikaTheme;
import io.acari.DDLC.themes.light.NatsukiTheme;
import io.acari.DDLC.themes.light.SayoriTheme;
import io.acari.DDLC.themes.light.YuriTheme;
import io.acari.DDLC.themes.dark.DeletedCharacterTheme;
import io.acari.DDLC.themes.dark.EdgyTheme;
import io.acari.DDLC.themes.dark.JustMonikaTheme;
import io.acari.DDLC.themes.dark.OnlyPlayWithMeTheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facade for accessing internal theme's methods.
 * Contains a list of predefined themes and will contain all bundled themes
 */
public enum DDLCThemes implements DDLCThemeFacade {
  MONIKA("MONIKA", new MonikaTheme(), new JustMonikaTheme()),
  SAYORI("SAYORI", new SayoriTheme(), new DeletedCharacterTheme()),
  NATSUKI("NATSUKI", new NatsukiTheme(), new OnlyPlayWithMeTheme()),
  YURI("YURI", new YuriTheme(), new EdgyTheme());

  private static final Map<String, DDLCThemeFacade> THEMES_MAP = Arrays.stream(values())
      .collect(Collectors.toMap(DDLCThemes::getName,
          Function.identity(),(a,b)->a, TreeMap::new));

  /**
   * The name of the theme (uppercase)
   */
  private final String name;
  /**
   * The instance of MTThemeable
   */
  private final transient MTThemeable mtTheme;
  private final transient MTThemeable darkTheme;

  DDLCThemes(final String name, final DokiDokiTheme mtTheme, DokiDokiTheme darkTheme) {
    this.name = name;
    this.mtTheme = mtTheme;
    this.darkTheme = darkTheme;
  }

  @NotNull
  @Override
  public String getThemeColorScheme() {
    return getTheme().getEditorColorsScheme();
  }

  @NotNull
  @Override
  public MTThemeable getTheme() {
    return DarkMode.isOn() ? darkTheme : mtTheme;
  }

  @Override
  public boolean isDark() {
    return DarkMode.isOn();
  }

  @NotNull
  @Override
  public String getChibi() {
    return JoyManager.isOn() ? getTheme().joyfulClubMember() : getTheme().getClubMember();
  }

  @Override
  public String getNormalChibi() {
    return  getTheme().getClubMember();
  }


  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getThemeName() {
    return getTheme().getName();
  }

  @NotNull
  @Override
  public String getThemeId() {
    return getName();
  }

  @Override
  public Icon getIcon() {
    return getTheme().getIcon();
  }

  @Override
  public String getAccentColor() {
    return getTheme().getAccentColor();
  }

  @Override
  public String getStartColor() {
    return getTheme().getStartColor();
  }

  @Override
  public String getStopColor() {
    return getTheme().getStopColor();
  }

  @Override
  public int getOrder() {
    return getTheme().getOrder();
  }

  @Override
  public String getNonProjectFileScopeColor() {
    return getTheme().getNonProjectFileScopeColor();
  }

  @Override
  public String getTestScope() {
    return getTheme().getTestScope();
  }

  @Override
  public boolean isPremium() {
    return false;
  }

  private static final Map<String, DDLCThemes> BETTER_THEME_MAP =  Arrays.stream(values())
      .collect(Collectors.toMap(DDLCThemes::getName,
          Function.identity(),(a,__)->a, HashMap::new));

  //todo: probably not a thing
  public static DDLCThemes getTheme(final String themeID) {
    return BETTER_THEME_MAP.getOrDefault(themeID, MONIKA);
  }


  /**
   * Get the list of all themes (native + bundled)
   */
  public static Vector<DDLCThemeFacade> getAllThemes() {
    final Vector<DDLCThemeFacade> DDLCThemeFacades = new Vector<>(THEMES_MAP.values());
    DDLCThemeFacades.sort(Comparator.comparingInt(DDLCThemeFacade::getOrder));
    return DDLCThemeFacades;
  }

  @Override
  public String toString() {
    return name;
  }

  public boolean isCustom() {
    return getTheme().isCustom();
  }
}
