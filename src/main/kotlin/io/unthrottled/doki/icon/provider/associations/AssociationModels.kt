package io.unthrottled.doki.icon.provider.associations

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

class AssociationModels(
  val associations: List<IconAssociationDefinition>
)
