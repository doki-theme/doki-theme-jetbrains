package com.chrisrm.idea.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;

//Todo: may not be needed.
public class MTWeebAction extends ToggleAction {

  public MTWeebAction() {
    super("Some Weeb Shit.");
  }

  @Override
  public boolean isSelected(final AnActionEvent e) {
    return WeebShitManager.getInstance().weebShitOn();
  }

  @Override
  public void setSelected(final AnActionEvent e, final boolean state) {
    WeebShitManager instance = WeebShitManager.getInstance();
    instance.setProjectRef();
    instance.toggleWeebShit();
    Notification n = new Notification(
        "extras",
        "Notice",
        String.format("Weeb Shit is %s.", instance.weebShitOn() ? "On":"Off"),
        NotificationType.INFORMATION);
    Notifications.Bus.notify(n);
  }
}
