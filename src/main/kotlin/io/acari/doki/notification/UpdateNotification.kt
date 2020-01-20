package io.acari.doki.notification

import com.intellij.notification.*
import com.intellij.openapi.project.Project

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
      <li>More Color and Icon Consistency.</li>
      <li>Android Studio 4.0 Support</li>
      </ul>
      <br>Please see the <a href="https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/changelog/CHANGELOG.md">Changelog</a> for more details.
      <br>
      Thanks again for downloading <b>The Doki Theme</b>! •‿•<br>
""".trimIndent()

const val CURRENT_VERSION = "5.2.1"

object UpdateNotification {

  private val notificationManager by lazy {
    SingletonNotificationManager(
      NotificationGroup("Doki Updates",
      NotificationDisplayType.STICKY_BALLOON, true),
      NotificationType.INFORMATION)
  }

  fun display(project: Project) {
    notificationManager.notify(
      "The Doki Theme updated to v${CURRENT_VERSION}",
      UPDATE_MESSAGE,
      project,
      NotificationListener.URL_OPENING_LISTENER
    )
  }

  fun displayRestartMessage(){
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
          |To remove them, un-check this action or remove them at "Help -> Find Action -> ide.intellij.laf.enable.animation". 
        """.trimMargin()
      )
    }
}