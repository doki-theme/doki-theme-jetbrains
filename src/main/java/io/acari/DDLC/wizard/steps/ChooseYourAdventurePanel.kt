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

/*
 * Created by JFormDesigner on Fri Jun 29 18:52:29 IDT 2018
 */

package io.acari.DDLC.wizard.steps

import com.intellij.ide.customize.AbstractCustomizeWizardStep
import com.intellij.ui.components.JBScrollPane
import io.acari.DDLC.actions.DarkMode
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class ChooseYourAdventurePanel : AbstractCustomizeWizardStep() {


  private var scrollPane: JBScrollPane? = null
  private var grid: JPanel? = null
  private var justMonikaPanel: JPanel? = null
  private var justMonikaButton: JRadioButton? = null
  private var justMonikaLabel: JLabel? = null
  private var onlyMonikaPanel: JPanel? = null
  private var onlyMonikaButton: JRadioButton? = null
  private var onlyMonikaLabel: JLabel? = null

  init {
    initComponents()
  }

  override fun beforeShown(forward: Boolean) {
    initRadioButton()
  }

  private fun initRadioButton() {
    justMonikaButtonActionPerformed()
  }

  override fun getTitle(): String {
    return "Theme Suite"
  }

  override fun getHTMLHeader(): String {
    return """<html>
            <body>
            <h2>Choose your selected theme suite: </h2>&nbsp;
            <ul>
                <li><strong>Literature Club</strong>: A collection of themes based off of the characters in the Doki-Doki Literature Club visual novel.</li>
                <li><strong>The Lovely Menagerie</strong>: A collection themes based off of various characters of anthropomorphized species.</li>
            </ul>
            </body></html>""".trimIndent()
  }

  private fun justMonikaButtonActionPerformed() {
    DarkMode.turnOff()
    justMonikaButton!!.isSelected = true
  }

  private fun onlyMonikaButtonActionPerformed() {
    DarkMode.turnOn()
    onlyMonikaButton!!.isSelected = true
  }


  private fun initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    val bundle = ResourceBundle.getBundle("messages.DDLCWizardBundle")
    scrollPane = JBScrollPane()
    grid = JPanel()
    justMonikaPanel = JPanel()
    justMonikaButton = JRadioButton()
    justMonikaLabel = JLabel()
    onlyMonikaPanel = JPanel()
    onlyMonikaButton = JRadioButton()
    onlyMonikaLabel = JLabel()

    //======== this ========
    layout = BorderLayout()

    //======== scrollPane ========
    run {
      scrollPane!!.border = null

      //======== grid ========
      run {
        grid!!.maximumSize = Dimension(2147483647, 200)
        grid!!.layout = MigLayout(
            "flowy,insets 0,align left top",
            // columns
            "[left]" + "[grow,fill]",
            // rows
            "[grow,top]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]")

        //======== justMonikaPanel ========
        run {
          justMonikaPanel!!.border = EmptyBorder(5, 5, 5, 5)
          justMonikaPanel!!.layout = BoxLayout(justMonikaPanel, BoxLayout.Y_AXIS)

          //---- justMonikaButton ----
          justMonikaButton!!.text = "Literature Club"
          justMonikaButton!!.horizontalAlignment = SwingConstants.LEFT
          justMonikaButton!!.actionCommand = bundle.getString("DDLCWizardThemesPanel.justMonikaButton.actionCommand")
          justMonikaButton!!.addActionListener { this.justMonikaButtonActionPerformed() }
          justMonikaPanel!!.add(justMonikaButton)

          //---- justMonikaLabel ----
          justMonikaLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/logo_fit.png"))
          justMonikaPanel!!.add(justMonikaLabel)
        }
        grid!!.add(justMonikaPanel!!, "cell 0 0")

        //======== onlyMonikaPanel ========
        run {
          onlyMonikaPanel!!.border = EmptyBorder(5, 5, 5, 5)
          onlyMonikaPanel!!.layout = BoxLayout(onlyMonikaPanel, BoxLayout.Y_AXIS)

          //---- onlyMonikaButton ----
          onlyMonikaButton!!.text = "The Lovely Menagerie"
          onlyMonikaButton!!.horizontalAlignment = SwingConstants.LEFT
          onlyMonikaButton!!.actionCommand = bundle.getString("DDLCWizardThemesPanel.onlyMonikaButton.text")
          onlyMonikaButton!!.addActionListener { this.onlyMonikaButtonActionPerformed() }
          onlyMonikaPanel!!.add(onlyMonikaButton)

          //---- onlyMonikaLabel ----
          onlyMonikaLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/logo_fit.png"))
          onlyMonikaPanel!!.add(onlyMonikaLabel)
        }
        grid!!.add(onlyMonikaPanel!!, "cell 1 0")
      }
      scrollPane!!.setViewportView(grid)
    }
    add(scrollPane!!, BorderLayout.CENTER)

    //---- selectedTheme ----
    val selectedTheme = ButtonGroup()
    selectedTheme.add(justMonikaButton)
    selectedTheme.add(onlyMonikaButton)
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables


}
