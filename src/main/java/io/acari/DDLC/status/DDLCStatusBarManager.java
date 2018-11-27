/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package io.acari.DDLC.status;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.listeners.ConfigNotifier;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public final class DDLCStatusBarManager implements Disposable, DumbAware {

  private final Project project;
  private boolean statusEnabled;
  private DDLCStatusWidget DDLCStatusWidget;
  private final MessageBusConnection connect;

  private DDLCStatusBarManager(@NotNull final Project project) {
    this.project = project;
    DDLCStatusWidget = new DDLCStatusWidget(project);
    statusEnabled = MTConfig.getInstance().isStatusBarTheme();

    connect = project.getMessageBus().connect();
    // todo: figure out what dafuq todo
//    connect.subscribe(ConfigNotifier.CONFIG_TOPIC, this::refreshWidget);
  }

  public static DDLCStatusBarManager create(@NotNull final Project project) {
    return new DDLCStatusBarManager(project);
  }

  private void refreshWidget(final MTConfig mtConfig) {
    if (mtConfig.isStatusBarThemeChanged(statusEnabled)) {
      statusEnabled = mtConfig.isStatusBarTheme();

      if (statusEnabled) {
        install();
      } else {
        uninstall();
      }
    }

    DDLCStatusWidget.refresh();
  }

  void install() {
    final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
    if (statusBar != null) {
      statusBar.addWidget(DDLCStatusWidget, "before Position", project);
    }
  }

  void uninstall() {
    final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
    if (statusBar != null) {
      statusBar.removeWidget(DDLCStatusWidget.ID());
    }
  }

  @Override
  public void dispose() {
    if (!ApplicationManager.getApplication().isHeadlessEnvironment()) {
      if (DDLCStatusWidget != null) {
        uninstall();
        DDLCStatusWidget = null;
      }
    }
    connect.disconnect();
  }
}
