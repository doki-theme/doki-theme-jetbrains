package com.chrisrm.idea.actions;

import com.chrisrm.idea.actions.themes.MTAbstractThemeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class MTWeebAction extends MTAbstractThemeAction {

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
