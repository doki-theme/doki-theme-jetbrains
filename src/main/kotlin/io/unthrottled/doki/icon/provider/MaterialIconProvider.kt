package io.unthrottled.doki.icon.provider

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.toOptional
import javax.swing.Icon

class MaterialIconProvider : IconProvider(), DumbAware {
  companion object {
    const val MATERIAL_DIRECTORY = "/icons/material"
  }

  override fun getIcon(element: PsiElement, flags: Int): Icon? =
    when (element) {
      is PsiDirectory -> getDirectoryIcon(element)
      is PsiFile -> getFileIcon(element)
      else -> null
    }

  private fun getDirectoryIcon(element: PsiDirectory): Icon? {
    return ThemeManager.instance.currentTheme
      .flatMap { PsiUtilCore.getVirtualFile(element).toOptional() }
      .filter { ThemeConfig.instance.isMaterialDirectories }
      .map { VirtualFileInfo(element, it) }
      .map { DirectoryIconProvider.getIcon(it) }
      .orElseGet { null }
  }

  private fun getFileIcon(element: PsiFile): Icon? {
    return ThemeManager.instance.currentTheme
      .flatMap { PsiUtilCore.getVirtualFile(element).toOptional() }
      .filter { ThemeConfig.instance.isMaterialFiles }
      .map { VirtualFileInfo(element, it) }
      .map { FileIconProvider.getIcon(it) }
      .orElseGet { null }
  }
}
