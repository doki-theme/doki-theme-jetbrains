package com.chrisrm.idea.themes.literature.club;

public final class MTNatsukiTheme extends MTDokiDokiTheme {
  public MTNatsukiTheme() {
    super("mt.natsuki", "Material Natsuki", false);
  }

  @Override
  protected String getBackgroundColorString() {
    return "fff3fc";
  }

  @Override
  protected String getSecondaryBackgroundColorString() {
    return "ffceeb";
  }

  @Override
  protected String getSecondaryForegroundColorString() {
    return "ff6fe2";
  }

  @Override
  protected String getSelectionForegroundColorString() {
    return "fa6fe2";
  }

  @Override
  protected String getSelectionBackgroundColorString() {
    return "ffd5f5";
  }

  @Override
  protected String getTreeSelectionBackgroundColorString() {
    return "ff6eec";
  }

  @Override
  protected String getMenuBarSelectionForegroundColorString() {
    return "ffffff";
  }

  @Override
  protected String getMenuBarSelectionBackgroundColorString() {
    return "d9031a";
  }

  @Override
  protected String getNotificationsColorString() {
    return "ffc7ec";
  }

  @Override
  protected String getContrastColorString() {
    return "fdceff";
  }

  @Override
  protected String getEditorTabColorString() {
    return getContrastColorString();
  }
}
