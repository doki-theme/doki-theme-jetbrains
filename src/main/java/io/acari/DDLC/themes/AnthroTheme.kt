package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.intellij.ui.ColorUtil
import io.acari.DDLC.DDLCThemeFacade
import java.awt.Color
import javax.swing.Icon

abstract class AnthroTheme(ddlcThemeId: String, colorScheme: String, isDarkTheme: Boolean, clubMemberName: String) :
    DokiDokiTheme(ddlcThemeId, colorScheme, isDarkTheme, clubMemberName), DDLCThemeFacade {

  override fun getTheme(): MTThemeable =
      this

  override fun isDark(): Boolean =
      theme.isDark

  override fun getOrder(): Int =
      9001

  override fun isPremium(): Boolean = false

  override fun isCustom(): Boolean = false

  override fun getThemeColorScheme(): String =
      editorColorsScheme

  override fun getThemeName(): String? =
      name
}