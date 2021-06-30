import com.google.gson.GsonBuilder
import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.Collections
import java.util.Comparator
import java.util.LinkedList
import java.util.Optional
import java.util.TreeMap
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

fun String.getStickerName(): String = this.substring(this.lastIndexOf("/") + 1)

open class BuildThemes : DefaultTask() {

  companion object {
    private const val COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
    private const val ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"
    private const val PLUGIN_NAME = "The Doki Theme"
    private val COLOR_HEX_PATTERN_RGB = Pattern.compile("^#([A-Fa-f0-9]{6})$")
    private val COLOR_HEX_PATTERN_RGBA = Pattern.compile("^#([A-Fa-f0-9]{8})$")
    private const val HEX_COLOR_LENGTH_RGB = 7
    private const val HEX_COLOR_LENGTH_RGBA = 9
    private const val DOKI_THEME_ULTIMATE = "ultimate"
    private const val LAF_TEMPLATE = "LAF"
    private const val COLOR_TEMPLATE = "COLOR"
  }

  private val gson = GsonBuilder().setPrettyPrinting().create()

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
    val buildAssetDirectory = getBuildAssetDirectory()
    val masterThemeDirectory = get(project.rootDir.absolutePath, "masterThemes")
    val dokiThemeTemplates = createThemeDefinitions(buildAssetDirectory, masterThemeDirectory)

    val dokiEditorThemeTemplates = createEditorThemeDefinitions(buildAssetDirectory)

    val (pluginXmlAndParsed, extension) = getPluginXmlMutationStuff()
    val (pluginXml, parsedPluginXml) = pluginXmlAndParsed

    cleanPluginXml(extension)
    cleanThemeDirectory()

    val jetbrainsDokiThemeDefinitionDirectory = getThemeDefinitionDirectory()
    getAllDokiThemeDefinitions(jetbrainsDokiThemeDefinitionDirectory, masterThemeDirectory)
      .forEach { pathMasterDefinitionAndJetbrainsDefinition ->
        val dokiThemeResourcePath = constructIntellijTheme(
          pathMasterDefinitionAndJetbrainsDefinition,
          dokiThemeTemplates,
          dokiEditorThemeTemplates
        )

        val themeId = pathMasterDefinitionAndJetbrainsDefinition.second.id
        addThemeToPluginXml(extension, themeId, dokiThemeResourcePath)
      }

    writeProductName(parsedPluginXml)

    writeXmlToFile(pluginXml, parsedPluginXml)
  }

  private fun getThemeDefinitionDirectory() = get(getBuildAssetDirectory().toString(), "themes")

  private fun getBuildAssetDirectory() = get(project.rootDir.absolutePath, "buildSrc", "assets")

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

  private fun getAllDokiThemeDefinitions(
    dokiThemeDefinitionDirectory: Path,
    masterThemeDirectory: Path
  ): Stream<Triple<Path, DokiBuildMasterThemeDefinition, DokiBuildJetbrainsThemeDefinition>> {
    val jetbrainsDefinitions =
      walk(dokiThemeDefinitionDirectory)
        .filter { !isDirectory(it) }
        .filter { it.fileName.toString().endsWith("jetbrains.definition.json") }
        .map { newInputStream(it) }
        .map {
          gson.fromJson(
            InputStreamReader(it, StandardCharsets.UTF_8),
            DokiBuildJetbrainsThemeDefinition::class.java
          )
        }.collect(Collectors.toMap(
          { it.id }, { it }
        ))

    val masterThemeDefinitionPath = get(masterThemeDirectory.toString(), "definitions")
    return walk(masterThemeDefinitionPath)
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith("master.definition.json") }
      .map { it to newInputStream(it) }
      .map {
        val masterThemePath = it.first.toString()
        val masterFileDefinition = masterThemePath.substringAfter("$masterThemeDefinitionPath")
        val jetbrainsThemeDefinitionPath =
          get(dokiThemeDefinitionDirectory.toString(), masterFileDefinition)
        val masterThemeDefinition = gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          DokiBuildMasterThemeDefinition::class.java
        )
        val jetbrainsThemeDefinition =
          jetbrainsDefinitions[masterThemeDefinition.id] ?: throw IllegalArgumentException(
            """
            Master Theme ${masterThemeDefinition.displayName} is missing the JetBrains definition file!
          """.trimIndent()
          )
        Triple(jetbrainsThemeDefinitionPath, masterThemeDefinition, jetbrainsThemeDefinition)
      }.filter {
        (it.second.product == DOKI_THEME_ULTIMATE && isUltimateBuild()) ||
          it.second.product != DOKI_THEME_ULTIMATE
      }
  }

  private fun isUltimateBuild() = System.getenv("PRODUCT") == DOKI_THEME_ULTIMATE

  private fun createThemeDefinitions(
    themeDirectory: Path,
    masterThemeDirectory: Path
  ): Map<String, Map<String, ThemeTemplateDefinition>> =
    Stream.concat(
      walk(get(themeDirectory.toString(), "templates")),
      walk(get(masterThemeDirectory.toString(), "templates"))
    )
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".template.json") }
      .map { newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          ThemeTemplateDefinition::class.java
        )
      }
      .collect(
        Collectors.groupingBy {
          it.type ?: throw IllegalArgumentException("Expected template ${it.name} to have a type")
        }
      )
      .entries.map {
        it.key to (it.value.map { kv -> kv.name to kv }.toMap())
      }.toMap()

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
    pathMasterAndJetbrainsDefinition: Triple<Path, DokiBuildMasterThemeDefinition, DokiBuildJetbrainsThemeDefinition>,
    dokiTemplates: Map<String, Map<String, ThemeTemplateDefinition>>,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    val (
      dokiThemeDefinitionPath,
      themeDefinition,
      jetbrainsDefinition
    ) = pathMasterAndJetbrainsDefinition
    val resourceDirectory = getResourceDirectory(themeDefinition)
    if (!exists(resourceDirectory)) {
      createDirectories(resourceDirectory)
    }

    val themeJson = get(resourceDirectory.toString(), "${themeDefinition.usableName}.theme.json")

    if (exists(themeJson)) {
      delete(themeJson)
    }


    val templateName = if (themeDefinition.dark) "dark" else "light"
    val dokiThemeTemplates =
      dokiTemplates[LAF_TEMPLATE] ?: throw IllegalStateException("Expected the $LAF_TEMPLATE template to be present")
    val topThemeDefinition =
      dokiThemeTemplates[jetbrainsDefinition.uiBase ?: templateName]
        ?: throw IllegalStateException("Theme $templateName does not exist.")

    val dokiColorTemplates =
      dokiTemplates[COLOR_TEMPLATE]
        ?: throw IllegalStateException("Expected the $COLOR_TEMPLATE template to be present")
    val resolvedNamedColors = resolveAttributes(
      dokiColorTemplates[templateName] ?: throw IllegalStateException("Theme $templateName does not exist."),
      dokiColorTemplates,
      themeDefinition.colors
    ) {
      it.colors ?: throw IllegalArgumentException("Expected the $LAF_TEMPLATE to have a 'colors' attribute")
    } as MutableMap<String, String>
    val colors = validateColors(themeDefinition, resolvedNamedColors)
    val finalTheme = JetbrainsThemeDefinition(
      id = themeDefinition.id,
      name = "${getLafNamePrefix(themeDefinition.group)}${themeDefinition.name}",
      displayName = themeDefinition.displayName,
      dark = themeDefinition.dark,
      author = themeDefinition.author,
      editorScheme = createEditorScheme(
        themeDefinition,
        jetbrainsDefinition,
        dokiThemeDefinitionPath,
        dokiEditorThemeTemplates,
        resolvedNamedColors
      ),
      group = themeDefinition.group,
      stickers = remapStickers(
        themeDefinition,
        dokiThemeDefinitionPath
      ),
      backgrounds = getBackgrounds(
        themeDefinition,
        jetbrainsDefinition,
        dokiThemeDefinitionPath
      ),
      colors = colors,
      ui = getUIDef(
        jetbrainsDefinition.ui,
        colors,
        topThemeDefinition,
        dokiThemeTemplates
      ),
      icons = getIcons(resolvedNamedColors, topThemeDefinition, dokiThemeTemplates),
      meta = themeDefinition.meta ?: Collections.emptyMap()
    )

    newBufferedWriter(themeJson, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        gson.toJson(finalTheme, writer)
      }
    return extractResourcesPath(themeJson)
  }

  private fun validateColors(
    masterThemeDefinition: DokiBuildMasterThemeDefinition,
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

  private fun isColorCode(text: String?): Boolean {
    if (text == null || !text.startsWith('#')) return false
    return if (text.length != HEX_COLOR_LENGTH_RGB &&
      text.length != HEX_COLOR_LENGTH_RGBA
    ) false
    else COLOR_HEX_PATTERN_RGB.matcher(
      text
    ).matches() || COLOR_HEX_PATTERN_RGBA.matcher(
      text
    ).matches()
  }

  private fun remapStickers(
    masterThemeDefinition: DokiBuildMasterThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): BuildStickers {
    val stickers = masterThemeDefinition.stickers
    val separator = File.separator
    val stickerDirectory = buildStickerPath(separator, masterThemeDefinition)
    val localStickerPath = get(getResourcesDirectory().toString(), stickerDirectory)

    val defaultStickerPath = get(
      dokiThemeDefinitionPath.parent.toString(),
      stickers.default
    ).toString()

    val secondarySticker = Optional.ofNullable(stickers.secondary)

    val secondaryStickerPath = secondarySticker
      .map { get(localStickerPath.toString(), it) }
      .map {
        get(dokiThemeDefinitionPath.parent.toString(), stickers.secondary).toString()
      }

    val defaultStickerResourcesPath =
      getStickerDefinitionPath(defaultStickerPath)
    return BuildStickers(
      sanitizePath(defaultStickerResourcesPath),
      secondaryStickerPath.map { sanitizePath(getStickerDefinitionPath(it)) }.orElseGet { null }
    )
  }

  private fun getBackgrounds(
    masterThemeDefinition: DokiBuildMasterThemeDefinition,
    jetbrainsThemeDefinition: DokiBuildJetbrainsThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): Backgrounds {
    val stickers = remapStickers(masterThemeDefinition, dokiThemeDefinitionPath)
    val defaultBackground = jetbrainsThemeDefinition.backgrounds?.default
    val secondaryBackground = jetbrainsThemeDefinition.backgrounds?.secondary
    return Backgrounds(
      Optional.ofNullable(defaultBackground)
        .map {
          Background(it.name ?: stickers.default.getStickerName(), it.position ?: "CENTER", it.opacity)
        }
        .orElse(
          Background(
            stickers.default.getStickerName(),
            "CENTER"
          )
        ),
      Optional.ofNullable(secondaryBackground)
        .map {
          Background(
            it.name ?: stickers.secondary?.getStickerName() ?: stickers.default.getStickerName(),
            it.position ?: "CENTER",
            it.opacity
          )
        }
        .map { Optional.of(it) }
        .orElseGet {
          Optional.ofNullable(stickers.secondary)
            .map {
              Background(
                it?.getStickerName() ?: stickers.default.getStickerName(),
                "CENTER"
              )
            }
        }
        .orElse(null)
    )
  }

  private fun sanitizePath(dirtyPath: String): String =
    dirtyPath.replace(File.separator, "/")

  private fun getStickerDefinitionPath(defaultStickerPath: String): String {
    return defaultStickerPath.substringAfter(
      getThemeDefinitionDirectory().toString()
    )
  }

  private fun buildStickerPath(separator: String?, masterThemeDefinition: DokiBuildMasterThemeDefinition) =
    "${separator}stickers${separator}${masterThemeDefinition.usableGroup.toLowerCase()}${separator}${masterThemeDefinition.usableName}${separator}"

  private fun cleanThemeDirectory() {
    val themeDirectory = get(
      getResourcesDirectory().toString(),
      "doki",
      "themes"
    )
    if (notExists(themeDirectory)) {
      createDirectories(themeDirectory)
    } else {
      walk(themeDirectory)
        .sorted(Comparator.reverseOrder())
        .forEach(Files::delete)
    }
  }

  private fun getResourceDirectory(masterThemeDefinition: DokiBuildMasterThemeDefinition): Path = get(
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
    colors: Map<String, Any>,
    topThemeDef: ThemeTemplateDefinition,
    themeDefs: Map<String, ThemeTemplateDefinition>
  ): Map<String, Any> = resolveTemplate(topThemeDef, themeDefs) { it.icons ?: mapOf() }
    .map { entry ->
      if (entry.key == "ColorPalette") {
        val palette = entry.value as Map<String, String>
        val updatedPalette: Map<String, String> = palette.entries.stream()
          .map { paletteEntry ->
            paletteEntry.key to colors.getOrDefault(paletteEntry.value, paletteEntry.value)
          }.collect(Collectors.toMap({ it.first }, { it.second },
            { _, b -> b },
            { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
          )
        entry.key to updatedPalette
      } else {
        entry.key to entry.value
      }
    }
    .collect(Collectors.toMap({ it.first }, { it.second }, { _, b -> b },
      { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
    )

  private fun getUIDef(
    ui: Map<String, Any>,
    colors: Map<String, String>,
    dokiThemeTemplates: ThemeTemplateDefinition,
    dokiThemeTemplates1: Map<String, ThemeTemplateDefinition>
  ): Map<String, Any> {
    val resolveAttributes = resolveAttributes(dokiThemeTemplates, dokiThemeTemplates1, ui) {
      it.ui ?: throw IllegalArgumentException("Expected the $LAF_TEMPLATE to have a ui attribute")
    }
    return resolveNamedColorsForMap(resolveAttributes, colors)

  }

  private fun resolveNamedColorsForMap(
    resolveAttributes: MutableMap<String, Any>,
    colors: Map<String, String>
  ): TreeMap<String, Any> = resolveAttributes.entries
    .stream()
    .map {
      it.key to when (val value = it.value) {
        is String -> resolveStringTemplate(value, colors)
        is Map<*, *> -> resolveNamedColorsForMap(value as MutableMap<String, Any>, colors)
        else -> value
      }
    }
    .collect(Collectors.toMap({ it.first }, { it.second }, { _, b -> b },
      { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
    )

  private fun resolveStringTemplate(value: String, colors: Map<String, String>): String =
    if (value.contains('&')) {
    val (end, replacementColor) = getReplacementColor(value, '&') { templateColor ->
      colors[templateColor]
        ?: throw IllegalArgumentException("$templateColor is not in the color definition.")
    }
    '#' + buildReplacement(replacementColor, value, end)
  } else {
    value
  }

  private fun resolveAttributes(
    childTemplate: ThemeTemplateDefinition,
    dokiTemplateDefinitions: Map<String, ThemeTemplateDefinition>,
    definitionAttributes: Map<String, Any>,
    attributeExtractor: (ThemeTemplateDefinition) -> Map<String, Any>
  ): MutableMap<String, Any> =
    Stream.of(
      resolveTemplate(childTemplate, dokiTemplateDefinitions, attributeExtractor),
      definitionAttributes.entries.stream()
    )
      .flatMap { it }
      .collect(Collectors.toMap({ it.key }, { it.value }, { _, b -> b },
        { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
      )

  private fun resolveTemplate(
    childTemplate: ThemeTemplateDefinition,
    allThemeTemplates: Map<String, ThemeTemplateDefinition>,
    entryExtractor: (ThemeTemplateDefinition) -> Map<String, Any>
  ): Stream<Map.Entry<String, Any>> =
    if (childTemplate.extends == null) {
      entryExtractor(childTemplate).entries.stream()
    } else {
      Stream.concat(
        resolveTemplate(
          allThemeTemplates[childTemplate.extends]
            ?: throw IllegalStateException("Theme template ${childTemplate.extends} is not a valid parent template"),
          allThemeTemplates,
          entryExtractor
        ), entryExtractor(childTemplate).entries.stream()
      )
    }

  private fun createEditorScheme(
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
    dokiDefinitionJetbrains: DokiBuildJetbrainsThemeDefinition,
    dokiThemeDefinitionDirectory: Path,
    dokiEditorThemeTemplates: Map<String, Node>,
    resolvedNamedColors: Map<String, Any>
  ): String {
    return when (val variant = dokiDefinitionJetbrains.editorScheme["type"]) {
      "custom" -> copyXml(dokiDefinitionMaster, dokiDefinitionJetbrains, dokiThemeDefinitionDirectory)
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
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
    dokiDefinitionJetbrains: DokiBuildJetbrainsThemeDefinition,
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

    val extendedTheme =
      extendTheme(
        childTheme,
        dokiEditorThemeTemplates
      )

    val themeTemplate =
      applyColorsToTemplate(extendedTheme, dokiDefinitionMaster, dokiDefinitionJetbrains, resolvedNamedColors)
    return createXmlFromDefinition(dokiDefinitionMaster, themeTemplate)
  }

  private val childrenICareAbout = listOf("colors", "attributes")

  private fun extendTheme(childTheme: Node, dokiEditorThemeTemplates: Map<String, Node>): Node {
    val parentScheme = childTheme.attribute("parent_scheme")
    if (parentScheme == "Default" || parentScheme == "Darcula") {
      return childTheme
    }

    val parentToExtend = (dokiEditorThemeTemplates[parentScheme]?.clone() as? Node
      ?: throw IllegalArgumentException("Expected parent scheme $parentScheme to be valid!"))

    val parentTheme = extendTheme(parentToExtend, dokiEditorThemeTemplates)

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

    return parentTheme
  }

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
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
    dokiDefinitionJetbrains: DokiBuildJetbrainsThemeDefinition,
    dokiEditorThemeTemplates: Map<String, Node>,
    resolvedNamedColors: Map<String, Any>
  ): String {
    val templateName = dokiDefinitionJetbrains.editorScheme["name"]
      ?: throw IllegalArgumentException("Missing 'name' from create editor scheme from template definition")

    val childTheme = (dokiEditorThemeTemplates[templateName]
      ?: throw IllegalArgumentException("Unrecognized template name $templateName"))
    val editorTemplate = extendTheme(childTheme, dokiEditorThemeTemplates)

    val themeTemplate =
      applyColorsToTemplate(editorTemplate, dokiDefinitionMaster, dokiDefinitionJetbrains, resolvedNamedColors)
    return createXmlFromDefinition(dokiDefinitionMaster, themeTemplate)
  }

  private fun applyColorsToTemplate(
    editorTemplate: Node,
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
    dokiDefinitionJetbrains: DokiBuildJetbrainsThemeDefinition,
    resolvedNamedColors: Map<String, Any>
  ): Node {
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
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
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
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

  private fun copyXml(
    dokiDefinitionMaster: DokiBuildMasterThemeDefinition,
    dokiDefinitionJetbrains: DokiBuildJetbrainsThemeDefinition,
    dokiThemeDefinitionDirectory: Path
  ): String {
    val customXmlFile = dokiDefinitionJetbrains.editorScheme["file"] as? String
      ?: throw IllegalArgumentException("Missing 'file' from create editor scheme from file copy")
    val destination = get(
      getResourceDirectory(dokiDefinitionMaster).toString(),
      customXmlFile
    )
    copy(
      get(dokiThemeDefinitionDirectory.parent.toString(), customXmlFile),
      destination,
      StandardCopyOption.REPLACE_EXISTING
    )
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
