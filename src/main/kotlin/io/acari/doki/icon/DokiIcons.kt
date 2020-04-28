package io.acari.doki.icon

import com.intellij.ui.IconManager.getInstance
import javax.swing.Icon

object DokiIcons {
  object CheckBox {
    const val CHECK_MARK_KEY = "CheckBoxMenuItem.checkIcon"
    val CHECK_MARK = load("/icons/icons/checkmark.svg")
  }

  object Tree {
    const val EXPANDED_KEY = "Tree.expandedIcon"
    const val SELECTED_EXPANDED_KEY = "Tree.expandedSelectedIcon"
    const val COLLAPSED_KEY = "Tree.collapsedIcon"
    const val SELECTED_COLLAPSED_KEY = "Tree.collapsedSelectedIcon"
    val COLLAPSED = load("/icons/tree/treeCollapsed.svg")
    val EXPANDED = load("/icons/tree/treeExpanded.svg")
  }

  private fun load(path: String): Icon =
    getInstance().getIcon(path, DokiIcons::class.java)
}