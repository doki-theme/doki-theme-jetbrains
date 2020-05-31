package io.unthrottled.doki.notification

import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.ide.plugins.PluginManagerCore.getPluginOrPlatformByClassName
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.SingletonNotificationManager
import com.intellij.openapi.project.Project

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
        <li>2020.2 Build Support</li>
      </ul>
      <br>Please see the <a href="https://github.com/Unthrottled/doki-theme-jetbrains/blob/master/changelog/CHANGELOG.md">Changelog</a> for more details.
      <br>
      Thanks again for downloading <b>The Doki Theme</b>! •‿•<br>
""".trimIndent()

object UpdateNotification {

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
    notificationManager.notify(
      "$pluginName updated to v$newVersion",
      UPDATE_MESSAGE,
      project,
      NotificationListener.URL_OPENING_LISTENER
    )
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
