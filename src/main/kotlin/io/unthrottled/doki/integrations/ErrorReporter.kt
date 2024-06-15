package io.unthrottled.doki.integrations

import com.google.gson.Gson
import com.intellij.ide.IdeBundle
import com.intellij.ide.ui.LafManager
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.util.Consumer
import com.intellij.util.text.DateFormatUtil
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.protocol.Message
import io.sentry.protocol.User
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import java.awt.Component
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class ErrorReporter : ErrorReportSubmitter() {
  override fun getReportActionText(): String = "Report Anonymously"

  companion object {
    init {
      Sentry.setUser(
        User().apply {
          this.id = ThemeConfig.instance.userId
        },
      )
    }
  }

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<in SubmittedReportInfo>,
  ): Boolean {
    return runSafelyWithResult({
      runSafely({
        Sentry.init { options: SentryOptions ->
          options.dsn =
            RestClient.performGet(
              "https://jetbrains.assets.unthrottled.io/doki-theme/sentry-dsn.txt",
            )
              .map { it.trim() }
              .orElse(
                "https://54daf566d8854f7d98e4c09ced2d34c5" +
                  "@o403546.ingest.sentry.io/5266340?maxmessagelength=50000",
              )
        }
      })
      events.forEach {
        Sentry.captureEvent(
          addSystemInfo(
            SentryEvent()
              .apply {
                this.level = SentryLevel.ERROR
                this.serverName = getAppName().second
                this.setExtra("Additional Info", additionalInfo ?: "None")
              },
          ).apply {
            this.message =
              Message().apply {
                this.message = it.throwableText
              }
          },
        )
      }
      true
    }) {
      false
    }
  }

  private fun addSystemInfo(event: SentryEvent): SentryEvent {
    val pair = getAppName()
    val appInfo = pair.first
    val appName = pair.second
    val properties = System.getProperties()
    return event.apply {
      setExtra("App Name", appName)
      setExtra("Build Info", getBuildInfo(appInfo))
      setExtra("JRE", getJRE(properties))
      setExtra("VM", getVM(properties))
      setExtra("System Info", SystemInfo.getOsNameAndVersion())
      setExtra("GC", getGC())
      setExtra("Memory", Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE)
      setExtra("Cores", Runtime.getRuntime().availableProcessors())
      setExtra("Current LAF", LafManager.getInstance()?.currentLookAndFeel?.name ?: "")
      setExtra("Doki Version", ThemeConfig.instance.version)
      setExtra("Doki Config", Gson().toJson(ThemeConfig.instance))
    }
  }

  private fun getJRE(properties: Properties): String {
    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
    val arch = properties.getProperty("os.arch", "")
    return IdeBundle.message("about.box.jre", javaVersion, arch)
  }

  private fun getVM(properties: Properties): String {
    val vmVersion = properties.getProperty("java.vm.name", "unknown")
    val vmVendor = properties.getProperty("java.vendor", "unknown")
    return IdeBundle.message("about.box.vm", vmVersion, vmVendor)
  }

  private fun getGC() =
    ManagementFactory.getGarbageCollectorMXBeans().stream()
      .map { it.name }.collect(Collectors.joining(","))

  private fun getBuildInfo(appInfo: ApplicationInfo): String {
    var buildInfo = IdeBundle.message("about.box.build.number", appInfo.build.asString())
    val cal = appInfo.buildDate
    var buildDate = ""
    if (appInfo.build.isSnapshot) {
      buildDate = SimpleDateFormat("HH:mm, ").format(cal.time)
    }
    buildDate += DateFormatUtil.formatAboutDialogDate(cal.time)
    buildInfo += IdeBundle.message("about.box.build.date", buildDate)
    return buildInfo
  }

  private fun getAppName(): Pair<ApplicationInfo, String> {
    val appInfo = ApplicationInfo.getInstance()
    var appName = appInfo.fullApplicationName
    val edition = ApplicationNamesInfo.getInstance().editionName
    if (edition != null) appName += " ($edition)"
    return Pair(appInfo, appName)
  }
}
