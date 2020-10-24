package io.unthrottled.doki.icon

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.util.ui.LafIconLookup
import io.unthrottled.doki.util.runSafelyWithResult
import javax.swing.Icon

// og class: com.intellij.util.ui.LafIconLookup
@Suppress("LongParameterList")
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

    return runSafelyWithResult({
      IconLoader.findIcon(
        "/com/intellij/ide/ui/laf/icons/darcula/$key.svg",
        LafIconLookup::class.java,
        true,
        isThrowErrorIfNotFound
      )
    }) {
      attemptToGetLegacy(key, isThrowErrorIfNotFound)
    }
  }

  private fun attemptToGetLegacy(key: String, isThrowErrorIfNotFound: Boolean): Icon? {
    return try {
      IconLoader.findLafIcon("darcula/$key", LafIconLookup::class.java, isThrowErrorIfNotFound)
    } catch (e: Throwable) {
      IconLoader.findLafIcon(
        "/com/intellij/ide/ui/laf/icons/darcula/$key",
        LafIconLookup::class.java,
        isThrowErrorIfNotFound
      )
    }
  }
}
