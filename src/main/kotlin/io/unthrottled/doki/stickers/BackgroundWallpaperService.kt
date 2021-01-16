package io.unthrottled.doki.stickers

import com.intellij.openapi.components.ServiceManager
import io.unthrottled.doki.themes.DokiTheme
import java.util.Optional

internal interface BackgroundWallpaperService {

  fun activateForTheme(dokiTheme: DokiTheme)
  fun remove()

  companion object {
    val instance: BackgroundWallpaperService
      get() = ServiceManager.getService(BackgroundWallpaperService::class.java)
  }

  fun checkForUpdates(dokiTheme: DokiTheme)
}
