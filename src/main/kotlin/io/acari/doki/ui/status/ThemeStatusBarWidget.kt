package io.acari.doki.ui.status

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.ui.JBColor
import com.intellij.util.ui.ImageUtil
import com.intellij.util.ui.JBUI.Fonts
import com.intellij.util.ui.JBUI.scale
import com.intellij.util.ui.UIUtil
import io.acari.doki.settings.ThemeSettings.Companion.THEME_SETTINGS_DISPLAY_NAME
import io.acari.doki.themes.DokiTheme
import io.acari.doki.themes.ThemeManager
import io.acari.doki.ui.UiUtils
import java.awt.*
import java.awt.font.TextAttribute
import java.awt.image.BufferedImage
import java.text.AttributedString
import javax.swing.JButton
import javax.swing.JComponent

class ThemeStatusBarWidget(private val project: Project) : JButton(), CustomStatusBarWidget {
  companion object {
    private const val ID = "Doki Theme Status Component"
    private val DEFAULT_FONT_SIZE = scale(11)
    private const val STATUS_PADDING = 4
    private const val STATUS_HEIGHT = 16
  }

  init {
    addActionListener {
      ApplicationManager.getApplication().invokeLater({
        ShowSettingsUtil.getInstance().showSettingsDialog(
          project, THEME_SETTINGS_DISPLAY_NAME
        )
      }, ModalityState.NON_MODAL)
    }
  }

  private var statusBarImage: Image? = null

  override fun ID(): String = ID

  override fun getComponent(): JComponent = this

  override fun install(statusBar: StatusBar) {}

  override fun dispose() {}

  override fun updateUI() {
    super.updateUI()
    statusBarImage = null
    font = Fonts.label(12f)
  }

  override fun paintComponents(g: Graphics?) {
    ThemeManager.instance.currentTheme.ifPresent {
      if (statusBarImage == null) {
        val displayName = it.name // todo: display Name

        val statusBarImage = ImageUtil.createImage(
          size.width, size.height, BufferedImage.TYPE_INT_ARGB
        )
        val graphics2D = statusBarImage.graphics.create() as Graphics2D
        graphics2D.setRenderingHints(UiUtils.renderingHints)

        val fontMetrics = graphics2D.fontMetrics
        val nameWidth = fontMetrics.charsWidth(
          displayName.toCharArray(),
          0,
          displayName.length
        )
        val nameHeight = fontMetrics.ascent

        val attributedDisplayName = AttributedString(displayName)
        attributedDisplayName.addAttribute(
          TextAttribute.FAMILY, font.family
        )
        attributedDisplayName.addAttribute(
          TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD
        )
        attributedDisplayName.addAttribute(
          TextAttribute.SIZE, DEFAULT_FONT_SIZE
        )


        graphics2D.color = it.contrastColor
        val arcs = Dimension(8, 8)
        val accentDiameter: Int = scale(STATUS_HEIGHT - 2)
        graphics2D.fillRoundRect(
          0,
          0,
          size.width + accentDiameter - scale(arcs.width),
          scale(STATUS_HEIGHT),
          arcs.width,
          arcs.height
        )

        graphics2D.color = UIUtil.getLabelForeground()
        graphics2D.font = font
        graphics2D.drawString(
          attributedDisplayName.iterator,
          (size.width - accentDiameter - nameWidth) / 2,
          nameHeight + (size.height - nameHeight) / 2 - scale(1)
        )

        val accentColor = JBColor.namedColor(DokiTheme.ACCENT_COLOR, Color.PINK)
        graphics2D.color = accentColor
        graphics2D.fillOval(
          size.width - scale(STATUS_HEIGHT),
          scale(1),
          accentDiameter,
          accentDiameter
        )
        graphics2D.dispose()
      }
      UIUtil.drawImage(
        g!!, statusBarImage!!, 0, 0, null
      )
    }
  }

  override fun getPreferredSize(): Dimension {
    return ThemeManager.instance.currentTheme
      .map {
        val themeName = it.name // todo: display name
        val width: Int = getFontMetrics(font).charsWidth(
          themeName.toCharArray(), 0,
          themeName.length
        ) + 2 * STATUS_PADDING
        val accentDiameter: Int = scale(STATUS_HEIGHT)
        Dimension(width + accentDiameter, accentDiameter)
      }.orElseGet {
        Dimension(0, 0)
      }
  }

  override fun getMinimumSize(): Dimension? = preferredSize

  override fun getMaximumSize(): Dimension? = preferredSize
}
