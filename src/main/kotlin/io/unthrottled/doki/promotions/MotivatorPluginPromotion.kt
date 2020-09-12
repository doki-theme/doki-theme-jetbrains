package io.unthrottled.doki.promotions

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

class MotivatorPluginPromotion(
  private val onPromotion: () -> Unit
) : Runnable {

  init {
    IdeEventQueue.getInstance().addIdleListener(
      this,
      5000
//      TimeUnit.MILLISECONDS.convert(
//        5,
//        TimeUnit.MINUTES
//      ).toInt()
    )

  }

  override fun run() {
    ThemeManager.instance.currentTheme.ifPresent { dokiTheme ->
      val themeId = dokiTheme.id
      val promotionAsset = getPromotionAsset(dokiTheme)
      WindowManager.getInstance().suggestParentWindow(
        ProjectManager.getInstance().openProjects.first()
      ).toOptional()
        .ifPresent {
          MotivatorPromotion(
              dokiTheme, it
          ).show()
        }
    }

    IdeEventQueue.getInstance().removeIdleListener(this)
  }

  private fun getPromotionAsset(dokiTheme: DokiTheme): String {
    return when (dokiTheme.id) {
      else -> "promotion.gif"
    }
  }
}