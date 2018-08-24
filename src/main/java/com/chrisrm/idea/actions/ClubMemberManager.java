package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP;
import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.FRAME_PROP;

//ie Monika
public final class ClubMemberManager {

  private final static ClubMemberManager instance = new ClubMemberManager();
  private static final String CLUB_MEMBER_ON = "CLUB_MEMBER_ON";
  private static final String SAVED_THEME = "CLUB_MEMBER_THEME_PROPERTY";
  public static final String RESOURCES_DIRECTORY = "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources";
  private final AtomicBoolean isOn = new AtomicBoolean(true);
  private MTThemes currentTheme = getSavedTheme();

  private static MTThemes getSavedTheme() {
    return MTThemes.getTheme(PropertiesComponent.getInstance().getValue(SAVED_THEME));
  }

  private ClubMemberManager() {
  }

  public static ClubMemberManager getInstance() {
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
        .setValue(CLUB_MEMBER_ON, isOn.get());
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

    setProperty(getImagePath(),
        "80",
        IdeBackgroundUtil.Fill.PLAIN.name(),
        IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name(),
        EDITOR_PROP);
    setProperty(getFrameBackground(),
        "80",
        IdeBackgroundUtil.Fill.SCALE.name(),
        IdeBackgroundUtil.Anchor.CENTER.name(),
        FRAME_PROP);

    PropertiesComponent.getInstance().setValue(SAVED_THEME, getTheme()
        .map(MTThemes::getName)
        .orElseGet(MTThemes.MONIKA::getName));
  }

  private String getFrameBackground() {
    return RESOURCES_DIRECTORY + "/themes/" + getLiteratureClubMember();
  }

  private String getClubMemberFallback() {
    return RESOURCES_DIRECTORY + "/club_members/" + getLiteratureClubMember();
  }

  private void setProperty(String imagePath, String opacity, String fill, String anchor, String editorProp) {
    //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
    //as to why this looks this way
    String property = Stream.of(imagePath, opacity, fill, anchor)
        .collect(Collectors.joining(","));
    PropertiesComponent.getInstance().setValue(editorProp, property);
  }

  private String getImagePath() {
    String literatureClubMember = getLiteratureClubMember();
    String theAnimesPath = "/club_members/" + literatureClubMember;
    Path weebStuff = Paths.get(".", theAnimesPath).normalize().toAbsolutePath();
    if (!Files.exists(weebStuff) || hasNoContents(weebStuff)) {
      creatDirectories(weebStuff);
      return copyAnimes(theAnimesPath, weebStuff)
          .orElseGet(this::getClubMemberFallback);
    }
    return weebStuff.toString();
  }

  // It is a good thing that I am refactoring this :)
  @NotNull
  private String getLiteratureClubMember() {
    return getTheme()
        .map(theme -> {
          switch (theme) {
            case SAYORI:
              return !JoyManager.isOn() ?  "sayori.png" : "sayori_joy.png";
            case YURI:
              return !JoyManager.isOn() ?  "yuri.png" : "yuri_joy.png";
            case NATSUKI:
              return !JoyManager.isOn() ?  "natsuki.png" : "natsuki_joy.png";
            default:
            case MONIKA:
              return !JoyManager.isOn() ?  "just_monika.png" : "just_monika_joy.png";
          }
        })
        .orElse("just_monika.png");
  }

  private boolean hasNoContents(Path weebStuff) {
    BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(weebStuff, BasicFileAttributeView.class);
    try {
      BasicFileAttributes basicFileAttributes = fileAttributeView.readAttributes();
      return basicFileAttributes.size() < 100L;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  private void creatDirectories(Path weebStuff) {
    try {
      Files.createDirectories(weebStuff.getParent());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Optional<String> copyAnimes(String theAnimesPath, Path weebStuff) {
    try (InputStream inputStream = new BufferedInputStream(this.getClass()
        .getClassLoader()
        .getResourceAsStream(theAnimesPath));
         OutputStream bufferedWriter = Files.newOutputStream(weebStuff, StandardOpenOption.CREATE)) {
      copy(inputStream, bufferedWriter);
      return Optional.of(weebStuff)
          .map(Path::toString);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private static final int BUFFER_SIZE = 8 * 1024;
  private void copy(InputStream inputStream, OutputStream bufferedWriter) throws IOException {
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      bufferedWriter.write(buffer, 0, bytesRead);
    }
  }

  private void removeWeebShit() {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, null);
  }

  private Optional<MTThemes> getTheme() {
    return Optional.ofNullable(this.currentTheme);
  }

  public void activate(MTThemes monika) {
    this.currentTheme = monika;
    removeWeebShit();
    IdeBackgroundUtil.repaintAllWindows();
    turnOnIfNecessary();
  }
}
