package io.acari.doki.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaRootPaneUI
import com.intellij.openapi.util.SystemInfo.*
import com.intellij.openapi.wm.ex.IdeFrameEx
import com.intellij.ui.JBColor.GRAY
import com.intellij.ui.JBColor.namedColor
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.JBUI.insets
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.UIUtil.getWindow
import io.acari.doki.config.ThemeConfig
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.toOptional
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeListener
import java.util.*
import javax.swing.*
import javax.swing.border.AbstractBorder
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicRootPaneUI

typealias Disposer = () -> Unit

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

    private fun hasTransparentTitleBar(): Boolean = isMac && ThemeConfig.instance.isThemedTitleBar
  }

  private var possibleDisposable: Optional<Disposer> = Optional.empty()

  override fun uninstallUI(c: JComponent?) {
    super.uninstallUI(c)
    possibleDisposable.ifPresent { it() }
  }

  override fun installUI(c: JComponent?) {
    super.installUI(c)
    ThemeManager.instance.currentTheme
      .filter { isMac || isLinux }
      .ifPresent {
        c?.putClientProperty(WINDOW_DARK_APPEARANCE, it.isDark)
        val rootPane = c as? JRootPane
        attemptTransparentTitle(c) { shouldBeTransparent ->
          c?.putClientProperty(TRANSPARENT_TITLE_BAR_APPEARANCE, shouldBeTransparent)
          if (shouldBeTransparent) {
            setThemedTitleBar(
              getWindow(c),
              rootPane
            )
          } else {
            {}
          }
        }() { disposer ->
          possibleDisposable = disposer.toOptional()
        }
      }
  }

  private fun setThemedTitleBar(
    window: Window?,
    rootPane: JRootPane?
  ): () -> Unit {
    val topWindowInset = JBUI.insetsTop(24)
    val customDecorationBorder = object : AbstractBorder() {
      override fun getBorderInsets(c: Component?): Insets {
        return if (isInFullScreen(window)) {
          return insets(0)
        } else {
          topWindowInset
        }
      }

      override fun paintBorder(
        c: Component?,
        g: Graphics?,
        x: Int,
        y: Int,
        width: Int,
        height: Int
      ) {
        if (isInFullScreen(window)) {
          return
        }

        val graphics = g!!.create() as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE)

        try {
          val headerRectangle = Rectangle(0, 0, c!!.width, topWindowInset.top)
          graphics.color = UIUtil.getPanelBackground()
          graphics.fill(headerRectangle)
          graphics.font = UIManager.getFont("Panel.font")
          val color: Color =
            if (window!!.isActive) namedColor("Label.foreground", Color.black)
            else namedColor("Label.disabledForeground", GRAY)
          graphics.color = color
          val controlButtonsWidth = 70
          val windowTitle: String = getTitle(window)
          val widthToFit = controlButtonsWidth * 2 + GraphicsUtil.stringWidth(
            windowTitle,
            g.font
          ) - c.width.toDouble()
          // Draw the title
          if (widthToFit <= 0) {
            UIUtil.drawCenteredString(graphics, headerRectangle, windowTitle)
          } else {
            val fm = graphics.fontMetrics
            val stringBounds = fm.getStringBounds(windowTitle, graphics)
            val bounds = AffineTransform.getTranslateInstance(
              controlButtonsWidth.toDouble(),
              fm.ascent + (headerRectangle.height - stringBounds.height) / 2
            ).createTransformedShape(stringBounds).bounds
            UIUtil.drawCenteredString(graphics, bounds, windowTitle, false, true)
          }
        } finally {
          graphics.dispose()
        }
      }
    }
    rootPane?.border = customDecorationBorder

    val windowAdapter: WindowAdapter = object : WindowAdapter() {
      override fun windowActivated(e: WindowEvent) {
        rootPane?.repaint()
      }

      override fun windowDeactivated(e: WindowEvent) {
        rootPane?.repaint()
      }
    }

    val changeListener = PropertyChangeListener { rootPane?.repaint() }
    window?.addPropertyChangeListener("title", changeListener)
    return {
      window?.removeWindowListener(windowAdapter)
      window?.removePropertyChangeListener(changeListener)
    }
  }

  private fun attemptTransparentTitle(
    component: JComponent?,
    handleIsTransparent: (Boolean) -> () -> Unit
  ): ((Disposer) -> Unit) -> Unit {
    return if (!isJavaVersionAtLeast(11)) {
      { resolve ->
        resolve(handleIsTransparent(true))
      }
    } else {
      return { resolve ->
        component?.addHierarchyListener {
          val window = getWindow(component)
          val title = getTitle(window)
          resolve(handleIsTransparent(title != "This should not be shown" &&
                                      title.isNotBlank()))
        }
      }
    }
  }

  private fun getTitle(window: Window?): String =
    when (window) {
      is JDialog -> window.title
      is JFrame -> window.title
      else -> ""
    }
}

private fun isInFullScreen(window: Window?): Boolean {
  val ultimateParent = UIUtil.findUltimateParent(window)
  if (ultimateParent == window && ultimateParent is IdeFrameEx) {
    val ultimateParentWindowForEvent = ultimateParent as IdeFrameEx
    return ultimateParentWindowForEvent.isInFullScreen
  }
  return false
}