package io.acari.doki.notification

import com.intellij.notification.*
import com.intellij.openapi.project.Project

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <br>Please see the <a href="https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/changelog/CHANGELOG.md">Changelog</a> for more details.
      <ul>
      <li>Brand New Look and Feel for all themes!</li>
      <li>Simplified Settings.
          <li>File Colors may need to be re-enabled.</li>
      </li>
      </ul>
      <br>
      Thanks again for downloading <b>The Doki Theme</b>! •‿•<br>
""".trimIndent()

const val CURRENT_VERSION = "5.0.0"

object UpdateNotification {

  private val notificationManager by lazy {
    SingletonNotificationManager(
      NotificationGroup("DokiUpdates",
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
}