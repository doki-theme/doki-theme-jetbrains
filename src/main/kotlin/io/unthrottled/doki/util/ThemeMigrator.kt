package io.unthrottled.doki.util

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import io.unthrottled.doki.TheDokiTheme
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.settings.ThemeSettings
import io.unthrottled.doki.settings.actors.setDokiTheme
import io.unthrottled.doki.stickers.StickerService
import io.unthrottled.doki.themes.ThemeManager

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
      .map { ThemeManager.instance.themeByName(ThemeManager.DEFAULT_THEME_NAME) }
      .ifPresent {
        setDokiTheme(it)
        StickerService.instance.clearPreviousSticker()
        ApplicationManager.getApplication().invokeLater {
          UpdateNotification.sendMessage(
            "Theme Not Available",
            """
      <p>Sorry friend, but your previously selected theme is no longer part of the Community Doki Theme.</p>
      <p>You need the Ultimate Doki Theme (which is free) to gain access your previous theme.</p>
      <p>The <a href='${ThemeSettings.ULTIMATE_INSTRUCTIONS}'>wiki</a> on the repository 
      should show you what you need to do to get it.</p>
      <p>Thanks!</p>
            """.trimIndent(),
            project
          )
        }
      }
  }
}
