package io.unthrottled.doki.service

import com.intellij.ui.ColorUtil
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.ThemeManager
import javax.swing.UIManager

object GlassNotificationService {

  fun makeNotificationSeeThrough() {
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.isSeeThroughNotifications }
      .ifPresent {
        val defaults = UIManager.getLookAndFeelDefaults()
        defaults["Notification.background"] = ColorUtil.toAlpha(
          UIUtil.getTreeBackground(),
          ThemeConfig.instance.notificationOpacity
        )
      }
  }
}
