package io.acari.doki.icon.provider

import com.intellij.openapi.util.IconLoader
import io.acari.doki.icon.provider.associations.Associations
import io.acari.doki.icon.provider.associations.IconAssociatiorFactory
import javax.swing.Icon

object DirectoryIconProvider {
  private val associator = IconAssociatiorFactory.create(Associations.DIRECTORY)
  fun getDirectoryIcon(virtualFileInfo: VirtualFileInfo): Icon? {
    return associator.findAssociation(virtualFileInfo)
      .map { IconLoader.getIcon("/icons/material/folders${it.iconPath}") }
      .orElseGet { null }
  }
}