/*
 * Created by JFormDesigner on Fri Aug 26 17:05:26 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author unknown
 */
public class MergeMgfPanel extends AbstractCommandPanel<CommandArguments.MgfMergerCommand> {

  private JTextField precursorsFileTF;
  private JTextField fragmentsFileTF;
  private JCheckBox replaceCbx;
  private JCheckBox filterCbx;

  public MergeMgfPanel() {
    initComponents();
  }

  public AbstractCommandPanel<CommandArguments.MgfMergerCommand> updatePanelFromCommand(CommandArguments.MgfMergerCommand command) {
    replaceCbx.setSelected(command.replace);
    filterCbx.setSelected(command.filter);
    precursorsFileTF.setText(command.inputFileName1.trim());
    fragmentsFileTF.setText(command.inputFileName2.trim());
    return this;
  }


  public boolean buildCommand(CommandArguments.MgfMergerCommand command) {
    command.inputFileName1 = precursorsFileTF.getText().trim();
    command.inputFileName2 = fragmentsFileTF.getText().trim();
    command.filter = filterCbx.isSelected();
    command.replace = replaceCbx.isSelected();
    command.outputFileName = outputFilePanel.getOutputFilepath();

    return true;
  }

  private void initComponents() {
    JPanel inputFilesPanel = new JPanel();
    precursorsFileTF = new JTextField();
    JButton fileChooser1 = new JButton();
    // not yet implemented
    fileChooser1.setEnabled(false);
    fragmentsFileTF = new JTextField();
    JButton fileChooser2 = new JButton();
    // not yet implemented
    fileChooser2.setEnabled(false);
    JPanel optionsPanel = new JPanel();
    replaceCbx = new JCheckBox();
    filterCbx = new JCheckBox();

    //======== this ========
    setLayout(new GridBagLayout());

    //======== panel1 ========
    final Insets fullInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, fullInsets, 0, 0);

     outputFilePanel = new OutputFilePanel();
     add(outputFilePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
      new Insets(5, 0, 5, 0), 0, 0));

    //======== inputFilesPanel ========

      inputFilesPanel.setBorder(new TitledBorder("input files"));
      inputFilesPanel.setLayout(new GridBagLayout());

      //---- label2 ----
      c.gridx = 0;
      JLabel label2 = new JLabel("precursors MGF file:");
      inputFilesPanel.add(label2, c);

      c.gridx++;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      inputFilesPanel.add(precursorsFileTF, c);

      //---- fileChooser1 ----
      fileChooser1.setText("Choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      inputFilesPanel.add(fileChooser1, c);

      //---- label3 ----
      c.gridx = 0;
      c.gridy++;
      JLabel label3 = new JLabel("fragments MGF file:");
      inputFilesPanel.add(label3, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      inputFilesPanel.add(fragmentsFileTF, c);

      //---- fileChooser2 ----
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      fileChooser2.setText("Choose");
      inputFilesPanel.add(fileChooser2, c);

    add(inputFilesPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
      new Insets(5, 5, 5, 5), 0, 0));

    //======== optionsPanel ========

      optionsPanel.setBorder(new TitledBorder("options"));
      optionsPanel.setLayout(new GridBagLayout());

      //---- replaceCbx ----
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      replaceCbx.setText("replace MS/MS fragments of precursor MGF file");
      optionsPanel.add(replaceCbx, c);

      c.gridy++;
      //---- filterCbx ----
      filterCbx.setText("filter peaklist from precursors MGF file if not found in fragments file");
      optionsPanel.add(filterCbx, c);

    add(optionsPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
      new Insets(5, 5, 5, 5), 0, 0));
  }


  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new MergeMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }

}
