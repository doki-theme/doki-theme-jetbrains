package io.unthrottled.doki.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.BalloonLayoutData
import com.intellij.ui.awt.RelativePoint
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.themes.ThemeManager
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.Nls
import java.awt.Dimension
import java.awt.Point

@Language("HTML")
private fun buildUpdateMessage(
  updateAsset: String,
  dimensions: Dimension
): String =
  """
      What's New?<br>
      <ul>
        <li>2021.2 Build Support!</li>
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
      width='${dimensions.width}' height='${dimensions.height}'><br/><br/>
      I hope you enjoy your new themes!
      </div>
  """.trimIndent()

object UpdateNotification {

  private val notificationGroup = NotificationGroupManager.getInstance()
    .getNotificationGroup("Doki Theme Updates")

  private val defaultListener = NotificationListener.UrlOpeningListener(false)

  fun showDokiNotification(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    project: Project? = null,
    listener: NotificationListener? = defaultListener,
    actions: List<AnAction> = emptyList(),
  ) {
    val notification = notificationGroup.createNotification(
      title,
      content,
      listener = listener
    ).setIcon(DokiIcons.General.PLUGIN_LOGO)
    actions.forEach {
      notification.addAction(it)
    }
    notification.isImportant = true
    notification.notify(project)
  }

  fun showNotificationAcrossProjects(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    listener: NotificationListener? = defaultListener,
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
    newVersion: String
  ) {
    val pluginName =
      getPlugin(
        getPluginOrPlatformByClassName(UpdateNotification::class.java.canonicalName)
      )?.name
    showNotification(
      project,
      notificationGroup.createNotification(
        "$pluginName updated to v$newVersion",
        buildUpdateMessage(
          AssetManager.resolveAssetUrl(
            AssetCategory.MISC,
            "update_celebration_v5.gif"
          ).orElseGet {
            "https://doki.assets.unthrottled.io/misc/update_celebration_v5"
          },
          Dimension(450, 253)
        ),
        NotificationType.INFORMATION
      )
        .setListener(NotificationListener.UrlOpeningListener(false))
        .setIcon(DokiIcons.General.PLUGIN_LOGO)
    )
  }

  private fun showNotification(
    project: Project,
    updateNotification: Notification
  ) {
    try {
      val ideFrame =
        WindowManager.getInstance().getIdeFrame(project) ?: WindowManager.getInstance().allProjectFrames.first()
      val frameBounds = ideFrame.component.bounds
      val notificationPosition = RelativePoint(ideFrame.component, Point(frameBounds.x + frameBounds.width, 20))
      val balloon = NotificationsManagerImpl.createBalloon(
        ideFrame,
        updateNotification,
        true,
        false,
        BalloonLayoutData.fullContent(),
        ThemeManager.instance
      )
      balloon.show(notificationPosition, Balloon.Position.atLeft)
    } catch (e: Throwable) {
      updateNotification.notify(project)
    }
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
