package io.acari.DDLC.integrations

import com.google.gson.GsonBuilder
import com.intellij.ide.IdeBundle
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.ui.LicensingFacade
import com.intellij.util.Consumer
import com.intellij.util.text.DateFormatUtil
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import java.awt.Component
import java.text.SimpleDateFormat
import java.util.ArrayList

class SlackReporter: ErrorReportSubmitter() {
  companion object{
    val httpClient = HttpClients.createMinimal()
    val slackWebhook = "https://hooks.slack.com/services/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"
    val gson = GsonBuilder().create()
  }
  override fun getReportActionText(): String = "Report Anonymously"

  override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<SubmittedReportInfo>): Boolean {
    return try {
      val httpPost = HttpPost(slackWebhook)
      val eventMessages = events.map {
        """
          Data: ${it.data}
          Message: ${it.message}
          Throwable Text: ${it.throwableText}
        """.trimIndent()
      }.joinToString(separator = "\n")
      val supplementedInfo = if(additionalInfo != null) """
        $eventMessages
        Additional Info: $additionalInfo
      """.trimIndent() else eventMessages
      val formattedMessage = "```$supplementedInfo```"
      val message = SlackMessage(formattedMessage)
      httpPost.entity = StringEntity(gson.toJson(message), ContentType.APPLICATION_JSON)
      httpClient.execute(httpPost)
      true
    } catch (e: Exception) {
      false
    }
  }



  fun getSystemInfo(){
//    val myInfo = StringBuilder()
//    val appInfo = ApplicationInfoEx.getInstanceEx() as ApplicationInfoImpl
//    var appName = appInfo.getFullApplicationName()
//    val edition = ApplicationNamesInfo.getInstance().editionName
//    if (edition != null) appName += " ($edition)"
//    myInfo.append(myLines.get(myLines.size() - 1).getText()).append("\n");
//
//    var buildInfo = IdeBundle.message("about.box.build.number", appInfo.getBuild().asString())
//    val cal = appInfo.getBuildDate()
//    var buildDate = ""
//    if (appInfo.getBuild().isSnapshot()) {
//      buildDate = SimpleDateFormat("HH:mm, ").format(cal.getTime())
//    }
//    buildDate += DateFormatUtil.formatAboutDialogDate(cal.getTime())
//    buildInfo += IdeBundle.message("about.box.build.date", buildDate)
//    myLines.add(AboutBoxLine(buildInfo))
//    myInfo.append(myLines.get(myLines.size() - 1).getText()).append("\n");
//
//
//
//    myLines.add(AboutBoxLine(""))
//
//    val properties = System.getProperties()
//    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
//    val arch = properties.getProperty("os.arch", "")
//    myLines.add(AboutBoxLine(IdeBundle.message("about.box.jre", javaVersion, arch)))
//    myInfo.append(myLines.get(myLines.size() - 1).getText()).append("\n");
//
//    val vmVersion = properties.getProperty("java.vm.name", "unknown")
//    val vmVendor = properties.getProperty("java.vendor", "unknown")
//    myLines.add(AboutBoxLine(IdeBundle.message("about.box.vm", vmVersion, vmVendor)))
//    myInfo.append(myLines.get(myLines.size() - 1).getText()).append("\n");
//
//    myLines.add(AboutBoxLine(""))
//    myLines.add(AboutBoxLine(""))
//    myLines.add(AboutBoxLine(IdeBundle.message("about.box.powered.by") + " ").keepWithNext())
//
//    val thirdPartyLibraries = loadThirdPartyLibraries()
//    if (thirdPartyLibraries != null) {
//      myLines.add(AboutBoxLine(IdeBundle.message("about.box.open.source.software")
//      ) { showOpenSoftwareSources(thirdPartyLibraries!!) })
//    } else {
//      // When compiled from sources, third-party-libraries.html file isn't generated, so window can't be shown
//      myLines.add(AboutBoxLine(IdeBundle.message("about.box.open.source.software")))
//    }

  }



}

data class SlackMessage(val text: String)