package io.unthrottled.doki.hax

import com.intellij.codeInsight.hint.HintUtil
import com.intellij.codeInsight.hint.TooltipRenderer
import com.intellij.execution.runners.ProcessProxy
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.ide.plugins.newui.PluginLogo
import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.ide.util.gotoByName.CustomMatcherModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.impl.EditorComposite
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.vcs.ex.DocumentTracker
import io.unthrottled.doki.stickers.DOKI_BACKGROUND_PROP
import io.unthrottled.doki.util.runSafely
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr

@Suppress("TooManyFunctions", "LargeClass")
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
    enableHintConsistency()
  }

  fun init() {
  }

  private fun enableHintConsistency() {
    hackReformatHintInfoForeground()
  }

  private fun hackReformatHintInfoForeground() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(Class.forName("com.intellij.codeInsight.actions.DirectoryFormattingOptions")))
      val ctClass = cp.get("com.intellij.codeInsight.actions.FileInEditorProcessor\$FormattedMessageBuilder")
      val init =
        ctClass.getDeclaredMethod(
          "getMessage",
        )
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "toHtmlColor") {
              e.replace("{ \$1 = com.intellij.util.ui.UIUtil.getContextHelpForeground();  \$_ = \$proceed(\$\$); }")
            }
          }
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackReformatHintInfoForeground for reasons")
    }
  }

  private fun enableSelectionConsistency() {
    hackSearchHighlight()
  }

  private fun enableBackgroundConsistency() {
    hackParameterInfoBackground()
    hackLivePreview()
  }

  private fun hackLivePreview() {
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
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackLivePreview for reasons.")
    }
  }

  private fun hackSearchHighlight() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(
        ClassClassPath(
          Class.forName("com.intellij.ui.SideBorder"),
        ),
      )
      val ctClass = cp.get("com.intellij.ui.SimpleColoredComponent")
      val doPaintText = ctClass.getDeclaredMethods("doPaintText")[0]
      doPaintText.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace(
                "{ \$_ = com.intellij.ui.JBColor.namedColor(\"SearchMatch.foreground\", " +
                  "com.intellij.ui.JBColor.BLACK); }",
              )
            }
          }
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackSearchHighlight for reasons.")
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
                    "com.intellij.codeInsight.hint.HintUtil.getInformationColor()); \$_ = \$proceed(\$\$); }",
                )
              }
            }
          },
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
      val init =
        ctClass.getDeclaredMethods(
          "getLiveIndicator",
        )[1]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "getIndicator") {
              e.replace(
                "{ \$4 = com.intellij.ui.JBColor.namedColor(\"Doki.Accent.color\", " +
                  "java.awt.Color.GREEN); \$_ = \$proceed(\$\$); }",
              )
            }
          }
        },
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
      val init =
        ctClass.getDeclaredMethods(
          "getListCellRendererComponent",
        )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "isUnderDarcula") { // dis for OptionDescription
              e.replace("{ \$_ = true; }")
            }
          }
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackActionModel for reasons.")
    }
  }

  fun hackLAF() {
    enableLafBorderConsistency()
  }

  private fun enableLafBorderConsistency() {
    hackPopupBorder()
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
                  "CustomFrameDecorations.separatorForeground(); }",
              )
            }
          }
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackPopupBorder for reasons.")
    }
  }

  private fun enableBorderConsistency() {
    hackEditorBorder()
    hackRunAnything()
    hackLineStatusMarkerBorder()
  }

  private fun hackEditorBorder() {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(ClassClassPath(EditorHistoryManager::class.java))
      val ctClass = cp.get("com.intellij.openapi.fileEditor.impl.EditorsSplitters")
      val init =
        ctClass.getDeclaredMethods(
          "paintComponent",
        )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: MethodCall?) {
            if (e?.methodName == "isUnderDarcula") {
              e.replace("{ \$_ = true; }")
            }
          }
        },
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
      val init =
        ctClass.getDeclaredMethods(
          "createTitle",
        )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
            }
          }
        },
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
      val init =
        ctClass.getDeclaredMethods(
          "getBorderColor",
        )[0]
      init.instrument(
        object : ExprEditor() {
          override fun edit(e: NewExpr?) {
            if (e?.className == "com.intellij.ui.JBColor") {
              e.replace("{ \$_ = com.intellij.util.ui.JBUI.CurrentTheme.Advertiser.borderColor(); }")
            }
          }
        },
      )
      ctClass.toClass()
    }) {
      log.warn("Unable to hackRunAnything for reasons.")
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
        },
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
        },
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

          override fun edit(m: MethodCall?) {
            m?.replace("{ \$_ = com.intellij.util.ui.UIUtil.getPanelBackground(); }")
          }
        },
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
        },
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
        },
      )

      ctClass2.toClass()
    }) {
      log.warn("Unable to hackBackgroundPaintingComponent for reasons.")
    }
  }

  override fun dispose() {
  }
}
