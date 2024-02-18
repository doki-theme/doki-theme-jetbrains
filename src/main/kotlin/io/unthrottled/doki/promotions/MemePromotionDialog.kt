package io.unthrottled.doki.promotions

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.installAndEnable
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.AssetManager.ASSET_SOURCE
import io.unthrottled.doki.assets.AssetManager.FALLBACK_ASSET_SOURCE
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.service.AMII_PLUGIN_ID
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.BalloonPosition
import io.unthrottled.doki.util.BalloonTools
import org.intellij.lang.annotations.Language
import java.util.Locale

class PromotionAssets(
  private val dokiTheme: DokiTheme
) {

  val pluginLogoURL: String
  val promotionAssetURL: String

  init {
    pluginLogoURL = getPluginLogo()
    promotionAssetURL = getPromotionAsset()
  }

  private fun getPluginLogo(): String = AssetManager.resolveAssetUrl(
    AssetCategory.PROMOTION,
    "amii/logo.png"
  ).orElse("$ASSET_SOURCE/promotion/amii/logo.png")

  private fun getPromotionAsset(): String =
    AssetManager.resolveAssetUrl(
      AssetCategory.PROMOTION,
      "motivator/${dokiTheme.displayName.lowercase(Locale.getDefault())}.gif"
    )
      .orElseGet {
        AssetManager.resolveAssetUrl(AssetCategory.PROMOTION, "motivator/promotion.gif")
          .orElse("$FALLBACK_ASSET_SOURCE/promotion/motivator/promotion.gif")
      }
}

class AniMemePromotionDialog(
  private val promotionAssets: PromotionAssets,
  private val project: Project,
  private val onPromotion: (PromotionResults) -> Unit
) {

  init {
    MessageBundle.message("amii.name")
  }

  private val notificationGroup = NotificationGroupManager.getInstance()
    .getNotificationGroup("Doki Theme Promotions")

  @Suppress("MaxLineLength")
  @Language("HTML")
  private fun buildPromotionMessage(promotionAssets: PromotionAssets): String {
    val amiiLogoURL = promotionAssets.pluginLogoURL
    return """
      <html lang="en">
      <body style='text-align: center'>
      <h2>Your new virtual companion!</h2>
      <div style='text-align: left'>
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
      <h3 style='text-align: center'>Bring Anime Memes to your IDE today!</h3>
      <br/>
      <div style='text-align: center'>
        <img style='text-align: center;' src="$amiiLogoURL" alt='AniMeme Plugin Logo'/>
      </div>
      <br/>
      </body>
      </html>
    """.trimIndent()
  }

  fun show() {
    val neverShowAction = object : NotificationAction(MessageBundle.message("promotions.dont.ask")) {
      override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        emitStatus(PromotionStatus.BLOCKED)
        notification.expire()
      }
    }
    val installAction = object : NotificationAction(MessageBundle.message("promotion.action.ok")) {
      override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        installAndEnable(
          e.project,
          setOf(
            PluginId.getId(AMII_PLUGIN_ID)
          )
        ) {
          emitStatus(PromotionStatus.ACCEPTED)
        }
        notification.expire()
      }
    }
    val updateNotification = notificationGroup.createNotification(
      buildPromotionMessage(
        promotionAssets
      ),
      NotificationType.INFORMATION
    )
      .setTitle("Doki Theme Promotion: ${MessageBundle.message("amii.name")}")
      .setIcon(DokiIcons.General.PLUGIN_LOGO)
      .addAction(installAction)
      .addAction(neverShowAction)
      .setListener { notification, hyperlinkEvent ->  }
      .setListener(NotificationListener.UrlOpeningListener(false))

    updateNotification.whenExpired {
      emitStatus(PromotionStatus.BLOCKED)
    }

    BalloonTools.showStickyNotification(
      project,
      updateNotification,
      BalloonPosition.RIGHT
    ) {
      emitStatus(PromotionStatus.BLOCKED)
    }
  }

  private var emitted = false
  private var savedStatus = PromotionStatus.UNKNOWN
  private fun emitStatus(status: PromotionStatus) {
    if (!emitted || savedStatus != status) {
      emitted = true
      savedStatus = status
      onPromotion(PromotionResults(status))
    }
  }
}
