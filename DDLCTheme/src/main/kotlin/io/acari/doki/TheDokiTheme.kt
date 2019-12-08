package io.acari.doki

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.config.ThemeConfig
import io.acari.doki.hax.HackComponent.hackLAF
import io.acari.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.laf.FileScopeColors.attemptToInstallColors
import io.acari.doki.laf.FileScopeColors.attemptToRemoveColors
import io.acari.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.acari.doki.notification.CURRENT_VERSION
import io.acari.doki.notification.UpdateNotification
import io.acari.doki.themes.DokiThemes
import io.acari.doki.util.LegacyThemeUtilities
import io.acari.doki.util.toOptional

class TheDokiTheme : Disposable {
  private val connection = ApplicationManager.getApplication().messageBus.connect()

  init {
    //////////// hax ////////////
    setSVGColorPatcher()
    hackLAF()
    //////////// ._. ////////////

    installAllUIComponents()

    migrateLegacyTheme()

    connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      DokiThemes.currentTheme
        .ifPresentOrElse({
          setSVGColorPatcher()
          installAllUIComponents()
          attemptToInstallColors()
        })
        {
          if (ThemeConfig.instance.isDokiFileColors) {
            attemptToRemoveColors()
          }
        }
    })

    connection.subscribe(ProjectLifecycleListener.TOPIC, object : ProjectLifecycleListener {
      override fun projectComponentsInitialized(project: Project) {
        if(ThemeConfig.instance.isDokiFileColors){
          setFileScopes(project)
        }

        if(ThemeConfig.instance.version != CURRENT_VERSION){
          ThemeConfig.instance.version = CURRENT_VERSION
          UpdateNotification.display(project)
        }
      }
    })
  }

  private fun migrateLegacyTheme() {
    if(!ThemeConfig.instance.processedLegacyStartup){
      ThemeConfig.instance.processedLegacyStartup = true
      val lastTheme = LegacyThemeUtilities.legacyThemeNameMapping(
        ThemeConfig.instance.selectedTheme
      )
      LafManager.getInstance().installedLookAndFeels.find {
        it.name.equals(lastTheme, true)
      }.toOptional()
        .ifPresent {
          LafManager.getInstance().setCurrentLookAndFeel(it)
        }
    }
  }

  override fun dispose() {
    connection.dispose()
  }
}