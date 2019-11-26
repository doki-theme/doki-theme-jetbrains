package io.acari.doki.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaRootPaneUI
import com.intellij.openapi.util.SystemInfo
import javax.swing.JComponent
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicRootPaneUI


open class TitlePaneUI : DarculaRootPaneUI() {

  companion object {
    private const val defaultPane = "com.sun.java.swing.plaf.windows.WindowsRootPaneUI"

    fun createUI(component: JComponent): ComponentUI =
      if (hasTransparentTitleBar()) {
        TitlePaneUI()
      } else {
        createDefaultRootPanUI()
      }

    private fun createDefaultRootPanUI(): ComponentUI = try {
      Class.forName(defaultPane).getConstructor().newInstance() as ComponentUI
    } catch (e: Exception) {
      BasicRootPaneUI()
    }

    private fun hasTransparentTitleBar(): Boolean = SystemInfo.isMac
  }
}