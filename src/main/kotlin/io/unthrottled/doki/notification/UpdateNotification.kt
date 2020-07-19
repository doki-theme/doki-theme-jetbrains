package io.unthrottled.doki.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.SingletonNotificationManager
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.BalloonLayoutData
import com.intellij.ui.awt.RelativePoint
import java.awt.Point

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
      </ul>
      Please see the <a href="https://github.com/doki-theme/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md">changelog</a> for more details.
      <br><br>
      Did you the <b>Doki Theme</b> is available <a href='https://github.com/doki-theme'>on other platforms?</a>
      <br><br>
      Thanks for downloading!
      <br><br>
      <img alt='Thanks for downloading!' src="https://doki.assets.unthrottled.io/misc/update_celebration.gif" width='256'>
       <br><br><br><br><br><br><br><br>
       Thanks!
""".trimIndent()

object UpdateNotification {

  private val notificationGroup = NotificationGroup(
    "Doki Theme Updates",
    NotificationDisplayType.BALLOON,
    false,
    "Doki Theme Updates"
  )

  private val notificationManager by lazy {
    SingletonNotificationManager(
      NotificationGroup(
        "Doki Updates",
        NotificationDisplayType.STICKY_BALLOON, true
      ),
      NotificationType.INFORMATION
    )
  }

  fun sendMessage(
    title: String,
    message: String,
    project: Project? = null
  ) {
    notificationManager.notify(
      title,
      message,
      project = project,
      listener = NotificationListener.URL_OPENING_LISTENER
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
        UPDATE_MESSAGE,
        NotificationType.INFORMATION
      )
        .setListener(NotificationListener.UrlOpeningListener(false))
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
        BalloonLayoutData.fullContent()
      ) {}
      balloon.show(notificationPosition, Balloon.Position.atLeft)
    } catch (e: Throwable) {
      updateNotification.notify(project)
    }
  }

  fun displayRestartMessage() {
    notificationManager.notify(
      "Please restart your IDE",
      "In order for the change to take effect, please restart your IDE. Thanks! ~"
    )
  }

  fun displayFileColorInstallMessage() {
    notificationManager.notify(
      "File Colors Installed",
      """File colors will remain in your IDE after uninstalling the plugin.
          |To remove them, un-check this action or remove them at "Settings -> Appearance -> File Colors". 
        """.trimMargin()
    )
  }

  fun displayAnimationInstallMessage() {
    notificationManager.notify(
      "Theme Transition Animation Enabled",
      """The animations will remain in your IDE after uninstalling the plugin.
          |To remove them, un-check this action or toggle the action at "Help -> Find Action -> ide.intellij.laf.enable.animation". 
        """.trimMargin()
    )
  }

  fun displayReadmeInstallMessage() {
    notificationManager.notify(
      "README.md will not show on startup",
      """This behavior will remain in your IDE after uninstalling the plugin.
          |To re-enable it, un-check this action or toggle the action at "Help -> Find Action -> ide.open.readme.md.on.startup". 
        """.trimMargin()
    )
  }
}
