package io.unthrottled.doki.promotions

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.JBColor
import com.intellij.ui.layout.panel
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.toHexString
import org.intellij.lang.annotations.Language
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

class CulturedContentDialog(
  private val bannerUrl: String,
  project: Project,
) : DialogWrapper(project, true) {

  companion object {
    const val ALLOW_CULTURE_EXIT_CODE = 69
  }

  init {
    title = MessageBundle.message("cultured.content.dialog.title")
    setCancelButtonText(MessageBundle.message("cultured.content.dialog.cancel"))
    init()
  }

  override fun createActions(): Array<Action> {
    return arrayOf(
      cancelAction,
      buildInstallAction()
    )
  }

  private fun buildInstallAction(): AbstractAction {
    return object : AbstractAction() {
      init {
        val message = MessageBundle.message("cultured.content.dialog.confirm")
        putValue(NAME, message)
        putValue(SMALL_ICON, AllIcons.Nodes.Favorite)
      }

      override fun actionPerformed(e: ActionEvent) {
        close(ALLOW_CULTURE_EXIT_CODE, true)
      }
    }
  }

  override fun createCenterPanel(): JComponent? {
    val culturedContentPane = buildCulturedContentPanel()
    return panel {
      row {
        culturedContentPane()
      }
    }
  }

  @Suppress("LongMethod")
  private fun buildCulturedContentPanel(): JEditorPane {
    val pane = JTextPane()
    pane.isEditable = false
    pane.contentType = "text/html"
    val accentHex = JBColor.namedColor(
      DokiTheme.ACCENT_COLOR,
      UIUtil.getTextAreaForeground()
    ).toHexString()
    val infoForegroundHex = UIUtil.getContextHelpForeground().toHexString()
    pane.background = JBColor.namedColor(
      "Menu.background",
      UIUtil.getEditorPaneBackground()
    )

    @Language("HTML")
    val content = """
      <html lang="en">
      <head>
          <style type='text/css'>
              body {
                font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
              }
              .center {
                text-align: center;
              }
              a {
                  color: $accentHex;
                  font-weight: bold;
              }
              p {
                color: ${UIUtil.getLabelForeground().toHexString()};
              }
              h2 {
                margin: 16px 0;
                font-weight: bold;
                font-size: 22px;
              }
              h3 {
                margin: 4px 0;
                font-weight: bold;
                font-size: 14px;
              }
              .accented {
                color: $accentHex;
              }
              .info-foreground {
                color: $infoForegroundHex;
                text-align: center;
              }
              .header {
                color: $accentHex;
                text-align: center;
              }
              .logo-container {
                margin-top: 8px;
                text-align: center;
              }
              .display-image {
                max-height: 256px;
                text-align: center;
              }
          </style>
          <title>Motivator</title>
      </head>
      <body>
      <div class='logo-container'><img src="$bannerUrl" class='display-image' 
        alt='Suggestive content warning banner'/> 
      </div>
      <h2 class='header'>Suggestive Content Ahead</h2>
      <div style='margin: 8px 0 0 100px'>
        <p>
          Hey friend! Just wanted to give you a bit of a warning.<br>
          The content you are about to install is a bit risque.
          Some of us are <br/> professional Otaku, who want to remain, well...professional.
          Don't worry <br/> if you choose to continue, I won't ask you again for this specific theme.
        </p>
      </div>
      <br/>
      <h3 class='info-foreground'>Do you want to continue?</h3>
      <br/>
      </body>
      </html>
      """
    pane.text = content.trimIndent()
    pane.preferredSize = Dimension(pane.preferredSize.width + 120, pane.preferredSize.height)
    pane.addHyperlinkListener {
      if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
        BrowserUtil.browse(it.url)
      }
    }
    return pane
  }
}
