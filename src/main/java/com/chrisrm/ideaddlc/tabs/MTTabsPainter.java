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

package com.chrisrm.ideaddlc.tabs;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.chrisrm.ideaddlc.utils.MTUI;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.paint.RectanglePainter2D;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.newImpl.DefaultEditorTabsPainter;
import com.intellij.ui.tabs.newImpl.JBDefaultTabPainter;
import com.intellij.ui.tabs.newImpl.JBEditorTabs;
import com.intellij.ui.tabs.newImpl.ShapeTransform;
import io.acari.DDLC.DDLCConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class MTTabsPainter extends JBDefaultTabPainter {
  private final MTConfig mtConfig = MTConfig.getInstance();

  @SuppressWarnings("unused")
  public MTTabsPainter() {
  }


  private Color getInactiveBackground() {
    boolean isContrast = MTConfig.getInstance().isContrastMode();
    return MTUI.TabbedPane.getInactiveBackground(isContrast);
  }

  @Override
  public void paintTab(@NotNull JBTabsPosition position, @NotNull Graphics2D g, @NotNull Rectangle rect, int borderThickness, @Nullable Color tabColor, boolean hovered) {
    Color inactiveBackground = this.getInactiveBackground();
    Color hoveredBackground = MTUI.TabbedPane.getHoveredBackground();
    g.setColor(hovered ? hoveredBackground : inactiveBackground);
    RectanglePainter2D.FILL.paint(g, (double)rect.x, (double)rect.y, (double)rect.width, (double)rect.height);
  }

  @Override
  public void paintSelectedTab(@NotNull JBTabsPosition position, @NotNull Graphics2D g, @NotNull Rectangle rect, int borderThickness, @Nullable Color tabColor, boolean active, boolean hovered) {
    g.setColor(hovered ? MTUI.TabbedPane.getHoveredBackground() : MTUI.TabbedPane.getBackground());
    RectanglePainter2D.FILL.paint(g, (double)rect.x, (double)rect.y, (double)rect.width, (double)rect.height);
    int borderThickness2 = this.mtConfig.getHighlightThickness() + 1;
    Color underlineColor = this.getIndicatorColor();
    g.setColor(underlineColor);
    MTTabsHighlightPainter.paintHighlight(borderThickness2, g, rect);
  }

  @Override
  public final Color getBackgroundColor() {
    final DDLCConfig config = DDLCConfig.getInstance();
    final MTThemeable mtTheme = config.getSelectedTheme().getTheme();
    return mtTheme.getBackgroundColor();
  }

  @SuppressWarnings("FeatureEnvy")
  public static Color getContrastColor() {
    final MTConfig config = MTConfig.getInstance();
    final MTThemeable mtTheme = DDLCConfig.getInstance().getSelectedTheme().getTheme();
    return config.isContrastMode() ? mtTheme.getContrastColor() : mtTheme.getBackgroundColor();
  }

  @NotNull
  private Color getIndicatorColor() {
    Color accentColor = ColorUtil.fromHex(this.mtConfig.getAccentColor());
    Color highlightColor = this.mtConfig.getHighlightColor();
    return this.mtConfig.isHighlightColorEnabled() ? highlightColor : accentColor;
  }
}
