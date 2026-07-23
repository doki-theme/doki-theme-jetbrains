package io.unthrottled.doki.promotions

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.assets.AssetCategory
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerComponent
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.doOrElse
import io.unthrottled.doki.util.toOptional
import java.util.Optional

object CulturedContentManager {
  private val culturedThemes =
    setOf(
      "ea9a13f6-fa7f-46a4-ba6e-6cefe1f55160",
    )

  private var isAsking = false

  fun safelyGetSticker(dokiTheme: DokiTheme): Optional<String> =
    safelyGetContent(
      dokiTheme,
      dokiTheme.stickers.secondary ?: dokiTheme.stickers.default,
    )

  fun safelyGetBackground(dokiTheme: DokiTheme): Optional<Background> =
    safelyGetContent(
      dokiTheme,
      dokiTheme.backgrounds.secondary ?: dokiTheme.backgrounds.default,
    )

  private fun <T> safelyGetContent(
    dokiTheme: DokiTheme,
    content: T,
  ) = if (isCultured(dokiTheme) && !hasAccepted(dokiTheme)) {
    if (!isAsking) {
      askToShowCulturedContent(dokiTheme)
    }
    Optional.empty()
  } else {
    content.toOptional()
  }
  private fun getFirstProject(): Optional<Project> =
    ProjectManager.getInstance().openProjects.toOptional().filter {it.isNotEmpty()}.map {it.first()}

  private fun askToShowCulturedContent(dokiTheme: DokiTheme) {
    isAsking = true
    getFirstProject()
      .doOrElse({
        ApplicationManager.getApplication().invokeLater {
          val dialog =
            CulturedContentDialog(
              AssetManager.resolveAssetUrl(
                AssetCategory.MISC,
                "suggestive/cultured.gif",
              ).orElse("${AssetManager.ASSET_SOURCE}/misc/suggestive/cultured.gif"),
              it,
            )
          dialog.showAndGet()

          if (dialog.exitCode == CulturedContentDialog.ALLOW_CULTURE_EXIT_CODE) {
            val currentLedger = CulturedContentLedgerMaster.readLedger()
            currentLedger.allowedCulturedContent[dokiTheme.id] = mutableSetOf(CurrentSticker.SECONDARY)
            CulturedContentLedgerMaster.persistLedger(currentLedger)
            StickerComponent.activateForTheme(dokiTheme)
          }
          isAsking = false
        }
      }) {
        isAsking = false
      }
  }

  private fun hasAccepted(dokiTheme: DokiTheme): Boolean =
    CulturedContentLedgerMaster.readLedger().allowedCulturedContent.containsKey(dokiTheme.id)

  private fun isCultured(dokiTheme: DokiTheme): Boolean = culturedThemes.contains(dokiTheme.id)
}
