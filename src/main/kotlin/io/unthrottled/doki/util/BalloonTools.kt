package io.unthrottled.doki.util

import com.intellij.notification.Notification
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IdeFrame
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.BalloonLayoutData
import com.intellij.ui.awt.RelativePoint
import java.awt.Point

enum class BalloonPosition {
  LEFT,
  RIGHT,
}

object BalloonTools {
  private const val NOTIFICATION_Y_OFFSET = 20
  private const val NOTIFICATION_X_OFFSET = 10

  fun showStickyNotification(
    project: Project,
    notificationToShow: Notification,
    balloonPosition: BalloonPosition,
    onClosed: () -> Unit,
  ) {
    try {
      val (ideFrame, notificationPosition) = fetchBalloonParameters(project, balloonPosition)
      val balloon =
        NotificationsManagerImpl.createBalloon(
          ideFrame,
          notificationToShow,
          true,
          false,
          BalloonLayoutData.fullContent(),
          Disposer.newDisposable(),
        )
      balloon.addListener(
        object : JBPopupListener {
          override fun onClosed(event: LightweightWindowEvent) {
            onClosed()
          }
        },
      )
      balloon.show(notificationPosition, Balloon.Position.below)
    } catch (e: Throwable) {
      notificationToShow.notify(project)
    }
  }

  private fun fetchBalloonParameters(
    project: Project,
    balloonPosition: BalloonPosition,
  ): Pair<IdeFrame, RelativePoint> {
    val ideFrame = getIDEFrame(project)
    val frameBounds = ideFrame.component.bounds
    val xPosition =
      when (balloonPosition) {
        BalloonPosition.RIGHT -> frameBounds.x + frameBounds.width
        BalloonPosition.LEFT -> frameBounds.x + NOTIFICATION_X_OFFSET
      }

    val notificationPosition =
      RelativePoint(
        ideFrame.component,
        Point(xPosition, NOTIFICATION_Y_OFFSET),
      )
    return Pair(ideFrame, notificationPosition)
  }

  fun getIDEFrame(project: Project) =
    (
      WindowManager.getInstance().getIdeFrame(project)
        ?: WindowManager.getInstance().allProjectFrames.first()
    )
}
