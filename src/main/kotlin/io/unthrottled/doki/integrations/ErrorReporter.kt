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
import io.sentry.Sentry
import io.sentry.event.UserBuilder
import io.unthrottled.doki.config.ThemeConfig
import java.awt.Component
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.stream.Collectors


class ErrorReporter : ErrorReportSubmitter() {
  companion object;

  override fun getReportActionText(): String = "Report Anonymously"

  override fun submit(
    events: Array<out IdeaLoggingEvent>,
    additionalInfo: String?,
    parentComponent: Component,
    consumer: Consumer<SubmittedReportInfo>
  ): Boolean {
    return try {
      events.forEach {
        Sentry.getContext().user =
          UserBuilder().setId(ThemeConfig.instance.userId).build()
        Sentry.getContext().addExtra(
          "Message",
          it.message
        )
        Sentry.getContext().addExtra(
          "Data",
          it.data
        )
        if (additionalInfo != null) {
          Sentry.getContext().addExtra(
            "Additional Info",
            additionalInfo
          )
        }
        addSystemInfo()
        Sentry.capture(it.throwable)
        Sentry.clearContext()
      }
      true
    } catch (e: Exception) {
      false
    }
  }

  private fun addSystemInfo() {
    val appInfo = ApplicationInfoEx.getInstanceEx() as ApplicationInfoImpl
    var appName = appInfo.fullApplicationName
    val edition = ApplicationNamesInfo.getInstance().editionName
    if (edition != null) appName += " ($edition)"
    Sentry.getContext().addExtra("App Name", appName)

    var buildInfo = IdeBundle.message("about.box.build.number", appInfo.build.asString())
    val cal = appInfo.buildDate
    var buildDate = ""
    if (appInfo.build.isSnapshot) {
      buildDate = SimpleDateFormat("HH:mm, ").format(cal.time)
    }
    buildDate += DateFormatUtil.formatAboutDialogDate(cal.time)
    buildInfo += IdeBundle.message("about.box.build.date", buildDate)
    Sentry.getContext().addExtra("Build Info", buildInfo)

    val properties = System.getProperties()
    val javaVersion = properties.getProperty("java.runtime.version", properties.getProperty("java.version", "unknown"))
    val arch = properties.getProperty("os.arch", "")
    Sentry.getContext().addExtra("JRE", IdeBundle.message("about.box.jre", javaVersion, arch))

    val vmVersion = properties.getProperty("java.vm.name", "unknown")
    val vmVendor = properties.getProperty("java.vendor", "unknown")
    Sentry.getContext().addExtra("VM", IdeBundle.message("about.box.vm", vmVersion, vmVendor))

    Sentry.getContext().addExtra("System Info", SystemInfo.getOsNameAndVersion())
    Sentry.getContext().addExtra(
      "GC", ManagementFactory.getGarbageCollectorMXBeans().stream()
        .map { it.name }.collect(Collectors.joining(","))
    )
    Sentry.getContext().addExtra("Memory", Runtime.getRuntime().maxMemory() / FileUtilRt.MEGABYTE)
    Sentry.getContext().addExtra("Cores", Runtime.getRuntime().availableProcessors())
    Sentry.getContext().addExtra("Registry", Registry.getAll().stream().filter { it.isChangedFromDefault }
      .map { v -> v.key + "=" + v.asString() }.collect(Collectors.joining(",")))
    Sentry.getContext().addExtra("Non-Bundled Plugins", Arrays.stream(PluginManagerCore.getPlugins())
      .filter { p -> !p.isBundled && p.isEnabled }
      .map { p -> p.pluginId.idString }.collect(Collectors.joining(","))
    )


    Sentry.getContext().addExtra("Current LAF", LafManager.getInstance().currentLookAndFeel?.name)

    Sentry.getContext().addExtra("Doki Config", ThemeConfig.instance.asJson())
  }
}

