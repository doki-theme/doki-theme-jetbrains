package io.unthrottled.doki.stickers

import com.intellij.openapi.components.ServiceManager
import io.unthrottled.doki.themes.DokiTheme
import java.util.Optional

interface StickerService {

  fun activateForTheme(dokiTheme: DokiTheme)
  fun remove()

  companion object {
    val instance: StickerService
      get() = ServiceManager.getService(StickerService::class.java)
  }

  fun getPreviousSticker(): Optional<String>
  fun clearPreviousSticker()
  fun checkForUpdates(dokiTheme: DokiTheme)
}
