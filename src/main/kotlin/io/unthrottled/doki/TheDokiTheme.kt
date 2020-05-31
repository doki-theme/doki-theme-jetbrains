package io.unthrottled.doki

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.hax.HackComponent.hackLAF
import io.unthrottled.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToAddIcons
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToRemoveIcons
import io.unthrottled.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.unthrottled.doki.laf.FileScopeColors.attemptToInstallColors
import io.unthrottled.doki.laf.FileScopeColors.attemptToRemoveColors
import io.unthrottled.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerService
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.ThemeMigrator
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional

class TheDokiTheme : Disposable {
  companion object {
    const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"
  }

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    setSVGColorPatcher()
    hackLAF()

    installAllUIComponents()

    ThemeMigrator.migrateLegacyTheme()

    attemptToAddIcons()

    connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      ThemeManager.instance.currentTheme
        .doOrElse({
          setSVGColorPatcher()
          installAllUIComponents()
          attemptToInstallColors()
          attemptToAddIcons()
        }) {
          if (ThemeConfig.instance.isDokiFileColors) {
            attemptToRemoveColors()
          }
          attemptToRemoveIcons()
        }
    })

    connection.subscribe(ProjectManager.TOPIC, object : ProjectManagerListener {
      override fun projectOpened(project: Project) {
        if (ThemeConfig.instance.isDokiFileColors) {
          setFileScopes(project)
        }

        ThemeMigrator.migrateToCommunityIfNecessary(project)

        ThemeManager.instance.currentTheme
          .filter { ThemeConfig.instance.stickerLevel == StickerLevel.ON.name }
          .ifPresent {
            StickerService.instance.checkForUpdates(it)
          }

        getVersion()
          .filter { it != ThemeConfig.instance.version }
          .ifPresent { newVersion ->
            ThemeConfig.instance.version = newVersion
            UpdateNotification.display(project, newVersion)
          }
      }
    })
  }

  private fun getVersion(): Optional<String> =
    PluginManagerCore.getPlugin(PluginId.getId(COMMUNITY_PLUGIN_ID))
      .toOptional()
      .map { it.toOptional() }
      .orElseGet {
        PluginManagerCore.getPlugin(
          PluginId.getId(ULTIMATE_PLUGIN_ID)
        ).toOptional()
      }
      .map { it.version }

  override fun dispose() {
    connection.dispose()
  }
}
