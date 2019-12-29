import com.google.gson.Gson
import groovy.util.Node
import groovy.util.XmlParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun parseXml(pluginXml: Path): Node {
  val parsedPlugin = Files.newInputStream(pluginXml).use { input ->
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

fun main() {
  val themePath =
    Paths.get("/home/alex/workspace/ddlc-jetbrains-theme/src/main/resources/doki/themes/literature/Sayori_Light.xml")

  val templatePath = Paths.get("/home/alex/workspace/ddlc-jetbrains-theme/themes/templates/light.template.xml")

  val preExistingThemeXml = parseXml(themePath)

  val templateXml = parseXml(templatePath)

  val templateColors = templateXml.breadthFirst()
    .map { it as Node }
    .map {
      when (it.name()) {
        "option" -> {
          val value = it.attribute("value") as? String
          if (value?.contains('$') == true) {
            val start = value.indexOf('$')
            val end = value.lastIndexOf('$')
            val templateColor = value.subSequence(start + 1, end)
            templateColor to getMeaningfulName(it) as String
          } else {
            null
          }
        }
        else -> null
      }
    }.filterIsInstance<Pair<String, String>>()
    .toMap()
    .map { it.value to it.key }
    .toMap()

  println(templateColors)
  val colourz = preExistingThemeXml.breadthFirst()
    .map { it as Node }
    .filter { it.name() == "option" }
    .map {
      val nam = getMeaningfulName(it)
      templateColors[nam] to it.attribute("value")
    }
    .filter { it.first != null }
    .toMap()

  println(colourz.size)
  println(templateColors.size)
  println(
    Gson().toJson(colourz)
  )
}

private fun getMeaningfulName(it: Node): Any? {
  return if (it.parent().name() == "value") {
    it.parent().parent().attribute("name")
  } else {
    it.attribute("name")
  }
}

