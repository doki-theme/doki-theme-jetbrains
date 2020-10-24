package io.unthrottled.doki.icon.provider

import com.intellij.openapi.util.IconLoader
import io.unthrottled.doki.icon.provider.MaterialIconProvider.Companion.MATERIAL_DIRECTORY
import io.unthrottled.doki.icon.provider.associations.Associations
import io.unthrottled.doki.icon.provider.associations.IconAssociatiorFactory
import javax.swing.Icon

object FileIconProvider : IconProvider {
  private val associator =
    IconAssociatiorFactory.create(Associations.FILE)

  override fun getIcon(virtualFileInfo: VirtualFileInfo): Icon? {
    return associator.findAssociation(virtualFileInfo)
      .map { IconLoader.getIcon("$MATERIAL_DIRECTORY/files${it.iconPath}") }
      .orElseGet { null }
  }
}
