package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.util.PropertiesComponent;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP;

public final class WeebShitManager {

  private final static WeebShitManager instance = new WeebShitManager();
  private static final String WEEB_SHIT_PROPERTY = "WEEB_SHIT_PROPERTY";
  private final AtomicBoolean isOn = new AtomicBoolean(false);
  private MTThemes mtThemes = MTThemes.MONIKA;

  private WeebShitManager() {
  }

  public static WeebShitManager getInstance() {
    return instance;
  }

  public void setProjectRef() {
    PropertiesComponent instance = PropertiesComponent.getInstance();
    isOn.getAndSet(instance.getBoolean(WEEB_SHIT_PROPERTY, false));
    if (isOn.get())
      turnOnWeebShit();
  }

  public boolean weebShitOn() {
    return isOn.get();
  }

  public void toggleWeebShit() {
    boolean weebShitIsOn = isOn.get();
    handleWeebShit(weebShitIsOn);
    PropertiesComponent.getInstance()
        .setValue(WEEB_SHIT_PROPERTY, isOn.getAndSet(!weebShitIsOn));
  }

  private void handleWeebShit(boolean weebShitIsOn) {
    if (weebShitIsOn) {
      removeWeebShit();
    } else {
      turnOnWeebShit();
    }
    UISettings.getInstance().fireUISettingsChanged();
    for (Window window : Window.getWindows()) {
      window.repaint();
    }
  }

  private void turnOnWeebShit() {
    String imagePath = getImagePath();
    String opacity = "75";
    String fill = "plain";//ref -> IdeBackgroundUtil.Fill.PLAIN
    String anchor = "bottom_right";//ref -> IdeBackgroundUtil.Anchor.BOTTOM_RIGHT;
    String property = Stream.of(imagePath, opacity, fill, anchor)
        .collect(Collectors.joining(","));
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, property);
  }

  private String getImagePath() {
    return this.getClass()
        .getClassLoader()
        .getResource("/webstuff/" + getTheme()
            .map(theme -> {
              switch (theme) {
                default:
                case MONIKA:
                  return "just_monika.png";
              }
            })
            .orElse("just_monika.png")).getFile();
  }

  private void removeWeebShit() {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, null);
  }

  private Optional<MTThemes> getTheme() {
    return Optional.ofNullable(this.mtThemes);
  }


  public void activate(MTThemes monika) {

  }
}
