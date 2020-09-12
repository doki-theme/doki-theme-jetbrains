package io.unthrottled.doki.promotions

import com.google.gson.GsonBuilder
import com.intellij.AbstractBundle
import com.intellij.ide.BrowserUtil
import com.intellij.ide.IdeEventQueue
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.intellij.ui.layout.panel
import com.intellij.util.io.exists
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService.createDirectories
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.DokiTheme.Companion.ACCENT_COLOR
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toHexString
import io.unthrottled.doki.util.toOptional
import org.jetbrains.annotations.PropertyKey
import java.awt.Window
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.time.Instant
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

object PromotionManager {

  private val log = Logger.getInstance(PromotionManager::class.java)

  private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

  private var initalized = false

  private val ledgerPath = AssetManager.constructLocalAssetPath(
    AssetCategory.PROMOTION,
    "ledger.json"
  )

  private val promotionLedger: PromotionLedger =
    if (ledgerPath.exists()) {
      readLedger()
    } else {
      PromotionLedger(mutableMapOf())
    }

  private fun readLedger(): PromotionLedger {
    return try {
      Files.newInputStream(ledgerPath)
        .use {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            PromotionLedger::class.java
          )
        }
    } catch (e: Throwable) {
      log.warn("Unable to read promotion ledger for raisins.", e)
      PromotionLedger(mutableMapOf())
    }
  }

  fun registerPromotion(newVersion: String) {
    if (initalized.not()) {
      promotionRegistry(newVersion)
    }
  }

  private fun promotionRegistry(newVersion: String) {
    val versionInstallDates = promotionLedger.versionInstallDates
    if (versionInstallDates.containsKey(newVersion).not()) {
      versionInstallDates[newVersion] = Instant.now()
      persistLedger()
    } else {
//      val latestInstallDate = versionInstallDates[newVersion]!!
//      if (Duration.between(latestInstallDate, Instant.now()).toDays() > 2) {
      setupPromotion()
//      }
    }
  }

  private fun setupPromotion() {
    if (isMotivatorInstalled().not() && shouldPromote()) {
      MotivatorPluginPromotion {
        // mark promoted
      }
    }
  }

  // todo: has been promoted as well
  private fun shouldPromote(): Boolean =
    ThemeConfig.instance.currentStickerLevel == StickerLevel.ON

  private fun isMotivatorInstalled(): Boolean =
    PluginManagerCore.getPlugin(
      PluginId.getId("zd.zero.waifu-motivator-plugin")
    ) != null

  private fun persistLedger() {
    if (ledgerPath.exists().not()) {
      createDirectories(ledgerPath)
    }

    try {
      Files.newBufferedWriter(
        ledgerPath,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
      ).use {
        it.write(
          gson.toJson(promotionLedger)
        )
      }
    } catch (e: Throwable) {
      log.warn("Unable to persist ledger for raisins", e)
    }
  }
}

class MotivatorPluginPromotion(
  private val onPromotion: () -> Unit
) : Runnable {

  init {
    IdeEventQueue.getInstance().addIdleListener(
      this,
      5000
//      TimeUnit.MILLISECONDS.convert(
//        5,
//        TimeUnit.MINUTES
//      ).toInt()
    )

  }

  override fun run() {
    ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
      val themeId = dokiTheme.id
      val promotionAsset = getPromotionAsset(dokiTheme)
      WindowManager.getInstance().suggestParentWindow(
        ProjectManager.getInstance().openProjects.first()
      ).toOptional()
        .ifPresent {
          MotivatorPromotion(
            dokiTheme, it
          ).show()
        }
    }

    IdeEventQueue.getInstance().removeIdleListener(this)
  }

  private fun getPromotionAsset(dokiTheme: DokiTheme): String {
    return when (dokiTheme.id) {
      else -> "promotion.gif"
    }
  }
}

// todo: global ledger
data class PromotionLedger(
  val versionInstallDates: MutableMap<String, Instant>
)

class MotivatorPromotion(
  private val dokiTheme: DokiTheme,
  parent: Window
) : DialogWrapper(parent, true) {

  init {
    isModal = false
    title = MessageBundle.message("motivator.title")
    setCancelButtonText(MessageBundle.message("motivator.action.cancel"))
    setOKButtonIcon(DokiIcons.Plugins.Motivator.TOOL_WINDOW)
    setOKButtonText(MessageBundle.message("motivator.action.ok"))
    setDoNotAskOption(DoNotPromote())
    init()
  }

  override fun createCenterPanel(): JComponent? {
    val pane = buildPromotionPane()
    return panel {
      row {
        pane()
      }
    }
  }

  private fun buildPromotionPane(): JEditorPane {
    val pane = JTextPane()
    pane.isEditable = false
    pane.contentType = "text/html"
    val accentHex = JBColor.namedColor(
      ACCENT_COLOR, UIUtil.getTextAreaForeground()
    ).toHexString()
    pane.text = """
      <html lang="en">
      <head>
          <style>
              a {
                  text-decoration: none;
                  color: $accentHex
              }
              p {
                color: ${UIUtil.getLabelForeground().toHexString()}
              }
          </style>
          <title>Motivator</title>
      </head>
      <body>
      <div style='text-align: center'><img src="https://doki.assets.unthrottled.io/misc/motivator_logo.png" alt='Motivator Plugin Logo'> </div>
      <h2 style='text-align: center; color: $accentHex'>Your new virtual IDE companion!</h2>
      <div style='margin: 8px 0 0'>
          <p>The <a href='https://plugins.jetbrains.com/plugin/13381-waifu-motivator'>Waifu Motivator Plugin</a>
              gives you a virtual companion. <br/> Your companion will interact with you as code is being built.<br/>
              These reactions are collection of various anime memes and gifs,<br/> most of which include your favorite
              character!</p>
      </div>
      <br/>
      <div style='text-align: center'><img src='https://doki.assets.unthrottled.io/misc/update_celebration.gif' alt='momsspaghetti'/></div>
      </body>
      </html>
    """.trimIndent()
    pane.addHyperlinkListener {
      if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
        BrowserUtil.browse(it.url)
      }
    }
    return pane
  }

}

const val MESSAGE_BUNDLE = "messages.MessageBundle"

object MessageBundle : AbstractBundle(MESSAGE_BUNDLE) {
  fun message(
    @PropertyKey(resourceBundle = MESSAGE_BUNDLE) key: String,
    vararg params: Any
  ): String {
    return getMessage(key, params)
  }
}

class DoNotPromote : DialogWrapper.DoNotAskOption {
  // todo : this
  override fun isToBeShown(): Boolean = true

  override fun setToBeShown(toBeShown: Boolean, exitCode: Int) {

  }

  override fun canBeHidden(): Boolean = true

  override fun shouldSaveOptionsOnCancel(): Boolean = true

  override fun getDoNotShowMessage(): String =
    MessageBundle.message("promotions.dont.ask")

}