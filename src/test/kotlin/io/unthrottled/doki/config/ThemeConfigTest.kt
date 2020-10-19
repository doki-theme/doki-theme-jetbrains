package io.unthrottled.doki.config

import io.unthrottled.doki.stickers.CurrentSticker
import io.unthrottled.doki.stickers.StickerLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ThemeConfigTest {

  @Test
  fun getCurrentStickerShouldCorrectBadSettings() {
    val themeConfig = ThemeConfig()
    listOf(
      Triple("potato", StickerLevel.ON, "ON"),
      Triple("bruh", StickerLevel.ON, "ON"),
      Triple("on", StickerLevel.ON, "on"),
      Triple("one", StickerLevel.ON, "ON"),
      Triple("On", StickerLevel.ON, "On"),
      Triple("oN", StickerLevel.ON, "oN"),
      Triple("ON", StickerLevel.ON, "ON"),
      Triple("off", StickerLevel.OFF, "off"),
      Triple("oFF", StickerLevel.OFF, "oFF"),
      Triple("OFF", StickerLevel.OFF, "OFF"),
      Triple("Off", StickerLevel.OFF, "Off")
    ).forEach {
      themeConfig.stickerLevel = it.first
      assertThat(themeConfig.currentStickerLevel).isEqualTo(it.second)
      assertThat(themeConfig.stickerLevel).isEqualTo(it.third)
    }
  }

  @Test
  fun getCurrentStickerLevelShouldCorrectBadSettings() {
    val themeConfig = ThemeConfig()
    listOf(
      Triple("potato", CurrentSticker.DEFAULT, "DEFAULT"),
      Triple("bruh", CurrentSticker.DEFAULT, "DEFAULT"),
      Triple("DEFAULT", CurrentSticker.DEFAULT, "DEFAULT"),
      Triple("default", CurrentSticker.DEFAULT, "default"),
      Triple("Default", CurrentSticker.DEFAULT, "Default"),
      Triple("Secondary", CurrentSticker.SECONDARY, "Secondary"),
      Triple("secondary", CurrentSticker.SECONDARY, "secondary"),
      Triple("SECONDARY", CurrentSticker.SECONDARY, "SECONDARY")
    ).forEach {
      themeConfig.currentStickerName = it.first
      assertThat(themeConfig.currentSticker).isEqualTo(it.second)
      assertThat(themeConfig.currentStickerName).isEqualTo(it.third)
    }
  }
}