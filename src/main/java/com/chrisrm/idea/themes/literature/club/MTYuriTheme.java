package com.chrisrm.idea.themes.literature.club;

public final class MTYuriTheme extends MTDokiDokiTheme {
  public MTYuriTheme() {
    super("mt.yuri", "Material Yuri", false);
  }

  @Override
  protected String getBackgroundColorString() {
    return "eaf0fc";
  }

  @Override
  protected String getSecondaryBackgroundColorString() {
    return "bbb8ff";
  }

  @Override
  protected String getSecondaryForegroundColorString() {
    return "b16ffa";
  }

  @Override
  protected String getSelectionForegroundColorString() {
    return "c06fff";
  }

  @Override
  protected String getSelectionBackgroundColorString() {
    return "e4caff";
  }

  @Override
  protected String getTreeSelectionBackgroundColorString() {
    return "d3a5fa";
  }

  @Override
  protected String getMenuBarSelectionForegroundColorString() {
    return "ffffff";
  }

  @Override
  protected String getMenuBarSelectionBackgroundColorString() {
    return "7c0e9d";
  }

  @Override
  protected String getNotificationsColorString() {
    return "d3ceff";
  }

  @Override
  protected String getContrastColorString() {
    return "ccaaff";
  }

  @Override
  protected String getEditorTabColorString() {
    return getContrastColorString();
  }
}
