package io.unthrottled.doki.integrations.grep

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.ui.JBColor
import com.intellij.util.text.VersionComparatorUtil
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.service.GREP_CONSOLE_PLUGIN_ID
import io.unthrottled.doki.themes.ThemeManager
import krasa.grepconsole.model.GrepExpressionGroup
import krasa.grepconsole.model.GrepExpressionItem
import krasa.grepconsole.model.Profile
import krasa.grepconsole.plugin.DefaultState.getGrepStyle
import krasa.grepconsole.plugin.DefaultState.newItem
import krasa.grepconsole.plugin.GrepConsoleApplicationComponent.getInstance
import krasa.grepconsole.plugin.ServiceManager
import java.awt.Color

class GrepConsoleIntegration : LafManagerListener, StartupActivity {
  override fun lookAndFeelChanged(source: LafManager) {
    installColors()
  }

  private fun installColors() {
    if (
      ThemeConfig.instance.allowGrepConsoleIntegration.not() ||
      isGrepConsoleTooOld()
    ) return

    val grepProfile = ThemeManager.instance.currentTheme
      .map {
        val dokiGrepProfile = getInstance().state.profiles.find { profile ->
          profile.id == DOKI_GREP_PROFILE_ID
        } ?: createDokiProfile()

        decorateWithDokiGrepColors(dokiGrepProfile)
      }
      .orElseGet {
        getInstance().state.profiles.first {
          it.id != DOKI_GREP_PROFILE_ID
        }
      }

    ConsoleViewService.instance.consoles.forEach { consoleView ->
      ServiceManager.getInstance()
        .profileChanged(consoleView, grepProfile)
    }
    ServiceManager.getInstance().rehighlight()
  }

  private fun createDokiProfile(): Profile {
    val dokiGrepProfile = getInstance().state.createProfile()
    dokiGrepProfile.id = DOKI_GREP_PROFILE_ID
    dokiGrepProfile.name = DOKI_GREP_PROFILE_NAME
    return dokiGrepProfile
  }

  private fun decorateWithDokiGrepColors(profile: Profile): Profile {
    val grepExpressionGroups = profile.grepExpressionGroups
    grepExpressionGroups.clear()
    grepExpressionGroups.add(GrepExpressionGroup("default"))
    val underDarcula = UIUtil.isUnderDarcula()
    val DARK = "@Dark Theme@"
    val LIGHT = "@Light Theme@"
    if (underDarcula) {
      grepExpressionGroups.add(
        GrepExpressionGroup(
          DARK,
          createDefaultItems(dark = true)
        )
      )
      grepExpressionGroups.add(
        GrepExpressionGroup(
          LIGHT,
          createDefaultItems(dark = false)
        )
      )
    } else {
      grepExpressionGroups.add(
        GrepExpressionGroup(
          LIGHT,
          createDefaultItems(dark = false)
        )
      )
      grepExpressionGroups.add(
        GrepExpressionGroup(
          DARK,
          createDefaultItems(dark = true)
        )
      )
    }

    return profile
  }

  private fun createDefaultItems(dark: Boolean): List<GrepExpressionItem> {

    val items: MutableList<GrepExpressionItem> = ArrayList()
    val fatalBackground = JBColor.namedColor(
      "GrepConsole.fatal.background", if (dark) JBColor.BLACK else JBColor.RED
    )
    val errorBackground = JBColor.namedColor(
      "GrepConsole.error.background", if (dark) Color(55, 0, 0, 200) else JBColor.ORANGE
    )
    val warnBackground = JBColor.namedColor(
      "GrepConsole.warn.background", if (dark) Color(26, 0, 55, 200) else JBColor.YELLOW
    )
    val debugForeground = JBColor.namedColor(
      "GrepConsole.debug.foreground", JBColor.GRAY
    )
    val traceForeground = JBColor.namedColor(
      "GrepConsole.trace.foreground", if (dark) JBColor.BLACK else JBColor.LIGHT_GRAY
    )
    items.add(
      newItem().enabled(true).style(
        getGrepStyle(fatalBackground, null).bold(true)
      ).grepExpression(
        ".*FATAL.*"
      )
    )
    items.add(
      newItem().enabled(true).style(getGrepStyle(errorBackground, null)).grepExpression(
        ".*ERROR.*"
      )
    )
    items.add(
      newItem().enabled(true).style(getGrepStyle(warnBackground, null)).grepExpression(
        ".*WARN.*"
      )
    )
    items.add(
      newItem().enabled(false).style(getGrepStyle(null, null)).grepExpression(
        ".*INFO.*"
      )
    )
    items.add(
      newItem().enabled(true).style(getGrepStyle(null, debugForeground)).grepExpression(
        ".*DEBUG.*"
      )
    )
    items.add(
      newItem().enabled(true).style(getGrepStyle(null, traceForeground)).grepExpression(
        ".*TRACE.*"
      )
    )
    return items
  }

  private fun isGrepConsoleTooOld(): Boolean {
    return VersionComparatorUtil.COMPARATOR.compare(
      PluginManagerCore.getPlugin(
        PluginId.getId(GREP_CONSOLE_PLUGIN_ID)
      )?.version ?: "0", OLDEST_GREP_CONSOLE_VERSION
    ) < 0
  }

  override fun runActivity(project: Project) {
    installColors()
  }

  companion object {
    private const val DOKI_GREP_PROFILE_ID = 69420L
    private const val DOKI_GREP_PROFILE_NAME = "DokiTheme"
    private const val OLDEST_GREP_CONSOLE_VERSION = "11.11.211.6086.0"
  }
}
