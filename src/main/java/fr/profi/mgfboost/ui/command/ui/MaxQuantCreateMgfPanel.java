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
public class MaxQuantCreateMgfPanel extends JPanel {

  private JTextField outputTF;
  private JTextField inputFile1TF;
  private JTextField inputFile2TF;

  public MaxQuantCreateMgfPanel() {
    initComponents();
  }

  public MaxQuantCreateMgfPanel(CommandArguments.MaxQuantMGFCommand command) {
    this();
    initValues(command);
  }

  private void initValues(CommandArguments.MaxQuantMGFCommand command) {
    inputFile1TF.setText(command.inputFileName1.trim());
    inputFile2TF.setText(command.inputFileName2.trim());
  }

  public void buildCommand(CommandArguments.MaxQuantMGFCommand command) {
    command.outputFileName = outputTF.getText().trim();
    command.inputFileName1 = inputFile1TF.getText().trim();
    command.inputFileName2 = inputFile2TF.getText().trim();
  }

  private void initComponents() {
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    outputTF = new JTextField();
    JButton button1 = new JButton();
    JPanel panel2 = new JPanel();
    JLabel label2 = new JLabel();
    inputFile1TF = new JTextField(20);
    JButton button2 = new JButton();
    JLabel label3 = new JLabel();
    inputFile2TF = new JTextField(20);
    JButton button3 = new JButton();

    //======== this ========
    setLayout(new GridBagLayout());
    final Insets defaultInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    //======== panel1 ========
      panel1.setLayout(new GridBagLayout());

      //---- label1 ----
      label1.setText("output File:");
      panel1.add(label1, c);
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
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 0, 5, 0), 0, 0));

    //======== panel2 =======
      panel2.setBorder(new TitledBorder("MaxQuant .apl files"));
      panel2.setLayout(new GridBagLayout());

      //---- label2 ----
      label2.setText("Multi apl file (.sil0.apl):");
      c.gridx = 0;
      c.gridy = 0;
      panel2.add(label2, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      panel2.add(inputFile1TF, c);

      //---- button2 ----
      button2.setText("choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      panel2.add(button2, c);

      //---- label3 ----
      c.gridx = 0;
      c.gridy++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      label3.setText("Peak apl file (.peak.apl):");
      panel2.add(label3, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      panel2.add(inputFile2TF, c);

      //---- button3 ----
      button3.setText("choose");
      c.gridx++;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      panel2.add(button3, c);

    add(panel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
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
