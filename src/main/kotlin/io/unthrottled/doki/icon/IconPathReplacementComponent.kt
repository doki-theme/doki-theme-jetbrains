package io.unthrottled.doki.icon

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.IconLoader
import io.unthrottled.doki.service.PluginService
import io.unthrottled.doki.themes.ThemeManager

data class IconReplacementPack(
  val iconPatcher: DokiIconPathPatcher
)

object IconPathReplacementComponent : LafManagerListener {
  private val iconInstallPacs =
    listOf(
      IconReplacementPack(
        DokiIconPathPatcher("ui-icons.path.mappings.json")
      )
    )

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  fun initialize() {
    connection.subscribe(LafManagerListener.TOPIC, this)
    installPatchers()
  }

  fun installPatchers() {
    iconInstallPacs.forEach { pak ->
      if (ThemeManager.instance.isCurrentThemeDoki && PluginService.areIconsInstalled().not()) {
        IconLoader.installPathPatcher(pak.iconPatcher)
      }
    }
  }

  fun dispose() {
    connection.dispose()
    removePatchers()
  }

  fun removePatchers() {
    iconInstallPacs.forEach { pak ->
      IconLoader.removePathPatcher(pak.iconPatcher)
    }
  }

  override fun lookAndFeelChanged(source: LafManager) {
    if (ThemeManager.instance.isCurrentThemeDoki) {
      installPatchers()
    } else {
      removePatchers()
    }
  }
}
