package io.unthrottled.doki.legacy

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager.*
import com.intellij.ui.ExperimentalUI
import io.unthrottled.doki.themes.ThemeManager
import javax.swing.UIDefaults
import javax.swing.UIManager

object EXPUIBastardizer: LafManagerListener, Disposable {

  private val connection = getApplication().messageBus.connect()

  init {
      connection.subscribe(LafManagerListener.TOPIC, this)
  }

  @Suppress("UnstableApiUsage")
  fun bastardizeExperimentalUI() {
    if (!ExperimentalUI.isNewUI()) return

    overrideSetProperties(0)
  }

  private fun overrideSetProperties(iterations: Int) {
    if(iterations > 5) {
      return
    }

    getApplication().invokeLater {
      ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
        val defaults = UIManager.getDefaults()
        setUIProperty("ToolWindow.Button.selectedBackground", dokiTheme.getColor("highlightColor"), defaults)
        setUIProperty("ToolWindow.Button.selectedForeground", dokiTheme.getColor("iconAccent"), defaults)
      }
     overrideSetProperties(iterations + 1)
    }
  }

  private fun setUIProperty(key: String, value: Any, defaults: UIDefaults) {
    defaults.remove(key)
    defaults[key] = value
  }

  override fun dispose() {
    connection.dispose()
  }

  override fun lookAndFeelChanged(source: LafManager) {
    bastardizeExperimentalUI()
  }

}