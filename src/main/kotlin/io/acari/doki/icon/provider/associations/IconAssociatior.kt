package io.acari.doki.icon.provider.associations

import io.acari.doki.icon.provider.VirtualFileInfo
import io.acari.doki.util.toOptional
import java.util.*

class IconAssociatior(private val iconAssociations: List<IconAssociation>) {
  fun findAssociation(virtualFileInfo: VirtualFileInfo): Optional<IconAssociation> =
    virtualFileInfo.name.toOptional()
      .flatMap { fileName ->
        iconAssociations.stream()
          .filter { it.associationRegex.matches(fileName) }
          .findAny()
      }
}