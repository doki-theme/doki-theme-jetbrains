package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.openapi.project.Project;

public class WeebShitManager {

  private final static WeebShitManager instance = new WeebShitManager();
  private Project project;

  public static WeebShitManager getInstance(){
    return instance;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  public boolean weebShitOn() {
    return false;
  }

  public void toggleWeebShit() {

  }

  public void activate(MTThemes monika) {

  }
}
