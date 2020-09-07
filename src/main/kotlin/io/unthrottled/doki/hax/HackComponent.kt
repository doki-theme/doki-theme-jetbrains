package io.unthrottled.doki.hax

import com.intellij.codeInsight.actions.DirectoryFormattingOptions
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.codeInsight.hint.TooltipRenderer
import com.intellij.execution.runners.ProcessProxy
import com.intellij.ide.IdeTooltipManager
import com.intellij.ide.actions.Switcher
import com.intellij.ide.plugins.newui.PluginLogo
import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.ide.util.TipPanel
import com.intellij.ide.util.gotoByName.CustomMatcherModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.impl.ActionMenu
import com.intellij.openapi.fileEditor.impl.EditorComposite
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.progress.util.ColorProgressBar
import com.intellij.openapi.wm.impl.TitleInfoProvider
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.messages.JBMacMessages
import com.intellij.ui.popup.util.MasterDetailPopupBuilder
import com.intellij.util.ui.UIUtil
import com.intellij.xdebugger.impl.ui.XDebuggerUIConstants
import io.unthrottled.doki.hax.FeildHacker.setFinalStatic
import io.unthrottled.doki.stickers.impl.DOKI_BACKGROUND_PROP
import io.unthrottled.doki.stickers.impl.DOKI_STICKER_PROP
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr
import java.awt.Color

object HackComponent : Disposable {
  init {
    enableDisposableStickers()
    createMonikasWritingTipOfTheDay()
    enablePluginWindowConsistency()
    enableBorderConsistency()
    enableSearchEverywhereConsistency()
    enableAccentConsistency()
    enableBackgroundConsistency()
    enableSelectionConsistency()
  }

  private fun enableSelectionConsistency() {
    hackWelcomeScreen()
  }

  private fun enableBackgroundConsistency() {
    hackParameterInfoBackground()
    hackSheetWindow()
    hackToolWindowDecorator()
    hackLivePreview()
  }

  private fun hackLivePreview() {
    try {
      val naughtySelectionColor = IdeTooltipManager::class.java.getDeclaredField("GRAPHITE_COLOR")
      val namedColor = JBColor.namedColor("TextPane.background", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }

    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.find.impl.livePreview.SearchResults")))
      val ctClass = cp.get("com.intellij.find.impl.livePreview.ReplacementView")
      val init = ctClass.declaredConstructors[0]
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "setForeground") {
            e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getLabelForeground(); \$_ = \$proceed(\$\$);}")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackWelcomeScreen() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.openapi.wm.impl.welcomeScreen.EditProjectGroupAction")))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame")
      val init = ctClass.getDeclaredMethods("getActionLinkSelectionColor")[0]
      init.insertAfter("\$_ = com.intellij.ui.JBColor.namedColor(\"List.selectionBackground\", java.awt.Color.GREEN);")
      val createLink = ctClass.getDeclaredMethod("getLinkNormalColor")
      createLink.instrument(object : ExprEditor() {
        override fun edit(e: NewExpr?) {
          if (e?.className == "com.intellij.ui.JBColor") {
            e.replace("{ \$_ = com.intellij.ui.JBColor.namedColor(\"Label.foreground\", com.intellij.ui.JBColor.BLACK); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackBookmarkBorder() {
    try {
      val naughtySelectionColor = MasterDetailPopupBuilder::class.java.getDeclaredField("BORDER_COLOR")
      val namedColor = JBColor.namedColor("Borders.color", Gray._135)
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackSheetWindow() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(JBMacMessages::class.java))
      val ctClass = cp.get("com.intellij.ui.messages.SheetController")
      ctClass.declaredClasses
        .filter { it.declaredMethods.any { m -> m.name == "paintComponent" } }
        .forEach { classDude ->
          classDude.getDeclaredMethods("paintComponent").forEach {
            it.instrument(object : ExprEditor() {
              override fun edit(e: NewExpr?) {
                if (e?.className == JBColor::class.java.name) {
                  e?.replace("{ \$_ = com.intellij.util.ui.UIUtil.getPanelBackground(); }")
                }
              }

              override fun edit(e: MethodCall?) {
                if (e?.methodName == "isUnderDarcula") {
                  e.replace("{ \$_ = true; }")
                }
              }
            })
          }
          classDude.toClass()
        }
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackParameterInfoBackground() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(HintUtil::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.hint.ParameterInfoComponent\$OneElementComponent")
      val hackBackground: (CtMethod) -> Unit = {
        it.instrument(object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "setBackground") {
              e.replace("{ \$1 = com.intellij.ui.JBColor.namedColor(\"ParameterInfo.background\", com.intellij.codeInsight.hint.HintUtil.getInformationColor()); \$_ = \$proceed(\$\$); }")
            }
          }
        })
      }
      ctClass.getDeclaredMethods("setup").forEach(hackBackground)
      ctClass.toClass()
      val bitchassClass = cp.get("com.intellij.codeInsight.hint.ParameterInfoComponent\$OneLineComponent")
      bitchassClass.getDeclaredMethods("doSetup").forEach(hackBackground)
      bitchassClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun enableAccentConsistency() {
    hackLiveIndicator()
    hackSearchHighlightBorder()
  }

  private fun hackSearchHighlightBorder() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.openapi.options.ex.ConfigurableWrapper")))
      val ctClass = cp.get("com.intellij.openapi.options.ex.GlassPanel")
      val init = ctClass.getDeclaredMethods(
        "paintSpotlight"
      )[0]
      var colors = 0
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "setColor") {
            colors++
            if (colors > 1) {
              e.replace("{ \$1 = com.intellij.ui.JBColor.namedColor(\"Doki.Accent.color\", com.intellij.ui.JBColor.ORANGE);  \$_ = \$proceed(\$\$); }")
            }
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackLiveIndicator() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ProcessProxy::class.java))
      val ctClass = cp.get("com.intellij.execution.runners.ExecutionUtil")
      val init = ctClass.getDeclaredMethods(
        "getLiveIndicator"
      )[1]
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "getIndicator") {
            e.replace("{ \$4 = com.intellij.ui.JBColor.namedColor(\"Doki.Accent.color\", java.awt.Color.GREEN); \$_ = \$proceed(\$\$); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun enableSearchEverywhereConsistency() {
    hackActionModel()
  }

  private fun hackActionModel() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(CustomMatcherModel::class.java))
      val ctClass = cp.get("com.intellij.ide.util.gotoByName.GotoActionModel\$GotoActionListCellRenderer")
      val init = ctClass.getDeclaredMethods(
        "getListCellRendererComponent"
      )[0]
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "isUnderDarcula") { // dis for OptionDescription
            e.replace("{ \$_ = true; }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackToolWindowDecorator() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(TitleInfoProvider::class.java))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.ToolWindowHeader")
      val init = ctClass.getDeclaredMethods(
        "paintChildren"
      )[0]
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "isUnderDarcula") { // dis for OptionDescription
            e.replace("{ \$_ = true; }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun hackLAF() {
    enableSwitcherLafConsistency()
    enableLafBorderConsistency()
    enableForegroundConsistency()
  }

  private fun enableLafBorderConsistency() {
    hackTipBorder()
    hackPopupBorder()
  }

  private fun hackPopupBorder() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(TooltipRenderer::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.hint.HintManagerImpl")
      val init = ctClass.getDeclaredMethods("createHintHint")[1]
      init.instrument(object : ExprEditor() {
        override fun edit(e: NewExpr?) {
          if (e?.className == "com.intellij.ui.JBColor") {
            e.replace("{ \$_ = com.intellij.util.ui.UIUtil.getBorderSeparatorColor(); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }

    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(DirectoryFormattingOptions::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.actions.FileInEditorProcessor\$FormattedMessageBuilder")
      val init = ctClass.getDeclaredMethod("getMessage")
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "toHex") {
            e.replace("{ \$_ = com.intellij.ui.ColorUtil.toHex(com.intellij.util.ui.UIUtil.getContextHelpForeground()); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackTipBorder() {
    try {
      val naughtySelectionColor = TipPanel::class.java.getDeclaredField("DIVIDER_COLOR")
      val namedColor = JBColor.namedColor("Borders.color", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun enableForegroundConsistency() {
    hackColors()
    hackSdkComboBox()
    hackDebuggerAttributes()
    hackSwitcher()
    hackFindInPath()
    hackTitleFrame()
    hackTestResults()
  }

  private fun hackTestResults() {
    val switcherClass = ColorProgressBar::class.java
    try {
      val naughtySelectionColor = switcherClass.getDeclaredField("YELLOW")
      val namedColor = JBColor.namedColor("ColorProgress.bar.yellow", JBColor(Color(0xa67a21), Color(0x91703a)))
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackTitleFrame() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.openapi.wm.impl.welcomeScreen.CardActionsPanel")))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame\$FlatWelcomeScreen")
      val init = ctClass.getDeclaredMethod(
        "createLogo"
      )
      var setForegrounds = 0
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "setForeground") {
            setForegrounds++
            if (setForegrounds > 1) {
              e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getContextHelpForeground();  \$_ = \$proceed(\$\$); }")
            }
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackColors() {
    try {
      val naughtySelectionColor = JBColor::class.java.getDeclaredField("GRAY")
      val namedColor = JBColor.namedColor("Label.infoForeground", Gray._128)
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackSdkComboBox() {
    try {
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("SIMPLE_CELL_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getLabelForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackSwitcher() {
    try {
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("GRAY_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackDebuggerAttributes() {
    try {
      val naughtySelectionColor = XDebuggerUIConstants::class.java.getDeclaredField("TYPE_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
    try {
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("REGULAR_ITALIC_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, UIUtil.getLabelForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
    try {
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("GRAY_ITALIC_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun enableSwitcherLafConsistency() {
    hackSwitcherSelection()
  }

  private fun hackSwitcherSelection() {
    val switcherClass = Switcher::class.java
    try {
      val naughtySelectionColor = switcherClass.getDeclaredField("ON_MOUSE_OVER_BG_COLOR")
      val namedColor = JBColor.namedColor("List.selectionBackground", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun enableBorderConsistency() {
    hackSwitcherBorder()
    hackEditorBorder()
    hackBookmarkBorder()
  }

  private fun hackEditorBorder() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(EditorHistoryManager::class.java))
      val ctClass = cp.get("com.intellij.openapi.fileEditor.impl.EditorsSplitters")
      val init = ctClass.getDeclaredMethods(
        "paintComponent"
      )[0]
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "isUnderDarcula") {
            e.replace("{ \$_ = true; }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackSwitcherBorder() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Switcher::class.java))
      val ctClass = cp.get("com.intellij.ide.actions.SwitcherToolWindowsListRenderer")
      val init = ctClass.getDeclaredMethod("customizeCellRenderer")
      init.instrument(object : ExprEditor() {
        override fun edit(e: NewExpr?) {
          if (e?.className == "com.intellij.ui.JBColor") {
            e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun hackFindInPath() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ActionMenu::class.java))
      val ctClass = cp.get("com.intellij.openapi.actionSystem.impl.ActionToolbarImpl")
      val init = ctClass.getDeclaredMethod("tweakActionComponentUI")
      init.instrument(object : ExprEditor() {
        override fun edit(e: MethodCall?) {
          if (e?.methodName == "dimmer") {
            e.replace("{ \$_ = com.intellij.util.ui.UIUtil.getLabelTextForeground(); }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun enablePluginWindowConsistency() {
    hackPluginWindow()
  }

  private fun hackPluginWindow() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(PluginLogo::class.java))
      val ctClass = cp.get("com.intellij.ide.plugins.newui.PluginDetailsPageComponent")
      val init = ctClass.getDeclaredMethod("createPluginPanel")
      init.instrument(object : ExprEditor() {
        override fun edit(e: NewExpr?) {
          if (e?.className == "com.intellij.ui.JBColor") {
            e.replace("{ \$_ = com.intellij.ide.plugins.PluginManagerConfigurable.SEARCH_FIELD_BORDER_COLOR; }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun createMonikasWritingTipOfTheDay() {
    hackTipDialog()
    hackTipPanel()
  }

  private fun hackTipDialog() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ExportToFileUtil::class.java))
      val ctClass = cp.get("com.intellij.ide.util.TipDialog")
      hackTip(ctClass)
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackTip(ctClass: CtClass) {
    try {
      val declaredConstructor = ctClass.constructors
      declaredConstructor.forEach {
        it.insertAfter("this.setTitle(\"Monika's Writing Tip of the Day\");")
      }
      ctClass.toClass()
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun hackTipPanel() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ChooseElementsDialog::class.java))
      val ctClass = cp.get("com.intellij.ide.util.TipPanel")
      val init = ctClass.getDeclaredMethod("getDoNotShowMessage")
      init.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "message") {
            m.replace("{ \$_ = \"Show \\\"Monika's Writing Tip of the Day\\\" on Startup!\"; }")
          }
        }
      })
      ctClass.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun enableDisposableStickers() {
    hackBackgroundPaintingComponent()
  }

  /**
   * Enables the ability to use the editor property
   * but also allows prevents the stickers from staying after installation.
   *
   *
   * Enables the ability to use the frame property
   * but also allows prevents the background image from staying after installation.
   */
  private fun hackBackgroundPaintingComponent() {
    try {
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(EditorComposite::class.java))
      val ctClass2 = cp.get("com.intellij.openapi.wm.impl.IdeBackgroundUtil")

      // enable themed startup
      val backgroundMethod = ctClass2.getDeclaredMethod("getIdeBackgroundColor")
      backgroundMethod.instrument(object : ExprEditor() {
        override fun edit(e: NewExpr?) {
          e?.replace("{ \$_ = com.intellij.util.ui.UIUtil.getPanelBackground(); }")
        }
      })

      // enable disposable stickers
      val method = ctClass2.getDeclaredMethod("withFrameBackground")
      method.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "withNamedPainters") {
            m.replace("{ \$2 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
          }
        }
      })
      val editorBackgroundMethod = ctClass2.getDeclaredMethod("withEditorBackground")
      editorBackgroundMethod.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "withNamedPainters") {
            m.replace("{ \$2 = \"$DOKI_STICKER_PROP\"; \$_ = \$proceed(\$\$); }")
          }
        }
      })

      val isEditorBackgroundImageSetMethod = ctClass2.getDeclaredMethod("isEditorBackgroundImageSet")
      isEditorBackgroundImageSetMethod.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "getBackgroundSpec") {
            m.replace("{ \$2 = \"$DOKI_STICKER_PROP\"; \$_ = \$proceed(\$\$); }")
          }
        }
      })

      val initEditorPaintersMethod = ctClass2.getDeclaredMethod("initEditorPainters")
      initEditorPaintersMethod.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "initWallpaperPainter") {
            m.replace("{ \$1 = \"$DOKI_STICKER_PROP\"; \$_ = \$proceed(\$\$); }")
          } else if (m.methodName == "getNamedPainters") {
            m.replace("{ \$1 = \"$DOKI_STICKER_PROP\"; \$_ = \$proceed(\$\$); }")
          }
        }
      })

      val initFramePaintersMethod = ctClass2.getDeclaredMethod("initFramePainters")
      initFramePaintersMethod.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "initWallpaperPainter") {
            m.replace("{ \$1 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
          } else if (m.methodName == "getNamedPainters") {
            m.replace("{ \$1 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
          }
        }
      })

      ctClass2.toClass()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun dispose() {
  }
}