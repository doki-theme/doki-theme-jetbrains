package io.unthrottled.doki

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFrame
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.hax.HackComponent.hackLAF
import io.unthrottled.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.unthrottled.doki.icon.IconPathReplacementComponent
import io.unthrottled.doki.laf.LookAndFeelInstaller
import io.unthrottled.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.unthrottled.doki.legacy.EXPUIFixer
import io.unthrottled.doki.service.ConsoleFontService.applyConsoleFont
import io.unthrottled.doki.service.CustomFontSizeService.applyCustomFontSize
import io.unthrottled.doki.stickers.EditorBackgroundWallpaperService
import io.unthrottled.doki.stickers.EmptyFrameWallpaperService
import io.unthrottled.doki.stickers.StickerPaneService
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional
import java.util.UUID

@Service
class TheDokiTheme : Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  companion object {
    const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    private const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"

    fun getInstance(): TheDokiTheme = ApplicationManager.getApplication().getService(TheDokiTheme::class.java)

    fun getVersion(): Optional<String> =
      PluginManagerCore.getPlugin(PluginId.getId(COMMUNITY_PLUGIN_ID))
        .toOptional()
        .map { it.toOptional() }
        .orElseGet {
          PluginManagerCore.getPlugin(
            PluginId.getId(ULTIMATE_PLUGIN_ID),
          ).toOptional()
        }
        .map { it.version }
  }


  init {
    hackLAF()
    IconPathReplacementComponent.initialize()
    EXPUIFixer.fixExperimentalUI()
    installAllUIComponents()
    applyFonts()

    connection.subscribe(
      ApplicationActivationListener.TOPIC,
      object : ApplicationActivationListener {
        override fun applicationActivated(ideFrame: IdeFrame) {
          ThemeManager.getInstance().currentTheme.ifPresent {
            setSVGColorPatcher(it)
          }
        }
      },
    )

    connection.subscribe(
      LafManagerListener.TOPIC,
      LafManagerListener {
        ThemeManager.getInstance().currentTheme
          .doOrElse({
            setSVGColorPatcher(it)
            installAllUIComponents()
            applyFonts()
          }) {
            IconPathReplacementComponent.removePatchers()
            LookAndFeelInstaller.removeIcons()
          }
      },
    )
  }

  private fun applyFonts() {
    applyCustomFontSize()
    applyConsoleFont()
  }

  fun projectOpened(project: Project) {
    EXPUIFixer.fixExperimentalUI()
    ThemeManager.getInstance().currentTheme
      .ifPresent {
        EditorBackgroundWallpaperService.getInstance().checkForUpdates(it)
        EmptyFrameWallpaperService.getInstance().checkForUpdates(it)
        StickerPaneService.getInstance().checkForUpdates(it)
      }

    getVersion()
      .ifPresent { version ->
        if (version != ThemeConfig.getInstance().version) {
          ThemeConfig.getInstance().version = version
        }
      }
    registerUser()
  }

  private fun registerUser() {
    if (ThemeConfig.getInstance().userId.isEmpty()) {
      ThemeConfig.getInstance().userId = UUID.randomUUID().toString()
    }
  }

  override fun dispose() {
    connection.dispose()
  }

  fun init() {
  }
}
