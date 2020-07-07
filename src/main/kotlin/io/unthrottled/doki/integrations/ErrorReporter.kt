package io.unthrottled.doki.integrations

import com.intellij.ide.IdeBundle
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.ui.LafManager
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
import io.sentry.DefaultSentryClientFactory
import io.sentry.SentryClient
import io.sentry.dsn.Dsn
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import io.sentry.event.UserBuilder
import io.unthrottled.doki.config.ThemeConfig
import java.awt.Component
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Properties
import java.util.stream.Collectors

class ErrorReporter : ErrorReportSubmitter() {
  override fun getReportActionText(): String = "Report Anonymously"

  companion object {
    private val sentryClient: SentryClient =
      DefaultSentryClientFactory().createSentryClient(Dsn(
        RestClient.performGet(
          "https://jetbrains.assets.unthrottled.io/doki-theme/sentry-dsn.txt"
        )
          .map { it.trim() }
          .orElse("https://54daf566d8854f7d98e4c09ced2d34c5@o403546.ingest.sentry.io/5266340?maxmessagelength=50000")
      ))

  }

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<SubmittedReportInfo>
  ): Boolean {
    return try {
      events.forEach {
        sentryClient.context.user =
          UserBuilder().setId(ThemeConfig.instance.userId).build()
        sentryClient.sendEvent(
          addSystemInfo(
            EventBuilder()
              .withLevel(Event.Level.ERROR)
              .withServerName(getAppName().second)
              .withExtra("Message", it.message)
              .withExtra("Additional Info", additionalInfo ?: "None")
          ).withMessage(it.throwableText)
        )
        sentryClient.clearContext()
      }
      true
    } catch (e: Exception) {
      false
    }
  }

  private fun addSystemInfo(event: EventBuilder): EventBuilder {
    val pair = getAppName()
    val appInfo = pair.first
    val appName = pair.second
    val properties = System.getProperties()
    return event
      .withExtra("App Name", appName)
      .withExtra("Build Info", getBuildInfo(appInfo))
      .withExtra("JRE", getJRE(properties))
      .withExtra("VM", getVM(properties))
      .withExtra("System Info", SystemInfo.getOsNameAndVersion())
      .withExtra("GC", getGC())
      .withExtra("Memory", Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE)
      .withExtra("Cores", Runtime.getRuntime().availableProcessors())
      .withExtra("Registry", getRegistry())
      .withExtra("Non-Bundled Plugins", getNonBundledPlugins())
      .withExtra("Current LAF", LafManager.getInstance().currentLookAndFeel?.name)
      .withExtra("Doki Config", ThemeConfig.instance.asJson())
  }

  private fun getJRE(properties: Properties): String? {
    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
    val arch = properties.getProperty("os.arch", "")
    return IdeBundle.message("about.box.jre", javaVersion, arch)
  }

  private fun getVM(properties: Properties): String? {
    val vmVersion = properties.getProperty("java.vm.name", "unknown")
    val vmVendor = properties.getProperty("java.vendor", "unknown")
    return IdeBundle.message("about.box.vm", vmVersion, vmVendor)
  }

  private fun getNonBundledPlugins(): String? {
    return Arrays.stream(PluginManagerCore.getPlugins())
      .filter { p -> !p.isBundled && p.isEnabled }
      .map { p -> p.pluginId.idString }.collect(Collectors.joining(","))
  }

  private fun getRegistry() = Registry.getAll().stream().filter { it.isChangedFromDefault }
    .map { v -> v.key + "=" + v.asString() }.collect(Collectors.joining(","))

  private fun getGC() = ManagementFactory.getGarbageCollectorMXBeans().stream()
    .map { it.name }.collect(Collectors.joining(","))

  private fun getBuildInfo(appInfo: ApplicationInfoImpl): String? {
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

  private fun getAppName(): Pair<ApplicationInfoImpl, String> {
    val appInfo = ApplicationInfoEx.getInstanceEx() as ApplicationInfoImpl
    var appName = appInfo.fullApplicationName
    val edition = ApplicationNamesInfo.getInstance().editionName
    if (edition != null) appName += " ($edition)"
    return Pair(appInfo, appName)
  }
}
