package io.acari.doki.icon.provider

import javax.swing.Icon

interface IconProvider {
  fun getIcon(virtualFileInfo: VirtualFileInfo): Icon?
}