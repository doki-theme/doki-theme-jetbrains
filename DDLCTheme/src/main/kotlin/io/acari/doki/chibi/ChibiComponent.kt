package io.acari.doki.chibi

import com.intellij.ide.ui.LafManagerListener
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import io.acari.doki.themes.DokiThemes
import javax.swing.UIManager

class ChibiComponent: Disposable {
    val connection = ApplicationManager.getApplication().messageBus.connect()

    init {
        processLaf(LafManagerImpl.getInstance().currentLookAndFeel)
        connection.subscribe(LafManagerListener.TOPIC, LafManagerListener {
            processLaf(it.currentLookAndFeel)
        }) 
    }

    private fun processLaf(currentLaf: UIManager.LookAndFeelInfo?) {
        DokiThemes.processLaf(currentLaf).ifPresentOrElse({
            ChibiService.instance.activateForTheme(it)
        }) {
            ChibiService.instance.remove()
        }
    }

    override fun dispose() {
        connection.dispose()
    }
}