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

package io.acari.DDLC.status

import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.Project

class DDLCStatusBarComponent(project: Project) : AbstractProjectComponent(project) {
  private var statusBarWidget: DDLCStatusBarManager? = null
  private var initalized = false

  init {
    MTThemeManager.addMaterialThemeActivatedListener { materialActive ->
      if (!(materialActive!! || initalized)) {
        initalized = true
        statusBarWidget!!.install()
      } else if (materialActive) {
        initalized = false
        statusBarWidget!!.uninstall()
      }
    }
  }

  override fun initComponent() {
    statusBarWidget = DDLCStatusBarManager.create(myProject)
  }

  override fun disposeComponent() {
    statusBarWidget!!.dispose()
  }

  override fun getComponentName(): String {
    return "DDLCStatusBarComponent"
  }

  override fun projectOpened() {
    if (MTConfig.getInstance().isStatusBarTheme && MTThemeManager.isDDLCActive()) {
      statusBarWidget!!.install()
    }
  }

  override fun projectClosed() {
    statusBarWidget!!.uninstall()
  }
}
