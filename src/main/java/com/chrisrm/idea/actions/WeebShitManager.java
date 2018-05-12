package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP;

public final class WeebShitManager {

  private final static WeebShitManager instance = new WeebShitManager();
  private static final String WEEB_SHIT_PROPERTY = "WEEB_SHIT_PROPERTY";
  private final AtomicBoolean isOn = new AtomicBoolean(true);
  private MTThemes mtThemes = MTThemes.MONIKA;

  private WeebShitManager() {
  }

  public static WeebShitManager getInstance() {
    return instance;
  }

  private void turnOnIfNecessary() {
    if (isOn.get())
      turnOnWeebShit();
    IdeBackgroundUtil.repaintAllWindows();
  }

  public boolean weebShitOn() {
    return isOn.get();
  }

  public void toggleWeebShit() {
    boolean weebShitIsOn = isOn.get();
    handleWeebShit(weebShitIsOn);
    isOn.getAndSet(!weebShitIsOn);
    PropertiesComponent.getInstance()
        .setValue(WEEB_SHIT_PROPERTY, isOn.get());
  }

  private void handleWeebShit(boolean weebShitIsOn) {
    if (weebShitIsOn) {
      removeWeebShit();
    } else {
      turnOnWeebShit();
    }
    IdeBackgroundUtil.repaintAllWindows();
  }

  private void turnOnWeebShit() {
    String imagePath = getImagePath();
    String opacity = "80";
    String fill = "plain";//ref -> IdeBackgroundUtil.Fill.PLAIN
    String anchor = "bottom_right";//ref -> IdeBackgroundUtil.Anchor.BOTTOM_RIGHT;
    String property = Stream.of(imagePath, opacity, fill, anchor)
        .collect(Collectors.joining(","));
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, property);
  }

  private String getImagePath() {
    String literatureClubMember = getTheme()
        .map(theme -> {
          switch (theme) {
            case SAYORI:
              return "sayori.png";
            case YURI:
              return "yuri.png";
            case NATSUKI:
              return "natsuki.png";
            default:
            case MONIKA:
              return "just_monika.png";
          }
        })
        .orElse("just_monika.png");
    String theAnimesPath = "/weebstuff/" + literatureClubMember;
    Path weebStuff = Paths.get(".", theAnimesPath).normalize().toAbsolutePath();
    if (!Files.exists(weebStuff)) {
      creatDirectories(weebStuff);
      copyAnimes(theAnimesPath, weebStuff);
    }
    return weebStuff.toString();
  }

  private void creatDirectories(Path weebStuff) {
    try {
      Files.createDirectories(weebStuff.getParent());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void copyAnimes(String theAnimesPath, Path weebStuff) {
    try (InputStream inputStream = new BufferedInputStream(this.getClass()
        .getClassLoader()
        .getResourceAsStream(theAnimesPath));
         OutputStream bufferedWriter = Files.newOutputStream(weebStuff, StandardOpenOption.CREATE)) {
      IOUtils.copy(inputStream, bufferedWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void removeWeebShit() {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, null);
  }

  private Optional<MTThemes> getTheme() {
    return Optional.ofNullable(this.mtThemes);
  }

  public void activate(MTThemes monika) {
    this.mtThemes = monika;
    removeWeebShit();
    IdeBackgroundUtil.repaintAllWindows();
    turnOnIfNecessary();
  }
}
