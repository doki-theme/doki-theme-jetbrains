package io.acari.doki.ui

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.darcula.ui.DarculaRootPaneUI
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.SystemInfo.isLinux
import com.intellij.openapi.util.SystemInfo.isMac
import com.intellij.util.ui.UIUtil.getWindow
import io.acari.doki.themes.DokiThemes.processLaf
import java.awt.Window
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JRootPane
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicRootPaneUI

class TitlePaneUI : DarculaRootPaneUI() {

  companion object {
    private const val defaultPane = "com.sun.java.swing.plaf.windows.WindowsRootPaneUI"
    const val WINDOW_DARK_APPEARANCE = "jetbrains.awt.windowDarkAppearance"
    const val TRANSPARENT_TITLE_BAR_APPEARANCE = "jetbrains.awt.transparentTitleBarAppearance"

    @JvmStatic
    @Suppress("ACCIDENTAL_OVERRIDE", "UNUSED", "UNUSED_PARAMETER")
    fun createUI(component: JComponent): ComponentUI =
      if (hasTransparentTitleBar()) {
        TitlePaneUI()
      } else {
        createDefaultRootPanUI()
      }

    private fun createDefaultRootPanUI(): ComponentUI = try {
      Class.forName(defaultPane).getConstructor().newInstance() as ComponentUI
    } catch (e: Throwable) {
      BasicRootPaneUI()
    }

    private fun hasTransparentTitleBar(): Boolean = isMac
  }

  override fun installUI(c: JComponent?) {
    super.installUI(c)
    processLaf(LafManager.getInstance().currentLookAndFeel) // todo: get laf better
      .ifPresent {
        if (isMac || isLinux) {
          c?.putClientProperty(WINDOW_DARK_APPEARANCE, it.isDark)
          val rootPane = c as? JRootPane
          attemptTransparentTitle(c) { shouldBeTransparent ->
            c?.putClientProperty(TRANSPARENT_TITLE_BAR_APPEARANCE, shouldBeTransparent)
            if (shouldBeTransparent) {
              setThemedTitleBar(
                getWindow(c),
                rootPane
              )
            }
          }
        }
      }
  }

  private fun setThemedTitleBar(
    window: Window?,
    rootPane: JRootPane?
  ) {

  }

  private fun attemptTransparentTitle(
    component: JComponent?,
    handleIsTransparent: (Boolean) -> Unit
  ) {
    val notEleven = !SystemInfo.isJavaVersionAtLeast(11)
    if (notEleven) {
      handleIsTransparent(true)
    } else {
      component?.addHierarchyListener {
        val window = getWindow(component)
        val title = getTitle(window)
        handleIsTransparent(title !== "This should not be shown")
      }
    }
  }

  private fun getTitle(window: Window?): String? =
    when (window) {
      is JDialog -> window.title
      is JFrame -> window.title
      else -> null
    }
}