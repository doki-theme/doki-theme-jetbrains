import com.google.gson.GsonBuilder
import groovy.util.Node
import groovy.util.NodeList
import groovy.util.XmlNodePrinter
import groovy.util.XmlParser
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.BufferedOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

open class BuildThemes : DefaultTask() {

  private val gson = GsonBuilder().setPrettyPrinting().create()

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run() {
    val themeDirectory = get(project.rootDir.absolutePath, "themes")
    val dokiThemeTemplates = createThemeDefinitions(themeDirectory)

    val dokiEditorThemeTemplates = createEditorThemeDefinitions(themeDirectory)

    val (pluginXmlAndParsed, extension) = getPluginXmlMutationStuff()
    val (pluginXml, parsedPluginXml) = pluginXmlAndParsed

    getAllDokiThemeDefinitions(get(themeDirectory.toString(), "definitions"))
      .forEach { stuff ->
        val dokiThemeResourcePath = constructIntellijTheme(
          stuff,
          dokiThemeTemplates,
          dokiEditorThemeTemplates
        )

        val themeId = stuff.second.id
        addThemeToPluginXml(extension, themeId, dokiThemeResourcePath)
      }

    writeXmlToFile(pluginXml, parsedPluginXml)
  }

  private fun writeXmlToFile(pluginXml: Path, parsedPluginXml: Node) {
    newOutputStream(pluginXml).use {
      val outputStream = BufferedOutputStream(it)
      val writer = PrintWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
      val printer = XmlNodePrinter(writer)
      printer.isPreserveWhitespace = true
      printer.print(parsedPluginXml)
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

  private fun getAllDokiThemeDefinitions(dokiThemeDefinitionDirectory: Path): Stream<Pair<Path, DokiBuildThemeDefinition>> {
    return walk(dokiThemeDefinitionDirectory)
      .filter { !isDirectory(it) }
      .filter { it.fileName.toString().endsWith(".doki.json") }
      .map { it to newInputStream(it) }
      .map {
        it.first to gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          DokiBuildThemeDefinition::class.java
        )
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

  private fun parseXml(pluginXml: Path): Node {
    val parsedPlugin = newInputStream(pluginXml).use { input ->
      val inputSource = InputSource(InputStreamReader(input, "UTF-8"))
      val parser = XmlParser(false, true, true)
      parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
      parser.errorHandler = object : ErrorHandler {
        override fun warning(exception: SAXParseException?) {

        }

        override fun error(exception: SAXParseException?) {

        }

        override fun fatalError(exception: SAXParseException) {
          throw exception
        }
      }

      parser.parse(inputSource)
    }
    return parsedPlugin
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
      name = themeDefinition.name,
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
      colors = TreeMap(themeDefinition.colors),
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

  private fun remapStickers(
    themeDefinition: DokiBuildThemeDefinition,
    dokiThemeDefinitionPath: Path
  ): BuildStickers {
    val stickers = themeDefinition.stickers
    val stickerDirectory = "/stickers/${themeDefinition.usableGroup.toLowerCase()}/${themeDefinition.usableName}/"
    val localStickerPath = get(getResourcesDirectory().toString(), stickerDirectory)

    if(!exists(localStickerPath)){
      createDirectories(localStickerPath)
    }

    val localDefaultStickerPath = get(localStickerPath.toString(), stickers.default)
    if(exists(localDefaultStickerPath)){
      delete(localDefaultStickerPath)
    }

    copy(get(dokiThemeDefinitionPath.parent.toString(), stickers.default), localDefaultStickerPath)

    val secondarySticker = Optional.ofNullable(stickers.secondary)

    secondarySticker
      .map { get(localStickerPath.toString(), it) }
      .ifPresent {
        if(exists(it)){
          delete(it)
        }
        copy(get(dokiThemeDefinitionPath.parent.toString(), stickers.secondary), it)
      }

    val defaultStickerResourcesPath = "$stickerDirectory${stickers.default}"
    return BuildStickers(
      defaultStickerResourcesPath,
      secondarySticker.map { "$stickerDirectory$it" }.orElseGet { null }
    )
  }

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
    val childTheme = parseXml(get(
      dokiThemeDefinitionDirectory.parent.toString(),
      dokiDefinition.editorScheme["file"] as String
    ))

    val extendedTheme =
      extendTheme(
        childTheme,
        dokiEditorThemeTemplates
      )

    return createXmlFromDefinition(dokiDefinition, extendedTheme)
  }

  // todo: theme extension
  private fun extendTheme(childTheme: Node, dokiEditorThemeTemplates: Map<String, Node>): Node {
    return childTheme
  }

  private fun createEditorSchemeFromTemplate(
    dokiDefinition: DokiBuildThemeDefinition,
    dokiEditorThemeTemplates: Map<String, Node>
  ): String {
    val templateName = dokiDefinition.editorScheme["name"]
    val editorTemplate = dokiEditorThemeTemplates[templateName]
      ?: throw IllegalArgumentException("Unrecognized template name $templateName")

    val themeTemplate = editorTemplate.clone() as Node
    themeTemplate.breadthFirst()
      .map { it as Node }
      .forEach{
        when (it.name()){
          "scheme" -> {
            it.attributes().replace("name", dokiDefinition.name)
          }
          "option" -> {
            val value = it.attribute("value") as? String
            if(value?.contains('$') == true){
              val start = value.indexOf('$')
              val end = value.lastIndexOf('$')
              val templateColor = value.subSequence(start+1, end)
              val replacementHexColor = dokiDefinition.colors[templateColor] as? String
                ?: throw IllegalArgumentException("$templateColor is not in ${dokiDefinition.name}'s color definition.")
              val replacementColor = replacementHexColor.substring(1)
              it.attributes()["value"] = "$replacementColor${value.substring(end + 1)}"
            }
          }
        }
    }

    return createXmlFromDefinition(dokiDefinition, themeTemplate)
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
    val customXmlFile = dokiDefinition.editorScheme["file"] as String
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
    return fullResourcesPath.substring(fullResourcesPath.indexOf("/doki/theme"))
  }
}
