package io.unthrottled.doki.promotions

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey

object MessageBundle : AbstractBundle(MESSAGE_BUNDLE) {
  fun message(
    @PropertyKey(resourceBundle = MESSAGE_BUNDLE) key: String,
    vararg params: Any
  ): String {
    return getMessage(key, params)
  }
}

const val MESSAGE_BUNDLE = "messages.MessageBundle"