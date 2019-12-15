package io.acari.doki

import com.intellij.ide.actions.QuickChangeLookAndFeel
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.impl.ProjectLifecycleListener
import io.acari.doki.config.ThemeConfig
import io.acari.doki.hax.HackComponent.hackLAF
import io.acari.doki.hax.SvgLoaderHacker.setSVGColorPatcher
import io.acari.doki.icon.patcher.MaterialPathPatcherManager.attemptToAddIcons
import io.acari.doki.laf.DokiAddFileColorsAction.setFileScopes
import io.acari.doki.laf.FileScopeColors.attemptToInstallColors
import io.acari.doki.laf.FileScopeColors.attemptToRemoveColors
import io.acari.doki.laf.LookAndFeelInstaller.installAllUIComponents
import io.acari.doki.notification.CURRENT_VERSION
import io.acari.doki.notification.UpdateNotification
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.themes.ThemeManager
import io.acari.doki.util.LegacyThemeUtilities
import io.acari.doki.util.LegacyThemeUtilities.DARK_MODE_PROP
import io.acari.doki.util.LegacyThemeUtilities.SAVED_STATE
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

    attemptToAddIcons()

    connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
      ThemeManager.instance.currentTheme
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
        if (ThemeConfig.instance.isDokiFileColors) {
          setFileScopes(project)
        }

        if (ThemeConfig.instance.version != CURRENT_VERSION) {
          ThemeConfig.instance.version = CURRENT_VERSION
          UpdateNotification.display(project)
        }
      }
    })
  }

  private fun migrateLegacyTheme() {
    if (!ThemeConfig.instance.processedLegacyStartup) {
      migrateLegacyCurrentSticker()
      migrateLegacyCurrentTheme()
      ThemeConfig.instance.processedLegacyStartup = true
    }
  }

  private fun migrateLegacyCurrentTheme() {
    val isDarkMode = PropertiesComponent.getInstance().getBoolean(DARK_MODE_PROP)
    PropertiesComponent.getInstance().unsetValue(DARK_MODE_PROP)
    val lastTheme = LegacyThemeUtilities.legacyThemeNameMapping(
      ThemeConfig.instance.selectedTheme,
      isDarkMode
    )
    LafManager.getInstance().installedLookAndFeels.find {
      it.name.equals(lastTheme, true)
    }.toOptional()
      .ifPresent {
        QuickChangeLookAndFeel.switchLafAndUpdateUI(
          LafManager.getInstance(),
          it,
          true
        )
      }
  }

  private fun migrateLegacyCurrentSticker() {
    ThemeConfig.instance.currentSticker =
      if (PropertiesComponent.getInstance()
          .getValue(SAVED_STATE)?.toBoolean() == true
      ) {
        CurrentSticker.SECONDARY
      } else {
        CurrentSticker.DEFAULT
      }

    PropertiesComponent.getInstance().unsetValue(SAVED_STATE)
  }

  override fun dispose() {
    connection.dispose()
  }
}