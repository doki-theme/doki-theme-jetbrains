package io.acari.doki.hax

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.impl.EditorComposite
import io.acari.doki.chibi.ChibiOrchestrator.DOKI_BACKGROUND_PROP
import io.acari.doki.chibi.ChibiOrchestrator.DOKI_CHIBI_PROP
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object HackComponent : Disposable {
    init {
        enableDisposableChibis()
    }

    private fun enableDisposableChibis() {
        hackBackgroundPaintingComponent()
    }

    /**
     * Enables the ability to use the editor property
     * but also allows prevents the chibi from staying after installation.
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
                        m.replace("{ \$2 = \"$DOKI_CHIBI_PROP\"; \$_ = \$proceed(\$\$); }")
                    }
                }
            })

            val isEditorBackgroundImageSetMethod = ctClass2.getDeclaredMethod("isEditorBackgroundImageSet")
            isEditorBackgroundImageSetMethod.instrument(object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.methodName == "getBackgroundSpec") {
                        m.replace("{ \$2 = \"$DOKI_CHIBI_PROP\"; \$_ = \$proceed(\$\$); }")
                    }
                }
            })


            val initEditorPaintersMethod = ctClass2.getDeclaredMethod("initEditorPainters")
            initEditorPaintersMethod.instrument(object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall?) {
                    if (m!!.methodName == "initWallpaperPainter") {
                        m.replace("{ \$1 = \"$DOKI_CHIBI_PROP\"; \$_ = \$proceed(\$\$); }")
                    } else if (m.methodName == "getNamedPainters") {
                        m.replace("{ \$1 = \"$DOKI_CHIBI_PROP\"; \$_ = \$proceed(\$\$); }")
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