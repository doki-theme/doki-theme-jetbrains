package io.unthrottled.doki.promotions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.PluginsAdvertiser
import com.intellij.ui.JBColor
import com.intellij.ui.layout.panel
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.toHexString
import java.awt.Dimension
import java.awt.Window
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

class MotivatorPromotionDialog(
  private val dokiTheme: DokiTheme,
  parent: Window,
  private val onPromotion: (PromotionResults) -> Unit
) : DialogWrapper(parent, true) {

  companion object {
    private const val INSTALLED_EXIT_CODE = 69
  }

  init {
    title = MessageBundle.message("motivator.title")
    setCancelButtonText(MessageBundle.message("motivator.action.cancel"))
    setDoNotAskOption(DoNotPromote { shouldContinuePromotion, exitCode ->
      onPromotion(
        PromotionResults(
          when {
            !shouldContinuePromotion -> PromotionStatus.BLOCKED
            exitCode == INSTALLED_EXIT_CODE -> PromotionStatus.ACCEPTED
            else -> PromotionStatus.REJECTED
          }
        )
      )
    })
    init()
  }

  override fun createActions(): Array<Action> {
    return arrayOf(
      buildInstallAction(),
      cancelAction
    )
  }

  private fun buildInstallAction(): AbstractAction {
    return object : AbstractAction() {
      init {
        val message = MessageBundle.message("motivator.action.ok")
        putValue(NAME, message)
        putValue(SMALL_ICON, DokiIcons.Plugins.Motivator.TOOL_WINDOW)
      }

      override fun actionPerformed(e: ActionEvent) {
        PluginsAdvertiser.installAndEnable(
          setOf(
            PluginId.getId(MOTIVATOR_PLUGIN_ID)
          )
        ) {
          close(INSTALLED_EXIT_CODE, true)
        }
      }
    }
  }

  override fun createCenterPanel(): JComponent? {
    val promotionPane = buildPromotionPane()
    return panel {
      row {
        promotionPane()
      }
    }
  }

  private fun buildPromotionPane(): JEditorPane {
    val pane = JTextPane()
    pane.isEditable = false
    pane.contentType = "text/html"
    val accentHex = JBColor.namedColor(
      DokiTheme.ACCENT_COLOR, UIUtil.getTextAreaForeground()
    ).toHexString()
    val infoForegroundHex = UIUtil.getContextHelpForeground().toHexString()
    val promotionAsset = getPromotionAsset(dokiTheme)
    pane.text = """
      <html lang="en">
      <head>
          <style>
              a {
                  color: $accentHex
              }
              p {
                color: ${UIUtil.getLabelForeground().toHexString()}
              }
          </style>
          <title>Motivator</title>
      </head>
      <body>
      <div style='text-align: center; margin-top: 8px'><img src="https://doki.assets.unthrottled.io/misc/motivator_logo.png" alt='Motivator Plugin Logo'> </div>
      <h2 style='text-align: center; color: $accentHex'>Your new virtual companion!</h2>
      <div style='margin: 8px 0 0 100px'>
        <p>
          The <a href='https://plugins.jetbrains.com/plugin/13381-waifu-motivator'>Waifu Motivator Plugin</a>
          gives your IDE more personality. <br/> Your assistant will interact with you as you build code.
          <br/>Such as when your programs fail to run or tests pass/fail, <br/> 
          then you'll receive a motivation event. Which will most likely <br/> take the form of a reaction gif of your favorite character(s)!
        </p>
      </div>
      <br/>
      <h3 style='text-align: center; color: $infoForegroundHex'>Bring Anime Memes to your IDE today!</h3>
      <br/>
      <div style='text-align: center'><img src='https://doki.assets.unthrottled.io/misc/promotion/$promotionAsset' alt='Character Promotion Asset'/></div>
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

  // TODO: 9/12/20 this
  private fun getPromotionAsset(dokiTheme: DokiTheme): String {
    return when (dokiTheme.id) {
      else -> "promotion.gif"
    }
  }
}

class DoNotPromote(
  private val onToBeShown: (Boolean, Int) -> Unit
) : DialogWrapper.DoNotAskOption {
  override fun isToBeShown(): Boolean = true

  override fun setToBeShown(toBeShown: Boolean, exitCode: Int) {
    onToBeShown(toBeShown, exitCode)
  }

  override fun canBeHidden(): Boolean = true

  override fun shouldSaveOptionsOnCancel(): Boolean = true

  override fun getDoNotShowMessage(): String =
    MessageBundle.message("promotions.dont.ask")

}