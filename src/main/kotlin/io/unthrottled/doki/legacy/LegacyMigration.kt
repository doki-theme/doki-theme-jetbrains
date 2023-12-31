package io.unthrottled.doki.legacy

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.installAndEnable
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.service.DOKI_ICONS_PLUGIN_ID
import io.unthrottled.doki.service.PluginService
import io.unthrottled.doki.settings.actors.ThemeActor.setDokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.BalloonPosition
import io.unthrottled.doki.util.toOptional

object LegacyMigration {
  fun migrateIfNecessary() {
  }

  fun newVersionMigration(project: Project) {
    handleThemeRenames()
    migrateMaterialIcons(project)
  }

  private fun migrateMaterialIcons(project: Project) {
    if (
      ThemeConfig.instance.isMaterialFiles ||
      ThemeConfig.instance.isMaterialDirectories ||
      ThemeConfig.instance.isMaterialPSIIcons
    ) {
      ThemeConfig.instance.isMaterialFiles = false
      ThemeConfig.instance.isMaterialDirectories = false
      ThemeConfig.instance.isMaterialPSIIcons = false

      if (PluginService.areIconsInstalled().not()) {
        StartupManager.getInstance(project).runWhenProjectIsInitialized {
          showMaterialMigrationMessage(project)
        }
      }
    }
  }

  private fun showMaterialMigrationMessage(project: Project) {
    val installAction =
      object : NotificationAction(MessageBundle.message("promotion.action.ok")) {
        override fun actionPerformed(
          e: AnActionEvent,
          notification: Notification,
        ) {
          installAndEnable(
            project,
            setOf(
              PluginId.getId(DOKI_ICONS_PLUGIN_ID),
            ),
          ) {
          }
          notification.expire()
        }
      }

    UpdateNotification.showStickyDokiNotification(
      MessageBundle.message("migration.material.title"),
      MessageBundle.message("migration.material.body"),
      actions = listOf(installAction),
      balloonPosition = BalloonPosition.LEFT,
      project = project,
    )
  }

  private val renamedThemes =
    setOf(
      "4fd5cb34-d36e-4a3c-8639-052b19b26ba1", // Zero Two Light
      "8c99ec4b-fda0-4ab7-95ad-a6bf80c3924b", // Zero Two Dark
      "5ca2846d-31a9-40b3-8908-965dad3c127d", // Rimiru -> Rimuru
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
          }.toOptional(),
        )
        setDokiTheme(renamedTheme.toOptional())
      }
  }
}
