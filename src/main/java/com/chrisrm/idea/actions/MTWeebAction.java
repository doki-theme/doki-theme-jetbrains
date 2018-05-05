package com.chrisrm.idea.actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

import java.nio.file.Paths;

//Todo: may not be needed.
public class MTWeebAction extends AnAction {

  public MTWeebAction() {
    super("Some Weeb Shit.");
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();
    WeebShitManager instance = WeebShitManager.getInstance();
    instance.setProject(project);
    instance.toggleWeebShit();
    Notification n = new Notification(
        "extras",
        "Notice",
        String.format("Weeb Shit is %s.", instance.weebShitOn() ? "On":"Off"),
        NotificationType.INFORMATION);

    Notifications.Bus.notify(n);
  }
}
