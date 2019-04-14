/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
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

package com.chrisrm.ideaddlc.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaOptionButtonUI
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.ui.popup.PopupFactoryImpl
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.Action
import com.intellij.openapi.util.Condition
import com.intellij.ui.popup.ActionPopupStep
import com.intellij.ui.popup.list.PopupListElementRenderer
import com.intellij.util.ui.JBUI
import java.awt.Color
import javax.swing.JComponent


open class MTOptionButtonUI : DarculaOptionButtonUI() {
    override val clipXOffset: Int
        get() = 0

    override fun paintSeparator(g: Graphics2D, c: JComponent) {
    }

    private var mouseListener: MouseListener? = null

    override fun installListeners() {
        super.installListeners()
        mouseListener = createMouseListener()?.apply(optionButton::addMouseListener)
    }

    override fun uninstallListeners() {
        super.uninstallListeners()
        mouseListener = null
    }

    protected open fun createMouseListener(): MouseListener? = object : MouseAdapter() {
        override fun mouseEntered(e: MouseEvent?) {
            super.mouseEntered(e)
            println("entered")
        }

        override fun mouseExited(e: MouseEvent?) {
            super.mouseExited(e)
            println("exited")
        }
    }


    companion object {
        @Suppress("UNUSED_PARAMETER")
        @JvmStatic
        fun createUI(c: JComponent) = MTOptionButtonUI()
    }

    override fun createPopup(toSelect: Action?, ensureSelection: Boolean): ListPopup {
        val (actionGroup, mapping) = createActionMapping()
        val dataContext = createActionDataContext()
        val actionItems = PopupFactoryImpl.ActionGroupPopup.getActionItems(actionGroup, dataContext, false, false, true, true, ActionPlaces.UNKNOWN)
        val defaultSelection = if (toSelect != null) Condition<AnAction> { mapping[it] == toSelect } else null

        return DokiOptionButtonPopup(OptionButtonPopupStep(actionItems, defaultSelection), dataContext, toSelect != null || ensureSelection)
    }

    open inner class DokiOptionButtonPopup(step: ActionPopupStep, dataContext: DataContext, private val ensureSelection: Boolean)
        : PopupFactoryImpl.ActionGroupPopup(null, step, null, dataContext, ActionPlaces.UNKNOWN, -1) {
        init {
            list.background = background
        }

        override fun afterShow() {
            if (ensureSelection) super.afterShow()
        }

        protected val background: Color? get() = mainButton.background
        protected val foreground: Color get() = mainButton.foreground // <- I added dis -Alex

        override fun createContent(): JComponent = super.createContent().also {
            list.clearSelection() // prevents first action selection if all actions are disabled
            list.border = JBUI.Borders.empty(2, 0)
        }

        override fun getListElementRenderer(): PopupListElementRenderer<Any> = object : PopupListElementRenderer<Any>(this) {
            override fun getBackground() = this@DokiOptionButtonPopup.background
            override fun getForeground(): Color = this@DokiOptionButtonPopup.foreground
            override fun createSeparator() = super.createSeparator().apply { border = JBUI.Borders.empty(2, 6) }
            override fun getDefaultItemComponentBorder() = JBUI.Borders.empty(6, 8)
        }
    }

}
