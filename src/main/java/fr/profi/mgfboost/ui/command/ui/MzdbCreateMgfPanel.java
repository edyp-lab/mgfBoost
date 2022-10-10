/*
 * Created by JFormDesigner on Tue Aug 23 15:12:25 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author CB205360
 */
public class MzdbCreateMgfPanel extends JPanel {

  private JTextField outputTF;
  private JComboBox<String> precComputerCombo;
  private JTextField mzToleranceTF;
  private JTextField intensityCutoffTF;
  private JCheckBox prolineTitleCbx;
  private JCheckBox pCleanCbx;
  private JComboBox<String> pCleanMethodCombo;

  public MzdbCreateMgfPanel() {
    initComponents();
  }

  public MzdbCreateMgfPanel(CommandArguments.MzDBCreateMgfCommand command) {
    this();
    initValues(command);
  }

  public void buildCommand(CommandArguments.MzDBCreateMgfCommand command) {
    command.outputFile = outputTF.getText().trim();
    command.msLevel = 2;
    command.precMzComputation = ((String)precComputerCombo.getSelectedItem()).toLowerCase();
    command.mzTolPPM = Float.parseFloat(mzToleranceTF.getText());
    command.intensityCutoff = Float.parseFloat(intensityCutoffTF.getText());
    command.exportProlineTitle = prolineTitleCbx.isSelected();
    String pCleanMethodName = (String) pCleanMethodCombo.getSelectedItem();
    command.pClean = pCleanCbx.isSelected();
    command.pCleanLabelMethodName = (pCleanMethodName.equalsIgnoreCase("none")) ? "" : pCleanMethodName.toUpperCase();
  }


  private void initValues(CommandArguments.MzDBCreateMgfCommand command) {
    precComputerCombo.setSelectedItem(command.precMzComputation);
    mzToleranceTF.setText(Float.toString(10.0f)); // mzToleranceTF.setText(Float.toString(command.mzTolPPM));
    intensityCutoffTF.setText(Float.toString(command.intensityCutoff));
    prolineTitleCbx.setSelected(command.exportProlineTitle);
    pCleanCbx.setSelected(command.pClean);
    pCleanMethodCombo.setSelectedItem((command.pCleanLabelMethodName.isEmpty()) ? "None" : command.pCleanLabelMethodName);

  }
  private void initComponents() {
    JLabel outputLabel = new JLabel();
    outputTF = new JTextField();
    JButton outputBtn = new JButton();
    JPanel precursorPanel = new JPanel();
    JLabel precComputerLabel = new JLabel();
    String[] precComputerValues = {"main_precursor_mz","mgf_boost_v3.6"};
    precComputerCombo = new JComboBox<>(precComputerValues);
    JLabel mzToleranceLabel = new JLabel();
    mzToleranceTF = new JTextField();
    JPanel optionsPanel = new JPanel();
    JLabel intensityCutoffLabel = new JLabel();
    intensityCutoffTF = new JTextField();
    prolineTitleCbx = new JCheckBox();
    JPanel fragmentsPanel = new JPanel();
    pCleanCbx = new JCheckBox();
    JLabel pCleanMethodLabel = new JLabel();
    String[] labelMethodValues = {"None", "ITRAQ4PLEX", "ITRAQ8PLEX", "TMT6PLEX", "TMT10PLEX", "TMT11PLEX", "TMT16PLEX", "TMT18PLEX"};
    pCleanMethodCombo = new JComboBox(labelMethodValues);
    pCleanMethodCombo.addActionListener(e -> {
      if ( !((String) pCleanMethodCombo.getSelectedItem()).equalsIgnoreCase("none")) {
        pCleanCbx.setSelected(true);
      }
    });
    pCleanCbx.addActionListener(e -> {
      if (!pCleanCbx.isSelected()) {
        pCleanMethodCombo.setSelectedIndex(0);
      }
    });

    //======== this ========
    setLayout(new GridBagLayout());

    //======== panel1 ========
    JPanel panel1 = new JPanel();
    final Insets defaultInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    panel1.setLayout(new GridBagLayout());

    //---- outputLabel ----
    outputLabel.setText("output File:");
    panel1.add(outputLabel, c);

    c.gridx++;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.BOTH;
    panel1.add(outputTF, c);

    //---- outputBtn ----
    c.gridx++;
    c.weightx = 0.0;
    c.fill = GridBagConstraints.NONE;

    outputBtn.setText("choose");
    outputBtn.addActionListener(e -> FileChooserUtil.saveAsChooser(outputTF));
    panel1.add(outputBtn, c);

    add(panel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(5, 0, 5, 0), 0, 0));

    //======== precursorPanel ========

    precursorPanel.setBorder(new TitledBorder("precursor"));
    precursorPanel.setLayout(new GridBagLayout());

      //---- precComputerLabel ----
    c.gridx = 0;
    c.gridy = 0;
    precComputerLabel.setText("computation method:");
      precursorPanel.add(precComputerLabel, c);
      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;

      precursorPanel.add(precComputerCombo, c);

      //---- mzToleranceLabel ----
    c.gridx = 0;
    c.gridy++;
    c.weightx = 0.0;
    c.fill = GridBagConstraints.NONE;
      mzToleranceLabel.setText("mz tolerance (ppm):");
      precursorPanel.add(mzToleranceLabel, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      precursorPanel.add(mzToleranceTF, c);

    add(precursorPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 5, 0,5), 0, 0));

    //======== optionsPanel ========

      optionsPanel.setBorder(new TitledBorder("options"));
      optionsPanel.setLayout(new GridBagLayout());

      //---- intensityCutoffLabel ----
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0.0;
      c.fill = GridBagConstraints.NONE;
      intensityCutoffLabel.setText("intensity cutoff:");
      optionsPanel.add(intensityCutoffLabel, c);

      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      optionsPanel.add(intensityCutoffTF, c);

      //---- prolineTitleCbx ----
      c.gridx = 0;
      c.gridy++;
      c.gridwidth = 2;
    prolineTitleCbx.setText("format spectrum title for Proline");
      optionsPanel.add(prolineTitleCbx, c);

    add(optionsPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 5, 0,5), 0, 0));

    //======== fragmentsPanel ========

      fragmentsPanel.setBorder(new TitledBorder("fragmentation peaks"));
      fragmentsPanel.setLayout(new GridBagLayout());
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 2;

      //---- pCleanCbx ----
      pCleanCbx.setText("apply pClean process");
      fragmentsPanel.add(pCleanCbx, c);

      //---- pCleanMethodLabel ----
      c.gridy++;
      c.weightx = 0.0;
    c.gridwidth = 1;
      c.fill = GridBagConstraints.NONE;
      pCleanMethodLabel.setText("labeling method:");
      fragmentsPanel.add(pCleanMethodLabel,c);
      c.gridx++;
      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      fragmentsPanel.add(pCleanMethodCombo, c);

    add(fragmentsPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 5, 0,5), 0, 0));
  }


  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new MzdbCreateMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }
}
