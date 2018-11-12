/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package io.acari.DDLC.actions

import com.chrisrm.ideaddlc.MTAnalytics
import com.chrisrm.ideaddlc.messages.MaterialThemeBundle
import com.chrisrm.ideaddlc.utils.Notify
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.search.scope.NonProjectFilesScope
import com.intellij.psi.search.scope.TestsScope
import com.intellij.ui.FileColorManager
import com.intellij.ui.tabs.FileColorManagerImpl
import com.intellij.ui.tabs.FileColorsModel
import io.acari.DDLC.DDLCConfig
import java.lang.reflect.Constructor
import java.util.stream.Collectors
import javax.swing.event.HyperlinkEvent

class DDLCAddFileColorsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        addDisabledFileColors(e.project)
    }

    private fun addDisabledFileColors(project: Project?) {
        setFileScopes(project)

        Notify.show(project!!,
                "",
                MaterialThemeBundle.message("mt.fileColorsInstalled"),
                NotificationType.INFORMATION,
                object : NotificationListener.Adapter() {
                    override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                        ApplicationManager.getApplication().invokeLater({
                            ShowSettingsUtil.getInstance().showSettingsDialog(
                                    project,
                                    "File Colors")
                        }, ModalityState.NON_MODAL)
                    }
                })

        MTAnalytics.getInstance().track(MTAnalytics.ADD_FILE_COLORS)
    }

    fun removeFileScopes(project: Project?) {
        if (project != null)
            replaceFileScopes(project) { a, b -> emptyList() }
    }

    fun setFileScopes(project: Project?) {
        if (project != null)
            replaceFileScopes(project, this::mutableList)

    }

    fun replaceFileScopes(project: Project?, scopeGenerator: (List<Pair<String, String>>, Constructor<out Any>) -> List<Any>) {
        val selectedTheme = DDLCConfig.getInstance().getSelectedTheme()
        val scopes = listOf(
                Pair(NonProjectFilesScope.NAME, selectedTheme.nonProjectFileScopeColor),
                Pair(TestsScope.NAME, selectedTheme.testScope),
                Pair("Local Unit Tests", selectedTheme.testScope),
                //  dis android bundle -> String message = AndroidBundle.message("android.test.run.configuration.type.name");
                Pair("Android Instrumented Tests", selectedTheme.testScope))


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
            val collect: List<Any> = scopeGenerator(scopes, constructor)
            val setConfig = FileColorsModel::class.java.getDeclaredMethod("setConfigurations", List::class.java, Boolean::class.java)
            setConfig.invoke(model, collect, false)
        } catch (e: Exception) {
            e.printStackTrace()
            val manager = FileColorManager.getInstance(project!!)
            scopes.forEach { manager.addScopeColor(it.first, it.second, false) }
        }
    }

    fun mutableList(scopes: List<Pair<String, String>>, constructor: Constructor<out Any>) =
            scopes.stream().map { constructor.newInstance(it.first, it.second) }
                    .collect(Collectors.toList())
}
