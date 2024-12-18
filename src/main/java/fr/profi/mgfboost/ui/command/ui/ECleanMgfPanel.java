/*
 * Created by JFormDesigner on Thu Sep 01 14:09:07 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.mgf.MGFECleaner;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author unknown
 */
public class ECleanMgfPanel extends AbstractCommandPanel<CommandArguments.MgfCleanerCommand> {

  private JTextField mzTolPpmTF;
  private JComboBox labelMethodCombo;

  public ECleanMgfPanel() {
    initComponents();
  }

  public ECleanMgfPanel(CommandArguments.MgfCleanerCommand command) {
    initComponents();
    updatePanelFromCommand(command);
  }

  public AbstractCommandPanel<CommandArguments.MgfCleanerCommand> updatePanelFromCommand(CommandArguments.MgfCleanerCommand command) {
    mzTolPpmTF.setText(command.mzTolPPM.toString());
    labelMethodCombo.setSelectedItem((command.labelingMethodName == null || command.labelingMethodName.isEmpty()) ? "None" : command.labelingMethodName);
    return this;
  }


  public boolean buildCommand(CommandArguments.MgfCleanerCommand command) {
    command.outputFileName = outputFilePanel.getOutputFilepath();
    command.mzTolPPM = Float.parseFloat(mzTolPpmTF.getText().trim());
    String methodName = (String) labelMethodCombo.getSelectedItem();
    command.labelingMethodName = (methodName.equalsIgnoreCase("none")) ? null : methodName.toUpperCase();
    return true;
  }

  private void initComponents() {

    mzTolPpmTF = new JTextField();

    //======== this ========
    setLayout(new GridBagLayout());

    outputFilePanel = new OutputFilePanel();

    final Insets defaultInsets = new Insets(0, 5, 0, 5);
    GridBagConstraints insidePanelGBC = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, defaultInsets, 0, 0);

    GridBagConstraints globalPanelGBC  = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(5, 5, 5, 5), 0, 0);

    globalPanelGBC.gridx = 0;
    globalPanelGBC.gridy = 0;

    add(outputFilePanel, globalPanelGBC);

    globalPanelGBC.gridy++;

    //======== panel1 ========
    {
      JPanel panel1 = new JPanel();
      panel1.setBorder(new TitledBorder("global parameters"));
      panel1.setLayout(new GridBagLayout());


      JLabel label1 = new JLabel("m/z fragments tolerance (ppm):");
      panel1.add(label1,insidePanelGBC);

      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.BOTH;
      panel1.add(mzTolPpmTF, insidePanelGBC);

      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy++;
      insidePanelGBC.weightx = 0.0;
      insidePanelGBC.fill = GridBagConstraints.NONE;
      JLabel label2 = new JLabel("label method:");
      panel1.add(label2,insidePanelGBC);

      final List<String> labelingMethods = Arrays.stream(MGFECleaner.IsobaricTag.values()).map(v -> v.name()).collect(Collectors.toList());
      labelingMethods.add(0, "None");
      String[] labelingMethodValues =  labelingMethods.toArray(String[]::new);
      labelMethodCombo = new JComboBox(labelingMethodValues);
      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.BOTH;
      panel1.add(labelMethodCombo, insidePanelGBC);

      add(panel1, globalPanelGBC);
    }
  }


  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new ECleanMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    EventQueue.invokeLater(() -> f.setVisible(true));
  }
}
