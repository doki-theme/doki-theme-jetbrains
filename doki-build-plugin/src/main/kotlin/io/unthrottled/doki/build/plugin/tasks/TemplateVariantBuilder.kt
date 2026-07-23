package io.unthrottled.doki.build.plugin.tasks

import io.unthrottled.doki.build.jvm.models.AssetTemplateDefinition
import io.unthrottled.doki.build.jvm.tools.CommonConstructionFunctions
import io.unthrottled.doki.build.plugin.ThemeVariant
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

abstract class TemplateVariantBuilder : DefaultTask() {
  init {
    group = "doki"
    // Runs this task every time.
    outputs.upToDateWhen { false }
    includeTemplates.convention(setOf("dark", "dark dim", "light", "light dim"))
    assetsTemplatesDirectory.convention(project.layout.projectDirectory.dir("doki-build-plugin/assets/templates"))
  }

  @get:InputDirectory
  abstract val assetsTemplatesDirectory: DirectoryProperty

  @get:Input
  abstract val variantName: Property<String>

  @get:Input
  abstract val includeTemplates: SetProperty<String>

  @TaskAction
  fun run() {
    val variantName = variantName.get()
    val includeTemplates = laFFileSuffix(includeTemplates.get())
    val templateDirectory = assetsTemplatesDirectory.get().asFile.toPath()
    val templatePaths = templatePaths(templateDirectory, includeTemplates)
    val templateJSONs = templateJSONs(templatePaths)
    val baseVariantTemplatePath = baseVariantTemplate(templateDirectory, variantName)
    val baseVariantJSON = baseVariantJSON(baseVariantTemplatePath)
    val modifiedJSONTemplates = mergeVariantToTemplates(templateJSONs, baseVariantJSON,variantName)
    val variantTemplates = changeFileNames(modifiedJSONTemplates, variantName)
    writeToJson(variantTemplates)
  }

  /*
  * Creates a file variant for each template type
  * */
  private fun writeToJson(variantTemplates: Map<Path, AssetTemplateDefinition>) =
    variantTemplates.forEach { (path, template) ->
      Files.newBufferedWriter(
        path, StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE
      ).use { writer ->
        CommonConstructionFunctions.gson.toJson(template, writer)
      }
    }

  /*
  * Changes the file names to include the variant name. Ex: doki.dark.<variant>.laf.template.json
  * */
  private fun changeFileNames(
    modifiedTemplates: Map<Path, AssetTemplateDefinition>,
    variantName: String
  ): Map<Path, AssetTemplateDefinition> =
    modifiedTemplates.mapKeys { (path, _) ->
      val fileName = path.fileName.toString()
      val nameSplit = fileName.split("[.]".toPattern())
      val startSplit = nameSplit.takeWhile { it != "laf" }
      val endSplit = nameSplit.takeLast(3)
      val newNameSplit = startSplit + variantName + endSplit
      val newName = newNameSplit.joinToString(".")
      path.resolveSibling(newName)
    }

  private fun mergeVariantToTemplates(
    templateJSONs: Map<Path, AssetTemplateDefinition>,
    variantJSON: AssetTemplateDefinition,
    variantName: String
  ): Map<Path, AssetTemplateDefinition> =
    templateJSONs.mapValues { (_, template) ->
      AssetTemplateDefinition(
        type = variantJSON.type ?: template.type,
        name = "${template.name} $variantName",
        extends = template.name,
        ui = variantJSON.ui,
        colors = variantJSON.colors
      )
    }

  /*
  * Transforms LaF templates into a JSON data class
  * */
  private fun templateJSONs(templatePaths: List<Path>): Map<Path, AssetTemplateDefinition> =
    templatePaths.associateWith { templatePath ->
      Files.newBufferedReader(templatePath).use { reader ->
        CommonConstructionFunctions.gson.fromJson(
          reader,
          AssetTemplateDefinition::class.java
        )
      }
    }

  /*
  * Transforms base.<variant-name> template into a JSON data class
  * */
  private fun baseVariantJSON(baseVariantTemplatePath: Path): AssetTemplateDefinition =
    Files.newBufferedReader(baseVariantTemplatePath).use { reader ->
      CommonConstructionFunctions.gson.fromJson(reader, AssetTemplateDefinition::class.java)
    }

  /*
  * Gets the base.<variant-name> template path
  * */
  private fun baseVariantTemplate(templateDirectory: Path, variantName: String): Path =
    Files.walk(templateDirectory).filter {  Files.isRegularFile(it) && it.fileName.toString().startsWith("base.$variantName") }.findFirst().orElse(null)
      ?: throw IllegalArgumentException("Cannot find 'base.$variantName.laf.template.json' in 'assets/templates'. This file must contain variant specific theme keys.")

  /*
  * Adds 'laf.template.json' suffix to all valid templates
  * */
  private fun laFFileSuffix(includeSet: Set<String>): Set<String> =
    includeSet.map { "${it.replace(' ', '.')}.laf.template.json" }.toSet()

  /*
  * Gets all templates excluding base.<variant-name>
  */
  private fun templatePaths(templateDirPath: Path, includeTemplates: Set<String>): List<Path> =
    Files.walk(templateDirPath).filter {
      val fileName = it.fileName.toString()
      fileName.endsWith("json") && fileName in includeTemplates && ThemeVariant.entries.any{ themeVariant -> !fileName.contains(".${themeVariant.lowercase}") }
    }.toList()
}
