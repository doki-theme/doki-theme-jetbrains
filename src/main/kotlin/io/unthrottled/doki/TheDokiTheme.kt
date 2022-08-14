package io.unthrottled.doki

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.UITheme
import com.intellij.ide.ui.laf.TempUIThemeBasedLookAndFeelInfo
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.wm.IdeFrame
import com.intellij.ui.ColorHexUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.hax.HackComponent.hackLAF
import io.unthrottled.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.unthrottled.doki.icon.IconPathReplacementComponent
import io.unthrottled.doki.laf.LookAndFeelInstaller
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
import java.awt.Color
import java.util.Optional
import java.util.UUID
import javax.swing.UIDefaults
import javax.swing.UIManager
import javax.swing.plaf.ColorUIResource

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
    PromotionManager.init()
    hackLAF()
    LegacyMigration.migrateIfNecessary()
    IconPathReplacementComponent.initialize()
    installAllUIComponents()

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
      LafManagerListener { manager ->
        ThemeManager.instance.currentTheme
          .doOrElse({
            setSVGColorPatcher(it)
            installAllUIComponents()
            applyCustomFontSize()
            applyConsoleFont()
            attemptToRefreshUpdateNotification(it)
            ApplicationManager.getApplication().invokeLater {
              val currentLookAndFeel = manager.currentLookAndFeel
              if(currentLookAndFeel is TempUIThemeBasedLookAndFeelInfo) {
                val theme = currentLookAndFeel.theme
                val field = UITheme::class.java.getDeclaredField("ui")
                field.isAccessible = true
                val ui = field.get(theme) as Map<*, *>
                val defaults = UIManager.getDefaults()
                setUIProperty(
                  "ToolWindow.Button.selectedBackground",
                  parseColor(ui["RunWidget.background"] as String),
                  defaults
                )
                setUIProperty(
                  "ToolWindow.Button.selectedForeground",
                  parseColor(ui["RunWidget.foreground"] as String),
                  defaults
                )
              }
            }
          }) {
            IconPathReplacementComponent.removePatchers()
            LookAndFeelInstaller.removeIcons()
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
                LegacyMigration.newVersionMigration(project)
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

private fun setUIProperty(key: String, value: Any?, defaults: UIDefaults) {
  defaults.remove(key)
  defaults[key] = value
}

private fun parseColor(value: String): Color? {
  var value: String? = value
  if (value != null) {
    if (value.startsWith("#")) {
      value = value.substring(1)
    }
    if (value.length == 8) {
      val color = ColorHexUtil.fromHex(value.substring(0, 6))
      try {
        val alpha = value.substring(6, 8).toInt(16)
        return ColorUIResource(Color(color.red, color.green, color.blue, alpha))
      } catch (ignore: Exception) {
      }
      return null
    }
  }
  val color = ColorHexUtil.fromHex(value, null)
  return color?.let { ColorUIResource(it) }
}