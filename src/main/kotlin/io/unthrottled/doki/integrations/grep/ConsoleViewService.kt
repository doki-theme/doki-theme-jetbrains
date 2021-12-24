package io.unthrottled.doki.integrations.grep

import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import java.util.concurrent.ConcurrentSkipListSet
import java.util.stream.Stream

class ConsoleViewService {

  companion object {
    val instance: ConsoleViewService = ApplicationManager.getApplication()
      .getService(ConsoleViewService::class.java)
  }

  private val consoleViews = ConcurrentSkipListSet<ConsoleView> { a, b ->
    a.hashCode().compareTo(b.hashCode())
  }


  fun registerConsoleWindow(consoleView: ConsoleView) {
    if (consoleViews.add(consoleView)) {
      Disposer.register(consoleView) {
        consoleViews.remove(consoleView)
      }
    }
  }

  val consoles: Stream<ConsoleView>
    get() = consoleViews.stream()
}