package com.chrisrm.idea.themes.literature.club;

public final class MTSayoriTheme extends MTDokiDokiTheme {
  public MTSayoriTheme() {
    super("mt.sayori", "Material Sayori", false, "Sayori");
  }

  @Override
  public String getBackgroundColorString() {
    return "f4fbfe";
  }

  @Override
  public String getSecondaryBackgroundColorString() {
    return "c7f2ff";
  }

  @Override
  public String getSecondaryForegroundColorString() {
    return "256fe2";
  }

  @Override
  public String getSelectionForegroundColorString() {
    return "256fe2";
  }

  @Override
  public String getSelectionBackgroundColorString() {
    return "99ebf0";
  }

  @Override
  public String getTreeSelectionBackgroundColorString() {
    return "546eec";
  }

  @Override
  public String getMenuBarSelectionForegroundColorString() {
    return "ffffff";
  }

  @Override
  public String getMenuBarSelectionBackgroundColorString() {
    return "de0a22";
  }

  @Override
  public String getNotificationsColorString() {
    return "c3e8ff";
  }

  @Override
  public String getContrastColorString() {
    return "d8f2ff";
  }

  @Override
  public String getEditorTabColorString() {
    return getContrastColorString();
  }
}
