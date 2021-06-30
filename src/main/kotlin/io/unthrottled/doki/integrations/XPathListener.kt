package io.unthrottled.doki.integrations

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColors.IDENTIFIER_UNDER_CARET_ATTRIBUTES
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import io.unthrottled.doki.themes.ThemeManager
import org.intellij.plugins.xpathView.Config
import org.intellij.plugins.xpathView.XPathAppComponent

class XPathListener : LafManagerListener, StartupActivity {
  override fun lookAndFeelChanged(source: LafManager) {
    installColors()
  }

  private fun installColors() {
    ThemeManager.instance.currentTheme.ifPresentOrElse({
      val schemeForCurrentUITheme = EditorColorsManager.getInstance().schemeForCurrentUITheme
      XPathAppComponent.getInstance().config.attributes.backgroundColor = schemeForCurrentUITheme.getColor(
        EditorColors.SELECTION_BACKGROUND_COLOR
      )
      XPathAppComponent.getInstance().config.attributes.foregroundColor = schemeForCurrentUITheme.getColor(
        EditorColors.SELECTION_FOREGROUND_COLOR
      )

      XPathAppComponent.getInstance().config.contextAttributes.backgroundColor =
        IDENTIFIER_UNDER_CARET_ATTRIBUTES.defaultAttributes.backgroundColor
    }) {
      val config = Config()
      XPathAppComponent.getInstance().config.attributes.backgroundColor = config.attributes.backgroundColor
      XPathAppComponent.getInstance().config.attributes.backgroundColor = config.attributes.foregroundColor

      XPathAppComponent.getInstance().config.contextAttributes.backgroundColor =
        config.contextAttributes.backgroundColor
      XPathAppComponent.getInstance().config.contextAttributes.foregroundColor =
        config.contextAttributes.foregroundColor
    }
  }

  override fun runActivity(project: Project) {
    installColors()
  }
}
