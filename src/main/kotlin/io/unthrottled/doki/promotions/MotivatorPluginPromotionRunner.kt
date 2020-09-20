package io.unthrottled.doki.promotions

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional
import java.util.concurrent.TimeUnit
import kotlin.random.Random

enum class PromotionStatus {
  ACCEPTED, REJECTED, BLOCKED
}

data class PromotionResults(
  val status: PromotionStatus
)

object MotivatorPromotionService {

  fun runPromotion(onPromotion: (PromotionResults) -> Unit) {
    MotivatorPluginPromotionRunner(onPromotion)
  }
}

class MotivatorPluginPromotionRunner(
  private val onPromotion: (PromotionResults) -> Unit
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
    MotivatorPluginPromotion.runPromotion(onPromotion)
    IdeEventQueue.getInstance().removeIdleListener(this)
  }
}

object MotivatorPluginPromotion {
  fun runPromotion(onPromotion: (PromotionResults) -> Unit) {
    ApplicationManager.getApplication().executeOnPooledThread {
      ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
        WindowManager.getInstance().suggestParentWindow(
          ProjectManager.getInstance().openProjects.first()
        ).toOptional()
          .ifPresent {
            val promotionAssets = PromotionAssets(dokiTheme)
            ApplicationManager.getApplication().invokeLater {
              MotivatorPromotionDialog(
                dokiTheme, promotionAssets, it, onPromotion
              ).show()
            }
          }
      }
    }
  }
}