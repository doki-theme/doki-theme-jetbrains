package com.chrisrm.idea.panes;

import com.intellij.ide.ui.laf.darcula.ui.DarculaEditorPaneUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class MTWeebPaneUI extends DarculaEditorPaneUI {

  public MTWeebPaneUI(JComponent comp) {
    super(comp);
    System.err.println("Rice Balls Erry Day");
  }

  @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
  public static ComponentUI createUI(JComponent comp) {
    return new MTWeebPaneUI(comp);
  }
}
