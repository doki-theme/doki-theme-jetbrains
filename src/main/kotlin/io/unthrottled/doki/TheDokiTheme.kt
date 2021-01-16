package io.unthrottled.doki

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.hax.HackComponent.hackLAF
import io.unthrottled.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToAddIcons
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToRemoveIcons
import io.unthrottled.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.unthrottled.doki.legacy.LegacyMigration
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.PromotionManager
import io.unthrottled.doki.settings.actors.setDokiTheme
import io.unthrottled.doki.stickers.BackgroundWallpaperService
import io.unthrottled.doki.stickers.StickerLevel
import io.unthrottled.doki.stickers.StickerPaneService
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional
import java.util.UUID

class TheDokiTheme : Disposable {
  companion object {
    const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    private const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"

    fun getVersion(): Optional<String> =
      PluginManagerCore.getPlugin(PluginId.getId(COMMUNITY_PLUGIN_ID))
        .toOptional()
        .map { it.toOptional() }
        .orElseGet {
          PluginManagerCore.getPlugin(
            PluginId.getId(ULTIMATE_PLUGIN_ID)
          ).toOptional()
        }
        .map { it.version }
  }

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    registerUser()
    setSVGColorPatcher()
    hackLAF()
    installAllUIComponents()

    attemptToAddIcons()

    userOnBoarding()

    connection.subscribe(
      LafManagerListener.TOPIC,
      LafManagerListener {
        ThemeManager.instance.currentTheme
          .doOrElse({
            setSVGColorPatcher()
            installAllUIComponents()
            attemptToAddIcons()
          }) {
            attemptToRemoveIcons()
          }
      }
    )

    connection.subscribe(
      ProjectManager.TOPIC,
      object : ProjectManagerListener {
        override fun projectOpened(project: Project) {
          LegacyMigration.migrateIfNecessary(project)

          ThemeManager.instance.currentTheme
            .filter { ThemeConfig.instance.currentStickerLevel == StickerLevel.ON }
            .ifPresent {
              BackgroundWallpaperService.instance.checkForUpdates(it)
              StickerPaneService.instance.checkForUpdates(it)
            }

          getVersion()
            .ifPresent { version ->
              if (version != ThemeConfig.instance.version) {
                ThemeConfig.instance.version = version
                StartupManager.getInstance(project).runWhenProjectIsInitialized {
                  UpdateNotification.display(project, version)
                }

                handleThemeRenames()
              }

              StartupManager.getInstance(project).runWhenProjectIsInitialized {
                PromotionManager.registerPromotion(version)
              }
            }
        }

        private fun handleThemeRenames() {
          ThemeManager.instance.currentTheme
            .filter { it.name != ThemeManager.DEFAULT_THEME_NAME }
            .ifPresent { currentTheme ->
              setDokiTheme(
                ThemeManager.instance.allThemes.first {
                  currentTheme.id != it.id
                }.toOptional()
              )
              setDokiTheme(currentTheme.toOptional())
            }
        }
      }
    )
  }

  private fun userOnBoarding() {
    if (ThemeConfig.instance.isFirstTime &&
      ThemeManager.instance.currentTheme.isPresent.not()
    ) {
      setDokiTheme(ThemeManager.instance.themeByName(ThemeManager.DEFAULT_THEME_NAME))
      ThemeConfig.instance.isFirstTime = false
    } else if (ThemeConfig.instance.isFirstTime) {
      ThemeConfig.instance.isFirstTime = false
    }
  }

  private fun registerUser() {
    if (ThemeConfig.instance.userId.isEmpty()) {
      ThemeConfig.instance.userId = UUID.randomUUID().toString()
    }
  }

  override fun dispose() {
    connection.dispose()
  }
}
