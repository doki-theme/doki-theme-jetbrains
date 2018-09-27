package io.acari.DDLC

import com.intellij.ide.util.ChooseElementsDialog
import com.intellij.ide.util.ExportToFileUtil
import com.intellij.openapi.components.ApplicationComponent
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.*


object DDLCHackComponent : ApplicationComponent {

    init {
        createMonikasWritingTipOfTheDay()
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

            val init = ctClass.getDeclaredMethod("initialize")
            init.instrument(object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.methodName == "message") {
                        m.replace("{ \$_ = \"Monika's Writing Tip of the Day\"; }")
                    }
                }
            })
            ctClass.toClass()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //todo: make the tips show up at least once after this :)
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