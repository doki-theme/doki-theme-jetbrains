package io.unthrottled.doki.legacy

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.NewUI
import com.intellij.util.ui.JBUI
import io.unthrottled.doki.themes.ThemeManager
import javax.swing.UIDefaults
import javax.swing.UIManager

object EXPUIFixer : LafManagerListener, Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    connection.subscribe(LafManagerListener.TOPIC, this)
  }

  fun fixExperimentalUI() {
    if (!NewUI.isEnabled()) return

    overrideSetProperties(0)
  }

  private fun overrideSetProperties(iterations: Int) {
    if (iterations > 5) {
      return
    }

    ApplicationManager.getApplication().invokeLater {
      ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
        val defaults = UIManager.getDefaults()
        setUIProperty("ToolWindow.Button.selectedBackground", dokiTheme.getColor("highlightColor"), defaults)
        setUIProperty("ToolWindow.Button.selectedForeground", dokiTheme.getColor("iconAccent"), defaults)
        setUIProperty("Editor.SearchField.borderInsets", JBUI.insets(7, 10, 7, 8), defaults)
        setUIProperty("Tree.border", JBUI.insets(4, 12), defaults)
      }
      overrideSetProperties(iterations + 1)
    }
  }

  private fun setUIProperty(
    key: String,
    value: Any,
    defaults: UIDefaults,
  ) {
    defaults.remove(key)
    defaults[key] = value
  }

  override fun dispose() {
    connection.dispose()
  }

  override fun lookAndFeelChanged(source: LafManager) {
    fixExperimentalUI()
  }
}
