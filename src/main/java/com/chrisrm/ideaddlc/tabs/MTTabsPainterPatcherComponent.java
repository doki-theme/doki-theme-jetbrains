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

package com.chrisrm.ideaddlc.tabs;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.MTThemeManager;
import com.chrisrm.ideaddlc.themes.models.MTThemeable;
import com.chrisrm.ideaddlc.utils.MTAccents;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.impl.DefaultEditorTabsPainter;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import com.intellij.ui.tabs.impl.JBEditorTabsPainter;
import com.intellij.ui.tabs.impl.ShapeTransform;
import com.intellij.util.Consumer;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.UIUtil;
import io.acari.DDLC.DDLCConfig;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Optional;
import java.lang.reflect.Method;


/**
 * Patch the Tabs Component to get the Material Design style
 *
 * @author Dennis.Ushakov
 */
public final class MTTabsPainterPatcherComponent implements BaseComponent {

  private final MTConfig config;
  private final Field pathField;
  private final Field fillPathField;
  private final Field labelPathField;

  public MTTabsPainterPatcherComponent() throws ClassNotFoundException, NoSuchFieldException {
    config = MTConfig.getInstance();

    // Get the shapeinfo class because it is protected
    final Class<?> clazz = Class.forName("com.intellij.ui.tabs.impl.JBTabsImpl$ShapeInfo");
    // Retrieve private fields of ShapeInfo class
    pathField = clazz.getField("path");
    fillPathField = clazz.getField("fillPath");
    labelPathField = clazz.getField("labelPath");
  }

  @Override
  public void disposeComponent() {

  }

  @NotNull
  @Override
  public String getComponentName() {
    return "DDLCMTTabsPainterPatcherComponent";
  }

  private boolean ddlcActive=false;
  private boolean initalized=false;
  private FileEditor fileEditor = null;

  @Override
  public void initComponent() {
    final MessageBus bus = ApplicationManagerEx.getApplicationEx().getMessageBus();

    final MessageBusConnection connect = bus.connect();
    MTThemeManager.addMaterialThemeActivatedListener(areOtherThemesActive-> {
      if(!(areOtherThemesActive || initalized)){
        this.ddlcActive = true;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> initializeTabs(fileEditor1, this::patchPainter));
      } else if(initalized && areOtherThemesActive) {
        ddlcActive = false;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> {
              initializeTabs(fileEditor1, a ->
                  replacePainters(new DefaultEditorTabsPainter(a), new DefaultEditorTabsPainter(a), a));
              initalized = false;
            });
      }
    });
    connect.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
      @Override
      public void selectionChanged(@NotNull final FileEditorManagerEvent event) {
        final FileEditor editor = event.getNewEditor();
        if(ddlcActive){
          initializeTabs(editor, a->patchPainter(a));
        }
        fileEditor = editor;
      }
    });
  }

  private void initializeTabs(FileEditor editor, Consumer<JBEditorTabs> painterWorker) {
    if (editor != null) {
      this.initalized = true;
      Component component = editor.getComponent();
      while (component != null) {
        if (component instanceof JBEditorTabs) {
          painterWorker.consume((JBEditorTabs) component);
          return;
        }
        component = component.getParent();
      }
    }
  }

  /**
   * Patch tabsPainter
   */
  private void patchPainter(final JBEditorTabs component) {
    final JBEditorTabsPainter painter = ReflectionUtil.getField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDarkPainter");
    final Color accentColor = ObjectUtils.notNull(ColorUtil.fromHex(config.getAccentColor()), new ColorUIResource(0x80cbc4));

    if (painter instanceof MTTabsPainter) {
      return;
    }

    final MTTabsPainter tabsPainter = new MTTabsPainter(component);
    final JBEditorTabsPainter proxy = (MTTabsPainter) Enhancer.create(MTTabsPainter.class, (MethodInterceptor) (o, method, objects,
                                                                                                                methodProxy) -> {
      final Object result = method.invoke(tabsPainter, objects);

      // Custom props
      final boolean isColorEnabled = config.isHighlightColorEnabled();
      final Color borderColor = isColorEnabled ? config.getHighlightColor() : accentColor;
      final int borderThickness = config.getHighlightThickness();

      if ("paintSelectionAndBorder".equals(method.getName())) {
        paintSelectionAndBorder(objects, borderColor, borderThickness, tabsPainter);
      }

      return result;
    });

    this.replacePainters(proxy, proxy, component);
  }

  private void replacePainters(JBEditorTabsPainter defaultPainter, JBEditorTabsPainter darkPainter, JBEditorTabs component){
    ReflectionUtil.setField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDefaultPainter", defaultPainter);
    ReflectionUtil.setField(JBEditorTabs.class, component, JBEditorTabsPainter.class, "myDarkPainter", darkPainter);
  }

  /**
   * Paint tab selected and highlight border
   *
   * @param objects
   * @param borderColor
   * @param borderThickness
   * @param tabsPainter
   */
  private void paintSelectionAndBorder(final Object[] objects,
                                       final Color borderColor,
                                       final int borderThickness,
                                       final MTTabsPainter tabsPainter)
      throws IllegalAccessException {

    // Retrieve arguments
    final Graphics2D g2d = (Graphics2D) objects[0];
    final Rectangle rect = (Rectangle) objects[1];
    final Object selectedShape = objects[2];
    final Insets insets = (Insets) objects[3];
    final Color tabColor = (Color) objects[4];

    final ShapeTransform path = (ShapeTransform) pathField.get(selectedShape);
    final ShapeTransform fillPath = (ShapeTransform) fillPathField.get(selectedShape);
    final ShapeTransform labelPath = (ShapeTransform) labelPathField.get(selectedShape);

    // Other properties needed for drawing
    final Insets i = path.transformInsets(insets);
    final int rectX = rect.x;
    final int rectY = rect.y;
    final int rectHeight = rect.height;

    // The tabs component
    final JBEditorTabs tabsComponent = tabsPainter.getTabsComponent();

    // Position of tabs
    final JBTabsPosition position = tabsComponent.getTabsPosition();

    // color me
    tabsPainter.fillSelectionAndBorder(g2d, fillPath, tabColor, rectX, rectY, rectHeight);

    // shadow
    if (MTConfig.getInstance().isTabsShadow()) {
      MTTabsShadowPainter.drawTabShadow(tabsPainter, g2d, rect, path, labelPath, position);
    }

    // Finally paint the active tab highlighter
    g2d.setColor(borderColor);
    MTTabsHighlightPainter.paintHighlight(borderThickness, g2d, rect);
  }

  @SuppressWarnings("WeakerAccess")
  private class MyMethodInterceptor implements MethodInterceptor {
    private final MTTabsPainter tabsPainter;
    private final Color accentColor;

    MyMethodInterceptor(final MTTabsPainter tabsPainter, final Color accentColor) {
      this.tabsPainter = tabsPainter;
      this.accentColor = accentColor;
    }

    @SuppressWarnings({"HardCodedStringLiteral",
        "CallToSuspiciousStringMethod",
        "SyntheticAccessorCall",
        "FeatureEnvy"})
    @Override
    public final Object intercept(final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy)
        throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
      final Object result = method.invoke(tabsPainter, objects);

      // Custom props
      final boolean isColorEnabled = config.isHighlightColorEnabled();
      final Color borderColor = isColorEnabled ? config.getHighlightColor() : accentColor;
      final int borderThickness = config.getHighlightThickness();

      if ("paintSelectionAndBorder".equals(method.getName())) {
        paintSelectionAndBorder(objects, borderColor, borderThickness, tabsPainter);
    }

      return result;
    }
  }
}

