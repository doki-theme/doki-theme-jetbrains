package io.unthrottled.doki.service

import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.ThemeManager
import java.awt.Color
import javax.swing.UIManager

object GlassNotificationService {
  fun makeNotificationSeeThrough() {
    ThemeManager.instance.currentTheme
      .filter { ThemeConfig.instance.isSeeThroughNotifications }
      .ifPresent {
        val defaults = UIManager.getLookAndFeelDefaults()
        val defaultNotificationBackground = UIUtil.getTreeBackground()
        val newNotificationBackground = toAlpha(defaultNotificationBackground)

        defaults["Notification.background"] = newNotificationBackground
        defaults["Notification.MoreButton.innerBorderColor"] = newNotificationBackground

        defaults["Notification.MoreButton.background"] =
          toAlpha(
            JBColor.namedColor(
              "Table.stripeColor",
              UIUtil.getHeaderActiveColor(),
            ),
          )

        defaults["Notification.errorBackground"] =
          toAlpha(
            JBColor.namedColor(
              "FileColor.Yellow",
              defaultNotificationBackground,
            ),
          )
      }
  }

  private fun toAlpha(treeBackground: Color) =
    ColorUtil.withAlpha(
      treeBackground,
      ThemeConfig.instance.notificationOpacity / 100.0,
    )
}
