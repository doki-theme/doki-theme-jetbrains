package io.acari.doki.icon.provider

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import io.acari.doki.util.toOptional
import javax.swing.Icon


class MaterialIconProvider : IconProvider(), DumbAware {
  override fun getIcon(element: PsiElement, flags: Int): Icon? =
    when (element) {
      is PsiDirectory -> getDirectoryIcon(element)
      else -> null
    }

  private fun getDirectoryIcon(element: PsiDirectory): Icon? {
    return PsiUtilCore.getVirtualFile(element)
      .toOptional()
      //todo: configure enable directory
      .map { VirtualFileInfo(element, it) }
      .map { DirectoryIconProvider.getDirectoryIcon(it) }
      .orElseGet { null }
  }
}

class VirtualFileInfo(
  val psiElement: PsiElement,
  private val virtualFile: VirtualFile
) {
  val name: String?
    get() = virtualFile.name

  val fileType: String?
    get() = virtualFile.fileType.name

}