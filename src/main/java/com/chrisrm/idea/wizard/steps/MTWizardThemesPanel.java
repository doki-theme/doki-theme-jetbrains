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
public class MTWizardThemesPanel extends AbstractCustomizeWizardStep {
  public MTWizardThemesPanel() {
    initComponents();
  }

  @Override
  protected String getTitle() {
    return "Club Members";
  }

  @Override
  protected String getHTMLHeader() {
    return "<html><body><h2>Select a Club Member</h2>&nbsp;</body></html>";
  }


  private final JustMonikaThemeAction justMonikaThemeAction = new JustMonikaThemeAction();
  private void justMonikaButtonActionPerformed(final ActionEvent e) {
    justMonikaThemeAction.selectionActivation();
  }

  private final SayoriThemeAction sayoriThemeAction = new SayoriThemeAction();
  private void sayoriButtonActionPerformed(final ActionEvent e) {
    sayoriThemeAction.selectionActivation();
  }

  private final NatsukiThemeAction natsukiThemeAction = new NatsukiThemeAction();
  private void natsukiButtonActionPerformed(final ActionEvent e) {
    natsukiThemeAction.selectionActivation();
  }

  private final YuriThemeAction yuriThemeAction = new YuriThemeAction();
  private void yuriButtonActionPerformed(final ActionEvent e) {
    yuriThemeAction.selectionActivation();
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
    sayoriLayout = new JPanel();
    sayoriButton = new JRadioButton();
    sayoriLabel = new JLabel();
    natsukiPanel = new JPanel();
    natsukiButton = new JRadioButton();
    natsukiLabel = new JLabel();
    yuriPanel = new JPanel();
    yuriButton = new JRadioButton();
    yuriLabel = new JLabel();

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

        //======== sayoriLayout ========
        {
          sayoriLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
          sayoriLayout.setLayout(new BoxLayout(sayoriLayout, BoxLayout.Y_AXIS));

          //---- sayoriButton ----
          sayoriButton.setText(bundle.getString("MTWizardThemesPanel.sayoriButton.text"));
          sayoriButton.setHorizontalAlignment(SwingConstants.LEFT);
          sayoriButton.addActionListener(this::sayoriButtonActionPerformed);
          sayoriLayout.add(sayoriButton);

          //---- sayoriLabel ----
          sayoriLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/sayori.png")));
          sayoriLayout.add(sayoriLabel);
        }
        grid.add(sayoriLayout, "cell 1 0,align center center,grow 0 0");

        //======== natsukiPanel ========
        {
          natsukiPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          natsukiPanel.setLayout(new BoxLayout(natsukiPanel, BoxLayout.Y_AXIS));

          //---- natsukiButton ----
          natsukiButton.setText(bundle.getString("MTWizardThemesPanel.natsukiButton.text"));
          natsukiButton.addActionListener(this::natsukiButtonActionPerformed);
          natsukiPanel.add(natsukiButton);

          //---- natsukiLabel ----
          natsukiLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/natsuki.png")));
          natsukiPanel.add(natsukiLabel);
        }
        grid.add(natsukiPanel, "cell 0 1,align center center,grow 0 0");

        //======== yuriPanel ========
        {
          yuriPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          yuriPanel.setLayout(new BoxLayout(yuriPanel, BoxLayout.Y_AXIS));

          //---- yuriButton ----
          yuriButton.setText(bundle.getString("MTWizardThemesPanel.yuriButton.text"));
          yuriButton.addActionListener(this::yuriButtonActionPerformed);
          yuriPanel.add(yuriButton);

          //---- yuriLabel ----
          yuriLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/yuri.png")));
          yuriPanel.add(yuriLabel);
        }
        grid.add(yuriPanel, "cell 1 1,align center center,grow 0 0");

      }
      scrollPane.setViewportView(grid);
    }
    add(scrollPane, BorderLayout.CENTER);

    //---- selectedTheme ----
    final ButtonGroup selectedTheme = new ButtonGroup();
    selectedTheme.add(justMonikaButton);
    selectedTheme.add(sayoriButton);
    selectedTheme.add(natsukiButton);
    selectedTheme.add(yuriButton);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JBScrollPane scrollPane;
  private JPanel grid;
  private JPanel justMonikaPanel;
  private JRadioButton justMonikaButton;
  private JLabel justMonikaLabel;
  private JPanel sayoriLayout;
  private JRadioButton sayoriButton;
  private JLabel sayoriLabel;
  private JPanel natsukiPanel;
  private JRadioButton natsukiButton;
  private JLabel natsukiLabel;
  private JPanel yuriPanel;
  private JRadioButton yuriButton;
  private JLabel yuriLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
