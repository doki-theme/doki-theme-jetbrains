package io.acari.doki.laf

import io.acari.doki.ui.tree.BorderTreeIndicator
import io.acari.doki.ui.tree.TreeIndicator
import javax.swing.UIManager


object LookAndFeelInstaller {

  fun installAllUIComponents(){
    installTreeIndicatorComponent()
  }

  fun installTreeIndicatorComponent(){
    val uiDefaults = UIManager.getDefaults()
    val indicatorPainter = BorderTreeIndicator()
    val treeIndicator = TreeIndicator(indicatorPainter)
    uiDefaults["List.sourceListSelectionBackgroundPainter"] = treeIndicator
    uiDefaults["List.sourceListFocusedSelectionBackgroundPainter"] = treeIndicator
  }
}