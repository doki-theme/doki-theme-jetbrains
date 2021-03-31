package io.unthrottled.doki.promotions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.JBColor
import com.intellij.ui.layout.panel
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.toHexString
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

class CulturedContentDialog(
  private val dokiTheme: DokiTheme,
  project: Project,
) : DialogWrapper(project, true) {

  companion object {
    const val ALLOW_CULTURE_EXIT_CODE = 69
  }

  init {
    title = MessageBundle.message("amii.name")
    setCancelButtonText(MessageBundle.message("promotion.action.cancel"))
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
        val message = MessageBundle.message("promotion.action.ok")
        putValue(NAME, message)
        putValue(SMALL_ICON, DokiIcons.Plugins.AMII.TOOL_WINDOW)
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
    val motivatorLogoURL = "file:///home/alex/Downloads/ibrowse.gif"
    pane.background = JBColor.namedColor(
      "Menu.background",
      UIUtil.getEditorPaneBackground()
    )
    pane.text =
      """
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
      <div class='logo-container'><img src="$motivatorLogoURL" class='display-image' alt='Motivator Plugin Logo'/> 
      </div>
      <h2 class='header'>Your new virtual companion!</h2>
      <div style='margin: 8px 0 0 100px'>
        <p>
          <a href='https://plugins.jetbrains.com/plugin/15865-amii'>AMII</a>
          gives your IDE more personality by using anime memes. <br/> You will get an assistant that will interact 
          with you as you build code.
          <br/>Such as when your programs fail to run or tests pass/fail. Your companion<br/> 
          has the ability to react to these events. Which will most likely take the form <br/> of a reaction gif of 
          your favorite character(s)!
        </p>
      </div>
      <br/>
      <h3 class='info-foreground'>Bring Anime Memes to your IDE today!</h3>
      <br/>
      alt='${dokiTheme.displayName}&#39;s Promotion Asset'/></div>
      <br/>
      </body>
      </html>
      """.trimIndent()
    pane.preferredSize = Dimension(pane.preferredSize.width + 120, pane.preferredSize.height)
    pane.addHyperlinkListener {
      if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
        BrowserUtil.browse(it.url)
      }
    }
    return pane
  }
}
