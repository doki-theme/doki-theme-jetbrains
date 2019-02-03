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

package com.chrisrm.ideaddlc.icons;

import com.chrisrm.ideaddlc.MTConfig;
import com.chrisrm.ideaddlc.MTThemeManager;
import com.chrisrm.ideaddlc.icons.patchers.*;
import com.chrisrm.ideaddlc.icons.patchers.glyphs.*;
import com.chrisrm.ideaddlc.listeners.ConfigNotifier;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.fileTypes.FileTypeEvent;
import com.intellij.openapi.fileTypes.FileTypeListener;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconPathPatcher;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBusConnection;
import io.acari.DDLC.icons.patchers.AccentTintedIconsPatcher;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@SuppressWarnings("OverlyCoupledClass")
public final class MTIconReplacerComponent implements BaseComponent {
  private static final Set<IconPathPatcher> installedPatchers = ContainerUtil.newHashSet();

  private MessageBusConnection connect;
  static {
    //todo: figure out how to have it be replaceable
    IconLoader.installPathPatcher(new AccentTintedIconsPatcher());
    IconLoader.installPathPatcher(new ThemedTintedIconsPatcher());
  }

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
  }

  public static void useDDLCIcons() {
    MTIconPatcher.clearCache();
    removePathPatchers();
    if (MTThemeManager.isDDLCActive()) {
        installPathPatchers();
        installPSIPatchers();
        installFileIconsPatchers();
    }
  }

  @SuppressWarnings("OverlyCoupledMethod")
  private static void installPathPatchers() {

    installPathPatcher(new AllIconsPatcher());
    installPathPatcher(new ImagesIconsPatcher());
    installPathPatcher(new VCSIconsPatcher());
    installPathPatcher(new GradleIconsPatcher());
    installPathPatcher(new TasksIconsPatcher());
    installPathPatcher(new MavenIconsPatcher());
    installPathPatcher(new TerminalIconsPatcher());
    installPathPatcher(new BuildToolsIconsPatcher());
    installPathPatcher(new RemoteServersIconsPatcher());
    installPathPatcher(new DatabaseToolsIconsPatcher());
    installPathPatcher(new WizardPluginsIconsPatcher());

    installPathPatcher(new PHPIconsPatcher());
    installPathPatcher(new PythonIconsPatcher());
    installPathPatcher(new AppEngineIconsPatcher());
    installPathPatcher(new CythonIconsPatcher());
    installPathPatcher(new MakoIconsPatcher());
    installPathPatcher(new JinjaIconsPatcher());
    installPathPatcher(new FlaskIconsPatcher());
    installPathPatcher(new DjangoIconsPatcher());
    installPathPatcher(new ChameleonIconsPatcher());
    installPathPatcher(new PyQtIconsPatcher());
    installPathPatcher(new Web2PythonIconsPatcher());

    installPathPatcher(new JavascriptIconsPatcher());
    installPathPatcher(new RubyIconsPatcher());
    installPathPatcher(new GroovyIconsPatcher());

    installPathPatcher(new GolandIconsPatcher());
    installPathPatcher(new DockerIconsPatcher());

    installPathPatcher(new DataGripIconsPatcher());
    installPathPatcher(new CLionIconsPatcher());
    installPathPatcher(new AppCodeIconsPatcher());
    installPathPatcher(new WebDeploymentIconsPatcher());
    installPathPatcher(new RestClientIconsPatcher());
    installPathPatcher(new UmlIconsPatcher());
    installPathPatcher(new MarkdownIconsPatcher());
    installPathPatcher(new KotlinIconsPatcher());

    installPathPatcher(new RiderIconsPatcher());
    installPathPatcher(new ResharperIconsPatcher());
  }

  @SuppressWarnings("OverlyCoupledMethod")
  private static void installPSIPatchers() {
    installPathPatcher(new GlyphsPatcher());
    installPathPatcher(new ActionsGlyphsPatcher());
    installPathPatcher(new GeneralGlyphsPatcher());
    installPathPatcher(new GutterGlyphsPatcher());
    installPathPatcher(new GroovyGlyphsPatcher());

    installPathPatcher(new JavascriptGlyphsPatcher());
    installPathPatcher(new PHPGlyphsPatcher());
    installPathPatcher(new PythonGlyphsPatcher());
    installPathPatcher(new RubyGlyphsPatcher());
    installPathPatcher(new DataGripGlyphsPatcher());
    installPathPatcher(new AppCodeGlyphsPatcher());
    installPathPatcher(new GolandGlyphsPatcher());
    installPathPatcher(new CLionGlyphsPatcher());
    installPathPatcher(new AopGlyphsPatcher());
    installPathPatcher(new UmlGlyphsPatcher());
    installPathPatcher(new SassGlyphsPatcher());
    installPathPatcher(new KotlinGlyphsPatcher());

    installPathPatcher(new OtherGlyphsPatcher());
  }

  private static void installFileIconsPatchers() {
    installPathPatcher(new PHPFileIconsPatcher());
    installPathPatcher(new SassIconsPatcher());
    installPathPatcher(new KotlinFileIconsPatcher());
  }

  private static void removePathPatchers() {
    for (final IconPathPatcher iconPathPatcher : installedPatchers) {
      removePathPatcher(iconPathPatcher);
    }
    installedPatchers.clear();
  }

  private static void installPathPatcher(final IconPathPatcher patcher) {
    installedPatchers.add(patcher);
    IconLoader.installPathPatcher(patcher);
  }

  private static void removePathPatcher(final IconPathPatcher patcher) {
    IconLoader.removePathPatcher(patcher);
  }

  @Override
  public void disposeComponent() {

    MTIconPatcher.clearCache();
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "com.chrisrm.ideaddlc.icons.MTIconReplacerComponent";
  }
}
