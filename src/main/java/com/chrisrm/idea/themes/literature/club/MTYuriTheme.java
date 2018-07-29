package com.chrisrm.idea.themes.literature.club;

public final class MTYuriTheme extends MTDokiDokiTheme {
  public MTYuriTheme() {
    super("mt.yuri", "Material Yuri", false, "Yuri");
  }

  @Override
  public String getBackgroundColorString() {
    return "e8e4ff";
  }

  @Override
  public String getSecondaryBackgroundColorString() {
    return "bbb8ff";
  }

  @Override
  public String getSecondaryForegroundColorString() {
    return "562474";
  }

  @Override
  public String getSelectionForegroundColorString() {
    return "c06fff";
  }

  @Override
  public String getSelectionBackgroundColorString() {
    return "e4caff";
  }

  @Override
  public String getTreeSelectionBackgroundColorString() {
    return "d3a5fa";
  }

  @Override
  public String getMenuBarSelectionForegroundColorString() {
    return "ffffff";
  }

  @Override
  public String getMenuBarSelectionBackgroundColorString() {
    return "7c0e9d";
  }

  @Override
  public String getNotificationsColorString() {
    return "d3ceff";
  }

  @Override
  public String getContrastColorString() {
    return "ccaaff";
  }

  @Override
  public String getEditorTabColorString() {
    return getContrastColorString();
  }
}
