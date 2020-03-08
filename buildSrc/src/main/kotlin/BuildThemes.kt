import com.google.gson.GsonBuilder
import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

open class BuildThemes : DefaultTask() {

  companion object {
    private val COLOR_HEX_PATTERN_RGB = Pattern.compile("^#([A-Fa-f0-9]{6})$")
    private val COLOR_HEX_PATTERN_RGBA = Pattern.compile("^#([A-Fa-f0-9]{8})$")
    private const val HEX_COLOR_LENGTH_RGB = 7
    private const val HEX_COLOR_LENGTH_RGBA = 9
    private const val DOKI_THEME_ULTIMATE =  "ultimate"
  }

  private val gson = GsonBuilder().setPrettyPrinting().create()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  private val themeSchema: ThemeDefinitionSchema = getThemeSchema()

  private fun getThemeSchema(): ThemeDefinitionSchema =
    newInputStream(get(getResourcesDirectory().toString(), "theme-schema", "doki.theme.schema.json")).use {
      gson.fromJson(
        InputStreamReader(it, StandardCharsets.UTF_8),
        ThemeDefinitionSchema::class.java
      )
    }

  @TaskAction
  fun run() {
    val themeDirectory = get(project.rootDir.absolutePath, "themes")
    val dokiThemeTemplates = createThemeDefinitions(themeDirectory)

    val dokiEditorThemeTemplates = createEditorThemeDefinitions(themeDirectory)

    val (pluginXmlAndParsed, extension) = getPluginXmlMutationStuff()
    val (pluginXml, parsedPluginXml) = pluginXmlAndParsed
    cleanPluginXml(extension)

    val masterThemeDirectory = get(project.rootDir.absolutePath, "masterThemes")
    val jetbrainsDokiThemeDefinitionDirectory = get(themeDirectory.toString(), "definitions")
    getAllDokiThemeDefinitions(jetbrainsDokiThemeDefinitionDirectory, masterThemeDirectory)
      .forEach { themeDefinitionAndPath ->
        val dokiThemeResourcePath = constructIntellijTheme(
          themeDefinitionAndPath,
          dokiThemeTemplates,
          dokiEditorThemeTemplates
        )

        val themeId = themeDefinitionAndPath.second.id
        addThemeToPluginXml(extension, themeId, dokiThemeResourcePath)
      }

    writeXmlToFile(pluginXml, parsedPluginXml)
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
  ): Stream<Pair<Path, DokiBuildThemeDefinition>> {
    return walk(masterThemeDirectory)
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".doki.json") }
      .map { it to newInputStream(it) }
      .map {
        val masterThemePath = it.first.toString()
        val masterFileDefinition = masterThemePath.substringAfter("$masterThemeDirectory")
        val jetbrainsThemeDefinitionPath =
          get(dokiThemeDefinitionDirectory.toString(), masterFileDefinition)
        jetbrainsThemeDefinitionPath to gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          DokiBuildThemeDefinition::class.java
        )
      }.filter {
        (it.second.product == DOKI_THEME_ULTIMATE && System.getenv("PRODUCT") == DOKI_THEME_ULTIMATE) ||
            it.second.product != DOKI_THEME_ULTIMATE
      }
  }

  private fun createThemeDefinitions(themeDirectory: Path): Map<String, ThemeTemplateDefinition> =
    walk(get(themeDirectory.toString(), "templates"))
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".template.json") }
      .map { newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          ThemeTemplateDefinition::class.java
        )
      }.collect(Collectors.toMap({ it.name }, { it }, { a, _ -> a }))

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
    dokiBuildThemeDefinition: Pair<Path, DokiBuildThemeDefinition>,
    dokiThemeTemplates: Map<String, ThemeTemplateDefinition>,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    val (dokiThemeDefinitionPath, themeDefinition) = dokiBuildThemeDefinition
    val resourceDirectory = getResourceDirectory(themeDefinition)
    if (!exists(resourceDirectory)) {
      createDirectories(resourceDirectory)
    }

    val themeJson = get(resourceDirectory.toString(), "${themeDefinition.usableName}.theme.json")

    if (exists(themeJson)) {
      delete(themeJson)
    }

    val templateName = if (themeDefinition.dark) "dark" else "light"
    val topThemeDefinition =
      dokiThemeTemplates[templateName] ?: throw IllegalStateException("Theme $templateName does not exist.")
    val finalTheme = IntellijDokiThemeDefinition(
      name = "${getLafNamePrefix(themeDefinition.group)}${themeDefinition.name}",
      displayName = themeDefinition.displayName,
      dark = themeDefinition.dark,
      author = themeDefinition.author,
      editorScheme = createEditorScheme(
        themeDefinition,
        dokiThemeDefinitionPath,
        dokiEditorThemeTemplates
      ),
      group = themeDefinition.group,
      stickers = remapStickers(
        themeDefinition,
        dokiThemeDefinitionPath
      ),
      colors = validateColors(themeDefinition),
      ui = getUIDef(
        themeDefinition.ui,
        topThemeDefinition,
        dokiThemeTemplates
      ),
      icons = getIcons(themeDefinition.colors, topThemeDefinition, dokiThemeTemplates)
    )

    newBufferedWriter(themeJson, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        gson.toJson(finalTheme, writer)
      }
    return extractResourcesPath(themeJson)
  }

  private fun validateColors(themeDefinition: DokiBuildThemeDefinition): Map<String, Any> {
    val colorz = themeDefinition.colors
    val colorsSchema = themeSchema.properties["colors"]?.required?.toSet()
      ?: throw IllegalStateException("doki.theme.schema.json is missing required attribute 'properties.colors.required'!")
    val missingColors = colorsSchema
      .filter { !isColorCode(colorz[it] as? String) }
    return if (missingColors.isEmpty()) colorz
    else throw IllegalArgumentException(
      """Theme definition for ${themeDefinition.name} is missing colors: 
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
    themeDefinition: DokiBuildThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): BuildStickers {
    val stickers = themeDefinition.stickers
    val separator = File.separator
    val stickerDirectory = buildStickerPath(separator, themeDefinition)
    val localStickerPath = get(getResourcesDirectory().toString(), stickerDirectory)

    if (!exists(localStickerPath)) {
      createDirectories(localStickerPath)
    }

    val localDefaultStickerPath = get(localStickerPath.toString(), stickers.default)
    if (exists(localDefaultStickerPath)) {
      delete(localDefaultStickerPath)
    }

    copy(get(dokiThemeDefinitionPath.parent.toString(), stickers.default), localDefaultStickerPath)

    val secondarySticker = Optional.ofNullable(stickers.secondary)

    secondarySticker
      .map { get(localStickerPath.toString(), it) }
      .ifPresent {
        if (exists(it)) {
          delete(it)
        }
        copy(get(dokiThemeDefinitionPath.parent.toString(), stickers.secondary), it)
      }

    val stickerResourcesDirectory = buildStickerPath("/", themeDefinition)
    val defaultStickerResourcesPath = "$stickerResourcesDirectory${stickers.default}"
    return BuildStickers(
      defaultStickerResourcesPath,
      secondarySticker.map { "$stickerResourcesDirectory$it" }.orElseGet { null }
    )
  }

  private fun buildStickerPath(separator: String?, themeDefinition: DokiBuildThemeDefinition) =
    "${separator}stickers${separator}${themeDefinition.usableGroup.toLowerCase()}${separator}${themeDefinition.usableName}${separator}"

  private fun getResourceDirectory(themeDefinition: DokiBuildThemeDefinition): Path = get(
    getResourcesDirectory().toString(),
    "doki",
    "themes",
    themeDefinition.usableGroup.toLowerCase()
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
  ): Map<String, Any> = getAllEntries(topThemeDef, themeDefs) { it.icons ?: mapOf() }
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
    dokiThemeTemplates: ThemeTemplateDefinition,
    dokiThemeTemplates1: Map<String, ThemeTemplateDefinition>
  ): Map<String, Any> = Stream.of(
    getAllEntries(dokiThemeTemplates, dokiThemeTemplates1) { it.ui },
    ui.entries.stream()
  )
    .flatMap { it }
    .collect(Collectors.toMap({ it.key }, { it.value }, { _, b -> b },
      { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
    )

  private fun getAllEntries(
    dokiThemeTemplates: ThemeTemplateDefinition,
    allThemeTemplates: Map<String, ThemeTemplateDefinition>,
    entryExtractor: (ThemeTemplateDefinition) -> Map<String, Any>
  ): Stream<Map.Entry<String, Any>> = if (dokiThemeTemplates.extends == null) {
    entryExtractor(dokiThemeTemplates).entries.stream()
  } else {
    Stream.concat(
      getAllEntries(
        allThemeTemplates[dokiThemeTemplates.extends]
          ?: throw IllegalStateException("Theme template ${dokiThemeTemplates.extends} is not a valid parent template"),
        allThemeTemplates,
        entryExtractor
      ), entryExtractor(dokiThemeTemplates).entries.stream()
    )
  }

  private fun createEditorScheme(
    dokiDefinition: DokiBuildThemeDefinition,
    dokiThemeDefinitionDirectory: Path,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    return when (val variant = dokiDefinition.editorScheme["type"]) {
      "custom" -> copyXml(dokiDefinition, dokiThemeDefinitionDirectory)
      "template" -> createEditorSchemeFromTemplate(dokiDefinition, dokiEditorThemeTemplates)
      "templateExtension" -> createEditorSchemeFromTemplateExtension(
        dokiDefinition,
        dokiEditorThemeTemplates,
        dokiThemeDefinitionDirectory
      )
      else -> throw IllegalArgumentException("I can't build a theme of type $variant.")
    }
  }

  private fun createEditorSchemeFromTemplateExtension(
    dokiDefinition: DokiBuildThemeDefinition,
    dokiEditorThemeTemplates: Map<String, Node>,
    dokiThemeDefinitionDirectory: Path
  ): String {
    val childTheme = parseXml(
      get(
        dokiThemeDefinitionDirectory.parent.toString(),
        dokiDefinition.editorScheme["file"] as? String
          ?: throw IllegalArgumentException("Missing 'file' from create editor scheme from template extension definition")
      )
    )

    val extendedTheme =
      extendTheme(
        childTheme,
        dokiEditorThemeTemplates
      )

    val themeTemplate = applyColorsToTemplate(extendedTheme, dokiDefinition)
    return createXmlFromDefinition(dokiDefinition, themeTemplate)
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
    dokiDefinition: DokiBuildThemeDefinition,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    val templateName = dokiDefinition.editorScheme["name"]
      ?: throw IllegalArgumentException("Missing 'name' from create editor scheme from template definition")

    val childTheme = (dokiEditorThemeTemplates[templateName]
      ?: throw IllegalArgumentException("Unrecognized template name $templateName"))
    val editorTemplate = extendTheme(childTheme, dokiEditorThemeTemplates)

    val themeTemplate = applyColorsToTemplate(editorTemplate, dokiDefinition)
    return createXmlFromDefinition(dokiDefinition, themeTemplate)
  }

  private fun applyColorsToTemplate(
    editorTemplate: Node,
    dokiDefinition: DokiBuildThemeDefinition
  ): Node {
    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .map { it as Node }
      .forEach {
        when (it.name()) {
          "scheme" -> {
            it.attributes().replace("name", dokiDefinition.name)
          }
          "option" -> {
            val value = it.attribute("value") as? String
            if (value?.contains('$') == true) {
              val (end, replacementColor) = getReplacementColor(value, '$') { templateColor ->
                dokiDefinition.overrides?.editorScheme?.colors?.get(templateColor) as? String
                  ?: dokiDefinition.colors[templateColor] as? String
                  ?: throw IllegalArgumentException("$templateColor is not in ${dokiDefinition.name}'s color definition.")
              }
              it.attributes()["value"] = buildReplacement(replacementColor, value, end)
            } else if (value?.contains('%') == true) {
              val (end, replacementColor) = getReplacementColor(value, '%') { templateColor ->
                dokiDefinition.colors[templateColor] as? String
                  ?: throw IllegalArgumentException("$templateColor is not in ${dokiDefinition.name}'s color definition.")
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
    dokiDefinition: DokiBuildThemeDefinition,
    themeTemplate: Node
  ): String {
    val themeName = dokiDefinition.usableName
    val destination = get(
      getResourceDirectory(dokiDefinition).toString(),
      "$themeName.xml"
    )

    if (exists(destination)) {
      delete(destination)
    }

    writeXmlToFile(destination, themeTemplate)

    return extractResourcesPath(destination)
  }

  private fun copyXml(dokiDefinition: DokiBuildThemeDefinition, dokiThemeDefinitionDirectory: Path): String {
    val customXmlFile = dokiDefinition.editorScheme["file"] as? String
      ?: throw IllegalArgumentException("Missing 'file' from create editor scheme from file copy")
    val destination = get(
      getResourceDirectory(dokiDefinition).toString(),
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
