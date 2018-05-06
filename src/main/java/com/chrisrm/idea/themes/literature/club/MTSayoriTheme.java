package com.chrisrm.idea.themes.literature.club;

public final class MTSayoriTheme extends MTDokiDokiTheme {
  public MTSayoriTheme() {
    super("mt.sayori", "Material Monika", false);
  }

  @Override
  protected String getBackgroundColorString() {
    return "fbfcfc";
  }

  @Override
  protected String getSecondaryBackgroundColorString() {
    return "c7f2ff";
  }

  @Override
  protected String getSecondaryForegroundColorString() {
    return "256fe2";
  }

  @Override
  protected String getSelectionForegroundColorString() {
    return "256fe2";
  }
}
