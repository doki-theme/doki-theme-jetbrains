package io.unthrottled.doki.config

import com.intellij.util.messages.Topic
import java.util.EventListener

val THEME_CONFIG_TOPIC: Topic<ThemeConfigListener> =
  Topic(ThemeConfigListener::class.java)

interface ThemeConfigListener : EventListener {
  fun themeConfigUpdated(themeConfig: ThemeConfig)
}
