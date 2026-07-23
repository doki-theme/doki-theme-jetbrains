package io.unthrottled.doki.build.plugin.util

import io.unthrottled.doki.build.jvm.models.HasId
import io.unthrottled.doki.build.jvm.models.MasterThemeDefinition
import io.unthrottled.doki.build.jvm.tools.CommonConstructionFunctions.gson
import io.unthrottled.doki.build.jvm.tools.DokiProduct
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

object ConstructionFunctions {
  fun <T : HasId> getAllJetbrainsDefinitions(
    dokiProduct: DokiProduct,
    productBuildSourceDirectory: Path,
    masterThemeDirectory: Path,
    clazz: Class<T>,
    variantName: String
  ): Stream<Triple<Path, MasterThemeDefinition, T>> {
    val allVariantDefinitions = getJetProductDefinitions(
      productBuildSourceDirectory,
      clazz,
      getThemeTemplateSuffix(dokiProduct.value, variantName)
    )
    val masterThemeDefinitionPath = Paths.get(masterThemeDirectory.toString(), "definitions")
    return Files.walk(masterThemeDefinitionPath)
      .filter { !Files.isDirectory(it) }
      .filter { endsWithMaster(it.fileName.toString(), variantName) }
      .map { it to Files.newInputStream(it) }
      .map {
        val masterThemePath = it.first.toString()
        val masterFileDefinition = masterThemePath.substringAfter("$masterThemeDefinitionPath")
        val productDefinitionDefinitionPath =
          removeJetbrainsDir(Paths.get(productBuildSourceDirectory.toString(), masterFileDefinition), it.first.fileName)
        val masterThemeDefinition = gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          MasterThemeDefinition::class.java
        )
        val key =
          masterThemeDefinition.id + if (variantName == "darcula" || variantName.startsWith("custom")) "" else variantName
        val variantDefinition = (allVariantDefinitions[key] ?: throw IllegalArgumentException(
          """
              doki-build-plugin/assets/themes,'${masterThemeDefinition.displayName}', is missing a $variantName variant definition file!
            """.trimIndent()
        ))

        Triple(productDefinitionDefinitionPath, masterThemeDefinition, variantDefinition)
      }
  }

  private fun <T : HasId> getJetProductDefinitions(
    productBuildSourceDirectory: Path,
    clazz: Class<T>,
    suffix: String
  ): Map<String, T> {
    return Files.walk(productBuildSourceDirectory)
      .filter { !Files.isDirectory(it) }
      .filter { it.fileName.toString().endsWith(suffix) }
      .map { Files.newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          clazz
        )
      }.collect(
        Collectors.toMap(
          { it.id },
          { it }
        )
      )
  }

  private fun getThemeTemplateSuffix(dokiProductName: String, variantName: String): String = when {
    variantName.startsWith("custom") -> {
      val variantSplit = variantName.split("-".toPattern()) // Ex [custom,variant]
      val variantName = variantSplit[0] // Ex: custom
      val variantType = variantSplit[1] // Ex: variant
      "$variantName.$variantType.${dokiProductName}.definition.json"
    }

    else -> "$variantName.${dokiProductName}.definition.json"
  }

  private fun endsWithMaster(fileName: String, variantName: String): Boolean = if (variantName.startsWith("custom")) {
    fileName.endsWith("custom.master.definition.json")
  } else if (!variantName.startsWith("custom") && !fileName.endsWith("custom.master.definition.json")) {
    fileName.endsWith("master.definition.json")
  } else {
    false
  }

  private fun removeJetbrainsDir(path: Path, fileName: Path): Path {
    if (!path.parent.endsWith("jetbrains")) {
      return path
    }
    return path.parent.resolveSibling(fileName)
  }

}
