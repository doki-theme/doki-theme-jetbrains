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

public class MTWeebAction extends AnAction {

  public MTWeebAction() {
    super("Some Weeb Shit.");
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();
    PropertiesComponent instance = PropertiesComponent.getInstance();
    instance.setValue(IdeBackgroundUtil.FRAME_PROP, null);
    instance.setValue(IdeBackgroundUtil.EDITOR_PROP, "./icons/actions/themes/material_monkia@2x.png");
    Notification n = new Notification(
        "extras",
        "Notice",
        "Weeb Shit toggled " + Paths.get(".").toAbsolutePath(),
        NotificationType.INFORMATION);

    Notifications.Bus.notify(n);
    IdeBackgroundUtil.repaintAllWindows();
  }
}
