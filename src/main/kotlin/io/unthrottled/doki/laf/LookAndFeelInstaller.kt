package io.unthrottled.doki.laf

import com.intellij.ui.JBColor.DARK_GRAY
import com.intellij.ui.JBColor.namedColor
import com.intellij.util.ui.LafIconLookup
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.service.GlassNotificationService
import io.unthrottled.doki.service.PluginService
import io.unthrottled.doki.ui.DokiTableSelectedCellHighlightBorder
import io.unthrottled.doki.ui.ToggleButtonUI
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.UIManager
import kotlin.collections.set

object LookAndFeelInstaller {
  init {
    installAllUIComponents()
  }

  fun installAllUIComponents() {
    installIcons()
    installButtons()
    installDefaults()
    GlassNotificationService.makeNotificationSeeThrough()
  }

  private fun installDefaults() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["TextPaneUI"] = "javax.swing.plaf.basic.BasicTextPaneUI"
    val tableSelectedBorder = DokiTableSelectedCellHighlightBorder()
    defaults["Table.focusSelectedCellHighlightBorder"] = tableSelectedBorder
    defaults["Table.focusCellHighlightBorder"] = tableSelectedBorder
    defaults["List.focusCellHighlightBorder"] = BorderFactory.createEmptyBorder()
    defaults["TitledBorder.border"] = BorderFactory.createLineBorder(
      namedColor("Doki.Accent.color", DARK_GRAY)
    )
  }

  private fun installIcons() {
    if (PluginService.areIconsInstalled()) {
      return
    }

    setTreeIcons(
      collapsed = DokiIcons.Tree.COLLAPSED,
      expanded = DokiIcons.Tree.EXPANDED
    )
  }

  fun removeIcons() {
    setTreeIcons(
      collapsed = LafIconLookup.getSelectedIcon("treeCollapsed"),
      expanded = LafIconLookup.getSelectedIcon("treeExpanded")
    )
  }

  private fun setTreeIcons(collapsed: Icon, expanded: Icon) {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults[DokiIcons.Tree.COLLAPSED_KEY] = collapsed
    defaults[DokiIcons.Tree.SELECTED_COLLAPSED_KEY] = collapsed
    defaults[DokiIcons.Tree.EXPANDED_KEY] = expanded
    defaults[DokiIcons.Tree.SELECTED_EXPANDED_KEY] = expanded
  }

  private fun installButtons() {
    val defaults = UIManager.getLookAndFeelDefaults()
    defaults["OnOffButtonUI"] = ToggleButtonUI::class.java.name
    defaults[ToggleButtonUI::class.java.name] = ToggleButtonUI::class.java
  }
}
