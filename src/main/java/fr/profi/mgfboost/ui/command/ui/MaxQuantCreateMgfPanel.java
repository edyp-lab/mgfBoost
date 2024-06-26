/*
 * Created by JFormDesigner on Mon Aug 29 17:47:19 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author unknown
 */
public class MaxQuantCreateMgfPanel extends AbstractCommandPanel<CommandArguments.MaxQuantMGFCommand> {

  private JTextField inputFile1TF;
  private JTextField inputFile2TF;

  public MaxQuantCreateMgfPanel() {
    initComponents();
  }

  public MaxQuantCreateMgfPanel(CommandArguments.MaxQuantMGFCommand command) {
    this();
    updatePanelFromCommand(command);
  }

  public AbstractCommandPanel<CommandArguments.MaxQuantMGFCommand> updatePanelFromCommand(CommandArguments.MaxQuantMGFCommand command) {
    return this;
  }

  public boolean buildCommand(CommandArguments.MaxQuantMGFCommand command) {
    command.outputFileName = outputFilePanel.getOutputFilepath();
    command.inputFileName1 = inputFile1TF.getText().trim();
    command.inputFileName2 = inputFile2TF.getText().trim();

    return true;
  }

  private void initComponents() {

    JPanel mqFilesPanel = new JPanel();
    inputFile1TF = new JTextField(20);
    JButton fileChooser1 = new JButton();
    // not yet implemented
    fileChooser1.setEnabled(false);
    inputFile2TF = new JTextField(20);
    JButton fileChooser2 = new JButton();
    // not yet implemented
    fileChooser2.setEnabled(false);


    //======== this ========
    setLayout(new GridBagLayout());
    final Insets defaultInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    //======== output file panel ========
    outputFilePanel = new OutputFilePanel();
    add(outputFilePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //======== mqFilesPanel =======
      mqFilesPanel.setBorder(new TitledBorder("MaxQuant .apl files"));
      mqFilesPanel.setLayout(new GridBagLayout());

      //---- label2 ----
      JLabel label2 = new JLabel("Multi apl file (.sil0.apl):");
      c.gridx = 0;
      c.gridy = 0;
      mqFilesPanel.add(label2, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      mqFilesPanel.add(inputFile1TF, c);

      //---- fileChooser1 ----
      fileChooser1.setText("Choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      mqFilesPanel.add(fileChooser1, c);

      //---- label3 ----
      c.gridx = 0;
      c.gridy++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      JLabel label3 = new JLabel("Peak apl file (.peak.apl):");
      mqFilesPanel.add(label3, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      mqFilesPanel.add(inputFile2TF, c);

      //---- fileChooser2 ----
      fileChooser2.setText("Choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      mqFilesPanel.add(fileChooser2, c);

    add(mqFilesPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 5, 0, 5), 0, 0));
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new MaxQuantCreateMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }

}
