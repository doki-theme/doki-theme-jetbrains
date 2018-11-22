/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Chris Magnussen and Elior Boukhobza
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

package com.chrisrm.ideaddlc.status;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.MTThemeManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public final class MTStatusBarComponent extends AbstractProjectComponent {
  private MTStatusBarManager statusBarWidget;
  private boolean initalized = false;

  public MTStatusBarComponent(@NotNull final Project project) {
    super(project);
    MTThemeManager.addMaterialThemeActivatedListener(materialActive->{
      if(!(materialActive || initalized)){
        initalized = true;
        statusBarWidget.install();
      }
    });
  }

  @Override
  public void initComponent() {
    statusBarWidget = MTStatusBarManager.create(myProject);
  }

  @Override
  public void disposeComponent() {
    statusBarWidget.dispose();
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "DDLCStatusBarComponent";
  }

  @Override
  public void projectOpened() {
    if (MTConfig.getInstance().isStatusBarTheme() && MTThemeManager.isDDLCActive()) {
      statusBarWidget.install();
    }
  }

  @Override
  public void projectClosed() {
    statusBarWidget.uninstall();
  }
}
