package io.acari.DDLC

import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.openapi.components.ApplicationComponent
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.expr.ExprEditor
import javassist.expr.MethodCall


object DDLCHackComponent : ApplicationComponent {

    init {
        createMonikasWritingTipOfTheDay()
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
            e.printStackTrace()
            hackLegacyTip(ctClass)
        }
    }

    private fun hackLegacyTip(ctClass: CtClass) {
        try {
            val declaredConstructor = ctClass.getDeclaredConstructor(emptyArray())
            declaredConstructor.insertAfter("setTitle(\"Monika's Writing Tip of the Day\");")
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