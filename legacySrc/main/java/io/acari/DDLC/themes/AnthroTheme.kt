package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.themes.models.MTThemeable
import io.acari.DDLC.DDLCThemeFacade
import java.util.stream.Stream

abstract class AnthroTheme(ddlcThemeId: String, colorScheme: String, isDarkTheme: Boolean, clubMemberName: String) :
    DokiDokiTheme(ddlcThemeId, colorScheme, isDarkTheme, clubMemberName), DDLCThemeFacade {

  override fun getTheme(): MTThemeable =
      this

  override fun getOrder(): Int =
      9001

  override fun isPremium(): Boolean = false

  override fun isCustom(): Boolean = false

  override fun getThemeColorScheme(): String =
      editorColorsScheme

  override fun getThemeName(): String? =
      name

  override fun iconPath(): String {
    return "/icons/ddlc/anthro/$name.svg"
  }

  override fun getMenuBarResources(): Stream<String> {
    return Stream.of(
        //I just want the literature club to have th special menubar
        "PopupMenu.background",
        "TitlePane.background"
    )
  }
}