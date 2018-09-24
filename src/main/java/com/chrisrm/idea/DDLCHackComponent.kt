package com.chrisrm.idea

import com.intellij.ide.util.TipPanel
import com.intellij.openapi.components.ApplicationComponent
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object DDLCHackComponent : ApplicationComponent {

    init {
        createMonikasWritingTipOfTheDay()
    }

    private fun createMonikasWritingTipOfTheDay() {
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
}