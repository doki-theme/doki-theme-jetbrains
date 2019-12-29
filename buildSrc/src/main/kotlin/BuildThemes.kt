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

    val (pluginXmlAndParsed, extension) = getPluginXmlMutationStuff()
    val (pluginXml, parsedPluginXml) = pluginXmlAndParsed

    getAllDokiThemeDefinitions(get(themeDirectory.toString(), "definitions"))
      .forEach { stuff ->
        val dokiThemeResourcePath = constructIntellijTheme(
          stuff,
          dokiThemeTemplates
        )

        val themeId = stuff.second.id
        addThemeToPluginXml(extension, themeId, dokiThemeResourcePath)
      }

    writeThemeChangesToPluginXml(pluginXml, parsedPluginXml)
  }

  private fun writeThemeChangesToPluginXml(pluginXml: Path, parsedPluginXml: Node) {
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

  private fun getPluginXmlMutationStuff(): Pair<Pair<Path, Node>, Node> {
    val pluginXml = get(
      getResourcesDirectory().toString(),
      "META-INF", "plugin.xml"
    )
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
    return pluginXml to parsedPlugin to (parsedPlugin["extensions"] as NodeList)
      .map { it as Node }
      .find { node ->
        node.attribute("defaultExtensionNs") == "com.intellij"
      }!!
  }

  private fun constructIntellijTheme(
    dokiBuildThemeDefinition: Pair<Path, DokiBuildThemeDefinition>,
    dokiThemeTemplates: Map<String, ThemeTemplateDefinition>
  ): String {
    val (definitionDirectory, themeDefinition) = dokiBuildThemeDefinition
    val resourceDirectory = getResourceDirectory(themeDefinition)
    if (!exists(resourceDirectory)) {
      createDirectories(resourceDirectory)
    }

    val themeJson = get(resourceDirectory.toString(), "${themeDefinition.name}.theme.json")

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
      editorScheme = createEditorScheme(themeDefinition, dokiThemeTemplates, definitionDirectory),
      group = themeDefinition.group,
      stickers = remapStickers(
        themeDefinition,
        extractResourcesPath(themeJson.parent)
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

  // todo: copy stickers
  private fun remapStickers(themeDefinition: DokiBuildThemeDefinition, extractResourcesPath: String): BuildStickers {
    val stickers = themeDefinition.stickers
    return BuildStickers(
      "$extractResourcesPath/${stickers.default}",
      if (stickers.secondary != null) "$extractResourcesPath/${stickers.secondary}" else null
    )
  }

  private fun getResourceDirectory(themeDefinition: DokiBuildThemeDefinition): Path = get(
    getResourcesDirectory().toString(),
    "doki",
    "themes",
    themeDefinition.group.toLowerCase()
  )

  private fun getResourcesDirectory(): Path = get(
    project.rootDir.absolutePath,
    "src",
    "main",
    "resources"
  )

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
            { a, b -> b },
            { TreeMap(Comparator.comparing { item -> item.toLowerCase() }) })
          )
        entry.key to updatedPalette
      } else {
        entry.key to entry.value
      }
    }
    .collect(Collectors.toMap({ it.first }, { it.second }, { a, b -> b },
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
    .collect(Collectors.toMap({ it.key }, { it.value }, { a, b -> b },
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
    dokiThemeTemplates: Map<String, ThemeTemplateDefinition>,
    dokiThemeDefinitionDirectory: Path
  ): String {
    return when (dokiDefinition.editorScheme["type"]) {
      "custom" -> copyXml(dokiDefinition, dokiThemeDefinitionDirectory)
      else -> TODO("THEME TEMPLATE NOT SUPPORTED YET")
    }
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
