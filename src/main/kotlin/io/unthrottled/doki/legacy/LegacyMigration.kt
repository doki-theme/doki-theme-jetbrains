package io.unthrottled.doki.legacy

import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.util.BuildNumber
import com.intellij.openapi.util.SystemInfo
import io.unthrottled.doki.config.ThemeConfig

object LegacyMigration {
  fun migrateIfNecessary() {
    migrateUsersAwayFromTitlePane()
  }

  private val nativeTitlePaneBuild = BuildNumber.fromString("222.2680.4")
  private fun migrateUsersAwayFromTitlePane() {
    val build = ApplicationInfoEx.getInstanceEx().build
    if (SystemInfo.isMac &&
      ThemeConfig.instance.isThemedTitleBar &&
      // is the current build greater that the
      // build that has native titlepane support
      (nativeTitlePaneBuild?.compareTo(build) ?: 0) <= 0
    ) {
      ThemeConfig.instance.isThemedTitleBar = false
    }
  }
}
