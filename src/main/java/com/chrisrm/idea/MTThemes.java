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

import com.chrisrm.idea.actions.DarkMode;
import com.chrisrm.idea.themes.*;
import com.chrisrm.idea.themes.literature.club.MTMonikaTheme;
import com.chrisrm.idea.themes.literature.club.MTNatsukiTheme;
import com.chrisrm.idea.themes.literature.club.MTSayoriTheme;
import com.chrisrm.idea.themes.literature.club.MTYuriTheme;
import com.chrisrm.idea.themes.literature.club.dark.DeletedCharacterTheme;
import com.chrisrm.idea.themes.literature.club.dark.EdgyTheme;
import com.chrisrm.idea.themes.literature.club.dark.JustMonikaTheme;
import com.chrisrm.idea.themes.literature.club.dark.OnlyPlayWithMeTheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facade for accessing internal theme's methods.
 * Contains a list of predefined themes and will contain all bundled themes
 */
public enum MTThemes implements MTThemeFacade {
  MONIKA("MONIKA", new MTMonikaTheme(), new JustMonikaTheme()),
  SAYORI("SAYORI", new MTSayoriTheme(), new DeletedCharacterTheme()),
  NATSUKI("NATSUKI", new MTNatsukiTheme(), new OnlyPlayWithMeTheme()),
  YURI("YURI", new MTYuriTheme(), new EdgyTheme());

  private static final Map<String, MTThemeFacade> THEMES_MAP = Arrays.stream(values())
      .collect(Collectors.toMap(MTThemes::getName,
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

  MTThemes(final String name, final MTAbstractTheme mtTheme, MTAbstractTheme darkTheme) {
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

  public String getLiteratureClubMember () {
    return getTheme().getClubMember();
  }

  @Override
  public boolean getThemeIsDark() {
    return getTheme().isDark();
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

  /**
   * Find for a native theme or a bundled theme by its id
   *
   * @param themeID
   */
  public static MTThemeFacade getThemeFor(final String themeID) {
    return THEMES_MAP.get(themeID);
  }

  private static final Map<String, MTThemes> BETTER_THEME_MAP =  Arrays.stream(values())
      .collect(Collectors.toMap(MTThemes::getName,
          Function.identity(),(a,b)->a, HashMap::new));

  public static MTThemes getTheme(final String themeID) {
    return BETTER_THEME_MAP.getOrDefault(themeID, MONIKA);
  }

  /**
   * Add a new theme to the enum
   *
   * @param themesInterface
   */
  public static MTThemeFacade addTheme(final MTThemeFacade themesInterface) {
    if (!THEMES_MAP.containsKey(themesInterface.getThemeId())) {
      THEMES_MAP.put(themesInterface.getThemeId(), themesInterface);
    }
    return themesInterface;
  }

  /**
   * Get the list of all themes (native + bundled)
   */
  public static Collection<MTThemeFacade> getAllThemes() {
    return THEMES_MAP.values();
  }

  /**
   * Generate a themeFacade from a theme
   *
   * @param theme
   */
  public static MTThemeFacade fromTheme(final MTThemeable theme) {
    return new MTThemeFacade() {
      @NotNull
      @Override
      public String getThemeColorScheme() {
        return theme.getEditorColorsScheme();
      }

      @NotNull
      @Override
      public MTThemeable getTheme() {
        return theme;
      }

      @Override
      public boolean getThemeIsDark() {
        return theme.isDark();
      }

      @NotNull
      @Override
      public String getName() {
        return theme.getId();
      }

      @Override
      public String getThemeName() {
        return theme.getName();
      }

      @NotNull
      @Override
      public String getThemeId() {
        return theme.getThemeId();
      }

      @Override
      public Icon getIcon() {
        return theme.getIcon();
      }
    };
  }

  @Override
  public String toString() {
    return name;
  }

  public boolean isCustom() {
    return getTheme().isCustom();
  }
}
