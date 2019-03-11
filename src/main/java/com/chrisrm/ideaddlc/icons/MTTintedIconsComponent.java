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

package com.chrisrm.ideaddlc.icons;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.util.messages.MessageBusConnection;
import io.acari.DDLC.LegacySupportUtility;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * Apply a tint to the icons. This is used either for accented icons and themed icons.
 */
public final class MTTintedIconsComponent implements BaseComponent {
  private static MessageBusConnection connect;
  private static Object patcher;

  static {
    LegacySupportUtility.INSTANCE.invokeClassSafely("com.intellij.ide.ui.laf.darcula.ui.DarculaSeparatorUI", () -> {
      connect = ApplicationManager.getApplication().getMessageBus().connect();
      Class<?> aClass = Class.forName("io.acari.DDLC.icons.TintedColorPatcher");
      Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
      patcher = declaredConstructors[0].newInstance(connect);
    });
  }

  @Override
  public void initComponent() {

  }

  @Override
  public void disposeComponent() {
    if(connect != null){
      connect.disconnect();
    }
  }

  @NonNls
  @Override
  @NotNull
  public String getComponentName() {
    return "DDLCMTTintedIconsComponent";
  }

}
