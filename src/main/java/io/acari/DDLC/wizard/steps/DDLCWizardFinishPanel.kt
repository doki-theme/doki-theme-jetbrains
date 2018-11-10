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
 * Created by JFormDesigner on Wed Jul 25 00:33:59 IDT 2018
 */

package io.acari.DDLC.wizard.steps

import com.chrisrm.idea.MTThemes
import io.acari.DDLC.actions.ClubMemberOrchestrator
import io.acari.DDLC.actions.DarkMode
import com.intellij.ide.customize.AbstractCustomizeWizardStep
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.labels.LinkLabel
import net.miginfocom.swing.MigLayout
import java.awt.Desktop
import java.awt.Font
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.swing.*

/**
 * @author Elior Boukhobza
 */
class DDLCWizardFinishPanel : AbstractCustomizeWizardStep() {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private var scrollPane: JBScrollPane? = null
    private var content: JPanel? = null
    private var summary: JTextArea? = null
    private var summaryLabel: JLabel? = null
    private var docLink: LinkLabel<Any>? = null
    private var paypalLabel: JLabel? = null
    private var paypalLink: LinkLabel<Any>? = null
    private var label1: JLabel? = null
    private var openCollLink: LinkLabel<Any>? = null
    private var vSpacer1: JPanel? = null
    private var summarySummary: JLabel? = null
    private var disclaimer: JLabel? = null
    private var disclaimerTwo: JLabel? = null

    init {
        initComponents()
    }

    override fun getTitle(): String {
        return "Finish"
    }

    override fun getHTMLHeader(): String? {
        return null
    }

    override fun getHTMLFooter(): String? {
        return "You can always change your settings at Settings | Appearance | DDLC Theme"
    }

    private fun initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        val bundle = ResourceBundle.getBundle("messages.DDLCWizardBundle")
        scrollPane = JBScrollPane()
        content = JPanel()
        summary = JTextArea()
        summaryLabel = JLabel()
        docLink = LinkLabel()
        paypalLabel = JLabel()
        paypalLink = LinkLabel()
        label1 = JLabel()
        openCollLink = LinkLabel()
        vSpacer1 = JPanel(null)
        summarySummary = JLabel()
        disclaimer = JLabel()
        disclaimerTwo = JLabel()

        //======== this ========
        layout = BoxLayout(this, BoxLayout.X_AXIS)

        //======== scrollPane ========
        run {

            //======== content ========
            run {
                content!!.border = null
                content!!.layout = MigLayout(
                        "hidemode 3,align center top",
                        // columns
                        "[]",
                        // rows
                        "[62,top]para" +
                                "[]0" +
                                "[]para" +
                                "[]para" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[]"
                )

                //---- summary ----
                summary!!.text = bundle.getString("DDLCWizardFinishPanel.summary.text")
                summary!!.font = Font("Roboto", summary!!.font.style, summary!!.font.size + 3)
                summary!!.background = UIManager.getColor("Panel.background")
                summary!!.isEditable = false
                summary!!.wrapStyleWord = true
                content!!.add(summary!!, "cell 0 0")

                //---- summaryLabel ----
                summaryLabel!!.text = bundle.getString("DDLCWizardFinishPanel.summaryLabel.text")
                content!!.add(summaryLabel!!, "cell 0 1")

                //---- docLink ----
                docLink!!.text = bundle.getString("DDLCWizardFinishPanel.docLink.text")
                docLink!!.icon = null
                content!!.add(docLink!!, "cell 0 1")

                //---- paypalLabel ----
                paypalLabel!!.text = bundle.getString("DDLCWizardFinishPanel.paypalLabel.text")
                content!!.add(paypalLabel!!, "cell 0 2")

                //---- paypalLink ----
                paypalLink!!.text = "Paypal"
                paypalLink!!.icon = null
                content!!.add(paypalLink!!, "cell 0 2")

                //---- label1 ----
                label1!!.text = "or"
                content!!.add(label1!!, "cell 0 2")

                //---- openCollLink ----
                openCollLink!!.text = "OpenCollective"
                openCollLink!!.icon = null
                content!!.add(openCollLink!!, "cell 0 2")
                content!!.add(vSpacer1!!, "cell 0 3")

                //---- summarySummary ----
                summarySummary!!.text = bundle.getString("DDLCWizardFinishPanel.summarySummary.text")
                summarySummary!!.font = summarySummary!!.font.deriveFont(summarySummary!!.font.size + 5f)
                content!!.add(summarySummary!!, "cell 0 4,alignx center,growx 0")

            }
            scrollPane!!.setViewportView(content)
        }
        add(scrollPane)
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        docLink!!.setListener({ aSource, aLinkData ->
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(URI("https://www.material-theme.com"))
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

            }
        }, null)

        paypalLink!!.setListener({ aSource, aLinkData ->
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(URI("https://paypal.me/mallowigi"))
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

            }
        }, null)

        openCollLink!!.setListener({ aSource, aLinkData ->
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(URI("https://opencollective.com/material-theme-jetbrains"))
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                }

            }
        }, null)

    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    override fun beforeShown(forward: Boolean) {
        super.beforeShown(forward)
        if (!DarkMode.isOn() && ClubMemberOrchestrator.currentActiveTheme() != MTThemes.MONIKA) {
            content!!.add(vSpacer1!!, "cell 0 5")
            val bundle = ResourceBundle.getBundle("messages.DDLCWizardBundle")
            //---- disclaimer ----
            disclaimer!!.text = bundle.getString("DDLCWizardFinishPanel.disclaimer.text")
            disclaimer!!.font = disclaimer!!.font.deriveFont(disclaimer!!.font.size)
            content!!.add(disclaimer!!, "cell 0 6")
            //---- disclaimerTwo ----
            disclaimerTwo!!.text = bundle.getString("DDLCWizardFinishPanel.disclaimerTwo.text")
            disclaimerTwo!!.font = disclaimerTwo!!.font.deriveFont(disclaimerTwo!!.font.size)
            content!!.add(disclaimerTwo!!, "cell 0 7")
        } else {
            try {
                content!!.remove(10)
                content!!.remove(9)
                content!!.remove(8)
            } catch (throwable: Throwable) {
            }
        }
        scrollPane!!.setViewportView(content)
    }
}
