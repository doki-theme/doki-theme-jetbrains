/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
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

package com.chrisrm.idea.wizard.steps

import com.chrisrm.idea.MTThemes
import com.chrisrm.idea.actions.ClubMemberOrchestrator
import com.chrisrm.idea.actions.DarkMode
import com.intellij.ide.customize.AbstractCustomizeWizardStep
import com.intellij.ui.components.JBScrollPane
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class MTWizardChibiPanel : AbstractCustomizeWizardStep() {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private var scrollPane: JBScrollPane? = null
    private var grid: JPanel? = null
    private var noChibisPanel: JPanel? = null
    private var noChibisButton: JRadioButton? = null
    private var noChibisLabel: JLabel? = null
    private var yesChibisPanel: JPanel? = null
    private var yesChibisButton: JRadioButton? = null
    private var yesChibisLabel: JLabel? = null

    init {
        initComponents()
    }

    override fun getTitle(): String {
        return "Chibi Activation"
    }

    private fun getSpecialMessage() =
            if (DarkMode.isOn() && ClubMemberOrchestrator.currentActiveTheme() != MTThemes.MONIKA)
                """
                <h4>Note: I see you have dark mode on! "Use Tools -> Panel Options -> Swap Chibi" to make your club member less c̟̺̱̱̪o͚̲̹̼͘r̡̭̤̭̼̟̭̜r͇͚u̘̰͝p͓̝͍̻̩̩t̼̣̙͍͍e͕̹͙̟̬̮͟ͅd̻ anytime :)</h4>
                """.trimIndent() else ""

    override fun getHTMLHeader(): String =
            """<html><body>
                <h2>Do you want Chibis?</h2>&nbsp;<br/>
                <h3>Head's Up: Activating Chibis will require special steps to remove the images once the plugin is uninstalled!</h3>
                ${getSpecialMessage()}
                </body></html>""".trimIndent()


    private fun noChibisButtonActionPerformed(e: ActionEvent) {
        ClubMemberOrchestrator.deactivateWeebShit()
    }

    private fun yesChibisButtonActionPerformed(e: ActionEvent) {
        ClubMemberOrchestrator.activateWeebShit()
    }

    private fun initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        scrollPane = JBScrollPane()
        grid = JPanel()
        noChibisPanel = JPanel()
        noChibisButton = JRadioButton()
        noChibisLabel = JLabel()
        yesChibisPanel = JPanel()
        yesChibisButton = JRadioButton()
        yesChibisLabel = JLabel()

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

                //======== noChibisPanel ========
                run {
                    noChibisPanel!!.border = EmptyBorder(5, 5, 5, 5)
                    noChibisPanel!!.layout = BoxLayout(noChibisPanel, BoxLayout.Y_AXIS)

                    //---- noChibisButton ----
                    noChibisButton!!.text = "No, I do not want Chibis."
                    noChibisButton!!.isSelected = !ClubMemberOrchestrator.weebShitOn()
                    noChibisButton!!.horizontalAlignment = SwingConstants.LEFT
                    noChibisButton!!.actionCommand = "noChibis"
                    noChibisButton!!.addActionListener { this.noChibisButtonActionPerformed(it) }
                    noChibisPanel!!.add(noChibisButton)

                    //---- noChibisLabel ----
                    noChibisLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/chibis/just_monika.png"))
                    noChibisPanel!!.add(noChibisLabel)
                }
                grid!!.add(noChibisPanel!!, "cell 0 0")

                //======== yesChibisPanel ========
                run {
                    yesChibisPanel!!.border = EmptyBorder(5, 5, 5, 5)
                    yesChibisPanel!!.layout = BoxLayout(yesChibisPanel, BoxLayout.Y_AXIS)

                    //---- yesChibisButton ----
                    yesChibisButton!!.text = "Yes! Chibis please!"
                    yesChibisButton!!.isSelected = ClubMemberOrchestrator.weebShitOn()
                    yesChibisButton!!.horizontalAlignment = SwingConstants.LEFT
                    yesChibisButton!!.actionCommand = "yesChibis"
                    yesChibisButton!!.addActionListener { this.yesChibisButtonActionPerformed(it) }
                    yesChibisPanel!!.add(yesChibisButton)

                    //---- yesChibisLabel ----
                    yesChibisLabel!!.icon = ImageIcon(javaClass.getResource("/wizard/chibis/only_monika.png"))
                    yesChibisPanel!!.add(yesChibisLabel)
                }
                grid!!.add(yesChibisPanel!!, "cell 1 0")
            }
            scrollPane!!.setViewportView(grid)
        }
        add(scrollPane!!, BorderLayout.CENTER)

        //---- selectedTheme ----
        val selectedTheme = ButtonGroup()
        selectedTheme.add(noChibisButton)
        selectedTheme.add(yesChibisButton)
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
