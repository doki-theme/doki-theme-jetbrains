package io.acari.doki.icon

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.util.ui.LafIconLookup
import javax.swing.Icon

object IconLookup {
  fun getIcon(
    name: String,
    selected: Boolean = false,
    focused: Boolean = false,
    enabled: Boolean = true,
    editable: Boolean = false,
    pressed: Boolean = false
  ): Icon {
    return findIcon(
      name,
      selected = selected,
      focused = focused,
      enabled = enabled,
      editable = editable,
      pressed = pressed,
      isThrowErrorIfNotFound = true
    ) ?: AllIcons.Actions.Stub
  }

  private fun findIcon(
    name: String,
    selected: Boolean = false,
    focused: Boolean = false,
    enabled: Boolean = true,
    editable: Boolean = false,
    pressed: Boolean = false,
    isThrowErrorIfNotFound: Boolean = false
  ): Icon? {
    var key = name
    if (editable) key += "Editable"
    if (selected) key += "Selected"

    when {
      pressed -> key += "Pressed"
      focused -> key += "Focused"
      !enabled -> key += "Disabled"
    }

    return try {
      IconLoader.findLafIcon("darcula/$key", LafIconLookup::class.java, isThrowErrorIfNotFound)
    } catch (e: Exception) {
      IconLoader.findLafIcon("/com/intellij/ide/ui/laf/icons/darcula/$key", LafIconLookup::class.java, isThrowErrorIfNotFound)
    }
  }
}
