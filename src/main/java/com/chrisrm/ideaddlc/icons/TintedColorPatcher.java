package com.chrisrm.ideaddlc.icons;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.listeners.AccentsListener;
import com.chrisrm.ideaddlc.listeners.MTTopics;
import com.chrisrm.ideaddlc.listeners.ThemeListener;
import com.chrisrm.ideaddlc.utils.MTAccents;
import com.intellij.ui.ColorUtil;
import com.intellij.util.SVGLoader;
import com.intellij.util.messages.MessageBusConnection;
import io.acari.DDLC.DDLCThemeFacade;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class TintedColorPatcher implements SVGLoader.SvgColorPatcher {
  @NonNls
  private static String accentColor = MTAccents.TURQUOISE.getHexColor();
  @NonNls
  private static String themedColor = MTAccents.OCEANIC.getHexColor();

  private static final MTConfig CONFIG = MTConfig.getInstance();

  TintedColorPatcher(MessageBusConnection connect) {
    SVGLoader.setColorPatcher(this);
    TintedColorPatcher self = this;

    // Listen for changes on the settings
    connect.subscribe(MTTopics.ACCENTS, new AccentsListener() {
      @Override
      public void accentChanged(@NotNull final Color accentColor) {
        SVGLoader.setColorPatcher(null);
        SVGLoader.setColorPatcher(self);
        TintedColorPatcher.refreshAccentColor(accentColor);
      }
    });

    connect.subscribe(MTTopics.THEMES, new ThemeListener() {
      @Override
      public void themeChanged(@NotNull final DDLCThemeFacade theme) {
        SVGLoader.setColorPatcher(null);
        SVGLoader.setColorPatcher(self);
        TintedColorPatcher.refreshThemeColor(theme);
      }
    });
    refreshColors();
  }

  static void refreshAccentColor(final Color accentColor) {
    TintedColorPatcher.accentColor = ColorUtil.toHex(accentColor);
  }

  static void refreshThemeColor(final DDLCThemeFacade theme) {
    themedColor = ColorUtil.toHex(theme.getTheme().getTintedIconColor());
  }

  private static void refreshColors() {
    accentColor = CONFIG.getAccentColor();
    themedColor = ColorUtil.toHex(CONFIG.getSelectedTheme().getTheme().getTintedIconColor());
  }

  @SuppressWarnings({"IfStatementWithTooManyBranches",
      "DuplicateStringLiteralInspection"})
  @Override
  public final void patchColors(@NonNls final Element svg) {
    @NonNls final String tint = svg.getAttribute("tint");
    @NonNls final String themed = svg.getAttribute("themed");

    if ("true".equals(tint) || "fill".equals(tint)) {
      svg.setAttribute("fill", "#" + accentColor);
    } else if ("stroke".equals(tint)) {
      svg.setAttribute("stroke", "#" + accentColor);
    } else if ("true".equals(themed) || "fill".equals(themed)) {
      svg.setAttribute("fill", "#" + themedColor);
    } else if ("stroke".equals(themed)) {
      svg.setAttribute("stroke", "#" + themedColor);
    }

    final NodeList nodes = svg.getChildNodes();
    final int length = nodes.getLength();
    for (int i = 0; i < length; i++) {
      final Node item = nodes.item(i);
      if (item instanceof Element) {
        patchColors((Element) item);
      }
    }
  }
}
