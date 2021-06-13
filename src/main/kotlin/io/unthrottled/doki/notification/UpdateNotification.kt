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
        <li>5 New Themes!
          <ul>
            <li>Hanekawa Tsubasa (Dark)</li>
            <li>Shima Rin (Dark)</li>
            <li>Hayase Nagatoro (Dark)</li>
            <li>Jabami Yumeko (Dark)</li>
            <li>Gasai Yuno (Dark)</li>
          </ul>
        </li>
        <li>2021.2 Build Support!</li>
        <li>New update notification (which you can't see at the moment ): )</li>
        <li>Fixed a bunch of small annoying issues!</li>
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
        "update_celebration_v6.gif"
      ).orElseGet {
        "https://doki.assets.unthrottled.io/misc/update_celebration_v6.gif"
      },
      Dimension(450, 253)
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
      "Please restart your IDE",
      "In order for the change to take effect, please restart your IDE. Thanks! ~"
    )
  }

  fun displayAnimationInstallMessage() {
    showDokiNotification(
      "Theme Transition Animation Enabled",
      """The animations will remain in your IDE after uninstalling the plugin.
          |To remove them, un-check this action or toggle the action at 
          |"Help -> Find Action -> ide.intellij.laf.enable.animation". 
        """.trimMargin()
    )
  }

  fun displayReadmeInstallMessage() {
    showDokiNotification(
      "README.md will not show on startup",
      """This behavior will remain in your IDE after uninstalling the plugin.
          |To re-enable it, un-check this action or toggle the action at 
          |"Help -> Find Action -> ide.open.readme.md.on.startup". 
        """.trimMargin()
    )
  }
}
