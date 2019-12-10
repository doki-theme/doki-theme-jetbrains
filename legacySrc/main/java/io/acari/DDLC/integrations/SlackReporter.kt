package io.acari.DDLC.integrations

import com.chrisrm.ideaddlc.utils.MTUiUtils
import com.google.gson.GsonBuilder
import com.intellij.ide.IdeBundle
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.util.registry.Registry
import com.intellij.util.Consumer
import com.intellij.util.text.DateFormatUtil
import io.acari.DDLC.DDLCConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import java.awt.Component
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class SlackReporter : ErrorReportSubmitter() {
  companion object {
    private val httpClient = HttpClients.createMinimal()
    private const val slackWebhook = "https://hooks.slack.com/services/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"
    private val gson = GsonBuilder().create()
  }

  override fun getReportActionText(): String = "Report Anonymously"

  override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<SubmittedReportInfo>): Boolean {
    return try {
      val httpPost = HttpPost(slackWebhook)
      val eventMessages = events.map {
        """_Message_: ${it.message}
          |_Throwable Text_: ${it.throwableText}""".trimMargin()
      }.joinToString(separator = "\n")
      val supplementedInfo = if (additionalInfo != null) """$eventMessages
        Additional Info: $additionalInfo
      """.trimIndent() else eventMessages
      val formattedMessage = """
        *System Info*:
        ```${getSystemInfo()}```
        *Error Info*:
        $supplementedInfo""".trimIndent()
      val message = SlackMessage(formattedMessage)
      httpPost.entity = StringEntity(gson.toJson(message), ContentType.APPLICATION_JSON)
      httpClient.execute(httpPost)
      true
    } catch (e: Exception) {
      false
    }
  }

  private fun getSystemInfo(): String {
    val myInfo = StringBuilder()
    val appInfo = ApplicationInfoEx.getInstanceEx() as ApplicationInfoImpl
    var appName = appInfo.fullApplicationName
    val edition = ApplicationNamesInfo.getInstance().editionName
    if (edition != null) appName += " ($edition)"
    myInfo.append(appName).append("\n")

    var buildInfo = IdeBundle.message("about.box.build.number", appInfo.build.asString())
    val cal = appInfo.buildDate
    var buildDate = ""
    if (appInfo.build.isSnapshot) {
      buildDate = SimpleDateFormat("HH:mm, ").format(cal.time)
    }
    buildDate += DateFormatUtil.formatAboutDialogDate(cal.time)
    buildInfo += IdeBundle.message("about.box.build.date", buildDate)
    myInfo.append(buildInfo).append("\n")

    val properties = System.getProperties()
    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
    val arch = properties.getProperty("os.arch", "")
    myInfo.append(IdeBundle.message("about.box.jre", javaVersion, arch)).append("\n")

    val vmVersion = properties.getProperty("java.vm.name", "unknown")
    val vmVendor = properties.getProperty("java.vendor", "unknown")
    myInfo.append(IdeBundle.message("about.box.vm", vmVersion, vmVendor)).append("\n")

    val dokiConfig = DDLCConfig.getInstance()
    return """$myInfo${extraInfo()}
      |Doki-Doki Config: ${dokiConfig.asJson()}
      |Doki-Doki Version: ${MTUiUtils.getVersion()}
      |Doki-Doki DarkMode: ${dokiConfig.isDarkMode}""".trimMargin()
  }

  private fun extraInfo(): String {
    return SystemInfo.getOsNameAndVersion() + "\n" +
        "GC: " + ManagementFactory.getGarbageCollectorMXBeans().stream()
        .map<String> { it.name }.collect(Collectors.joining(",")) + "\n" +

        "Memory: " + Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE + "M" + "\n" +

        "Cores: " + Runtime.getRuntime().availableProcessors() + "\n" +

        "Registry: " + Registry.getAll().stream().filter { it.isChangedFromDefault }
        .map { v -> v.key + "=" + v.asString() }.collect(Collectors.joining(",")) + "\n" +

        "Non-Bundled Plugins: " + Arrays.stream(PluginManagerCore.getPlugins()).filter { p -> !p.isBundled && p.isEnabled }
        .map { p -> p.pluginId.idString }.collect(Collectors.joining(","))
  }

}

data class SlackMessage(val text: String)