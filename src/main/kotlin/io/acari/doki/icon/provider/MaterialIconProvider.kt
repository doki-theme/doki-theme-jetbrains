package io.acari.doki.icon.provider

import com.google.gson.Gson
import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.ResourceUtil
import io.acari.doki.util.toOptional
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
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


object DirectoryIconProvider {

  fun getDirectoryIcon(virtualFileInfo: VirtualFileInfo): Icon? {
    return null
  }
}

class AssociationsDefinition(
  val associations: List<IconAssociationDefinition>
)

class IconAssociationDefinition(
  val name: String,
  val associationPattern: String,
  val iconPath: String
)

data class IconAssociation(
  val associationRegex: Regex,
  val iconPath: String
)

enum class Associations(val fileName: String) {
  DIRECTORY("directory.association.json"),
  FILE("file.association.json");
}

class IconAssociatior(private val iconAssociations: List<IconAssociation>)

object IconAssociatiorFactory {
  private val gson = Gson()
  fun create(association: Associations): IconAssociatior {
    return try {
      ResourceUtil.getResource(
        IconAssociation::class.java,
        "/associations/", association.fileName
      ).openStream().use {
        val def = gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          AssociationsDefinition::class.java
        )
        IconAssociatior(def.associations.map { iad ->
          IconAssociation(
            Regex(iad.associationPattern),
            iad.iconPath
          )
        })
      }
    } catch (t: Throwable) {
      t.printStackTrace()
      IconAssociatior(listOf())
    }
  }
}