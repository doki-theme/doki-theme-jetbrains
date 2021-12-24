package io.unthrottled.doki.integrations.grep

import com.intellij.execution.filters.ConsoleDependentFilterProvider
import com.intellij.execution.filters.Filter
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

class GrepConsole : ConsoleDependentFilterProvider() {

  companion object {
    private val emptyFilter = emptyArray<Filter>()
  }

  override fun getDefaultFilters(consoleView: ConsoleView, project: Project, scope: GlobalSearchScope): Array<Filter> {
    ConsoleViewService.instance.registerConsoleWindow(consoleView)
    return emptyFilter
  }

}