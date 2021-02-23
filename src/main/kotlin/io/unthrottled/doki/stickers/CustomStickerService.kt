package io.unthrottled.doki.stickers

import com.intellij.ide.util.PropertiesComponent
import io.unthrottled.doki.util.toOptional
import java.util.Optional
import java.nio.file.Paths

object CustomStickerService {
  private const val CUSTOM_STICKER_PROPERTY = "io.unthrottled.doki.theme.custom-sticker"
  private const val CUSTOM_STICKER_ENABLED_PROPERTY = "io.unthrottled.doki.theme.custom-sticker.enabled"
  fun setCustomStickerPath(path: String) {
    PropertiesComponent.getInstance().setValue(
      CUSTOM_STICKER_PROPERTY,
      path
    )
  }

  fun getCustomStickerPath(): Optional<String> =
    PropertiesComponent.getInstance().getValue(CUSTOM_STICKER_PROPERTY)
      .toOptional()
      .filter { it.isNotBlank() }

  fun getCustomStickerUrl(): Optional<String> =
    getCustomStickerPath()
      .map { Paths.get(it).toUri().toString() }

  var isCustomStickers: Boolean
    get() = PropertiesComponent.getInstance().getBoolean(CUSTOM_STICKER_ENABLED_PROPERTY, false)
    set(value) = PropertiesComponent.getInstance().setValue(CUSTOM_STICKER_ENABLED_PROPERTY, value)
}
