package io.acari.doki

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.config.ThemeConfig
import io.acari.doki.hax.HackComponent.hackLAF
import io.acari.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.acari.doki.icon.patcher.MaterialPathPatcherManager.attemptToAddIcons
import io.acari.doki.icon.patcher.MaterialPathPatcherManager.attemptToRemoveIcons
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.laf.FileScopeColors.attemptToInstallColors
import io.acari.doki.laf.FileScopeColors.attemptToRemoveColors
import io.acari.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.acari.doki.notification.CURRENT_VERSION
import io.acari.doki.notification.UpdateNotification
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.ThemeMigrator
import io.acari.doki.util.doOrElse

class TheDokiTheme : Disposable {
  companion object {
    const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"
  }

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    //////////// hax ////////////
    setSVGColorPatcher()
    hackLAF()
    //////////// ._. ////////////

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
        })
        {
          if (ThemeConfig.instance.isDokiFileColors) {
            attemptToRemoveColors()
          }
          attemptToRemoveIcons()
        }
    })

    connection.subscribe(ProjectLifecycleListener.TOPIC, object : ProjectLifecycleListener {
      override fun projectComponentsInitialized(project: Project) {
        if (ThemeConfig.instance.isDokiFileColors) {
          setFileScopes(project)
        }

        ThemeMigrator.migrateToCommunityIfNecessary(project)

        if (ThemeConfig.instance.version != CURRENT_VERSION) {
          ThemeConfig.instance.version = CURRENT_VERSION
          UpdateNotification.display(project)
        }
      }
    })
  }

  override fun dispose() {
    connection.dispose()
  }
}