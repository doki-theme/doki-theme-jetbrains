package com.chrisrm.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;

public class MTWeebAction extends ToggleAction {

  public MTWeebAction() {
    super("Some Weeb Shit.");
  }

  @Override
  public boolean isSelected(final AnActionEvent e) {
    return ClubMemberManager.getInstance().weebShitOn();
  }

  @Override
  public void setSelected(final AnActionEvent e, final boolean state) {
    ClubMemberManager instance = ClubMemberManager.getInstance();
    instance.toggleWeebShit();
  }
}
