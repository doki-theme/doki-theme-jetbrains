package io.unthrottled.doki.legacy

import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.util.BuildNumber
import com.intellij.openapi.util.SystemInfo
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.settings.actors.ThemeActor.setDokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional

object LegacyMigration {
  fun migrateIfNecessary() {
    migrateUsersAwayFromTitlePane()
  }

  fun newVersionMigration() {
    handleThemeRenames()
  }

  private val renamedThemes = setOf(
    "4fd5cb34-d36e-4a3c-8639-052b19b26ba1", // Zero Two Light
    "8c99ec4b-fda0-4ab7-95ad-a6bf80c3924b", // Zero Two Dark
  )

  private fun handleThemeRenames() {
    ThemeManager.instance.currentTheme
      .filter {
        renamedThemes.contains(it.id)
      }
      .ifPresent { renamedTheme ->
        setDokiTheme(
          ThemeManager.instance.allThemes.first {
            renamedTheme.id != it.id
          }.toOptional()
        )
        setDokiTheme(renamedTheme.toOptional())
      }
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
