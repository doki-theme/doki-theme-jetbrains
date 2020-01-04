package io.acari.doki.ide

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import io.acari.doki.ide.DokiThemeJsonUtil.DOKI_THEME_EXTENSION
import io.acari.doki.ide.DokiThemeJsonUtil.isThemeFilename

class DokiThemeJsonPsiReferenceContributor : PsiReferenceContributor() {

  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
    registrar.registerReferenceProvider(
      PlatformPatterns.psiElement(JsonStringLiteral::class.java)
        .inVirtualFile(PlatformPatterns.virtualFile().with(object :
          PatternCondition<VirtualFile>(DOKI_THEME_EXTENSION) {
          override fun accepts(file: VirtualFile, context: ProcessingContext): Boolean {
            return isThemeFilename(file.name)
          }
        })),
      ThemeJsonNamedColorPsiReferenceProvider()
    )
  }
}