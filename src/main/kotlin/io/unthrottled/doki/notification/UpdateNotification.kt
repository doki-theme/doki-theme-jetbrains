package io.unthrottled.doki.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toHexString
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.Nls
import java.awt.Dimension

@Suppress("LongMethod", "MaxLineLength")
@Language("HTML")
private fun buildUpdateMessage(
  updateAsset: String,
  dimensions: Dimension
): String {
  val backgroundColor = UIUtil.getEditorPaneBackground().toHexString()
  val accentHex = JBColor.namedColor(
    DokiTheme.ACCENT_COLOR,
    UIUtil.getTextAreaForeground()
  ).toHexString()
  val infoForegroundHex = UIUtil.getContextHelpForeground().toHexString()
  return """
    <html lang='en'>
    <head>
          <style>
              body {
                padding: 1rem;
                background-color: $backgroundColor;
                color: ${UIUtil.getLabelForeground().toHexString()};
                font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
              }
              .center {
                text-align: center;
              }
              a {
                  color: $accentHex;
                  font-weight: bold;
              }
              h2 {
                margin: 16px 0;
                font-weight: 300;
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
      </head>
    <body>
    <h2>What's New?</h2>
    <ul>
        <li>1 New theme<ul>
        <li>Jahy-sama (Dark Theme)</li>
        </ul></li>
        <li>Updated VCS inlay hint color.</li>
        <li>Fixed bug for new users.</li>
    </ul>
    Please see the <a href="https://github.com/doki-theme/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md">
        changelog</a> for more details.
    <br><br>
    Did you know the <b>Doki Theme</b> is available <a href='https://github.com/doki-theme'>on other platforms?</a>
    <br><br>
    Thanks for downloading!
    <br><br>
    <div style='text-align: center'><img alt='Thanks for downloading!' src="$updateAsset"
                                         ${dimensions.width} width='missingValue'height='${dimensions.height}'><br/><br/>
        I hope you enjoy your new themes!
    </div>
    </body>
    </html>
  """.trimIndent()
}

object UpdateNotification {

  private val notificationGroup = NotificationGroupManager.getInstance()
    .getNotificationGroup("Doki Theme Updates")

  private val defaultListener = NotificationListener.UrlOpeningListener(false)

  private fun showDokiNotification(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    project: Project? = null,
    listener: NotificationListener = defaultListener,
    actions: List<AnAction> = emptyList(),
  ) {
    val notification = notificationGroup.createNotification(
      content,
      NotificationType.INFORMATION,
    )
      .setTitle(title)
      .setListener(listener)
      .setIcon(DokiIcons.General.PLUGIN_LOGO)
    actions.forEach {
      notification.addAction(it)
    }
    notification.isImportant = true
    notification.notify(project)
  }

  fun showNotificationAcrossProjects(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    listener: NotificationListener = defaultListener,
    actions: List<(Project?) -> AnAction> = emptyList()
  ) {
    ProjectManager.getInstance().openProjects.forEach { project ->
      showDokiNotification(title, content, project, listener, actions.map { it(project) })
    }
  }

  fun sendMessage(
    title: String,
    message: String,
    project: Project? = null
  ) {
    showDokiNotification(
      title,
      message,
      project = project,
      listener = defaultListener
    )
  }

  fun display(
    project: Project,
    newVersion: String,
    isNewUser: Boolean,
  ) {
    val pluginName =
      getPlugin(
        getPluginOrPlatformByClassName(UpdateNotification::class.java.canonicalName)
      )?.name
    val content = buildUpdateMessage(
      AssetManager.resolveAssetUrl(
        AssetCategory.MISC,
        "update_celebration_v9.gif"
      ).orElseGet {
        "https://doki.assets.unthrottled.io/misc/update_celebration_v9.gif"
      },
      Dimension(640, 640)
    )
    val currentTheme = ThemeManager.instance.currentTheme.orElse(ThemeManager.instance.defaultTheme)

    val urlParameters =
      if (isNewUser) ""
      else "/products/jetbrains/updates/$newVersion"
    HTMLEditorProvider.openEditor(
      project,
      "$pluginName Update",
      "https://doki-theme.unthrottled.io$urlParameters?themeId=${currentTheme.id}",
      content,
    )
  }

  fun displayRestartMessage() {
    showDokiNotification(
      MessageBundle.getMessage("notification.restart.title"),
      MessageBundle.getMessage("notification.restart.body"),
    )
  }

  fun displayAnimationInstallMessage() {
    showDokiNotification(
      MessageBundle.getMessage("notification.animation.install.title"),
      MessageBundle.getMessage("notification.animation.install.body"),
    )
  }

  fun displayReadmeInstallMessage() {
    showDokiNotification(
      MessageBundle.message("notification.no.show.readme.title"),
      MessageBundle.message("notification.no.show.readme.body"),
    )
  }
}
