/*
 * Created by JFormDesigner on Tue Aug 23 15:12:25 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
  private JLabel pCleanMethodLabel;
  private JLabel pCleanConfigLabel;
  private JComboBox<String> pCleanMethodCombo;
  private JComboBox<String> pCleanConfigCombo;

  private final boolean showOutputTF;
  private final static Logger LOG = LoggerFactory.getLogger(MzdbCreateMgfPanel.class);

  private String buildCmdErrorMsg;
  private boolean buildCmdSuccess = true;

  public MzdbCreateMgfPanel() {
    this(true);
  }

  public MzdbCreateMgfPanel(boolean showOutputTF) {
    this.showOutputTF = showOutputTF;
    initComponents();
  }

  public MzdbCreateMgfPanel(CommandArguments.MzDBCreateMgfCommand command) {
    this();
    initValues(command);
  }

  public MzdbCreateMgfPanel(CommandArguments.MzDBCreateMgfCommand command, boolean showOutputTF) {
    this(showOutputTF);
    initValues(command);
  }

  public boolean buildCommand(CommandArguments.MzDBCreateMgfCommand command) {
    buildCmdSuccess = true;
    buildCmdErrorMsg = "";
    if(showOutputTF) {
      command.outputFile = outputTF.getText().trim();
      if (command.outputFile.isEmpty()) {
        buildCmdErrorMsg = "No output file specified ! ";
        buildCmdSuccess = false;
      }
    }
    command.msLevel = 2;
    command.precMzComputation = ((String)precComputerCombo.getSelectedItem()).toLowerCase();
    try {
      command.mzTolPPM = Float.parseFloat(mzToleranceTF.getText());
      command.intensityCutoff = Float.parseFloat(intensityCutoffTF.getText());
    } catch (NumberFormatException nfe){
      if(!buildCmdSuccess)
        buildCmdErrorMsg += "\n";
      buildCmdErrorMsg+="Error reading mz Tolerance and/or intensity cutoff";
      buildCmdSuccess = false;
    }
    command.exportProlineTitle = prolineTitleCbx.isSelected();

    String pCleanMethodName = (String) pCleanMethodCombo.getSelectedItem();
    command.pClean = pCleanCbx.isSelected();
    if(command.pClean) {
      command.pCleanConfig = (CommandArguments.PCleanConfig) pCleanConfigCombo.getSelectedItem();
      command.pCleanLabelMethodName = (pCleanMethodName.equalsIgnoreCase("none")) ? "" : pCleanMethodName.toUpperCase();
      if(command.pCleanConfig.equals(CommandArguments.PCleanConfig.TMT_LABELED) && command.pCleanLabelMethodName.isEmpty()){
        if(!buildCmdSuccess)
          buildCmdErrorMsg += "\n";
        buildCmdErrorMsg+="pClean labelling method should be specified for "+CommandArguments.PCleanConfig.TMT_LABELED.getDisplayValue();
        buildCmdSuccess = false;
      }
    }
    if(!buildCmdSuccess)
      LOG.warn(buildCmdErrorMsg);
    return buildCmdSuccess;
  }

  public void showErrorMessage(){
    if(!buildCmdSuccess)
      JOptionPane.showMessageDialog(this, buildCmdErrorMsg, "Create Mgf",JOptionPane.ERROR_MESSAGE);

  }

  public void updateComponents(){
    updateConfigCombo();
    updateMethodCombo();
    updatePCleanOption();
  }

  private void initValues(CommandArguments.MzDBCreateMgfCommand command) {
    precComputerCombo.setSelectedItem(command.precMzComputation);
    mzToleranceTF.setText(Float.toString(command.mzTolPPM));
    intensityCutoffTF.setText(Float.toString(command.intensityCutoff));
    prolineTitleCbx.setSelected(command.exportProlineTitle);
    pCleanCbx.setSelected(command.pClean);
    pCleanMethodCombo.setSelectedItem((command.pCleanLabelMethodName.isEmpty()) ? "None" : command.pCleanLabelMethodName);
  }

  private void initComponents() {
    JLabel outputLabel = null;
    JButton outputBtn = null;
    if(showOutputTF) {
      outputLabel = new JLabel();
      outputTF = new JTextField();
      outputBtn = new JButton();
    }

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
    pCleanMethodLabel = new JLabel();
    String[] labelMethodValues = {"None", "ITRAQ4PLEX", "ITRAQ8PLEX", "TMT6PLEX", "TMT10PLEX", "TMT11PLEX", "TMT16PLEX", "TMT18PLEX"};
    pCleanMethodCombo = new JComboBox(labelMethodValues);
    pCleanMethodCombo.setSelectedIndex(0);
    pCleanMethodCombo.addActionListener(e ->  updateMethodCombo() );

    pCleanConfigLabel = new JLabel();
//    List<String> cfgMethodVals = Arrays.stream(CommandArguments.PCleanConfig.values()).map(pclean ->  pclean.getDisplayValue() ).collect(Collectors.toList());
    CommandArguments.PCleanConfig[] configValues =  CommandArguments.PCleanConfig.values();
//    String[] configMethodValues = cfgMethodVals.toArray(new String[0]);
    pCleanConfigCombo = new JComboBox(configValues);
    pCleanConfigCombo.addActionListener(e ->  updateConfigCombo() );
    pCleanConfigCombo.setSelectedItem(CommandArguments.PCleanConfig.LABEL_FREE);
    pCleanCbx.addActionListener(e -> updatePCleanOption());
    pCleanCbx.setSelected(false);
    updatePCleanOption();

    //======== this ========
    setLayout(new GridBagLayout());
    final Insets defaultInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints insidePanelGBC = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    GridBagConstraints globalPanelGBC  = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(5, 0, 5, 0), 0, 0);
    globalPanelGBC.gridx =0;
    globalPanelGBC.gridy =0;

    //======== panel1 : output  ========
    //---- outputLabel ----
    if(showOutputTF) {
      JPanel panel1 = new JPanel();
      panel1.setLayout(new GridBagLayout());
      outputLabel.setText("output File:");
      panel1.add(outputLabel, insidePanelGBC);

      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.BOTH;
      panel1.add(outputTF, insidePanelGBC);


      //---- outputBtn ----
      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 0.0;
      insidePanelGBC.fill = GridBagConstraints.NONE;

      outputBtn.setText("choose");
      outputBtn.addActionListener(e -> FileChooserUtil.saveAsChooser(outputTF));
      panel1.add(outputBtn, insidePanelGBC);

      add(panel1,globalPanelGBC);
      globalPanelGBC.gridy++;
    }

    //======== precursorPanel ========
    precursorPanel.setBorder(new TitledBorder("precursor"));
    precursorPanel.setLayout(new GridBagLayout());

      //---- precComputerLabel ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy = 0;
    precComputerLabel.setText("computation method:");
    precursorPanel.add(precComputerLabel, insidePanelGBC);
    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;

    precursorPanel.add(precComputerCombo, insidePanelGBC);

      //---- mzToleranceLabel ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy++;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    mzToleranceLabel.setText("mz tolerance (ppm):");
    precursorPanel.add(mzToleranceLabel, insidePanelGBC);

    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    precursorPanel.add(mzToleranceTF, insidePanelGBC);

    globalPanelGBC.insets = new Insets(0, 5, 0,5);
    add(precursorPanel, globalPanelGBC);
    globalPanelGBC.gridy++;

    //======== optionsPanel ========

    optionsPanel.setBorder(new TitledBorder("options"));
    optionsPanel.setLayout(new GridBagLayout());

    //---- intensityCutoffLabel ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy = 0;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    intensityCutoffLabel.setText("intensity cutoff:");
    optionsPanel.add(intensityCutoffLabel, insidePanelGBC);

    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    optionsPanel.add(intensityCutoffTF, insidePanelGBC);

      //---- prolineTitleCbx ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy++;
    insidePanelGBC.gridwidth = 2;
    prolineTitleCbx.setText("format spectrum title for Proline");
    optionsPanel.add(prolineTitleCbx, insidePanelGBC);

    globalPanelGBC.insets= new Insets(5, 0, 5, 0);
    add(optionsPanel, globalPanelGBC);
    globalPanelGBC.gridy++;

    //======== fragmentsPanel ========

    fragmentsPanel.setBorder(new TitledBorder("fragmentation peaks"));
    fragmentsPanel.setLayout(new GridBagLayout());

    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy = 0;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    insidePanelGBC.gridwidth = 2;
    //---- pCleanCbx ----
    pCleanCbx.setText("apply pClean process");
    fragmentsPanel.add(pCleanCbx, insidePanelGBC);

    //---- pCleanConfigLabel ----
    insidePanelGBC.gridy++;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.gridwidth = 1;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    pCleanConfigLabel.setText("pClean Configuration:");
    fragmentsPanel.add(pCleanConfigLabel,insidePanelGBC);
    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    fragmentsPanel.add(pCleanConfigCombo, insidePanelGBC);

      //---- pCleanMethodLabel ----
    insidePanelGBC.gridy++;
    insidePanelGBC.gridx = 0;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.gridwidth = 1;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    pCleanMethodLabel.setText("labeling method:");
    fragmentsPanel.add(pCleanMethodLabel,insidePanelGBC);
    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    fragmentsPanel.add(pCleanMethodCombo, insidePanelGBC);

    globalPanelGBC.insets= new Insets(0, 5, 0,5);
    add(fragmentsPanel, globalPanelGBC);
  }

  private boolean updateOnGoing = false;
  private void updateMethodCombo(){
    if(updateOnGoing)
      return;
    try {
      updateOnGoing = true;
      if (!((String) pCleanMethodCombo.getSelectedItem()).equalsIgnoreCase("none")) {
        pCleanConfigCombo.setSelectedItem(CommandArguments.PCleanConfig.TMT_LABELED/*getDisplayValue()*/);
      }
    } finally {
      updateOnGoing = false;
    }
  }

  private void updateConfigCombo(){
    if(updateOnGoing)
      return;

    try {
      updateOnGoing = true;
//      if (!((String) pCleanConfigCombo.getSelectedItem()).equalsIgnoreCase(CommandArguments.PCleanConfig.TMT_LABELED.getDisplayValue())) {
      if(!pCleanConfigCombo.getSelectedItem().equals(CommandArguments.PCleanConfig.TMT_LABELED)){
        pCleanMethodCombo.setEnabled(false);
        pCleanMethodCombo.setSelectedIndex(0);
      } else {
        pCleanMethodCombo.setEnabled(true);
      }
    } finally {
      updateOnGoing = false;
    }
  }

  private void updatePCleanOption(){
    if(updateOnGoing){
      return;
    }
    try {
      updateOnGoing = true;
      if (!pCleanCbx.isSelected()) {
        pCleanMethodLabel.setEnabled(false);
        pCleanConfigLabel.setEnabled(false);
        pCleanMethodCombo.setEnabled(false);
        pCleanConfigCombo.setEnabled(false);
      } else {
        pCleanMethodLabel.setEnabled(true);
        pCleanConfigLabel.setEnabled(true);
        pCleanConfigCombo.setEnabled(true);
        if(!pCleanConfigCombo.getSelectedItem().equals(CommandArguments.PCleanConfig.TMT_LABELED))
          pCleanMethodCombo.setEnabled(false);
        else
          pCleanMethodCombo.setEnabled(true);

      }
    }finally {
      updateOnGoing = false;
    }
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    MzdbCreateMgfPanel panel = new  MzdbCreateMgfPanel();
    f.getContentPane().add(panel, BorderLayout.CENTER);
    f.pack();

//    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        if(panel.buildCommand(new CommandArguments.MzDBCreateMgfCommand()))
          System.exit(0);
        else
          panel.showErrorMessage();
      }
    });
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }
}
