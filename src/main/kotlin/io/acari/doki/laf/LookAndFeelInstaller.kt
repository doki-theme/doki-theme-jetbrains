package io.acari.doki.laf

import com.intellij.ide.ui.laf.darcula.ui.DarculaCheckBoxMenuItemUI
import com.intellij.ide.ui.laf.darcula.ui.DarculaRadioButtonMenuItemUI
import com.intellij.ui.JBColor.*
import io.acari.doki.icon.DokiIcons
import io.acari.doki.ui.*
import javax.swing.BorderFactory
import javax.swing.UIManager

object LookAndFeelInstaller {
  init {
    installAllUIComponents()
  }

  fun installAllUIComponents() {
    installIcons()
    installTitlePane()
    installButtons()
    installCheckboxes()
    installRadioButtons()
    installDefaults()
  }

  private fun installDefaults() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["List.focusCellHighlightBorder"] = BorderFactory.createEmptyBorder()
    defaults["TitledBorder.border"] = BorderFactory.createLineBorder(
      namedColor("Doki.Accent.color", DARK_GRAY)
    )
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

  private fun installRadioButtons() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["RadioButtonUI"] = DokiRadioButtonUI::class.java.name
    defaults[DokiRadioButtonUI::class.java.name] = DokiRadioButtonUI::class.java
    defaults["RadioButtonMenuItemUI"] = DarculaRadioButtonMenuItemUI::class.java.name
    defaults[DarculaRadioButtonMenuItemUI::class.java.name] = DarculaRadioButtonMenuItemUI::class.java
  }

  private fun installTitlePane() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["RootPaneUI"] = TitlePaneUI::class.java.name
    defaults[TitlePaneUI::class.java.name] = TitlePaneUI::class.java
  }
}