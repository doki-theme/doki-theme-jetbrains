/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package io.acari.DDLC.actions.themes.literature.club

import com.chrisrm.ideaddlc.MTAnalytics
import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.actions.accents.MTAbstractAccentAction
import io.acari.DDLC.actions.DDLCAddFileColorsAction
import io.acari.DDLC.tree.DDLCProjectViewNodeDecorator
import com.chrisrm.ideaddlc.ui.MTButtonUI
import com.chrisrm.ideaddlc.ui.MTTreeUI
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.containers.stream
import io.acari.DDLC.DDLCConfig
import io.acari.DDLC.DDLCThemeFacade
import io.acari.DDLC.chibi.ChibiOrchestrator
import java.util.*
import java.util.stream.Stream

open class ClubMemberThemeAction(private val theme: DDLCThemeFacade,
                                 private val accentAction: MTAbstractAccentAction) : BaseThemeAction() {
    private val dokiAddFileColors = DDLCAddFileColorsAction()

    override fun selectionActivation(project: Optional<Project>) {
        super.selectionActivation(project)
        accentAction.setAccentToTheme()
        MTTreeUI.resetIcons()
        MTButtonUI.resetCache()
        DDLCProjectViewNodeDecorator.resetCache()
        MTThemeManager.setLookAndFeel(theme)
        ChibiOrchestrator.activateChibiForTheme(theme)

        project.map {
            Stream.of(it)
        }.orElseGet {
            ProjectManager.getInstance().openProjects.stream()
        }
        .forEach {
            projectReference ->
            if(DDLCConfig.getInstance().isDokiFileColors){
                dokiAddFileColors.setFileScopes(projectReference)
            }
        }

        MTAnalytics.getInstance().trackValue(MTAnalytics.SELECT_THEME, theme)
    }

    override fun isSelected(e: AnActionEvent): Boolean =
            DDLCConfig.getInstance().getSelectedTheme() === theme

}

abstract class BaseThemeAction : ToggleAction() {

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        selectionActivation(Optional.ofNullable(e.project))
        projectSpecificActivation(e)
    }

    open fun projectSpecificActivation(e: AnActionEvent){
        //lul dunno
    }

    open fun selectionActivation(project: Optional<Project> = Optional.empty()) {
        MTTreeUI.resetIcons()
        MTButtonUI.resetCache()
        DDLCProjectViewNodeDecorator.resetCache()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        return false
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = MTConfig.getInstance().isMaterialTheme
    }
}