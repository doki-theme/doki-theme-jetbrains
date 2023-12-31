package io.unthrottled.doki.stickers

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafelyWithResult
import java.util.concurrent.ConcurrentHashMap

class MarginService : Logging {
  companion object {
    private const val STICKER_Y_OFFSET = 0.05
    private const val STICKER_X_OFFSET = 0.03

    val instance: MarginService =
      ApplicationManager.getApplication().getService(MarginService::class.java)
  }

  private val gson =
    GsonBuilder()
      .create()

  private val savedMargins: MutableMap<String, Margin> = readMargins()

  private fun readMargins(): ConcurrentHashMap<String, Margin> {
    return runSafelyWithResult({
      gson.fromJson(
        ThemeConfig.instance.savedMargins,
        object : TypeToken<ConcurrentHashMap<String, Margin>>() {}.type,
      )
    }) {
      logger().warn("Unable to read saved margins!", it)
      ConcurrentHashMap()
    }
  }

  fun reset() {
    savedMargins.clear()
    ThemeConfig.instance.savedMargins = "{}"
  }

  fun getMargin(windowGuy: Any): Margin {
    return savedMargins[getWindowKey(windowGuy)] ?: Margin(STICKER_X_OFFSET, STICKER_Y_OFFSET)
  }

  private fun getWindowKey(windowBro: Any) = windowBro::class.java.canonicalName

  fun saveMargin(
    window: Any,
    margin: Margin,
  ) {
    savedMargins[getWindowKey(window)] = margin
    ThemeConfig.instance.savedMargins = gson.toJson(savedMargins.toMutableMap())
    UpdateNotification.showNotificationAcrossProjects(
      MessageBundle.message("notification.margin.saved.title"),
      MessageBundle.message("notification.margin.saved.message"),
    )
  }
}
