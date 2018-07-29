package com.chrisrm.idea.themes.literature.club;

public final class MTNatsukiTheme extends MTDokiDokiTheme {
  public MTNatsukiTheme() {
    super("mt.natsuki", "Material Natsuki", false, "Natsuki");
  }

  @Override
  public String getBackgroundColorString() {
    return "fff3fc";
  }

  @Override
  public String getSecondaryBackgroundColorString() {
    return "ffceeb";
  }

  @Override
  public String getSecondaryForegroundColorString() {
    return "b9198d";
  }

  @Override
  public String getSelectionForegroundColorString() {
    return "fa6fe2";
  }

  @Override
  public String getSelectionBackgroundColorString() {
    return "ffd5f5";
  }

  @Override
  public String getTreeSelectionBackgroundColorString() {
    return "ff6eec";
  }

  @Override
  public String getMenuBarSelectionForegroundColorString() {
    return "ffffff";
  }

  @Override
  public String getMenuBarSelectionBackgroundColorString() {
    return "d9031a";
  }

  @Override
  public String getNotificationsColorString() {
    return "ffc7ec";
  }

  @Override
  public String getContrastColorString() {
    return "fdceff";
  }

  @Override
  public String getEditorTabColorString() {
    return getContrastColorString();
  }
}
