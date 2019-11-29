package io.acari.doki.laf

import io.acari.doki.ui.TitlePaneUI
import io.acari.doki.ui.ToggleButtonUI
import javax.swing.UIManager


object LookAndFeelInstaller {

  fun installAllUIComponents() {
    installTitlePane()
    installButtons()
  }

  private fun installButtons() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["OnOffButtonUI"] = ToggleButtonUI::class.java.name
    defaults[ToggleButtonUI::class.java.name] = ToggleButtonUI::class.java
  }

  fun installTitlePane() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["RootPaneUI"] = TitlePaneUI::class.java.name
    defaults[TitlePaneUI::class.java.name] = TitlePaneUI::class.java
  }
}