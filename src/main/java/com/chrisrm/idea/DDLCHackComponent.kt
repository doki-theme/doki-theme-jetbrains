package com.chrisrm.idea

import com.intellij.ide.util.TipPanel
import com.intellij.openapi.components.ApplicationComponent
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.*


object DDLCHackComponent : ApplicationComponent {

    init {
        createMonikasWritingTipOfTheDay()
    }

    private fun createMonikasWritingTipOfTheDay() {
        hackDialog()
        hackMenu()
    }

    private fun hackDialog() {
        try {
            val cp = ClassPool(true)
            cp.insertClassPath(ClassClassPath(TipPanel::class.java))
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

    private fun hackMenu() {
        try {
            val cp = ClassPool.getDefault()
            cp.insertClassPath("/com/intellij/idea/")
            val ctClass = cp.get("com.intellij.idea.ActionsBundle")

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
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}