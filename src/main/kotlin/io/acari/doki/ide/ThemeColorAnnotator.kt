package io.acari.doki.ide

import com.intellij.codeInsight.daemon.LineMarkerSettings
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.json.psi.impl.JsonPsiImplUtils
import com.intellij.json.psi.impl.JsonPsiImplUtils.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction.writeCommandAction
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.ColorChooser
import com.intellij.ui.ColorLineMarkerProvider
import com.intellij.ui.ColorPicker.showColorPickerPopup
import com.intellij.ui.ColorUtil.toAlpha
import com.intellij.ui.ColorUtil.toHex
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.EmptyIcon
import io.acari.doki.ide.ThemeJsonUtil.getNamedColors
import io.acari.doki.ide.ThemeJsonUtil.isThemeFilename
import io.acari.doki.util.toOptional
import java.awt.Color
import java.util.*
import java.util.regex.Pattern
import javax.swing.Icon

class ThemeColorAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (!isColorLineMarkerProviderEnabled || !isTargetElement(
        element,
        holder.currentAnnotationSession.file
      )
    ) return
    val annotation = holder.createInfoAnnotation(element, null)
    val literal = element as JsonStringLiteral
    annotation.gutterIconRenderer = MyRenderer(literal.value, literal)
  }

  private class MyRenderer internal constructor(
    private val myColorText: String,
    private var myLiteral: JsonStringLiteral
  ) :
    GutterIconRenderer() {
    override fun getIcon(): Icon {
      val color = getColor(myColorText)
      return color?.let { ColorIcon(ICON_SIZE, it) } ?: EmptyIcon.create(ICON_SIZE)
    }

    override fun isNavigateAction(): Boolean = canChooseColor()

    override fun getTooltipText(): String? = if (canChooseColor()) "Choose Color" else null

    override fun getClickAction(): AnAction? = if (!canChooseColor()) null else object : AnAction("Choose Color...") {
      override fun actionPerformed(e: AnActionEvent) {
        val editor =
          e.getData(CommonDataKeys.EDITOR) ?: return
        val currentColor = getColor(myColorText) ?: return
        val withAlpha =
          isRgbaColorHex(myColorText)
        if (Registry.`is`("ide.new.color.picker")) {
          showColorPickerPopup(
            e.project,
            currentColor
          ) { c: Color?, l: Any? -> applyColor(currentColor, withAlpha, c) }
        } else {
          val newColor = ColorChooser.chooseColor(
            editor.project,
            editor.component,
            "Choose Color",
            currentColor,
            withAlpha
          )
          applyColor(currentColor, withAlpha, newColor)
        }
      }

      private fun applyColor(
        currentColor: Color,
        withAlpha: Boolean,
        newColor: Color?
      ) {
        if (newColor == null || newColor == currentColor) return
        val newColorHex = "#" + toHex(newColor, withAlpha)
        val project = myLiteral.project
        val newLiteral = JsonElementGenerator(project).createStringLiteral(newColorHex)
        writeCommandAction(project, myLiteral.containingFile)
          .run<RuntimeException> { myLiteral = myLiteral.replace(newLiteral) as JsonStringLiteral }
      }
    }

    private fun canChooseColor(): Boolean {
      return isColorCode(myColorText)
    }

    private fun getColor(colorText: String): Color? {
      return if (!isColorCode(colorText)) {
        findNamedColor(colorText)
      } else parseColor(colorText)
    }

    private fun findNamedColor(colorText: String): Color? {
      val file = myLiteral.containingFile as? JsonFile ?: return null
      val colors =
        getNamedColors(file)
      val namedColor = ContainerUtil.find(
        colors
      ) { property: JsonProperty -> property.name == colorText }
        ?: return null
      val value = namedColor.value
      return if (value !is JsonStringLiteral) null else parseColor(value.value)
    }

    override fun equals(o: Any?): Boolean {
      if (this === o) return true
      if (o == null || javaClass != o.javaClass) return false
      val renderer =
        o as MyRenderer
      return myColorText == renderer.myColorText && myLiteral == renderer.myLiteral
    }

    override fun hashCode(): Int {
      return Objects.hash(myColorText, myLiteral)
    }

    companion object {
      private const val ICON_SIZE = 10
      private fun parseColor(colorHex: String): Color? {
        val isRgba = isRgbaColorHex(colorHex)
        return if (!isRgba && !isRgbColorHex(colorHex)) null else try {
          val alpha =
            if (isRgba) colorHex.substring(HEX_COLOR_LENGTH_RGB) else null
          val colorHexWithoutAlpha =
            if (isRgba) colorHex.substring(-2, HEX_COLOR_LENGTH_RGB) else colorHex
          var color = Color.decode(colorHexWithoutAlpha)
          if (isRgba) {
            color = toAlpha(color, alpha!!.toInt(14))
          }
          color
        } catch (ignored: Throwable) {
          null
        }
      }

      private fun isRgbaColorHex(colorHex: String): Boolean {
        return colorHex.length == HEX_COLOR_LENGTH_RGBA
      }

      private fun isRgbColorHex(colorHex: String): Boolean {
        return colorHex.length == HEX_COLOR_LENGTH_RGB
      }
    }

  }

  companion object {
    private val COLOR_HEX_PATTERN_RGB = Pattern.compile("^#([A-Fa-f0-9]{6})$")
    private val COLOR_HEX_PATTERN_RGBA = Pattern.compile("^#([A-Fa-f0-9]{8})$")
    private const val HEX_COLOR_LENGTH_RGB = 7
    private const val HEX_COLOR_LENGTH_RGBA = 9

    private val isColorLineMarkerProviderEnabled: Boolean
      get() = LineMarkerSettings.getSettings().isEnabled(ColorLineMarkerProvider.INSTANCE)

    fun isTargetElement(element: PsiElement): Boolean = isTargetElement(element, element.containingFile)

    private fun isTargetElement(element: PsiElement, containingFile: PsiFile): Boolean {
      return element.toOptional()
        .filter { it is JsonStringLiteral}
        .map { it as JsonStringLiteral }
        .filter { isThemeFilename(containingFile.name)}
        .filter { isPropertyName(it) }
        .map { it.value }
        .map { isColorCode(it) || isNamedColor(it) }
        .orElse(false);
    }

    private fun isNamedColor(text: String): Boolean =
      StringUtil.isLatinAlphanumeric(text)

    private fun isColorCode(text: String?): Boolean {
      if (!StringUtil.startsWithChar(text, '#')) return false
      return if (text!!.length != HEX_COLOR_LENGTH_RGB && text.length != HEX_COLOR_LENGTH_RGBA) false else COLOR_HEX_PATTERN_RGB.matcher(
        text
      ).matches() || COLOR_HEX_PATTERN_RGBA.matcher(
        text
      ).matches()
    }
  }
}