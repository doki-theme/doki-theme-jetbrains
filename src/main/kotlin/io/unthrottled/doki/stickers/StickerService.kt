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

  fun checkForUpdates(dokiTheme: DokiTheme)
}
