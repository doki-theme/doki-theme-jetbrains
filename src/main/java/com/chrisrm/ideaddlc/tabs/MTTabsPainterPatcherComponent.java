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
import com.chrisrm.ideaddlc.MTThemeManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.ui.tabs.JBTabPainter;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.newImpl.JBEditorTabs;
import com.intellij.ui.tabs.newImpl.TabLabel;
import com.intellij.util.Consumer;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;


/**
 * Todo: revisit me.
 * <p>
 * Patch the Tabs Component to get the Material Design style
 *
 * @author Dennis.Ushakov
 */
@SuppressWarnings("WeakerAccess")
public final class MTTabsPainterPatcherComponent implements BaseComponent {

  private final MTConfig config = MTConfig.getInstance();
  private boolean ddlcActive = false;
  private boolean initalized = false;
  private FileEditor fileEditor = null;

  public MTTabsPainterPatcherComponent() {
  }

  @Override
  public void disposeComponent() {
  }

  @NonNls
  @NotNull
  @Override
  public String getComponentName() {
    return "DDLCMTTabsPainterPatcherComponent";
  }

  @Override
  public void initComponent() {
    final MessageBus bus = ApplicationManagerEx.getApplicationEx().getMessageBus();

    final MessageBusConnection connect = bus.connect();
    MTThemeManager.addMaterialThemeActivatedListener(areOtherThemesActive -> {
      if (!(areOtherThemesActive || initalized)) {
        this.ddlcActive = true;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> initializeTabs(fileEditor1, this::patchPainter));
      } else if (initalized && areOtherThemesActive) {
        ddlcActive = false;
        Optional.ofNullable(this.fileEditor)
            .ifPresent(fileEditor1 -> {
//              todo: revisit this
//              initializeTabs(fileEditor1, a ->
//                  replacePainters(new JBDefaultTabPainter(a), new JBDefaultTabPainter(a), a));
              initalized = false;
            });
      }
    });
    connect.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
      @Override
      public void selectionChanged(@NotNull final FileEditorManagerEvent event) {
        final FileEditor editor = event.getNewEditor();
        if (ddlcActive) {
          initializeTabs(editor, a -> patchPainter(a));
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
  void patchPainter(final JBEditorTabs component) {
    final MTTabsPainter tabsPainter = new MTTabsPainter();
    final JBTabPainter proxy = (JBTabPainter) Enhancer.create(MTTabsPainter.class, new TabPainterInterceptor(tabsPainter));

    applyCustomFontSize(component);
    ReflectionUtil.setField(JBEditorTabs.class, component, JBTabPainter.class, "myTabPainter", proxy);
    replacePainters(proxy,proxy, component);
  }

  private void replacePainters(JBTabPainter defaultPainter, JBTabPainter darkPainter, JBEditorTabs component) {
    ReflectionUtil.setField(JBEditorTabs.class, component, JBTabPainter.class, "myTabPainter", defaultPainter);
    ReflectionUtil.setField(JBEditorTabs.class, component, JBTabPainter.class, "myDarkPainter", darkPainter);
  }

  private void applyCustomFontSize(final JBEditorTabs component) {
    if (config.isTabFontSizeEnabled()) {
      final float tabFontSize = config.getTabFontSize();
      final Map<TabInfo, TabLabel> myInfo2Label = component.myInfo2Label;

      for (final TabLabel value : myInfo2Label.values()) {
        final Font font = value.getLabelComponent().getFont().deriveFont(tabFontSize);
        value.getLabelComponent().setFont(font);
      }
    }
  }

  private class TabPainterInterceptor implements MethodInterceptor {
    private final MTTabsPainter tabsPainter;

    TabPainterInterceptor(MTTabsPainter tabsPainter) {
      this.tabsPainter = tabsPainter;
    }

    public final Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws IllegalAccessException, InvocationTargetException {
      return method.invoke(this.tabsPainter, objects);
    }
  }
}

