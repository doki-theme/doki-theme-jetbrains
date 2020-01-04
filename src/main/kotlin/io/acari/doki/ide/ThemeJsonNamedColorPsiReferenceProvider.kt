package io.acari.doki.ide

import com.intellij.json.psi.JsonLiteral
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

internal class ThemeJsonNamedColorPsiReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(
    element: PsiElement,
    context: ProcessingContext
  ): Array<PsiReference> {
    if (element !is JsonLiteral) return PsiReference.EMPTY_ARRAY
    val literal = element
    val parent = literal.parent as? JsonProperty ?: return PsiReference.EMPTY_ARRAY
    return if (parent.value !== literal) PsiReference.EMPTY_ARRAY else arrayOf(DokiThemeJsonNamedColorPsiReference(literal))
  }
}