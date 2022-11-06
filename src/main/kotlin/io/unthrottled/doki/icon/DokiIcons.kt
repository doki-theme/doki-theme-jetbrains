package io.unthrottled.doki.icon

import com.intellij.ui.IconManager.getInstance
import javax.swing.Icon

object DokiIcons {
  object General {

    @JvmStatic
    val PLUGIN_LOGO = load("icons/doki/Doki-Doki-Logo.svg")
  }
  object Plugins {
    object Motivator {
      val TOOL_WINDOW = load("icons/plugins/motivator/motivator_toolwindow.svg")
    }

    object Icons {
      val TOOL_WINDOW = load("icons/doki/neko_glyph.svg")
    }

    object AMII {
      val TOOL_WINDOW = load("icons/plugins/amii/plugin-tool-window.svg")
    }

    object Randomizer {
      val TOOL_WINDOW = load("icons/plugins/randomizer/randomizer-plugin-tool-window.svg")
    }
  }

  object CheckBox {
    const val CHECK_MARK_KEY = "CheckBoxMenuItem.checkIcon"
    val CHECK_MARK = load("icons/icons/checkmark.svg")
  }

  object Tree {
    const val EXPANDED_KEY = "Tree.expandedIcon"
    const val SELECTED_EXPANDED_KEY = "Tree.expandedSelectedIcon"
    const val COLLAPSED_KEY = "Tree.collapsedIcon"
    const val SELECTED_COLLAPSED_KEY = "Tree.collapsedSelectedIcon"
    val COLLAPSED = load("icons/tree/treeCollapsed.svg")
    val EXPANDED = load("icons/tree/treeExpanded.svg")
  }

  private fun load(path: String): Icon =
    getInstance().getIcon(path, DokiIcons::class.java)
}
