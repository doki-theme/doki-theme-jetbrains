package io.acari.DDLC

import com.intellij.ide.util.TipPanel
import com.intellij.idea.IdeaTestApplication
import com.intellij.openapi.components.ApplicationComponent
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.*
import java.util.*


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
            val cp = ClassPool(true)
            cp.insertClassPath(ClassClassPath(IdeaTestApplication::class.java))
            val ctClass = cp.get("com.intellij.idea.ActionsBundle")
            val bundle = java.util.ResourceBundle.getBundle("io.acari.DDLC.DDLCActionsBundle", Locale.US)
            val init = ctClass.getDeclaredMethod("getBundle")
            init.instrument(object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.methodName == "getBundle") {
                        m.replace("{ \$_ = java.util.ResourceBundle.getBundle(\"io.acari.DDLC.DDLCActionsBundle\", java.util.Locale.US); }")
                    }
                }
            })
            ctClass.toClass()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}