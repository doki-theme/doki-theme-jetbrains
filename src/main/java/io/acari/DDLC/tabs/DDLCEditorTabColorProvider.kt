package io.acari.DDLC.tabs

import com.chrisrm.ideaddlc.MTThemeManager
import io.acari.DDLC.actions.DarkMode
import com.intellij.openapi.fileEditor.impl.EditorTabColorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.NonPhysicalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.ProjectScope
import com.intellij.ui.ColorUtil
import com.intellij.ui.FileColorManager
import java.awt.Color

class DDLCEditorTabColorProvider : EditorTabColorProvider {
    override fun getEditorTabColor(project: Project, file: VirtualFile): Color? {
        val colorManager = FileColorManager.getInstance(project)
        return if (colorManager.isEnabledForTabs && MTThemeManager.isDDLCActive()) {
            val fileColor = colorManager.getFileColor(file)
            if (isNonProject(project, file) && fileColor != null) {
                adjustColor(fileColor)
            } else {
                fileColor
            }
        } else null
    }

    private fun adjustColor(fileColor: Color) =
            if (DarkMode.isOn()) ColorUtil.brighter(fileColor, 10)
            else fileColor


    private fun isNonProject(project: Project, virtualFile: VirtualFile) =
            if (virtualFile.getFileSystem() is NonPhysicalFileSystem) {
                false
            } else if (!virtualFile.isInLocalFileSystem()) {
                true
            } else if (isInsideProjectContent(project, virtualFile)) {
                false
            } else {
                !ProjectScope.getProjectScope(project).contains(virtualFile)
            }

    private fun isInsideProjectContent(project: Project, file: VirtualFile): Boolean {
        if (!file.isInLocalFileSystem) {
            val projectBaseDir = project.basePath
            if (projectBaseDir != null) {
                return FileUtil.isAncestor(projectBaseDir, file.path, false)
            }
        }
        return false
    }

}
