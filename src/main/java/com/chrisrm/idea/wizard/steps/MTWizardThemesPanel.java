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

import com.chrisrm.idea.actions.DarkMode;
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
    DarkMode.turnOff();
    justMonikaThemeAction.selectionActivation();
  }
  private void onlyMonikaButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOn();
    justMonikaThemeAction.selectionActivation();
  }

  private final SayoriThemeAction sayoriThemeAction = new SayoriThemeAction();
  private void deletedCharacterButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOn();
    sayoriThemeAction.selectionActivation();
  }
  private void sayoriButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOff();
    sayoriThemeAction.selectionActivation();
  }

  private final NatsukiThemeAction natsukiThemeAction = new NatsukiThemeAction();
  private void onlyPlayWithMeButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOn();
    natsukiThemeAction.selectionActivation();
  }
  private void natsukiButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOff();
    natsukiThemeAction.selectionActivation();
  }

  private final YuriThemeAction yuriThemeAction = new YuriThemeAction();
  private void edgyButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOn();
    yuriThemeAction.selectionActivation();
  }
  private void yuriButtonActionPerformed(final ActionEvent e) {
    DarkMode.turnOff();
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
    onlyMonikaPanel = new JPanel();
    onlyMonikaButton = new JRadioButton();
    onlyMonikaLabel = new JLabel();
    deletedCharacterLayout = new JPanel();
    deletedCharacterButton = new JRadioButton();
    deletedCharacterLabel = new JLabel();
    onlyPlayWithMePanel = new JPanel();
    onlyPlayWithMeButton = new JRadioButton();
    onlyPlayWithMeLabel = new JLabel();
    edgyPanel = new JPanel();
    edgyButton = new JRadioButton();
    edgyLabel = new JLabel();

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
        grid.add(sayoriLayout, "cell 0 1,align center center,grow 0 0");

        //======== deletedCharacterLayout ========
        {
          deletedCharacterLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
          deletedCharacterLayout.setLayout(new BoxLayout(deletedCharacterLayout, BoxLayout.Y_AXIS));

          //---- deletedCharacterButton ----
          deletedCharacterButton.setText(bundle.getString("MTWizardThemesPanel.deletedCharacterButton.text"));
          deletedCharacterButton.setHorizontalAlignment(SwingConstants.LEFT);
          deletedCharacterButton.addActionListener(this::deletedCharacterButtonActionPerformed);
          deletedCharacterLayout.add(deletedCharacterButton);

          //---- deletedCharacterLabel ----
          deletedCharacterLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/deletedCharacter.png")));
          deletedCharacterLayout.add(deletedCharacterLabel);
        }
        grid.add(deletedCharacterLayout, "cell 1 1,align center center,grow 0 0");

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
        grid.add(natsukiPanel, "cell 0 2,align center center,grow 0 0");

        //======== onlyPlayWithMePanel ========
        {
          onlyPlayWithMePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          onlyPlayWithMePanel.setLayout(new BoxLayout(onlyPlayWithMePanel, BoxLayout.Y_AXIS));

          //---- onlyPlayWithMeButton ----
          onlyPlayWithMeButton.setText(bundle.getString("MTWizardThemesPanel.onlyPlayWithMeButton.text"));
          onlyPlayWithMeButton.addActionListener(this::onlyPlayWithMeButtonActionPerformed);
          onlyPlayWithMePanel.add(onlyPlayWithMeButton);

          //---- onlyPlayWithMeLabel ----
          onlyPlayWithMeLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/onlyPlayWithMe.png")));
          onlyPlayWithMePanel.add(onlyPlayWithMeLabel);
        }
        grid.add(onlyPlayWithMePanel, "cell 1 2,align center center,grow 0 0");

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
        grid.add(yuriPanel, "cell 0 3,align center center,grow 0 0");

        //======== edgyPanel ========
        {
          edgyPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
          edgyPanel.setLayout(new BoxLayout(edgyPanel, BoxLayout.Y_AXIS));

          //---- edgyButton ----
          edgyButton.setText(bundle.getString("MTWizardThemesPanel.edgyButton.text"));
          edgyButton.addActionListener(this::edgyButtonActionPerformed);
          edgyPanel.add(edgyButton);

          //---- edgyLabel ----
          edgyLabel.setIcon(new ImageIcon(getClass().getResource("/wizard/edgy.png")));
          edgyPanel.add(edgyLabel);
        }
        grid.add(edgyPanel, "cell 1 3,align center center,grow 0 0");

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
    selectedTheme.add(onlyMonikaButton);
    selectedTheme.add(deletedCharacterButton);
    selectedTheme.add(onlyPlayWithMeButton);
    selectedTheme.add(edgyButton);
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
  private JPanel onlyMonikaPanel;
  private JRadioButton onlyMonikaButton;
  private JLabel onlyMonikaLabel;
  private JPanel deletedCharacterLayout;
  private JRadioButton deletedCharacterButton;
  private JLabel deletedCharacterLabel;
  private JPanel onlyPlayWithMePanel;
  private JRadioButton onlyPlayWithMeButton;
  private JLabel onlyPlayWithMeLabel;
  private JPanel edgyPanel;
  private JRadioButton edgyButton;
  private JLabel edgyLabel;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
