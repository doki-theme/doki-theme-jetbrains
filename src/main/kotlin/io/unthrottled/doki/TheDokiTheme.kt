package io.unthrottled.doki

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.wm.IdeFrame
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.hax.HackComponent.hackLAF
import io.unthrottled.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToAddIcons
import io.unthrottled.doki.icon.patcher.MaterialPathPatcherManager.attemptToRemoveIcons
import io.unthrottled.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.unthrottled.doki.legacy.LegacyMigration
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.PromotionManager
import io.unthrottled.doki.service.ConsoleFontService.applyConsoleFont
import io.unthrottled.doki.service.CustomFontSizeService.applyCustomFontSize
import io.unthrottled.doki.service.UpdateNotificationUpdater
import io.unthrottled.doki.service.UpdateNotificationUpdater.attemptToRefreshUpdateNotification
import io.unthrottled.doki.settings.actors.ThemeActor.setDokiTheme
import io.unthrottled.doki.stickers.EditorBackgroundWallpaperService
import io.unthrottled.doki.stickers.EmptyFrameWallpaperService
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
    hackLAF()
    LegacyMigration.migrateIfNecessary()
    installAllUIComponents()

    attemptToAddIcons()

    connection.subscribe(
      ApplicationActivationListener.TOPIC,
      object : ApplicationActivationListener {
        override fun applicationActivated(ideFrame: IdeFrame) {
          userOnBoarding()
          ThemeManager.instance.currentTheme.ifPresent {
            setSVGColorPatcher(it)
          }
        }
      }
    )

    connection.subscribe(
      LafManagerListener.TOPIC,
      LafManagerListener {
        ThemeManager.instance.currentTheme
          .doOrElse({
            setSVGColorPatcher(it)
            installAllUIComponents()
            attemptToAddIcons()
            applyCustomFontSize()
            applyConsoleFont()
            attemptToRefreshUpdateNotification(it)
          }) {
            attemptToRemoveIcons()
          }
      }
    )

    connection.subscribe(
      ProjectManager.TOPIC,
      object : ProjectManagerListener {
        override fun projectOpened(project: Project) {
          ThemeManager.instance.currentTheme
            .ifPresent {
              EditorBackgroundWallpaperService.instance.checkForUpdates(it)
              EmptyFrameWallpaperService.instance.checkForUpdates(it)
              StickerPaneService.instance.checkForUpdates(it)
            }

          val isNewUser = ThemeConfig.instance.userId.isEmpty()
          getVersion()
            .ifPresent { version ->
              if (version != ThemeConfig.instance.version) {
                LegacyMigration.newVersionMigration()
                ThemeConfig.instance.version = version
                ThemeManager.instance.currentTheme.ifPresent {
                  StartupManager.getInstance(project).runWhenProjectIsInitialized {
                    UpdateNotification.display(
                      project,
                      version,
                      isNewUser,
                    )
                  }
                }
              }

              StartupManager.getInstance(project).runWhenProjectIsInitialized {
                PromotionManager.registerPromotion(version)
              }
            }
          registerUser()
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
    UpdateNotificationUpdater.dispose()
  }
}
