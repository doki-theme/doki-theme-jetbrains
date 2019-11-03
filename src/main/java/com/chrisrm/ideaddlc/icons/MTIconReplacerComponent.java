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

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.MTThemeManager;
import com.chrisrm.ideaddlc.icons.patchers.*;
import com.chrisrm.ideaddlc.listeners.ConfigNotifier;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.fileTypes.FileTypeEvent;
import com.intellij.openapi.fileTypes.FileTypeListener;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconPathPatcher;
import com.intellij.util.messages.MessageBusConnection;
import io.acari.DDLC.LegacySupportUtility;
import io.acari.DDLC.hax.LegacyIconHackerKt;
import jdk.nashorn.internal.objects.annotations.Property;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class MTIconReplacerComponent implements BaseComponent {
  private static final Set<IconPathPatcher> installedPatchers = new HashSet<>();
  private final CheckStyleIconPatcher checkStyleIconPatcher = new CheckStyleIconPatcher();

  @Property
  private final IconPathPatchers iconPathPatchers = IconPatchersFactory.create();


  private MessageBusConnection connect;


  @SuppressWarnings("OverlyCoupledMethod")
  @Override
  public void initComponent() {
    useDDLCIcons();
    connect = ApplicationManager.getApplication().getMessageBus().connect();

    connect.subscribe(ConfigNotifier.CONFIG_TOPIC, new ConfigNotifier() {
      @Override
      public void configChanged(final MTConfig mtConfig) {
        useDDLCIcons();
      }
    });
    connect.subscribe(FileTypeManager.TOPIC, new FileTypeListener() {
      @Override
      public void fileTypesChanged(@NotNull final FileTypeEvent event) {
        useDDLCIcons();
      }
    });
    connect.subscribe(LegacyIconHackerKt.getIconChangedTopic(), this::useDDLCIcons);
  }

  public void useDDLCIcons() {
    MTIconPatcher.clearCache();
    removePathPatchers();
    if (MTThemeManager.isDDLCActive()) {
      IconLoader.installPathPatcher(checkStyleIconPatcher);
      installPathPatchers();
      installPSIPatchers();
      installFileIconsPatchers();
    }
  }

  private void installPathPatchers() {
    for (final IconPathPatcher externalPatcher : iconPathPatchers.getIconPatchers()) {
      installPathPatcher(externalPatcher);
    }
  }

  private void installPSIPatchers() {
    for (final IconPathPatcher externalPatcher : iconPathPatchers.getGlyphPatchers()) {
      installPathPatcher(externalPatcher);
    }
  }

  private void installFileIconsPatchers() {
    for (final IconPathPatcher externalPatcher : iconPathPatchers.getFilePatchers()) {
      installPathPatcher(externalPatcher);
    }
  }


  private void removePathPatchers() {
    for (final IconPathPatcher iconPathPatcher : installedPatchers) {
      removePathPatcher(iconPathPatcher);
    }
    IconLoader.removePathPatcher(checkStyleIconPatcher);
    installedPatchers.clear();
  }

  private static void installPathPatcher(final IconPathPatcher patcher) {
    installedPatchers.add(patcher);
    IconLoader.installPathPatcher(patcher);
  }

  private static void removePathPatcher(final IconPathPatcher patcher) {
    LegacySupportUtility.INSTANCE.invokeVoidMethodSafely(IconLoader.class,
        "removePathPatcher",
        () -> IconLoader.removePathPatcher(patcher),
        () -> {
        },
        IconPathPatcher.class);
  }

  @Override
  public void disposeComponent() {

    MTIconPatcher.clearCache();
    connect.disconnect();
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "com.chrisrm.ideaddlc.icons.MTIconReplacerComponent";
  }
}
