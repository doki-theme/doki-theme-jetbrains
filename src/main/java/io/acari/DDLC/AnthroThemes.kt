package io.acari.DDLC

import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.intellij.ui.ColorUtil
import io.acari.DDLC.themes.DokiDokiTheme
import javax.swing.Icon

//NEERA,
//SANYA,
//SYRENA,
//FEN,
//ELENIEL,
//CLEO,
//WYLA,
//ZAHRA,


abstract class AthroTheme(private val dokiDokiTheme: DokiDokiTheme): DDLCThemeFacade {

  override fun getTheme(): MTThemeable =
      dokiDokiTheme

  override fun isDark(): Boolean =
      theme.isDark

  override fun getName(): String = theme.name

  override fun getThemeName(): String = theme.name

  override fun getThemeId(): String =
      theme.id

  override fun getIcon(): Icon =
      theme.icon

  override fun getAccentColor(): String =
      theme.accentColor

  override fun getStartColor(): String =
      theme.startColor

  override fun getStopColor(): String =
      theme.stopColor

  override fun getExcludedColor(): String =
      ColorUtil.toHex(theme.excludedColor)

  override fun getOrder(): Int =
      9001

  override fun getNonProjectFileScopeColor(): String =
      theme.nonProjectFileScopeColor

  override fun getTestScope(): String =
      theme.testScope

  override fun isPremium(): Boolean = false

  override fun isCustom(): Boolean = false

  override fun getThemeColorScheme(): String =
      theme.editorColorsScheme

}