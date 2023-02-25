import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import groovy.util.Node
import groovy.util.NodeList
import io.unthrottled.doki.build.jvm.models.AssetTemplateDefinition
import io.unthrottled.doki.build.jvm.models.Background
import io.unthrottled.doki.build.jvm.models.Backgrounds
import io.unthrottled.doki.build.jvm.models.BuildSticker
import io.unthrottled.doki.build.jvm.models.BuildStickers
import io.unthrottled.doki.build.jvm.models.JetbrainsAppDefinition
import io.unthrottled.doki.build.jvm.models.JetbrainsStickers
import io.unthrottled.doki.build.jvm.models.JetbrainsThemeDefinition
import io.unthrottled.doki.build.jvm.models.MasterThemeDefinition
import io.unthrottled.doki.build.jvm.models.StringDictionary
import io.unthrottled.doki.build.jvm.models.ThemeDefinitionSchema
import io.unthrottled.doki.build.jvm.tools.BuildFunctions
import io.unthrottled.doki.build.jvm.tools.BuildFunctions.combineMaps
import io.unthrottled.doki.build.jvm.tools.ColorTools.isColorCode
import io.unthrottled.doki.build.jvm.tools.CommonConstructionFunctions
import io.unthrottled.doki.build.jvm.tools.ConstructableAsset
import io.unthrottled.doki.build.jvm.tools.ConstructableAssetSupplier
import io.unthrottled.doki.build.jvm.tools.ConstructableAssetSupplierFactory
import io.unthrottled.doki.build.jvm.tools.ConstructableTypes
import io.unthrottled.doki.build.jvm.tools.DokiProduct
import io.unthrottled.doki.build.jvm.tools.GroupToNameMapping.getLafNamePrefix
import io.unthrottled.doki.build.jvm.tools.PathTools.cleanDirectory
import io.unthrottled.doki.build.jvm.tools.resolveColor
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files.createDirectories
import java.nio.file.Files.delete
import java.nio.file.Files.exists
import java.nio.file.Files.isDirectory
import java.nio.file.Files.newBufferedWriter
import java.nio.file.Files.newInputStream
import java.nio.file.Files.walk
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardOpenOption
import java.util.Collections
import java.util.LinkedList
import java.util.Optional
import java.util.TreeMap
import java.util.stream.Collectors
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

fun String.getStickerName(): String = this.substring(this.lastIndexOf("/") + 1)

open class BuildThemes : DefaultTask() {

  companion object {
    private const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    private const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"
    private const val PLUGIN_NAME = "Doki Theme"
    private const val DOKI_THEME_ULTIMATE = "ultimate"
  }

  private val gson = GsonBuilder()
    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
    .setPrettyPrinting()
    .create()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  private val themeSchema: ThemeDefinitionSchema = getThemeSchema()

  private fun getThemeSchema(): ThemeDefinitionSchema =
    newInputStream(get(getResourcesDirectory().toString(), "theme-schema", "master.theme.schema.json")).use {
      gson.fromJson(
        InputStreamReader(it, StandardCharsets.UTF_8),
        ThemeDefinitionSchema::class.java
      )
    }

  @TaskAction
  fun run() {
    val buildSourceAssetDirectory = getBuildSourceAssetDirectory()
    val masterThemesDirectory = get(project.rootDir.absolutePath, "masterThemes")
    val constructableAssetSupplier =
      ConstructableAssetSupplierFactory.createCommonAssetsTemplate(
        buildSourceAssetDirectory,
        masterThemesDirectory
      )
    val dokiEditorThemeTemplates = createEditorThemeDefinitions(buildSourceAssetDirectory)

    val (pluginXmlAndParsed, extension) = getPluginXmlMutationStuff()
    val (pluginXml, parsedPluginXml) = pluginXmlAndParsed

    cleanPluginXml(extension)
    cleanDirectory(
      get(
        getResourcesDirectory().toString(),
        "doki",
        "themes"
      )
    )

    val jetbrainsDokiThemeDefinitionDirectory = getThemeDefinitionDirectory()


    CommonConstructionFunctions.getAllDokiThemeDefinitions(
      DokiProduct.JETBRAINS_THEME,
      jetbrainsDokiThemeDefinitionDirectory,
      masterThemesDirectory,
      JetbrainsAppDefinition::class.java
    )
      .forEach { pathMasterDefinitionAndJetbrainsDefinition ->
        val dokiThemeResourcePath = constructIntellijTheme(
          pathMasterDefinitionAndJetbrainsDefinition,
          constructableAssetSupplier,
          dokiEditorThemeTemplates
        )

        val themeId = pathMasterDefinitionAndJetbrainsDefinition.second.id
        addThemeToPluginXml(extension, themeId, dokiThemeResourcePath)
      }

    writeProductName(parsedPluginXml)

    writeXmlToFile(pluginXml, parsedPluginXml)
  }

  private fun getThemeDefinitionDirectory() = get(getBuildSourceAssetDirectory().toString(), "themes")

  private fun getBuildSourceAssetDirectory() = get(project.rootDir.absolutePath, "buildSrc", "assets")

  private fun writeProductName(pluginXml: Node) {
    val nameNodeList = pluginXml["name"] as NodeList
    val productPostfix = if (isUltimateBuild()) " Ultimate" else ""
    val nameNode = nameNodeList[0] as Node
    nameNode.setValue("$PLUGIN_NAME$productPostfix")
    val pluginId = if (isUltimateBuild()) ULTIMATE_PLUGIN_ID else COMMUNITY_PLUGIN_ID
    val idNodeList = pluginXml["id"] as NodeList
    val idNode = idNodeList[0] as Node
    idNode.setValue(pluginId)
  }

  private fun cleanPluginXml(extension: Node) {
    val themeProviders = extension["themeProvider"] as NodeList
    themeProviders
      .map { it as Node }
      .forEach {
        extension.remove(it)
      }
  }

  private fun addThemeToPluginXml(extension: Node, themeId: String, dokiThemeResourcePath: String) {
    val themeProviders = extension["themeProvider"] as NodeList
    val preExistingThemeProvider = themeProviders
      .map { it as Node }
      .find { it.attribute("id") == themeId }
    if (preExistingThemeProvider != null) {
      extension.remove(preExistingThemeProvider)
    }
    extension.appendNode(
      "themeProvider",
      mutableMapOf(
        "id" to themeId,
        "path" to dokiThemeResourcePath
      )
    )
  }

  private fun isUltimateBuild() = System.getenv("PRODUCT") == DOKI_THEME_ULTIMATE

  private fun createEditorThemeDefinitions(themeDirectory: Path): Map<String, Node> =
    walk(get(themeDirectory.toString(), "templates"))
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".template.xml") }
      .map { parseXml(it) }
      .collect(Collectors.toMap({ it.attribute("name") as String }, { it }, { a, _ -> a }))

  private fun getPluginXmlMutationStuff(): Pair<Pair<Path, Node>, Node> {
    val pluginXml = get(
      getResourcesDirectory().toString(),
      "META-INF", "plugin.xml"
    )
    val parsedPlugin = parseXml(pluginXml)
    return pluginXml to parsedPlugin to (parsedPlugin["extensions"] as NodeList)
      .map { it as Node }
      .find { node ->
        node.attribute("defaultExtensionNs") == "com.intellij"
      }!!
  }

  private fun constructIntellijTheme(
    pathMasterAndJetbrainsDefinition: Triple<Path, MasterThemeDefinition, JetbrainsAppDefinition>,
    constructableAssetSupplier: ConstructableAssetSupplier,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    val (
      dokiThemeDefinitionPath,
      masterThemeDefinition,
      jetbrainsDefinition
    ) = pathMasterAndJetbrainsDefinition
    val resourceDirectory = getResourceDirectory(masterThemeDefinition)
    if (!exists(resourceDirectory)) {
      createDirectories(resourceDirectory)
    }

    val themeJson = get(resourceDirectory.toString(), "${masterThemeDefinition.usableName}.theme.json")

    if (exists(themeJson)) {
      delete(themeJson)
    }


    val initialParentTemplateName = if (masterThemeDefinition.dark) "dark" else "light"
    val constructableLookAndFeel =
      constructableAssetSupplier.getConstructableAsset(ConstructableTypes.LookAndFeel)
        .orElseThrow {
          IllegalStateException("Expected the ${ConstructableTypes.LookAndFeel} template to be present")
        }

    val dokiColorTemplates =
      constructableAssetSupplier.getConstructableAsset(ConstructableTypes.Color)
        .orElseThrow { IllegalStateException("Expected the ${ConstructableTypes.Color} template to be present") }

    val resolvedNamedColors =
      BuildFunctions.resolveTemplateWithCombini(
        AssetTemplateDefinition(
          colors = masterThemeDefinition.colors,
          name = "jetbrains color template",
          extends = initialParentTemplateName,
        ),
        dokiColorTemplates.definitions,
        { it.colors!! },
        { it.extends },
        { parent, child -> combineMaps(parent, child) }
      )

    val colors = validateColors(masterThemeDefinition, resolvedNamedColors)
    val finalTheme = JetbrainsThemeDefinition(
      id = masterThemeDefinition.id,
      name = "${getLafNamePrefix(masterThemeDefinition.group)}${masterThemeDefinition.name}",
      displayName = masterThemeDefinition.displayName,
      dark = masterThemeDefinition.dark,
      author = masterThemeDefinition.author,
      editorScheme = createEditorScheme(
        masterThemeDefinition,
        jetbrainsDefinition,
        dokiThemeDefinitionPath,
        dokiEditorThemeTemplates,
        resolvedNamedColors
      ),
      group = masterThemeDefinition.group,
      stickers = buildJetbrainsStickers(masterThemeDefinition, dokiThemeDefinitionPath),
      backgrounds = getBackgrounds(
        masterThemeDefinition,
        jetbrainsDefinition,
      ),
      colors = createColors(
        colors,
        masterThemeDefinition,
      ),
      ui = getUIDef(
        jetbrainsDefinition,
        colors,
        constructableLookAndFeel,
        initialParentTemplateName,
      ),
      icons = getIcons(resolvedNamedColors, constructableLookAndFeel, initialParentTemplateName),
      meta = masterThemeDefinition.meta ?: Collections.emptyMap()
    )

    newBufferedWriter(themeJson, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        gson.toJson(finalTheme, writer)
      }
    return extractResourcesPath(themeJson)
  }

  private fun createColors(
    colors: Map<String, String>,
    themeDefinition: MasterThemeDefinition
  ): Map<String, Any> {
    val mapWithNewStuff = colors.toMutableMap()
    mapWithNewStuff["editorAccentColor"] = mapWithNewStuff.getOrDefault(
      "editorAccentColor",
      themeDefinition.overrides?.editorScheme
        ?.colors?.get("accentColor")?.toString() ?: colors["accentColor"]!!.toString()
    )

    // sort to make diffing changes easier for
    // future me.
    //      ❤️Past Me
    return mapWithNewStuff
      .entries
      .sortedBy { it.key }
      .associateBy({ it.key }) { it.value }
  }

  private fun validateColors(
    masterThemeDefinition: MasterThemeDefinition,
    colorz: Map<String, String>
  ): Map<String, String> {
    val colorsSchema = themeSchema.properties["colors"]?.required?.toSet()
      ?: throw IllegalStateException("doki.theme.schema.json is missing required attribute 'properties.colors.required'!")
    val missingColors = colorsSchema
      .filter { !isColorCode(colorz[it]) }
    return if (missingColors.isEmpty()) colorz
    else throw IllegalArgumentException(
      """Theme definition for ${masterThemeDefinition.name} is missing colors: 
      ${missingColors.joinToString(",\n\t  ")}
    """.trimIndent()
    )
  }

  private fun remapStickers(
    masterThemeDefinition: MasterThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): BuildStickers {
    val stickers = masterThemeDefinition.stickers
    val separator = File.separator
    val stickerDirectory = buildStickerPath(separator, masterThemeDefinition)
    val localStickerPath = get(getResourcesDirectory().toString(), stickerDirectory)

    val defaultStickerPath = get(
      dokiThemeDefinitionPath.parent.toString(),
      stickers.default.name
    ).toString()

    val secondarySticker = Optional.ofNullable(stickers.secondary)

    val secondaryStickerPath = secondarySticker
      .map { get(localStickerPath.toString(), it.name) }
      .map {
        get(dokiThemeDefinitionPath.parent.toString(), stickers.secondary?.name).toString()
      }

    val defaultStickerResourcesPath =
      getStickerDefinitionPath(defaultStickerPath)
    return BuildStickers(
      BuildSticker(
        sanitizePath(defaultStickerResourcesPath),
        stickers.default.anchor,
        stickers.default.opacity,
      ),
      secondaryStickerPath.map {
        BuildSticker(
          sanitizePath(getStickerDefinitionPath(it)),
          stickers.secondary!!.anchor,
          stickers.secondary!!.opacity
        )

      }.orElseGet { null }
    )
  }

  private fun translateAnchor(anchor: String): String =
    when (anchor) {
      "right" -> "MIDDLE_RIGHT"
      "left" -> "MIDDLE_LEFT"
      else -> "CENTER"
    }

  private fun getBackgrounds(
    masterThemeDefinition: MasterThemeDefinition,
    jetbrainsDefinition: JetbrainsAppDefinition,
  ): Backgrounds {
    val defaultSticker = masterThemeDefinition.stickers.default
    val defaultAnchor = defaultSticker.anchor
    return Backgrounds(
      Background(
        jetbrainsDefinition.backgrounds?.default?.name ?: defaultSticker.name,
        translateAnchor(defaultAnchor),
        defaultSticker.opacity
      ),
      Optional.ofNullable(masterThemeDefinition.stickers.secondary)
        .map {
          Background(
            it.name,
            translateAnchor(it.anchor),
            it.opacity
          )
        }
        .orElse(null)
    )
  }

  private fun buildJetbrainsStickers(
    themeDefinition: MasterThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): JetbrainsStickers {
    val remapStickers = remapStickers(
      themeDefinition,
      dokiThemeDefinitionPath
    )
    return JetbrainsStickers(
      remapStickers.default.name, remapStickers.secondary?.name
    )
  }

  private fun sanitizePath(dirtyPath: String): String =
    dirtyPath.replace(File.separator, "/")

  private fun getStickerDefinitionPath(defaultStickerPath: String): String {
    return defaultStickerPath.substringAfter(
      getThemeDefinitionDirectory().toString()
    )
  }

  private fun buildStickerPath(separator: String?, masterThemeDefinition: MasterThemeDefinition) =
    "${separator}stickers${separator}${masterThemeDefinition.usableGroup.toLowerCase()}${separator}${masterThemeDefinition.usableName}${separator}"

  private fun getResourceDirectory(masterThemeDefinition: MasterThemeDefinition): Path = get(
    getResourcesDirectory().toString(),
    "doki",
    "themes",
    masterThemeDefinition.usableGroup.toLowerCase()
  )

  private fun getResourcesDirectory(): Path = get(
    project.rootDir.absolutePath,
    "src",
    "main",
    "resources"
  )

  @Suppress("UNCHECKED_CAST")
  private fun getIcons(
    colors: Map<String, String>,
    constructableAsset: ConstructableAsset,
    initialParentTemplateName: String
  ): Map<String, Any> {
    val template = constructableAsset.definitions["base"]
      ?: throw IllegalArgumentException("Expected constructable type '${constructableAsset.type}' to have type 'base'!")
    val parentTemplate = constructableAsset.definitions[initialParentTemplateName]
      ?: throw IllegalArgumentException("Expected constructable type '${constructableAsset.type}' to have type '${initialParentTemplateName}'!")
    val parentIcons = template.icons?.toMutableMap() ?: mutableMapOf()
    val childIcons = parentTemplate.icons?.toMutableMap() ?: mutableMapOf()
    val combinedPalette = combineMaps(
      (parentIcons["ColorPalette"] ?: emptyMap<String, String>()) as StringDictionary<Any>,
      (childIcons["ColorPalette"] ?: emptyMap<String, String>()) as StringDictionary<Any>
    )
    parentIcons["ColorPalette"] = combinedPalette
    return resolveNamedColorsForMap(
      parentIcons,
      colors,
    )
  }

  private fun getUIDef(
    jetbrainsAppDefinition: JetbrainsAppDefinition,
    colors: Map<String, String>,
    constructableAsset: ConstructableAsset,
    initialParentTemplateName: String,
  ): Map<String, Any> {
    val resolveAttributes =
      BuildFunctions.resolveTemplateWithCombini(
        AssetTemplateDefinition(
          ui = jetbrainsAppDefinition.ui ?: emptyMap(),
          name = "jetbrains UI",
          extends = jetbrainsAppDefinition.uiBase ?: initialParentTemplateName,
        ),
        constructableAsset.definitions,
        { it.ui!! },
        { it.extends },
        { parent, child -> combineMaps(parent, child) }
      )
    return resolveNamedColorsForMap(resolveAttributes, colors)

  }

  private fun resolveNamedColorsForMap(
    resolveAttributes: StringDictionary<Any>,
    colors: Map<String, String>,
    valueResolver: (String, StringDictionary<String>) -> String = { value, namedColors ->
      resolveColor(
        value,
        namedColors
      )
    }
  ): TreeMap<String, Any> = resolveAttributes.entries
    .stream()
    .map {
      val attributeKey = it.key
      attributeKey to when (val value = it.value) {
        is String -> valueResolver(value, colors)
        is Map<*, *> -> resolveNamedColorsForMap(
          value as MutableMap<String, Any>,
          colors
        ) { valToResolve, namedColors ->
          if (attributeKey == "ColorPalette") {
            // ColorPalette is special, they need the hex in place of just
            // the "namedColor" value.
            namedColors.getOrDefault(valToResolve, valToResolve)
          } else {
            valueResolver(valToResolve, namedColors)
          }
        }

        else -> value
      }
    }
    .collect(Collectors.toMap({ it.first }, { it.second }, { _, b -> b },
      { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
    )

  private fun createEditorScheme(
    dokiDefinitionMaster: MasterThemeDefinition,
    dokiDefinitionJetbrains: JetbrainsAppDefinition,
    dokiThemeDefinitionDirectory: Path,
    dokiEditorThemeTemplates: Map<String, Node>,
    resolvedNamedColors: Map<String, Any>
  ): String {
    return when (val variant = dokiDefinitionJetbrains.editorScheme["type"]) {
      "template" -> createEditorSchemeFromTemplate(
        dokiDefinitionMaster,
        dokiDefinitionJetbrains,
        dokiEditorThemeTemplates,
        resolvedNamedColors
      )

      "templateExtension" -> createEditorSchemeFromTemplateExtension(
        dokiDefinitionMaster,
        dokiDefinitionJetbrains,
        dokiEditorThemeTemplates,
        dokiThemeDefinitionDirectory,
        resolvedNamedColors
      )

      else -> throw IllegalArgumentException("I can't build a theme of type $variant.")
    }
  }

  private fun createEditorSchemeFromTemplateExtension(
    dokiDefinitionMaster: MasterThemeDefinition,
    dokiDefinitionJetbrains: JetbrainsAppDefinition,
    dokiEditorThemeTemplates: Map<String, Node>,
    dokiThemeDefinitionDirectory: Path,
    resolvedNamedColors: Map<String, Any>
  ): String {
    val childTheme = parseXml(
      get(
        dokiThemeDefinitionDirectory.parent.toString(),
        dokiDefinitionJetbrains.editorScheme["file"] as? String
          ?: throw IllegalArgumentException("Missing 'file' from create editor scheme from template extension definition")
      )
    )
    return resolveEditorScheme(
      childTheme,
      dokiEditorThemeTemplates,
      dokiDefinitionMaster,
      dokiDefinitionJetbrains,
      resolvedNamedColors
    )
  }

  private val childrenICareAbout = listOf("colors", "attributes")

  private fun sortXmlAttributes(parentTheme: Node) {
    val queue = LinkedList<Any>()
    queue.addAll(childrenICareAbout.map { parentTheme[it] })
    while (queue.isNotEmpty()) {
      when (val currentDude = queue.pollFirst()) {
        is Node -> {
          val value = currentDude.value()
          if (value != null) {
            queue.push(value)
          }
        }

        is NodeList -> {
          if (currentDude.isNotEmpty() &&
            currentDude.firstOrNull { dudeChild ->
              dudeChild !is Node ||
                (dudeChild.name().toString() != "value")
            } != null
          ) {
            Collections.sort(currentDude) { a, b ->
              val left = a as Node
              val right = b as Node

              getComparable(left).compareTo(
                getComparable(right)
              )
            }
            queue.addAll(currentDude)
          }
        }
      }
    }
  }

  private fun getComparable(left: Node): String =
    (left.attribute("name") as? String) ?: left.name().toString()

  private fun createEditorSchemeFromTemplate(
    dokiDefinitionMaster: MasterThemeDefinition,
    dokiDefinitionJetbrains: JetbrainsAppDefinition,
    dokiEditorThemeTemplates: Map<String, Node>,
    resolvedNamedColors: Map<String, Any>
  ): String {
    val root = Node(
      null,
      "root",
      mutableMapOf("parent_scheme" to dokiDefinitionJetbrains.editorScheme["name"])
    )
    childrenICareAbout.forEach {
      root.appendNode(Node(root, it))
    }
    return resolveEditorScheme(
      root,
      dokiEditorThemeTemplates,
      dokiDefinitionMaster,
      dokiDefinitionJetbrains,
      resolvedNamedColors
    )
  }

  private fun resolveEditorScheme(
    child: Node,
    dokiEditorThemeTemplates: Map<String, Node>,
    dokiDefinitionMaster: MasterThemeDefinition,
    dokiDefinitionJetbrains: JetbrainsAppDefinition,
    resolvedNamedColors: Map<String, Any>
  ): String {
    val composedEditorTemplate: Node =
      BuildFunctions.composeTemplateWithCombini(
        child,
        dokiEditorThemeTemplates,
        { it },
        { childTheme ->
          val parentScheme = childTheme.attribute("parent_scheme") as String
          if (parentScheme == "Default" || parentScheme == "Darcula") {
            null
          } else {
            parentScheme.split(",").map { it.trim() }
          }
        }
      ) { _parentTheme, childTheme ->
        val parentTheme = _parentTheme.clone() as Node
        childrenICareAbout
          .forEach { attribute ->
            val childList = childTheme[attribute] as NodeList
            val parentListNode = parentTheme[attribute] as NodeList
            childList.zip(parentListNode)
              .filter { it.first is Node && it.second is Node }
              .map { it.first as Node to it.second as Node }
              .flatMap {
                (it.first.value() as NodeList).map { childNode ->
                  childNode as Node to it.second.value() as NodeList
                }
              }
              .forEach { childeNodeWithParent ->
                val (childNode, parentList) = childeNodeWithParent
                val childName = childNode.attribute("name")
                val parentNode = parentList.map { it as Node }
                  .indexOfFirst { parentNode ->
                    parentNode.attribute("name") == childName
                  }

                if (parentNode > -1) {
                  parentList.removeAt(parentNode)
                }
                parentList.add(childNode)
              }
          }

        sortXmlAttributes(parentTheme)
        parentTheme
      }

    val themeTemplate =
      applyColorsToTemplate(composedEditorTemplate, dokiDefinitionMaster, dokiDefinitionJetbrains, resolvedNamedColors)
    return createXmlFromDefinition(dokiDefinitionMaster, themeTemplate)
  }

  private fun applyColorsToTemplate(
    editorTemplate: Node,
    dokiDefinitionMaster: MasterThemeDefinition,
    dokiDefinitionJetbrains: JetbrainsAppDefinition,
    resolvedNamedColors: Map<String, Any>
  ): Node {
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .filterIsInstance<Node>()
      .map { it as Node }
      .forEach {
        when (it.name()) {
          "scheme" -> {
            it.attributes().replace("name", dokiDefinitionMaster.name)
          }

          "option" -> {
            val value = it.attribute("value") as? String
            if (value?.contains('$') == true) {
              val (end, replacementColor) = getReplacementColor(value, '$') { templateColor ->
                dokiDefinitionMaster.overrides?.editorScheme?.colors?.get(templateColor) as? String
                  ?: dokiDefinitionJetbrains.overrides?.editorScheme?.colors?.get(templateColor) as? String
                  ?: resolvedNamedColors[templateColor] as? String
                  ?: throw IllegalArgumentException("$templateColor is not in ${dokiDefinitionMaster.name}'s color definition.")
              }
              it.attributes()["value"] = buildReplacement(replacementColor, value, end)
            } else if (value?.contains('%') == true) {
              val (end, replacementColor) = getReplacementColor(value, '%') { templateColor ->
                resolvedNamedColors[templateColor] as? String
                  ?: throw IllegalArgumentException("$templateColor is not in ${dokiDefinitionMaster.name}'s color definition.")
              }
              it.attributes()["value"] = buildReplacement(replacementColor, value, end)
            }
          }
        }
      }
    return themeTemplate
  }

  private fun buildReplacement(replacementColor: String, value: String, end: Int) =
    "$replacementColor${value.substring(end + 1)}"

  private fun getReplacementColor(
    value: String,
    templateDelemiter: Char,
    replacementSupplier: (CharSequence) -> String
  ): Pair<Int, String> {
    val start = value.indexOf(templateDelemiter)
    val end = value.lastIndexOf(templateDelemiter)
    val templateColor = value.subSequence(start + 1, end)
    val replacementHexColor = replacementSupplier(templateColor)
    val replacementColor = replacementHexColor.substring(1)
    return Pair(end, replacementColor)
  }

  private fun createXmlFromDefinition(
    dokiDefinitionMaster: MasterThemeDefinition,
    themeTemplate: Node
  ): String {
    val themeName = dokiDefinitionMaster.usableName
    val destination = get(
      getResourceDirectory(dokiDefinitionMaster).toString(),
      "$themeName.xml"
    )

    if (exists(destination)) {
      delete(destination)
    }

    writeXmlToFile(destination, themeTemplate)

    return extractResourcesPath(destination)
  }

  private fun extractResourcesPath(destination: Path): String {
    val fullResourcesPath = destination.toString()
    val separator = File.separator
    val editorPathResources =
      fullResourcesPath.substring(fullResourcesPath.indexOf("${separator}doki${separator}theme"))
    return editorPathResources.replace(separator.toString(), "/")
  }
}
