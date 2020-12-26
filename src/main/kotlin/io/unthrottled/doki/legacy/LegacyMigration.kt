package io.unthrottled.doki.legacy

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.util.ThemeMigrator

object LegacyMigration {
  fun migrateIfNecessary(project: Project) {
    ThemeMigrator.migrateToCommunityIfNecessary(project)

    migrateFileColors(project)
  }

  // todo: remove on next major release
  private fun migrateFileColors(project: Project) {
    FileColorsMigrationAction.setDefaultFileScopes(project)
    if (ThemeConfig.instance.isDokiFileColors) {
      StartupManager.getInstance(project)
        .runWhenProjectIsInitialized {
          UpdateNotification.sendMessage(
            "File Colors set to default",
            """
              <div>
              The file colors action is no longer necessary. 
              The plugin will now use the default <code>green</code> and 
              <code>yellow</code> file colors.
              
              See <a href='https://www.jetbrains.com/help/idea/settings-file-colors.html'>
the documentation
</a> for more details
</div>
            """.trimIndent()

          )
          ThemeConfig.instance.isDokiFileColors = false
        }
    }
  }
}
