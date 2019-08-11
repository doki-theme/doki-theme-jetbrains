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
import io.acari.DDLC.AnthroThemes
import io.acari.DDLC.chibi.ChibiOrchestrator
import io.acari.DDLC.wizard.ThemeSuite
import io.acari.DDLC.wizard.WizardConfig
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class ChooseYourAdventurePanel : AbstractCustomizeWizardStep() {


  private var scrollPane: JBScrollPane? = null
  private var grid: JPanel? = null
  private var themeSuitePanel: JPanel? = null
  private var literatureClubButton: JRadioButton? = null
  private var literatureClubLabel: JLabel? = null
  private var misMenageriePanel: JPanel? = null
  private var misMenagerieButton: JRadioButton? = null
  private var misMenagerieLabel: JLabel? = null

  init {
    initComponents()
  }

  override fun beforeShown(forward: Boolean) {
    initRadioButton()
  }

  private fun initRadioButton() {
    val currentTheme = ChibiOrchestrator.currentActiveTheme().value
    if(AnthroThemes.getAllThemes().contains(currentTheme)){
      misMenagerieButtonActionPerformed()
    } else {
      literatureClubButtonActionPerformed()
    }
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
                <li><strong>Mistress's Menagerie</strong>: A collection themes based off of various characters of anthropomorphized species.</li>
            </ul>
            </body></html>""".trimIndent()
  }

  private fun literatureClubButtonActionPerformed() {
    literatureClubButton!!.isSelected = true
    WizardConfig.chosenThemeSuite = ThemeSuite.LITERATURE_CLUB
  }

  private fun misMenagerieButtonActionPerformed() {
    misMenagerieButton!!.isSelected = true
    WizardConfig.chosenThemeSuite = ThemeSuite.MENAGERIE
  }


  private fun initComponents() {
    val bundle = ResourceBundle.getBundle("messages.DDLCWizardBundle")
    scrollPane = JBScrollPane()
    grid = JPanel()
    themeSuitePanel = JPanel()
    literatureClubButton = JRadioButton()
    literatureClubLabel = JLabel()
    misMenageriePanel = JPanel()
    misMenagerieButton = JRadioButton()
    misMenagerieLabel = JLabel()

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

        //======== literatureClubPanel ========
        run {
          themeSuitePanel!!.border = EmptyBorder(5, 5, 5, 5)
          themeSuitePanel!!.layout = BoxLayout(themeSuitePanel, BoxLayout.Y_AXIS)

          //---- literatureClubButton ----
          literatureClubButton!!.text = "Literature Club"
          literatureClubButton!!.horizontalAlignment = SwingConstants.LEFT
          literatureClubButton!!.addActionListener { this.literatureClubButtonActionPerformed() }
          themeSuitePanel!!.add(literatureClubButton)

          //---- literatureClubLabel ----
          literatureClubLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/logo_fit.png"))
          literatureClubLabel!!.addMouseListener(createMouseListener { this.literatureClubButtonActionPerformed() })
          themeSuitePanel!!.add(literatureClubLabel)
        }
        grid!!.add(themeSuitePanel!!, "cell 0 0")

        //======== misMenageriePanel ========
        run {
          misMenageriePanel!!.border = EmptyBorder(5, 5, 5, 5)
          misMenageriePanel!!.layout = BoxLayout(misMenageriePanel, BoxLayout.Y_AXIS)

          //---- misMenagerieButton ----
          misMenagerieButton!!.text = "Mistress's Menagerie"
          misMenagerieButton!!.horizontalAlignment = SwingConstants.LEFT
          misMenagerieButton!!.addActionListener { this.misMenagerieButtonActionPerformed() }
          misMenageriePanel!!.add(misMenagerieButton)

          //---- misMenagerieLabel ----
          misMenagerieLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/menagerie.png"))
          var self = this
          misMenagerieLabel!!.addMouseListener (createMouseListener {this.misMenagerieButtonActionPerformed()})
          misMenageriePanel!!.add(misMenagerieLabel)
        }
        grid!!.add(misMenageriePanel!!, "cell 1 0,align center")
      }
      scrollPane!!.setViewportView(grid)
    }
    add(scrollPane!!, BorderLayout.CENTER)

    //---- selectedTheme ----
    val selectedTheme = ButtonGroup()
    selectedTheme.add(literatureClubButton)
    selectedTheme.add(misMenagerieButton)
  }
}
