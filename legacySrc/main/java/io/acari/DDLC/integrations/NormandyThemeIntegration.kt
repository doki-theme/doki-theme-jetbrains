package io.acari.DDLC.integrations

import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.google.gson.Gson
import com.intellij.ui.ColorUtil
import com.intellij.util.messages.MessageBus
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.themes.AccentChangedInformation
import io.acari.DDLC.themes.ThemeChangedInformation
import java.awt.Color

const val OVERRIDE_CLASS: String = "com.intellij.compiler.server.CustomBuilderMessageHandler"

//todo: revisit
object NormandyThemeIntegration {
  private val gson = Gson()

  val isEnabled: Boolean = LegacySupportUtility.orGetLegacy(OVERRIDE_CLASS, {true}, {false})

  fun themeChanged(messageBus: MessageBus, newTheme: DDLCThemeFacade) {
    if(isEnabled) {

    }
  }

  fun accentChanged(messageBus: MessageBus, accentColor: Color) {
    if(isEnabled) {

    }
  }

  private fun createAccentDeltas(accentColor: Color): AccentChangedInformation =
      AccentChangedInformation(ColorUtil.toHex(accentColor))

  private fun createThemeDeltas(themeable: MTThemeable): ThemeChangedInformation =
      ThemeChangedInformation(
          themeable.isDark,
          themeable.accentColor,
          themeable.contrastColorString,
          themeable.secondaryBackgroundColorString,
          ColorUtil.toHex(themeable.foregroundColor))
}