package io.acari.doki.laf

import io.acari.doki.icon.DokiIcons
import io.acari.doki.ui.TitlePaneUI
import io.acari.doki.ui.ToggleButtonUI
import javax.swing.UIManager


object LookAndFeelInstaller {

  fun installAllUIComponents() {
    installIcons()
    installTitlePane()
    installButtons()
  }

  private fun installIcons() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults[DokiIcons.Tree.COLLAPSED_KEY] = DokiIcons.Tree.COLLAPSED
    defaults[DokiIcons.Tree.SELECTED_COLLAPSED_KEY] = DokiIcons.Tree.COLLAPSED
    defaults[DokiIcons.Tree.EXPANDED_KEY] = DokiIcons.Tree.EXPANDED
    defaults[DokiIcons.Tree.SELECTED_EXPANDED_KEY] = DokiIcons.Tree.EXPANDED
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