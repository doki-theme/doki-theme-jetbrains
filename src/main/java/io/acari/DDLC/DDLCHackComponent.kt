package io.acari.DDLC

import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.fileEditor.impl.EditorComposite
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import io.acari.DDLC.chibi.ChibiOrchestrator
import javassist.*
import javassist.expr.ExprEditor
import javassist.expr.MethodCall


object DDLCHackComponent : ApplicationComponent {

  init {
    createMonikasWritingTipOfTheDay()
  }

  fun enableDDLCBackground() {
//    hackFrameMethod()
  }

  private fun hackFrameMethod() {
    try {
      val cp = ClassPool.getDefault()
      cp.insertClassPath(ClassClassPath(EditorComposite::class.java))
      val ctClass = cp.get("com.intellij.openapi.wm.impl.IdeBackgroundUtil")
//      ctClass.instrument(object : ExprEditor() {
//        @Throws(CannotCompileException::class)
//        override fun edit(m: MethodCall?) {
//          if (m!!.methodName == "withFrameBackground") {
//            m.replace("{ \$_ = \$proceed(\$\$); }")
//          }
//        }
//      })
      val withFrameString = "withFrameBackground"
      val withFrameMethod = ctClass.getDeclaredMethod(withFrameString)
//      withFrameMethod.name = "$withFrameString\$impl"
//      val hackedWithFrameMethod = CtNewMethod.copy(withFrameMethod, withFrameString, ctClass, null)
//      hackedWithFrameMethod.setBody("""
//                  {
//                    System.err.println("hax success");
//                    if (suppressBackground($2)) return (java.awt.Graphics2D)$1;
//                    return withNamedPainters($1, "io.acari.ddlc.background", $2);
//                  }
//            """.trimIndent())
//      ctClass.addMethod(hackedWithFrameMethod)
      withFrameMethod.instrument(object : ExprEditor() {
        @Throws(CannotCompileException::class)
        override fun edit(m: MethodCall?) {
          if (m!!.methodName == "withNamedPainters") {
            m.replace("{ \$2 = \"io.acari.ddlc.background\"; System.err.println(\"h4444x\"); \$_ = \$proceed(\$\$); }")
          }
        }
      })
      ctClass.writeFile()
//      ctClass.toClass()
    } catch (e: Throwable) {
      e.printStackTrace()
    }
  }

  private fun createMonikasWritingTipOfTheDay() {
    hackTipDialog()
    hackTipPanel()
  }

  val titleInstrument = object : ExprEditor() {
    @Throws(CannotCompileException::class)
    override fun edit(m: MethodCall?) {
      if (m!!.methodName == "message") {
        m.replace("{ \$_ = \"Monika's Writing Tip of the Day\"; }")
      }
    }
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
      val init = ctClass.getDeclaredMethod("initialize")
      init.instrument(titleInstrument)
      ctClass.toClass()
    } catch (e: Exception) {
      if (!(e is NullPointerException)) {
        e.printStackTrace()
      }
      hackLegacyTip(ctClass)
    }
  }

  private fun hackLegacyTip(ctClass: CtClass) {
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
}