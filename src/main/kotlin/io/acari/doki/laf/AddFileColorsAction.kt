package io.acari.doki.laf

import com.intellij.openapi.project.Project
import com.intellij.psi.search.scope.NonProjectFilesScope
import com.intellij.psi.search.scope.TestsScope
import com.intellij.ui.ColorUtil
import com.intellij.ui.FileColorManager
import com.intellij.ui.tabs.FileColorManagerImpl
import com.intellij.ui.tabs.FileColorsModel
import io.acari.doki.themes.ThemeManager
import java.lang.reflect.Constructor
import java.util.*
import java.util.stream.Collectors

object DokiAddFileColorsAction {

  fun removeFileScopes(project: Project?) {
    if (project != null)
      replaceFileScopes(project) { emptyList() }
  }

  fun isSet(project: Project?): Boolean {
    return ThemeManager.instance.currentTheme
      .map { selectedTheme ->
        val manager = FileColorManager.getInstance(project!!)
        val quickScope = manager.getScopeColor(NonProjectFilesScope.NAME)
        quickScope != null &&
          selectedTheme.nonProjectFileScopeColor ==
          ColorUtil.toHex(quickScope)
      }.orElse(false)

  }

  fun setFileScopes(project: Project?) {
    if (project != null)
      replaceFileScopes(project) {
        try {
          mutableList(getScopes(), it)
        } catch (e: Throwable) {
          Collections.emptyList<Any>()
        }
      }
  }

  private fun getScopes(): List<Pair<String, String>> =
    ThemeManager.instance.currentTheme
      .map { selectedTheme ->
        listOf(
          Pair(NonProjectFilesScope.NAME, selectedTheme.nonProjectFileScopeColor),
          Pair(TestsScope.NAME, selectedTheme.testScopeColor),
          Pair("Local Unit Tests", selectedTheme.testScopeColor),
          //  dis android bundle -> String message = AndroidBundle.message("android.test.run.configuration.type.name");
          Pair("Android Instrumented Tests", selectedTheme.testScopeColor)
        )
      }.orElseGet { Collections.emptyList() }


  private fun replaceFileScopes(
    project: Project?,
    scopeSupplier: (Constructor<out Any>) -> List<Any>
  ) {
    try {
      /**
       * "I don't know who you are.
       * I don't know what you want.
       * If you are looking for encapsulation I can tell you I don't have have access right now,
       * but what I do have are a very particular set of skills.
       * Skills I have acquired over a very long career.
       * Skills that make me a nightmare for people like you.
       * If you let me use your class right now that'll be the end of it.
       * I will not look for you, I will not pursue you, but if you don't,
       * I will look for you, I will find you and I will use your classes.
       */
      val manager = FileColorManager.getInstance(project!!) as FileColorManagerImpl
      val getMode = FileColorManagerImpl::class.java.getDeclaredMethod("getModel")
      getMode.isAccessible = true
      val model = getMode.invoke(manager) as FileColorsModel
      val fileColorConfiguration = Class.forName("com.intellij.ui.tabs.FileColorConfiguration")
      val constructor = fileColorConfiguration.getDeclaredConstructor(String::class.java, String::class.java)
      constructor.isAccessible = true
      val collect: List<Any> = scopeSupplier(constructor)
      val setConfig =
        FileColorsModel::class.java.getDeclaredMethod("setConfigurations", List::class.java, Boolean::class.java)
      setConfig.invoke(model, collect, false)
    } catch (e: Exception) {
      e.printStackTrace()
      val manager = FileColorManager.getInstance(project!!)
      getScopes().forEach { manager.addScopeColor(it.first, it.second, false) }
    }
  }
}

private fun mutableList(scopes: List<Pair<String, String>>, constructor: Constructor<out Any>): MutableList<Any> =
  scopes.stream()
    .map { constructor.newInstance(it.first, it.second) }
    .collect(Collectors.toList())