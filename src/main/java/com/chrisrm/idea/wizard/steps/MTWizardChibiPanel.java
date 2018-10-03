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

package com.chrisrm.idea.wizard.steps;

import com.chrisrm.idea.actions.ClubMemberOrchestrator;
import com.chrisrm.idea.actions.DarkMode;
import com.chrisrm.idea.actions.MTWeebAction;
import com.chrisrm.idea.actions.themes.literature.club.JustMonikaThemeAction;
import com.chrisrm.idea.actions.themes.literature.club.NatsukiThemeAction;
import com.chrisrm.idea.actions.themes.literature.club.SayoriThemeAction;
import com.chrisrm.idea.actions.themes.literature.club.YuriThemeAction;
import com.intellij.ide.customize.AbstractCustomizeWizardStep;
import com.intellij.ui.components.JBScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * @author Elior Boukhobza
 */
public class MTWizardChibiPanel extends AbstractCustomizeWizardStep {
  public MTWizardChibiPanel() {
    initComponents();
  }

  @Override
  protected String getTitle() {
    return "Chibi Activation";
  }

  @Override
  protected String getHTMLHeader() {
    return "<html><body><h2>Do you want Chibis?</h2>&nbsp;</body></html>";
  }


  private void justMonikaButtonActionPerformed(final ActionEvent e) {
  }
  private void onlyMonikaButtonActionPerformed(final ActionEvent e) {

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    final ResourceBundle bundle = ResourceBundle.getBundle("messages.MTWizardBundle");
    scrollPane = new JBScrollPane();
    grid = new JPanel();
    justMonikaPanel = new JPanel();
    justMonikaButton = new JRadioButton();
    justMonikaLabel = new JLabel();
    onlyMonikaPanel = new JPanel();
    onlyMonikaButton = new JRadioButton();
    onlyMonikaLabel = new JLabel();

    //======== this ========
    setLayout(new BorderLayout());

    //======== scrollPane ========
    {
      scrollPane.setBorder(null);

      //======== grid ========
      {
        grid.setMaximumSize(new Dimension(2147483647, 200));
        grid.setLayout(new MigLayout(
            "flowy,insets 0,align left top",
            // columns
            "[left]" +
                "[grow,fill]",
            // rows
            "[grow,top]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

        //======== justMonikaPanel ========
        {
          justMonikaPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          justMonikaPanel.setLayout(new BoxLayout(justMonikaPanel, BoxLayout.Y_AXIS));

          //---- justMonikaButton ----
          justMonikaButton.setText("Just Monika");
          justMonikaButton.setHorizontalAlignment(SwingConstants.LEFT);
          justMonikaButton.setActionCommand(bundle.getString("MTWizardThemesPanel.justMonikaButton.actionCommand"));
          justMonikaButton.addActionListener(this::justMonikaButtonActionPerformed);
          justMonikaPanel.add(justMonikaButton);

          //---- justMonikaLabel ----
          justMonikaLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/justMonika.png")));
          justMonikaPanel.add(justMonikaLabel);
        }
        grid.add(justMonikaPanel, "cell 0 0");

        //======== onlyMonikaPanel ========
        {
          onlyMonikaPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          onlyMonikaPanel.setLayout(new BoxLayout(onlyMonikaPanel, BoxLayout.Y_AXIS));

          //---- onlyMonikaButton ----
          onlyMonikaButton.setText("Just Monika");
          onlyMonikaButton.setHorizontalAlignment(SwingConstants.LEFT);
          onlyMonikaButton.setActionCommand(bundle.getString("MTWizardThemesPanel.onlyMonikaButton.text"));
          onlyMonikaButton.addActionListener(this::onlyMonikaButtonActionPerformed);
          onlyMonikaPanel.add(onlyMonikaButton);

          //---- onlyMonikaLabel ----
          onlyMonikaLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/onlyMonika.png")));
          onlyMonikaPanel.add(onlyMonikaLabel);
        }
        grid.add(onlyMonikaPanel, "cell 1 0,align center center,grow 0 0");

        {


        }

        {


        }

        {


        }

        {


        }

        {


        }

        {


        }

      }
      scrollPane.setViewportView(grid);
    }
    add(scrollPane, BorderLayout.CENTER);

    //---- selectedTheme ----
    final ButtonGroup selectedTheme = new ButtonGroup();
    selectedTheme.add(justMonikaButton);
    selectedTheme.add(onlyMonikaButton);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JBScrollPane scrollPane;
  private JPanel grid;
  private JPanel justMonikaPanel;
  private JRadioButton justMonikaButton;
  private JLabel justMonikaLabel;
  private JPanel onlyMonikaPanel;
  private JRadioButton onlyMonikaButton;
  private JLabel onlyMonikaLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
