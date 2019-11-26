package io.acari.doki.ui;

import com.intellij.ide.ui.laf.darcula.ui.DarculaRootPaneUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class TitlePaneUIFactory extends DarculaRootPaneUI {

    public static ComponentUI createUI(final JComponent component) {
        return TitlePaneUI.Companion.createUI(component);
    }
}
