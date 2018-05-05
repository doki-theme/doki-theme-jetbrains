package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP;

public final class WeebShitManager {

  private final static WeebShitManager instance = new WeebShitManager();
  private static final String WEEB_SHIT_PROPERTY = "WEEB_SHIT_PROPERTY";
  private final AtomicBoolean isOn = new AtomicBoolean(false);
  private Optional<Project> projectRef = Optional.empty();
  private MTThemes mtThemes = MTThemes.MONIKA;

  private WeebShitManager() {
  }

  public static WeebShitManager getInstance() {
    return instance;
  }

  public void setProjectRef(Project projectRef) {
    this.projectRef = Optional.of(projectRef);
    this.projectRef.ifPresent(project1 -> {
      PropertiesComponent instance = PropertiesComponent.getInstance();
      isOn.getAndSet(instance.getBoolean(WEEB_SHIT_PROPERTY, false));
      if (isOn.get())
        turnOnWeebShit(project1);
    });
  }

  public boolean weebShitOn() {
    return isOn.get();
  }

  public void toggleWeebShit() {
    this.projectRef.ifPresent(project -> {
      boolean weebShitIsOn = isOn.get();
      handleWeebShit(weebShitIsOn, project);
      PropertiesComponent.getInstance()
          .setValue(WEEB_SHIT_PROPERTY, isOn.getAndSet(!weebShitIsOn));
    });
  }

  private void handleWeebShit(boolean weebShitIsOn, Project project) {
    if (weebShitIsOn) {
      removeWeebShit(project);
    } else {
      turnOnWeebShit(project);
    }
  }

  private void turnOnWeebShit(Project project) {
    String imagePath = getImagePath();
    String opacity = "75";
    String fill = "plain";//ref -> IdeBackgroundUtil.Fill.PLAIN
    String anchor = "bottom_right";//ref -> IdeBackgroundUtil.Anchor.BOTTOM_RIGHT;
    String property = Stream.of(imagePath, opacity, fill, anchor)
        .collect(Collectors.joining(","));
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, property);
  }

  private String getImagePath() {
    return "/webstuff/" + getTheme()
        .map(theme -> {
          switch (theme){
            default:
            case MONIKA:
              return "just_monika.png";
          }
        })
        .orElse("just_monika.png");
  }

  private void removeWeebShit(Project project) {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, null);
  }

  private Optional<MTThemes> getTheme(){
    return Optional.ofNullable(this.mtThemes);
  }


  public void activate(MTThemes monika) {

  }
}
