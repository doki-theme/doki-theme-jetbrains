package io.acari.doki.icon.patcher

import java.util.*

class PathPatcherDefinition(
  val name: String,
  val pathToRemove: String,
  val pathToAppend: String
)

class PathPatchersDefinition(
  val glyphs: List<PathPatcherDefinition>
)


class MaterialPathPatchers(
  val glyphs: List<MaterialPathPatcher> = Collections.emptyList()
)