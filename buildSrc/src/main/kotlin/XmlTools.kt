import groovy.util.Node
import groovy.util.XmlNodePrinter
import groovy.util.XmlParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import java.io.BufferedOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

fun writeXmlToFile(pluginXml: Path, parsedPluginXml: Node) {
  Files.newOutputStream(pluginXml).use {
    val outputStream = BufferedOutputStream(it)
    val writer = PrintWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
    val printer = XmlNodePrinter(writer)
    printer.isPreserveWhitespace = true
    printer.print(parsedPluginXml)
  }
}


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
