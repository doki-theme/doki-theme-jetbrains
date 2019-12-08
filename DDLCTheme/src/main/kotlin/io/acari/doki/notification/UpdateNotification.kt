package io.acari.doki.notification

import com.intellij.notification.*
import com.intellij.openapi.project.Project

val UPDATE_MESSAGE: String = """
      What's New?<br>
      <ul>
      <li>Restored Editor Tab Height.</li>
      <li>Enhanced non-project Java File Icons.</li>
      </ul>
      <br>
      Thanks again for downloading <b>The Doki Theme</b>! •‿•<br>
      <br>Please see the <a href="https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/docs/CHANGELOG.md">Changelog</a> for more details.
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
}