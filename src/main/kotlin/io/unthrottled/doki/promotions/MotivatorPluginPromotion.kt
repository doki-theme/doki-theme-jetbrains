package io.unthrottled.doki.promotions

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

enum class PromotionStatus {
  ACCEPTED, REJECTED, BLOCKED
}

data class PromotionResults(
  val status: PromotionStatus
)

class MotivatorPluginPromotion(
  private val onPromotion: (PromotionResults) -> Unit
) : Runnable {

  init {
    IdeEventQueue.getInstance().addIdleListener(
      this,
      3000
//      TimeUnit.MILLISECONDS.convert(
//        5,
//        TimeUnit.MINUTES
//      ).toInt()
    )

  }

  override fun run() {
    ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
      WindowManager.getInstance().suggestParentWindow(
        ProjectManager.getInstance().openProjects.first()
      ).toOptional()
        .ifPresent {
          MotivatorPromotion(
            dokiTheme, it, onPromotion
          ).show()
        }
    }

    IdeEventQueue.getInstance().removeIdleListener(this)
  }
}