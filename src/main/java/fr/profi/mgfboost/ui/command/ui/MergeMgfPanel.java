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
public class MergeMgfPanel extends JPanel {

  private JTextField outputTF;
  private JTextField precursorsFileTF;
  private JTextField fragmentsFileTF;
  private JCheckBox replaceCbx;
  private JCheckBox filterCbx;

  public MergeMgfPanel(CommandArguments.MgfMergerCommand command) {
    this();
    initValues(command);
  }

  private void initValues(CommandArguments.MgfMergerCommand command) {
    replaceCbx.setSelected(command.replace);
    filterCbx.setSelected(command.filter);
    precursorsFileTF.setText(command.inputFileName1.trim());
    fragmentsFileTF.setText(command.inputFileName2.trim());
  }


  public void buildCommand(CommandArguments.MgfMergerCommand command) {
    command.inputFileName1 = precursorsFileTF.getText().trim();
    command.inputFileName2 = fragmentsFileTF.getText().trim();
    command.filter = filterCbx.isSelected();
    command.replace = replaceCbx.isSelected();
    command.outputFileName = outputTF.getText().trim();
  }

  public MergeMgfPanel() {
    initComponents();
  }

  private void initComponents() {
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    outputTF = new JTextField();
    JButton button1 = new JButton();
    JPanel panel2 = new JPanel();
    JLabel label2 = new JLabel();
    precursorsFileTF = new JTextField();
    JButton button2 = new JButton();
    JLabel label3 = new JLabel();
    fragmentsFileTF = new JTextField();
    JButton button3 = new JButton();
    JPanel panel3 = new JPanel();
    replaceCbx = new JCheckBox();
    filterCbx = new JCheckBox();

    //======== this ========
    setLayout(new GridBagLayout());

    //======== panel1 ========
    final Insets fullInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, fullInsets, 0, 0);


      panel1.setLayout(new GridBagLayout());
      //---- label1 ----
      label1.setText("output File:");
      panel1.add(label1, c);
      //---- outputTF ----
      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      panel1.add(outputTF, c);
      //---- button1 ----
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      button1.setText("choose");
      button1.addActionListener(e -> FileChooserUtil.saveAsChooser(outputTF));
      panel1.add(button1, c);

     add(panel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
      new Insets(5, 0, 5, 0), 0, 0));

    //======== panel2 ========

      panel2.setBorder(new TitledBorder("input files"));
      panel2.setLayout(new GridBagLayout());

      //---- label2 ----
      c.gridx = 0;
      label2.setText("precursors MGF file:");
      panel2.add(label2, c);

      c.gridx++;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      panel2.add(precursorsFileTF, c);

      //---- button2 ----
      button2.setText("choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      panel2.add(button2, c);

      //---- label3 ----
      c.gridx = 0;
      c.gridy++;
      label3.setText("fragments MGF file:");
      panel2.add(label3, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      panel2.add(fragmentsFileTF, c);

      //---- button3 ----
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      button3.setText("choose");
      panel2.add(button3, c);

    add(panel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
      new Insets(5, 5, 5, 5), 0, 0));

    //======== panel3 ========

      panel3.setBorder(new TitledBorder("options"));
      panel3.setLayout(new GridBagLayout());

      //---- replaceCbx ----
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      replaceCbx.setText("replace MS/MS fragments of precursor MGF file");
      panel3.add(replaceCbx, c);

      c.gridy++;
      //---- filterCbx ----
      filterCbx.setText("filter peaklist from precursors MGF file if not found in fragments file");
      panel3.add(filterCbx, c);

    add(panel3, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
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
