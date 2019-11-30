package io.acari.doki.laf

import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxMenuItemUI
import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxUI
import io.acari.doki.icon.DokiIcons
import io.acari.doki.ui.DokiCheckboxUI
import io.acari.doki.ui.TitlePaneUI
import io.acari.doki.ui.ToggleButtonUI
import javax.swing.UIManager


object LookAndFeelInstaller {

  fun installAllUIComponents() {
    installIcons()
    installTitlePane()
    installButtons()
    installCheckboxes()
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

  private fun installCheckboxes() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults[DokiIcons.CheckBox.CHECK_MARK_KEY] = DokiIcons.CheckBox.CHECK_MARK
    defaults["CheckBoxMenuItem.borderPainted"] = false
    defaults["CheckBoxUI"] = DokiCheckboxUI::class.java.name
    defaults[DokiCheckboxUI::class.java.name] = DokiCheckboxUI::class.java
    defaults["CheckBoxMenuItemUI"] = DarculaCheckBoxMenuItemUI::class.java.name
    defaults[DarculaCheckBoxMenuItemUI::class.java.name] = DarculaCheckBoxMenuItemUI::class.java
  }

  private fun installTitlePane() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["RootPaneUI"] = TitlePaneUI::class.java.name
    defaults[TitlePaneUI::class.java.name] = TitlePaneUI::class.java
  }
}