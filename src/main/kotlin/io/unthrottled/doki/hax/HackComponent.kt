package io.unthrottled.doki.hax

import com.intellij.codeInsight.actions.DirectoryFormattingOptions
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.codeInsight.hint.TooltipRenderer
import com.intellij.execution.runners.ProcessProxy
import com.intellij.ide.IdeTooltipManager
import com.intellij.ide.actions.Switcher
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.ide.plugins.newui.PluginLogo
import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.ide.util.TipPanel
import com.intellij.ide.util.gotoByName.CustomMatcherModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.impl.ActionMenu
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.impl.EditorComposite
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.progress.util.ColorProgressBar
import com.intellij.openapi.vcs.ex.DocumentTracker
import com.intellij.openapi.vcs.ex.LineStatusMarkerRenderer
import com.intellij.openapi.wm.impl.AnchoredButton
import com.intellij.openapi.wm.impl.TitleInfoProvider
import com.intellij.ui.CaptionPanel
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.messages.JBMacMessages
import com.intellij.ui.popup.util.MasterDetailPopupBuilder
import com.intellij.util.ui.UIUtil
import com.intellij.xdebugger.impl.ui.XDebuggerUIConstants
import com.intellij.xdebugger.memory.ui.ClassesTable
import io.unthrottled.doki.hax.FieldHacker.setFinalStatic
import io.unthrottled.doki.stickers.DOKI_BACKGROUND_PROP
import io.unthrottled.doki.ui.TitlePaneUI.Companion.LOL_NOPE
import io.unthrottled.doki.util.runSafely
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr
import java.awt.Color
import javax.swing.JDialog

@Suppress("TooManyFunctions")
object HackComponent : Disposable {
  private val log = Logger.getInstance(javaClass)

  init {
    enableDisposableBackground()
    createMonikasWritingTipOfTheDay()
    enablePluginWindowConsistency()
    enableBorderConsistency()
    enableSearchEverywhereConsistency()
    enableAccentConsistency()
    enableBackgroundConsistency()
    enableSelectionConsistency()
    enableTitlePaneConsistency()
    enableHoverConsistency()
    enableHintConsistency()
  }

  private fun enableHintConsistency() {
    hackReformatHintInfoForeground()
  }

  private fun hackReformatHintInfoForeground() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.codeInsight.actions.DirectoryFormattingOptions")))
      val ctClass = cp.get("com.intellij.codeInsight.actions.FileInEditorProcessor\$FormattedMessageBuilder")
      val init = ctClass.getDeclaredMethod(
        "getMessage"
      )
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "toHtmlColor") {
              e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getContextHelpForeground();  \$_ = \$proceed(\$\$); }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackReformatHintInfoForeground for reasons")
    }
  }

  private fun enableTitlePaneConsistency() {
    hackSheetMessage()
  }

  private fun enableSelectionConsistency() {
    hackWelcomeScreen()
  }

  private fun enableHoverConsistency() {
    hackDebuggerTable()
  }

  private fun hackDebuggerTable() {
    runSafely({
      val naughtySelectionColor = ClassesTable::class.java.getDeclaredField("CLICKABLE_COLOR")
      val namedColor = JBColor.namedColor(
        "Table.hoverBackground",
        JBColor(Color(250, 251, 252), Color(62, 66, 69))
      )
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackLivePreview for reasons.")
    }
  }

  private fun enableBackgroundConsistency() {
    hackParameterInfoBackground()
    hackSheetWindow()
    hackToolWindowDecorator()
    hackLivePreview()
    hackEmptyTextPanel()
  }

  private fun hackLivePreview() {
    runSafely({
      val naughtySelectionColor = IdeTooltipManager::class.java.getDeclaredField("GRAPHITE_COLOR")
      val namedColor = JBColor.namedColor("TextPane.background", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackLivePreview for reasons.")
    }

    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.find.impl.livePreview.SearchResults")))
      val ctClass = cp.get("com.intellij.find.impl.livePreview.ReplacementView")
      val init = ctClass.declaredConstructors[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "setForeground") {
              e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getLabelForeground(); \$_ = \$proceed(\$\$);}")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackLivePreview for reasons.")
    }
  }

  private fun hackWelcomeScreen() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(
        ClassClassPath(
          Class.forName("com.intellij.openapi.wm.impl.welcomeScreen.EditProjectGroupAction")
        )
      )
      val ctClass = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame")
      val init = ctClass.getDeclaredMethods("getActionLinkSelectionColor")[0]
      init.insertAfter(
        "\$_ = com.intellij.ui.JBColor.namedColor(\"List.selectionBackground\", java.awt.Color.GREEN);"
      )
      val createLink = ctClass.getDeclaredMethod("getLinkNormalColor")
      createLink.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace(
                "{ \$_ = com.intellij.ui.JBColor.namedColor(\"Label.foreground\", com.intellij.ui.JBColor.BLACK); }"
              )
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackWelcomeScreen for reasons.")
    }
  }

  private fun hackBookmarkBorder() {
    runSafely({
      val naughtySelectionColor = MasterDetailPopupBuilder::class.java.getDeclaredField("BORDER_COLOR")
      val namedColor = JBColor.namedColor("Borders.color", Gray._135)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackBookMarkBorder  for reasons.")
    }
  }

  private fun hackSheetMessage() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(JBMacMessages::class.java))
      val ctClass = cp.get("com.intellij.ui.messages.SheetMessage")
      ctClass.declaredConstructors
        .forEach { constructorDude ->
          constructorDude.instrument(
            object : ExprEditor() {
              override fun edit(e: NewExpr?) {
                if (e?.className == JDialog::class.java.name) {
                  e?.replace("{ \$2 = \"$LOL_NOPE\"; \$_ = \$proceed(\$\$); }")
                }
              }
            }
          )
        }
      ctClass.toClass()
    }) {
      log.warn("Unable to hackSheetMessage for reasons.")
    }
  }

  private fun hackSheetWindow() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(JBMacMessages::class.java))
      val ctClass = cp.get("com.intellij.ui.messages.SheetController")
      ctClass.declaredClasses
        .filter { it.declaredMethods.any { m -> m.name == "paintComponent" } }
        .forEach { classDude ->
          classDude.getDeclaredMethods("paintComponent").forEach {
            it.instrument(
              object : ExprEditor() {
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
              }
            )
          }
          classDude.toClass()
        }
      ctClass.toClass()
    }) {
      log.warn("Unable to hackSheetWindow for reasons.")
    }
  }

  private fun hackParameterInfoBackground() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(HintUtil::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.hint.ParameterInfoComponent\$OneElementComponent")
      val hackBackground: (CtMethod) -> Unit = {
        it.instrument(
          object : ExprEditor() {
            override fun edit(e: MethodCall?) {
              if (e?.methodName == "setBackground") {
                e.replace(
                  "{ \$1 = com.intellij.ui.JBColor.namedColor(\"ParameterInfo.background\", " +
                    "com.intellij.codeInsight.hint.HintUtil.getInformationColor()); \$_ = \$proceed(\$\$); }"
                )
              }
            }
          }
        )
      }
      ctClass.getDeclaredMethods("setup").forEach(hackBackground)
      ctClass.toClass()
      val bitchassClass = cp.get("com.intellij.codeInsight.hint.ParameterInfoComponent\$OneLineComponent")
      bitchassClass.getDeclaredMethods("doSetup").forEach(hackBackground)
      bitchassClass.toClass()
    }) {
      log.warn("Unable to hackParameterInfoBackground for reasons.")
    }
  }

  private fun enableAccentConsistency() {
    hackLiveIndicator()
  }

  private fun hackLiveIndicator() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ProcessProxy::class.java))
      val ctClass = cp.get("com.intellij.execution.runners.ExecutionUtil")
      val init = ctClass.getDeclaredMethods(
        "getLiveIndicator"
      )[1]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "getIndicator") {
              e.replace(
                "{ \$4 = com.intellij.ui.JBColor.namedColor(\"Doki.Accent.color\", " +
                  "java.awt.Color.GREEN); \$_ = \$proceed(\$\$); }"
              )
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackLiveIndicator for reasons.")
    }
  }

  private fun enableSearchEverywhereConsistency() {
    hackActionModel()
  }

  private fun hackActionModel() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(CustomMatcherModel::class.java))
      val ctClass = cp.get("com.intellij.ide.util.gotoByName.GotoActionModel\$GotoActionListCellRenderer")
      val init = ctClass.getDeclaredMethods(
        "getListCellRendererComponent"
      )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "isUnderDarcula") { // dis for OptionDescription
              e.replace("{ \$_ = true; }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackActionModel for reasons.")
    }
  }

  private fun hackToolWindowDecorator() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(TitleInfoProvider::class.java))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.ToolWindowHeader")
      val init = ctClass.getDeclaredMethods(
        "paintChildren"
      )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "isUnderDarcula") { // dis for OptionDescription
              e.replace("{ \$_ = true; }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackToolWindowDecorator for reasons.")
    }
  }

  private fun hackEmptyTextPanel() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(AnchoredButton::class.java))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.InternalDecoratorImpl")
      ctClass.constructors.forEach {
        ctConstructor ->
        ctConstructor.insertAfter("this.setBackground(com.intellij.util.ui.UIUtil.getPanelBackground());")
      }
      ctClass.toClass()
    }) {
      log.warn("Unable to hackEmptyTextPanel for reasons.")
    }
  }

  fun hackLAF() {
    enableSwitcherLafConsistency()
    enableLafBorderConsistency()
    enableForegroundConsistency()
  }

  private fun enableLafBorderConsistency() {
    hackTipBorder()
    hackCaptionPanel()
    hackPopupBorder()
    hackLineStatusColor()
  }

  private fun hackLineStatusColor() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(LineStatusMarkerRenderer::class.java))
      val bitchassClass = cp.get("com.intellij.openapi.vcs.ex.LineStatusMarkerPopupRenderer\$PopupPanel")
      bitchassClass.declaredConstructors.forEach { ctConstructor ->
        ctConstructor.instrument(
          object : ExprEditor() {
            override fun edit(e: NewExpr?) {
              if (e?.className == "com.intellij.ui.JBColor") {
                e.replace("{ \$_ = com.intellij.util.ui.UIUtil.getBorderSeparatorColor(); }")
              }
            }
          }
        )
      }
      bitchassClass.toClass()
    }) {
      log.warn("Unable to hackStatusLineColor for reasons.")
    }
  }

  private fun hackPopupBorder() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(TooltipRenderer::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.hint.HintManagerImpl")
      val init = ctClass.getDeclaredMethods("createHintHint")[1]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace(
                "{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme." +
                  "CustomFrameDecorations.separatorForeground(); }"
              )
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackPopupBorder for reasons.")
    }

    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(DirectoryFormattingOptions::class.java))
      val ctClass = cp.get("com.intellij.codeInsight.actions.FileInEditorProcessor\$FormattedMessageBuilder")
      val init = ctClass.getDeclaredMethod("getMessage")
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "toHex") {
              e.replace(
                "{ \$_ = com.intellij.ui.ColorUtil.toHex(" +
                  "com.intellij.util.ui.UIUtil.getContextHelpForeground()); }"
              )
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackPopupBorder for reasons.")
    }
  }

  private fun hackTipBorder() {
    runSafely({
      val naughtySelectionColor = TipPanel::class.java.getDeclaredField("DIVIDER_COLOR")
      val namedColor = JBColor.namedColor("Borders.color", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackTipBorder for reasons.")
    }
  }

  private fun hackCaptionPanel() {
    runSafely({
      val naughtySelectionColor = CaptionPanel::class.java.getDeclaredField("CNT_ACTIVE_BORDER_COLOR")
      val namedColor = JBColor.namedColor("Borders.color", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackCaptionPanel for reasons.")
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
    runSafely({
      val naughtySelectionColor = switcherClass.getDeclaredField("YELLOW")
      val namedColor = JBColor.namedColor("ColorProgress.bar.yellow", JBColor(Color(0xa67a21), Color(0x91703a)))
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackTestResults for reasons.")
    }
  }

  private fun hackTitleFrame() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.openapi.wm.impl.welcomeScreen.CardActionsPanel")))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame\$FlatWelcomeScreen")
      val init = ctClass.getDeclaredMethod(
        "createLogo"
      )
      var setForegrounds = 0
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "setForeground") {
              setForegrounds++
              if (setForegrounds > 1) {
                e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getContextHelpForeground();  \$_ = \$proceed(\$\$); }")
              }
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackTitleFrame for reasons.")
    }
  }

  private fun hackColors() {
    runSafely({
      val naughtySelectionColor = JBColor::class.java.getDeclaredField("GRAY")
      val namedColor = JBColor.namedColor("Label.infoForeground", Gray._128)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackColors for reasons.")
    }
  }

  private fun hackSdkComboBox() {
    runSafely({
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("SIMPLE_CELL_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getLabelForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackSdkComboBox for reasons.")
    }
  }

  private fun hackSwitcher() {
    runSafely({
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("GRAY_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackSwitcher for reasons.")
    }
  }

  private fun hackDebuggerAttributes() {
    runSafely({
      val naughtySelectionColor = XDebuggerUIConstants::class.java.getDeclaredField("TYPE_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackDebuggerAttributes for reasons.")
    }
    runSafely({
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("REGULAR_ITALIC_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, UIUtil.getLabelForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackDebuggerAttributes for reasons.")
    }
    runSafely({
      val naughtySelectionColor = SimpleTextAttributes::class.java.getDeclaredField("GRAY_ITALIC_ATTRIBUTES")
      val namedColor = SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, UIUtil.getContextHelpForeground())
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackDebuggerAttributes for reasons.")
    }
  }

  private fun enableSwitcherLafConsistency() {
    hackSwitcherSelection()
  }

  private fun hackSwitcherSelection() {
    val switcherClass = Switcher::class.java
    runSafely({
      val naughtySelectionColor = switcherClass.getDeclaredField("ON_MOUSE_OVER_BG_COLOR")
      val namedColor = JBColor.namedColor("List.selectionBackground", 0xf2f2f2)
      setFinalStatic(naughtySelectionColor, namedColor)
    }) {
      log.warn("Unable to hackSwitcherSelection for reasons.")
    }
  }

  private fun enableBorderConsistency() {
    hackSwitcherBorder()
    hackEditorBorder()
    hackBookmarkBorder()
    hackRunAnything()
    hackLineStatusMarkerBorder()
  }

  private fun hackEditorBorder() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(EditorHistoryManager::class.java))
      val ctClass = cp.get("com.intellij.openapi.fileEditor.impl.EditorsSplitters")
      val init = ctClass.getDeclaredMethods(
        "paintComponent"
      )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "isUnderDarcula") {
              e.replace("{ \$_ = true; }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackEditorBorder for reasons.")
    }
  }

  private fun hackRunAnything() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(RunAnythingAction::class.java))
      val ctClass = cp.get("com.intellij.ide.actions.runAnything.RunAnythingUtil")
      val init = ctClass.getDeclaredMethods(
        "createTitle"
      )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackRunAnything for reasons.")
    }
  }

  private fun hackLineStatusMarkerBorder() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(DocumentTracker::class.java))
      val ctClass = cp.get("com.intellij.openapi.vcs.ex.LineStatusMarkerPopupPanel")
      val init = ctClass.getDeclaredMethods(
        "getBorderColor"
      )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackRunAnything for reasons.")
    }
  }

  private fun hackSwitcherBorder() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Switcher::class.java))
      val ctClass = cp.get("com.intellij.ide.actions.SwitcherToolWindowsListRenderer")
      val init = ctClass.getDeclaredMethod("customizeCellRenderer")
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackSwitcherBorder for reasons.")
    }
  }

  private fun hackFindInPath() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ActionMenu::class.java))
      val ctClass = cp.get("com.intellij.openapi.actionSystem.impl.ActionToolbarImpl")
      val init = ctClass.getDeclaredMethod("tweakActionComponentUI")
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "dimmer") {
              e.replace("{ \$_ = com.intellij.util.ui.UIUtil.getLabelTextForeground(); }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackFindInPath for reasons.")
    }
  }

  private fun enablePluginWindowConsistency() {
    hackPluginWindow()
  }

  private fun hackPluginWindow() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(PluginLogo::class.java))
      val ctClass = cp.get("com.intellij.ide.plugins.newui.PluginDetailsPageComponent")
      val init = ctClass.getDeclaredMethod("createPluginPanel")
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.ide.plugins.PluginManagerConfigurable.SEARCH_FIELD_BORDER_COLOR; }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackPluginWindow for reasons.")
    }
  }

  private fun createMonikasWritingTipOfTheDay() {
    hackTipDialog()
    hackTipPanel()
  }

  private fun hackTipDialog() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ExportToFileUtil::class.java))
      val ctClass = cp.get("com.intellij.ide.util.TipDialog")
      hackTip(ctClass)
    }) {
      log.warn("Unable to hackTipDialog for reasons.")
    }
  }

  private fun hackTip(ctClass: CtClass) {
    runSafely({
      val declaredConstructor = ctClass.constructors
      declaredConstructor.forEach {
        it.insertAfter("this.setTitle(\"Monika's Writing Tip of the Day\");")
      }
      ctClass.toClass()
    }) {
      log.warn("Unable to hackTip for reasons.")
    }
  }

  private fun hackTipPanel() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(ChooseElementsDialog::class.java))
      val ctClass = cp.get("com.intellij.ide.util.TipPanel")
      val init = ctClass.getDeclaredMethod("getDoNotShowMessage")
      init.instrument(
        object : ExprEditor() {
          @Throws(CannotCompileException::class)
          override fun edit(m: MethodCall?) {
            if (m!!.methodName == "message") {
              m.replace("{ \$_ = \"Show \\\"Monika's Writing Tip of the Day\\\" on Startup!\"; }")
            }
          }
        }
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackTipPanel for reasons.")
    }
  }

  private fun enableDisposableBackground() {
    hackBackgroundPaintingComponent()
  }

  /**
   * Enables the ability to use the frame property
   * but also allows prevents the background image from staying after plugin removal.
   */
  private fun hackBackgroundPaintingComponent() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(EditorComposite::class.java))
      val ctClass2 = cp.get("com.intellij.openapi.wm.impl.IdeBackgroundUtil")

      // enable themed startup
      val backgroundMethod = ctClass2.getDeclaredMethod("getIdeBackgroundColor")
      backgroundMethod.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            e?.replace("{ \$_ = com.intellij.util.ui.UIUtil.getPanelBackground(); }")
          }
        }
      )

      // enable disposable stickers
      val method = ctClass2.getDeclaredMethod("withFrameBackground")
      method.instrument(
        object : ExprEditor() {
          @Throws(CannotCompileException::class)
          override fun edit(m: MethodCall?) {
            if (m!!.methodName == "withNamedPainters") {
              m.replace("{ \$2 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
            }
          }
        }
      )

      val initFramePaintersMethod = ctClass2.getDeclaredMethod("initFramePainters")
      initFramePaintersMethod.instrument(
        object : ExprEditor() {
          @Throws(CannotCompileException::class)
          override fun edit(m: MethodCall?) {
            if (m!!.methodName == "initWallpaperPainter") {
              m.replace("{ \$1 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
            } else if (m.methodName == "getNamedPainters") {
              m.replace("{ \$1 = \"$DOKI_BACKGROUND_PROP\"; \$_ = \$proceed(\$\$); }")
            }
          }
        }
      )

      ctClass2.toClass()
    }) {
      log.warn("Unable to hackBackgroundPaintingComponent for reasons.")
    }
  }

  override fun dispose() {
  }
}
