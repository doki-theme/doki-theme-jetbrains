package io.acari.DDLC.icons

import com.chrisrm.ideaddlc.listeners.AccentsListener
import com.chrisrm.ideaddlc.listeners.MTTopics
import com.chrisrm.ideaddlc.listeners.ThemeListener
import com.chrisrm.ideaddlc.utils.MTAccents
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.ColorUtil
import com.intellij.util.SVGLoader
import com.intellij.util.messages.MessageBusConnection
import io.acari.DDLC.DDLCThemeFacade
import org.jetbrains.annotations.NonNls
import org.w3c.dom.Element
import java.awt.Color

class TintedColorPatcher internal constructor() : SVGLoader.SvgColorPatcher, Disposable {
  private val messageBusConnection: MessageBusConnection = ApplicationManager.getApplication().messageBus.connect()
  override fun dispose() {
    messageBusConnection.disconnect()
  }

  init {
    SVGLoader.setColorPatcher(this)
    val self = this

    // Listen for changes on the settings
    messageBusConnection.subscribe(MTTopics.ACCENTS, object : AccentsListener {
      override fun accentChanged(accentColor: Color) {
        SVGLoader.setColorPatcher(null)
        SVGLoader.setColorPatcher(self)
        refreshAccentColor(accentColor)
      }
    })

    messageBusConnection.subscribe(MTTopics.THEMES, object : ThemeListener {
      override fun themeChanged(theme: DDLCThemeFacade) {
        SVGLoader.setColorPatcher(null)
        SVGLoader.setColorPatcher(self)
        refreshThemeColor(theme)
      }
    })
  }

  override fun patchColors(@NonNls svg: Element) {
    val tint = svg.getAttribute("tint")
    val themed = svg.getAttribute("themed")
    val themedStartAttr = svg.getAttribute("themedStart")
    val themedStopAttr = svg.getAttribute("themedStop")
    val themedFillAttr = svg.getAttribute("themedFill")

    if ("true" == tint || "fill" == tint) {
      svg.setAttribute("fill", "#$accentColor")
    } else if ("all" == tint || "fill" == tint) {
      svg.setAttribute("fill", "#$accentColor")
      svg.setAttribute("stroke", "#$accentColor")
    } else if ("stroke" == tint) {
      svg.setAttribute("stroke", "#$accentColor")
    } else if ("true" == themed || "fill" == themed) {
      svg.setAttribute("fill", "#$themedColor")
    } else if ("stroke" == themed) {
      svg.setAttribute("stroke", "#$themedColor")
    } else if ("true" == themedStartAttr){
      svg.setAttribute("stop-color","#$themedStart")
      svg.setAttribute("fill","#$themedStart")
    } else if ("true" == themedStopAttr){
      svg.setAttribute("stop-color","#$themedStop")
      svg.setAttribute("fill","#$themedStop")
    } else if ("true" == themedFillAttr){
      svg.setAttribute("fill","#$themedStart")
      svg.setAttribute("stroke","#$themedStart")
    }

    val nodes = svg.childNodes
    val length = nodes.length
    for (i in 0 until length) {
      val item = nodes.item(i)
      if (item is Element) {
        patchColors(item)
      }
    }
  }

  companion object {
    @NonNls
    private var accentColor = MTAccents.TURQUOISE.hexColor
    @NonNls
    private var themedColor = MTAccents.OCEANIC.hexColor

    private var themedStart = MTAccents.CYAN.hexColor
    private var themedStop = MTAccents.TURQUOISE.hexColor

    internal fun refreshAccentColor(accentColor: Color) {
      TintedColorPatcher.accentColor = ColorUtil.toHex(accentColor)
    }

    internal fun refreshThemeColor(theme: DDLCThemeFacade) {
      themedColor = ColorUtil.toHex(theme.theme.tintedIconColor)
      themedStart = theme.theme.startColor
      themedStop = theme.theme.stopColor
    }
  }
}
