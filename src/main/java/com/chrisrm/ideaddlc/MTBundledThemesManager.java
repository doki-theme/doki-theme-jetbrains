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

package com.chrisrm.ideaddlc;

import com.chrisrm.ideaddlc.themes.BundledThemeEP;
import com.chrisrm.ideaddlc.themes.MTThemeable;
import com.chrisrm.ideaddlc.themes.models.*;
import com.intellij.openapi.components.ServiceManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import io.acari.DDLC.DDLCConfig;
import io.acari.DDLC.DDLCThemeFacade;
import io.acari.DDLC.DDLCThemes;
import io.acari.DDLC.themes.models.MonikaBundledTheme;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the Bundled themes (external themes)
 */
public final class MTBundledThemesManager {
  private final Map<String, MTBundledTheme> bundledThemes = new HashMap<>();

  public MTBundledThemesManager() {

  }

  public static MTBundledThemesManager getInstance() {
    return ServiceManager.getService(MTBundledThemesManager.class);
  }

  public Map<String, MTBundledTheme> getBundledThemes() {
    return bundledThemes;
  }

  /**
   * Load external themes
   *
   * @throws Exception
   */
  public void loadBundledThemes() throws Exception {
    for (final BundledThemeEP ep : EP_NAME.getExtensions()) {
      final MTBundledTheme mtBundledTheme = loadBundledTheme(ep.path + ".xml", ep);
      mtBundledTheme.setName(ep.name);
      getBundledThemes().put(mtBundledTheme.getThemeId(), mtBundledTheme);
    }

    for (final MTBundledTheme mtBundledTheme : bundledThemes.values()) {
      if (DDLCThemes.getThemeFor(mtBundledTheme.getId()) == null) {
        DDLCThemes.addTheme(DDLCThemes.fromTheme(mtBundledTheme));
      }
    }

  }

  public MTThemeable getActiveTheme() {
    final DDLCThemeFacade selectedTheme = DDLCConfig.getInstance().getSelectedTheme();
    return selectedTheme.getTheme();
  }

  public MTBundledTheme findTheme(final String selectedTheme) {
    return bundledThemes.get(selectedTheme);
  }

  private MTBundledTheme loadBundledTheme(final String resource, final BundledThemeEP ep) throws Exception {
    final URL url = ep.getLoaderForClass().getResource(resource);
    if (url == null) {
      throw new Exception("Cannot read theme from " + resource);
    }

    final XStream xStream = new XStream(new DomDriver());
    xStream.alias("mtTheme", MonikaBundledTheme.class);
    xStream.alias("color", MTThemeColor.class);

    xStream.useAttributeFor(MTThemeColor.class, "id");
    xStream.useAttributeFor(MTThemeColor.class, "value");

    xStream.registerConverter(new MTThemesConverter(
        xStream.getConverterLookup().lookupConverterForType(MTBundledTheme.class),
        xStream.getReflectionProvider()
    ));

    xStream.addDefaultImplementation(MonikaBundledTheme.class, MTBundledTheme.class);

    try {
      return (MTBundledTheme) xStream.fromXML(url);
    } catch (final Exception e) {
      return new MonikaBundledTheme();
    }
  }

  public final class MTThemesConverter implements Converter {
    private final Converter defaultConverter;
    private final ReflectionProvider reflectionProvider;

    public MTThemesConverter(final Converter defaultConverter, final ReflectionProvider reflectionProvider) {
      this.defaultConverter = defaultConverter;
      this.reflectionProvider = reflectionProvider;
    }

    @Override
    public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
      defaultConverter.marshal(source, writer, context);
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
      final boolean dark = Boolean.parseBoolean(reader.getAttribute("dark"));
      //TODO: DIS JANK
      final Class<? extends MTBundledTheme> themeClass = dark ? MonikaBundledTheme.class : MonikaBundledTheme.class;
      final Object result = reflectionProvider.newInstance(themeClass);
      return context.convertAnother(result, themeClass, defaultConverter);
    }

    @Override
    public boolean canConvert(final Class type) {
      return MTBundledTheme.class.isAssignableFrom(type);
    }
  }
}
