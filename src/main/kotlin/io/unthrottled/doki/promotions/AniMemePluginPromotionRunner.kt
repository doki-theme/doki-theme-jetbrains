package io.unthrottled.doki.promotions

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional
import java.util.concurrent.TimeUnit
import kotlin.random.Random

enum class PromotionStatus {
  ACCEPTED, REJECTED, BLOCKED
}

data class PromotionResults(
  val status: PromotionStatus
)

object AniMemePromotionService {

  fun runPromotion(
    onPromotion: (PromotionResults) -> Unit,
    onReject: () -> Unit,
  ) {
    AniMemePluginPromotionRunner(onPromotion, onReject)
  }
}

class AniMemePluginPromotionRunner(
  private val onPromotion: (PromotionResults) -> Unit,
  private val onReject: () -> Unit
) : Runnable {

  init {
    IdeEventQueue.getInstance().addIdleListener(
      this,
      TimeUnit.MILLISECONDS.convert(
        5,
        TimeUnit.MINUTES
      ).toInt() + Random(System.currentTimeMillis())
        .nextInt(0, 2000)

    )
  }

  override fun run() {
    AniMemePluginPromotion.runPromotion(onPromotion, onReject)
    IdeEventQueue.getInstance().removeIdleListener(this)
  }
}

object AniMemePluginPromotion {
  fun runPromotion(
    onPromotion: (PromotionResults) -> Unit,
    onReject: () -> Unit,
  ) {
    ApplicationManager.getApplication().executeOnPooledThread {
      ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
        getFirstProject()
          .map {
            WindowManager.getInstance().suggestParentWindow(
              it
            )
          }
          .doOrElse(
            {
              val promotionAssets = PromotionAssets(dokiTheme)
              ApplicationManager.getApplication().invokeLater {
                AniMemePromotionDialog(
                  dokiTheme,
                  promotionAssets,
                  it!!,
                  onPromotion
                ).show()
              }
            },
            onReject
          )
      }
    }
  }
}

fun getFirstProject(): Optional<Project> = ProjectManager.getInstance().openProjects
  .toOptional()
  .filter { it.isNotEmpty() }
  .map { it.first() }
