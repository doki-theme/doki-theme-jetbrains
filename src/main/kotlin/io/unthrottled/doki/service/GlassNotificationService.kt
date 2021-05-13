package io.unthrottled.doki.service

import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
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
        val alpha = ThemeConfig.instance.notificationOpacity / 100.0
        val toAlpha = ColorUtil.withAlpha(
          UIUtil.getTreeBackground(),
          alpha
        )

        defaults["Notification.background"] = toAlpha
        defaults["Notification.MoreButton.innerBorderColor"] = toAlpha

        defaults["Notification.MoreButton.background"] = ColorUtil.withAlpha(
          JBColor.namedColor(
            "Table.stripeColor",
            UIUtil.getHeaderActiveColor()
          ),
          alpha
        )
      }
  }
}
