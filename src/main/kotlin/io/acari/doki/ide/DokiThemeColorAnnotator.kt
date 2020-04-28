package io.acari.doki.ide

import com.intellij.codeInsight.daemon.LineMarkerSettings
import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.json.psi.impl.JsonPsiImplUtils.isPropertyName
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
import io.acari.doki.ide.DokiThemeJsonUtil.getNamedColors
import io.acari.doki.ide.DokiThemeJsonUtil.isThemeFilename
import io.acari.doki.util.toColor
import io.acari.doki.util.toOptional
import java.awt.Color
import java.util.*
import java.util.regex.Pattern
import javax.swing.Icon

class DokiThemeColorAnnotator : Annotator {

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (!isColorLineMarkerProviderEnabled || !isTargetElement(
        element,
        holder.currentAnnotationSession.file
      )
    ) return
    val annotation = holder.createInfoAnnotation(element, null)
    val literal = element as JsonStringLiteral
    annotation.gutterIconRenderer = ColorBoxRenderer(literal.value, literal)
  }

  private class ColorBoxRenderer internal constructor(
    private val myColorText: String,
    private var myLiteral: JsonStringLiteral
  ) : GutterIconRenderer() {

    override fun getIcon(): Icon =
      getColor(myColorText)
        ?.let { ColorIcon(ICON_SIZE, it) }
        ?: EmptyIcon.create(ICON_SIZE)

    override fun isNavigateAction(): Boolean = canChooseColor()

    override fun getTooltipText(): String? =
      if (canChooseColor()) "Choose Color"
      else null

    override fun getClickAction(): AnAction? =
      if (canChooseColor()) buildColorChooseAction()
      else null

    private fun buildColorChooseAction(): AnAction {
      return object : AnAction("Choose Color...") {
        override fun actionPerformed(e: AnActionEvent) {
          e.getData(CommonDataKeys.EDITOR).toOptional()
            .flatMap {
              getColor(myColorText).toOptional()
                .map { color -> color to it }
            }.ifPresent { colorAndEditor ->
              val (currentColor, editor) = colorAndEditor
              val withAlpha = isRgbaColorHex(myColorText)
              if (Registry.`is`("ide.new.color.picker")) {
                showColorPickerPopup(
                  e.project,
                  currentColor
                ) { c: Color?, _ -> applyColor(currentColor, withAlpha, c) }
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
        }

        private fun applyColor(
          currentColor: Color,
          withAlpha: Boolean,
          newColor: Color?
        ) {
          newColor.toOptional()
            .filter { it != currentColor }
            .ifPresent {
              val newColorHex = "#" + toHex(it, withAlpha)
              val project = myLiteral.project
              val newLiteral = JsonElementGenerator(project).createStringLiteral(newColorHex)
              writeCommandAction(project, myLiteral.containingFile)
                .run<RuntimeException> { myLiteral = myLiteral.replace(newLiteral) as JsonStringLiteral }
            }
        }
      }
    }

    private fun canChooseColor(): Boolean =
      isColorCode(myColorText)

    private fun getColor(colorText: String): Color? =
      if (!isColorCode(colorText)) findNamedColor(colorText)
      else parseColor(colorText)

    private fun findNamedColor(colorText: String): Color? =
      myLiteral.containingFile.toOptional()
        .map { it as JsonFile }
        .map { file ->
          val namedColors = getNamedColors(file)
          ContainerUtil.find(namedColors) { property ->
            property.name == colorText
          }?.value
        }
        .filter { it is JsonStringLiteral }
        .map { it as JsonStringLiteral }
        .map { parseColor(it.value) }
        .orElseGet { null }

    override fun equals(o: Any?): Boolean {
      if (this === o) return true
      if (o == null || javaClass != o.javaClass) return false
      val renderer =
        o as ColorBoxRenderer
      return myColorText == renderer.myColorText &&
        myLiteral == renderer.myLiteral
    }

    override fun hashCode(): Int =
      Objects.hash(myColorText, myLiteral)

    companion object {
      private const val ICON_SIZE = 10
      private fun parseColor(colorHex: String): Color? =
        colorHex.toOptional()
          .map { isRgbaColorHex(it) }
          .filter { it || isRgbColorHex(colorHex) }
          .map { isRgba ->
            try {
              val alpha = if (isRgba) colorHex.substring(HEX_COLOR_LENGTH_RGB) else null
              val colorHexWithoutAlpha = if (isRgba) colorHex.substring(0, HEX_COLOR_LENGTH_RGB)
              else colorHex
              val color = colorHexWithoutAlpha.toColor()
              if (isRgba) toAlpha(color, alpha?.toInt(16) ?: 1)
              else color
            } catch (t: Throwable) {
              null
            }
          }
          .orElseGet { null }

      private fun isRgbaColorHex(colorHex: String): Boolean =
        colorHex.length == HEX_COLOR_LENGTH_RGBA

      private fun isRgbColorHex(colorHex: String): Boolean =
        colorHex.length == HEX_COLOR_LENGTH_RGB
    }
  }

  companion object {
    private val COLOR_HEX_PATTERN_RGB = Pattern.compile("^#([A-Fa-f0-9]{6})$")
    private val COLOR_HEX_PATTERN_RGBA = Pattern.compile("^#([A-Fa-f0-9]{8})$")
    private const val HEX_COLOR_LENGTH_RGB = 7
    private const val HEX_COLOR_LENGTH_RGBA = 9

    private val isColorLineMarkerProviderEnabled: Boolean
      get() = LineMarkerSettings.getSettings().isEnabled(ColorLineMarkerProvider.INSTANCE)

    private fun isTargetElement(element: PsiElement, containingFile: PsiFile): Boolean {
      return element.toOptional()
        .filter { it is JsonStringLiteral }
        .map { it as JsonStringLiteral }
        .filter { isThemeFilename(containingFile.name) }
        .filter { !isPropertyName(it) }
        .map { it.value }
        .map { isColorCode(it) || isNamedColor(it) }
        .orElse(false)
    }

    private fun isNamedColor(text: String): Boolean =
      StringUtil.isLatinAlphanumeric(text)

    private fun isColorCode(text: String?): Boolean {
      if (!StringUtil.startsWithChar(text, '#')) return false
      return if (text!!.length != HEX_COLOR_LENGTH_RGB &&
        text.length != HEX_COLOR_LENGTH_RGBA
      ) false
      else COLOR_HEX_PATTERN_RGB.matcher(
        text
      ).matches() || COLOR_HEX_PATTERN_RGBA.matcher(
        text
      ).matches()
    }
  }
}