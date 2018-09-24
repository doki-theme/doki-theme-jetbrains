package com.chrisrm.idea

import com.intellij.ide.util.TipDialog
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object DDLCHackComponent {

    init {
        createMonikasWritingTipOfTheDay()
    }

    private fun createMonikasWritingTipOfTheDay() {


        try {
            val cp = ClassPool(true)
            cp.insertClassPath(ClassClassPath(TipDialog::class.java))
            val ctClass = cp.get("com.intellij.ide.util.TipDialog")
            val declaredConstructor = ctClass.declaredConstructors[0]
            declaredConstructor.instrument(object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.methodName == "setBackground") {
                        println("ayy")
//                        val bgColor = "com.intellij.util.ui.UIUtil.getToolTipBackground().brighter();"
//                        m.replace(String.format("{ $1 = %s; \$proceed($$); }", bgColor))
                    }
                }
            })

            ctClass.toClass()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}