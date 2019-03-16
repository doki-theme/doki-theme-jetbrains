package io.acari.DDLC.integrations

import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.google.gson.Gson
import com.intellij.compiler.server.CustomBuilderMessageHandler
import com.intellij.ui.ColorUtil
import com.intellij.util.messages.MessageBus
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.themes.AccentChangedInformation
import io.acari.DDLC.themes.ThemeChangedInformation
import java.awt.Color

const val OVERRIDE_CLASS = "com.intellij.compiler.server.CustomBuilderMessageHandler"

object NormandyThemeIntegration {
  private val gson = Gson()

  val isEnabled = LegacySupportUtility.orGetLegacy(OVERRIDE_CLASS, {true}, {false})

  fun themeChanged(messageBus: MessageBus, newTheme: DDLCThemeFacade) {
    if(isEnabled){
      messageBus.syncPublisher(CustomBuilderMessageHandler.TOPIC)
          .messageReceived("io.acari.DDLCTheme",
              "Theme Changed",
              gson.toJson(createThemeDeltas(newTheme.theme)))
    }
  }

  fun accentChanged(messageBus: MessageBus, accentColor: Color) {
    if(isEnabled){
      messageBus.syncPublisher(CustomBuilderMessageHandler.TOPIC)
          .messageReceived("io.acari.DDLCTheme",
              "Accent Changed",
              gson.toJson(createAccentDeltas(accentColor)))
    }
  }

  private fun createAccentDeltas(accentColor: Color): AccentChangedInformation =
      AccentChangedInformation(ColorUtil.toHex(accentColor))

  private fun createThemeDeltas(themeable: MTThemeable): ThemeChangedInformation =
      ThemeChangedInformation(
          themeable.isDark,
          themeable.accentColor,
          themeable.contrastColorString,
          themeable.treeSelectionBackgroundColorString,
          ColorUtil.toHex(themeable.foregroundColor))
}