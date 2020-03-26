package io.acari.doki.util

import com.intellij.ide.actions.QuickChangeLookAndFeel
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import io.acari.doki.TheDokiTheme
import io.acari.doki.config.ThemeConfig
import io.acari.doki.notification.UpdateNotification
import io.acari.doki.settings.ThemeSettings
import io.acari.doki.settings.actors.setDokiTheme
import io.acari.doki.stickers.CurrentSticker
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.ThemeManager

object ThemeMigrator {

  private val ULTIMATE_THEME_SET = setOf("mistress")

  fun migrateToCommunityIfNecessary(project: Project) {
    if (isCommunity()) {
      sendToMonika(project)
    }
  }

  private fun isCommunity(): Boolean =
    PluginManagerCore.getPlugin(PluginId.getId(TheDokiTheme.COMMUNITY_PLUGIN_ID)) != null

  private fun sendToMonika(project: Project) {
    StickerService.instance.getPreviousSticker()
      .filter { stickerProp ->
        ULTIMATE_THEME_SET.any { theme -> stickerProp.contains(theme) }
      }
      .map { ThemeManager.instance.themeByName(ThemeManager.MONIKA_DARK) }
      .ifPresent {
        setDokiTheme(it)
        StickerService.instance.clearPreviousSticker()
        ApplicationManager.getApplication().invokeLater {
          UpdateNotification.sendMessage(
            "Theme Not Available",
            """
      <p>Sorry friend, but your previously selected theme is no longer part of the Community Doki Theme.</p>
      <p>You need the Ultimate Doki Theme (which is free) to gain access your previous theme.</p>
      <p>The <a href='${ThemeSettings.ULTIMATE_INSTRUCTIONS}'>wiki</a> on the repository should show you what you need to do to get it.</p>
      <p>Thanks!</p>
    """.trimIndent(),
            project
          )
        }
      }
  }

  fun migrateLegacyTheme() {
    if (!ThemeConfig.instance.processedLegacyStartup) {
      migrateLegacyCurrentSticker()
      migrateLegacyCurrentTheme()
      ThemeConfig.instance.processedLegacyStartup = true
    }
  }

  private fun migrateLegacyCurrentTheme() {
    val isDarkMode = PropertiesComponent.getInstance().getBoolean(LegacyThemeUtilities.DARK_MODE_PROP)
    PropertiesComponent.getInstance().unsetValue(LegacyThemeUtilities.DARK_MODE_PROP)
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
          .getValue(LegacyThemeUtilities.SAVED_STATE)?.toBoolean() == true
      ) {
        CurrentSticker.SECONDARY
      } else {
        CurrentSticker.DEFAULT
      }

    PropertiesComponent.getInstance().unsetValue(LegacyThemeUtilities.SAVED_STATE)
  }

}