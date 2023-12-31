package io.unthrottled.doki.listener

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.TheDokiTheme
import io.unthrottled.doki.hax.HackComponent
import io.unthrottled.doki.stickers.StickerComponent

class ApplicationLifecycleListener : AppLifecycleListener, DumbAware {
  companion object {
    init {
      HackComponent.init()
    }
  }

  override fun appFrameCreated(commandLineArgs: MutableList<String>) {
    TheDokiTheme.instance.init()
    StickerComponent.instance.init()
  }

  override fun appClosing() {
  }
}
