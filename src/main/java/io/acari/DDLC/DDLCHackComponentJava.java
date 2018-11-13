package io.acari.DDLC;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.impl.EditorComposite;
import io.acari.DDLC.chibi.ChibiOrchestrator;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class DDLCHackComponentJava implements ApplicationComponent {
  static {
    enableChibis();
  }

  private static void enableChibis() {
    hackBackgroundPaintingComponent();
  }

  /**
   * Enables the ability to use the editor property
   * but also allows prevents the chibi from staying after installation.
   * <p>
   * Enables the ability to use the frame property
   * but also allows prevents the background image from staying after installation.
   */
  private static void hackBackgroundPaintingComponent() {
    try {
      final ClassPool cp = new ClassPool(true);
      cp.insertClassPath(new ClassClassPath(EditorComposite.class));
      final CtClass ctClass2 = cp.get("com.intellij.openapi.wm.impl.IdeBackgroundUtil");
      final CtMethod method = ctClass2.getDeclaredMethod("withFrameBackground");
      method.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("withNamedPainters")) {
            m.replace("{ $2 = \"" + ChibiOrchestrator.DDLC_BACKGROUND_PROP + "\"; $_ = $proceed($$); }");
          }
        }
      });
      final CtMethod editorBackgroundMethod = ctClass2.getDeclaredMethod("withEditorBackground");
      editorBackgroundMethod.instrument(new ExprEditor() {
        @Override
        public void edit(final MethodCall m) throws CannotCompileException {
          if (m.getMethodName().equals("withNamedPainters")) {
            m.replace("{ $2 = \"" + ChibiOrchestrator.DDLC_CHIBI_PROP + "\"; $_ = $proceed($$); }");
          }
        }
      });
      ctClass2.toClass();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }


}
