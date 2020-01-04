package io.acari.doki.ide

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonLiteral
import com.intellij.json.psi.JsonProperty
import com.intellij.model.SymbolResolveResult
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.jsonSchema.impl.JsonSchemaBaseReference
import io.acari.doki.ide.DokiThemeJsonUtil.getNamedColors

class DokiThemeJsonNamedColorPsiReference(element: JsonLiteral) :
  JsonSchemaBaseReference<JsonLiteral?>(
    element,
    if (element.textLength >= 2) TextRange(1, element.textLength - 1) else TextRange.EMPTY_RANGE
  ) {
  private val myName: String = StringUtil.unquoteString(element.text)

  override fun resolveInner(): PsiElement? {
    val containingFile = element.containingFile as? JsonFile ?: return null
    val namedColors =
      getNamedColors(containingFile)
    return ContainerUtil.find(
      namedColors
    ) { property: JsonProperty -> property.name == myName }
  }

  override fun getVariants(): Array<out PsiReference> {
    return PsiReference.EMPTY_ARRAY
  }

  override fun resolveReference(): Collection<SymbolResolveResult> = emptyList()

}
