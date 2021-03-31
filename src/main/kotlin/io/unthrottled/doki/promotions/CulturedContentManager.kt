package io.unthrottled.doki.promotions

import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.toOptional
import java.util.Optional

object CulturedContentManager {

  private val culturedThemes = setOf(
    "ea9a13f6-fa7f-46a4-ba6e-6cefe1f55160",
  )

  fun safelyGet(dokiTheme: DokiTheme): Optional<String> =
    if (isCultured(dokiTheme) && !hasAccepted(dokiTheme)) {
      // todo: is asking
      getFirstProject()
        .ifPresent {
          ApplicationManager.getApplication().invokeLater {
            val dialog = CulturedContentDialog(dokiTheme, it)
            dialog.showAndGet()

            if (dialog.exitCode == CulturedContentDialog.ALLOW_CULTURE_EXIT_CODE) {
              val currentLedger = CulturedContentLedgerMaster.readLedger()
              currentLedger.allowedCulturedContent[dokiTheme.id] = mutableSetOf(CurrentSticker.SECONDARY)
              CulturedContentLedgerMaster.persistLedger(currentLedger)
              // todo: redraw
            }
          }
        }
      Optional.empty()
    } else {
      val sticker = dokiTheme.stickers.secondary ?: dokiTheme.stickers.default
      sticker.toOptional()
    }

  private fun hasAccepted(dokiTheme: DokiTheme): Boolean =
    CulturedContentLedgerMaster.readLedger().allowedCulturedContent.containsKey(dokiTheme.id)

  private fun isCultured(dokiTheme: DokiTheme): Boolean =
    culturedThemes.contains(dokiTheme.id)
}