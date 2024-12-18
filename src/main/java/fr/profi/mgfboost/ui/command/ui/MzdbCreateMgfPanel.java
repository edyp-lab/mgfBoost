/*
 * Created by JFormDesigner on Tue Aug 23 15:12:25 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.mgf.MGFECleaner;
import fr.profi.mzknife.mzdb.MgfBoostConfigTemplate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CB205360
 */
public class MzdbCreateMgfPanel extends AbstractCommandPanel<CommandArguments.MzDBCreateMgfCommand> {

  private JComboBox<String> precComputerCombo;
  private JTextField mzToleranceTF;
  private JTextField intensityCutoffTF;
  private JCheckBox prolineTitleCbx;
  private JComboBox<String> cleanMethodCombo;
  private JLabel cleanLabelingMethodLabel;
  private JLabel cleanConfigLabel;
  private JComboBox<String> cleanLabelingMethodCombo;
  private JComboBox<String> cleanConfigCombo;

  private final boolean showOutputTF;
  private final static Logger LOG = LoggerFactory.getLogger(MzdbCreateMgfPanel.class);

  private MgfBoostConfigTemplate mgfBoostConfigTemplate = null;
  private String buildCmdErrorMsg;
  private boolean buildCmdSuccess = true;
  private boolean updateOnGoing = false;

  public MzdbCreateMgfPanel() {
    this(true);
  }

  public MzdbCreateMgfPanel(boolean showOutputTF) {
    this.showOutputTF = showOutputTF;
    initComponents();
  }

  public MzdbCreateMgfPanel(CommandArguments.MzDBCreateMgfCommand command, boolean showOutputTF) {
    this(showOutputTF);
    updatePanelFromCommand(command);
  }

  public void setMgfBoostConfigTemplate(MgfBoostConfigTemplate mgfBoostConfigTemplate) {
    this.mgfBoostConfigTemplate = mgfBoostConfigTemplate;
    if (mgfBoostConfigTemplate != null) {
      String configName = "mgf_boost - " + mgfBoostConfigTemplate.toString();
      precComputerCombo.setSelectedItem(configName);
    } else {
      precComputerCombo.setSelectedItem("main_precursor_mz");
    }
  }

  public MgfBoostConfigTemplate getMgfBoostConfigTemplate() {
    return mgfBoostConfigTemplate;
  }

  public boolean buildCommand(CommandArguments.MzDBCreateMgfCommand command) {
    buildCmdSuccess = true;
    buildCmdErrorMsg = "";
    if(showOutputTF) {
      command.outputFile = outputFilePanel.getOutputFilepath();
    }
    command.msLevel = 2;
    String[] precComputationParams = ((String) precComputerCombo.getSelectedItem()).split("-");
    command.precMzComputation = precComputationParams[0].trim().toLowerCase();
    if (command.precMzComputation.equalsIgnoreCase("mgf_boost")) {
      try {
        command.useHeader = mgfBoostConfigTemplate.isUseHeader();
        command.useSelectionWindow = mgfBoostConfigTemplate.isUseSelectionWindow();
        command.swMaxPrecursorsCount = mgfBoostConfigTemplate.getSwMaxPrecursorsCount();
        command.swIntensityThreshold = mgfBoostConfigTemplate.getSwIntensityThreshold();
        command.pifThreshold = mgfBoostConfigTemplate.getPifThreshold();
        command.rankThreshold = mgfBoostConfigTemplate.getRankThreshold();
        command.scanSelectorMode = CommandArguments.ScanSelectorMode.valueOf(mgfBoostConfigTemplate.getScanSelector().toString());
      } catch (IllegalArgumentException iae) {
        buildCmdSuccess = false;
        buildCmdErrorMsg += "\n";
        buildCmdErrorMsg+="MGF boost config template should be specified ";
      }
    }
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

    String cleanLabelingMethodName = (String) cleanLabelingMethodCombo.getSelectedItem();
    command.cleanMethod = (String) cleanMethodCombo.getSelectedItem();
    if(!command.cleanMethod.equalsIgnoreCase("None")) {
      command.cleanConfig = (CommandArguments.CleanConfig) cleanConfigCombo.getSelectedItem();
      command.cleanLabelMethodName = (cleanLabelingMethodName.equalsIgnoreCase("none")) ? "" : cleanLabelingMethodName.toUpperCase();
      if(command.cleanConfig.equals(CommandArguments.CleanConfig.TMT_LABELED) && command.cleanLabelMethodName.isEmpty()){
        if(!buildCmdSuccess)
          buildCmdErrorMsg += "\n";
        buildCmdErrorMsg+="clean labeling method should be specified for "+ CommandArguments.CleanConfig.TMT_LABELED.getDisplayValue();
        buildCmdSuccess = false;
      }
    }
    if(!buildCmdSuccess)
      LOG.warn(buildCmdErrorMsg);
    return buildCmdSuccess;
  }

  @Override
  public void showErrorMessage(){
    if(!buildCmdSuccess)
      JOptionPane.showMessageDialog(this, buildCmdErrorMsg, "Create Mgf",JOptionPane.ERROR_MESSAGE);
  }

  public void updateComponents(){
    cleanConfigUpdated();
    cleanLabelingMethodUpdated();
    cleanMethodUpdated();
  }

  public AbstractCommandPanel<CommandArguments.MzDBCreateMgfCommand> updatePanelFromCommand(CommandArguments.MzDBCreateMgfCommand command) {
    precComputerCombo.setSelectedItem(command.precMzComputation);
    mzToleranceTF.setText(Float.toString(command.mzTolPPM));
    intensityCutoffTF.setText(Float.toString(command.intensityCutoff));
    prolineTitleCbx.setSelected(command.exportProlineTitle);
    cleanMethodCombo.setSelectedItem(command.cleanMethod);
    cleanLabelingMethodCombo.setSelectedItem((command.cleanLabelMethodName.isEmpty()) ? "None" : command.cleanLabelMethodName);
    return this;
  }

  private void initComponents() {

    JPanel precursorPanel = new JPanel();
    JLabel precComputerLabel = new JLabel();

    List<String> strings = Arrays.stream(MgfBoostConfigTemplate.values()).map(v -> "mgf_boost - "+ v.toString()).collect(Collectors.toList());
    strings.add(0, "main_precursor_mz");

    String[] precComputerValues = strings.toArray(new String[0]);

    precComputerCombo = new JComboBox<>(precComputerValues);
    precComputerCombo.addActionListener(e -> precComputerConfigUpdated());
    JLabel mzToleranceLabel = new JLabel();
    mzToleranceTF = new JTextField();
    JPanel optionsPanel = new JPanel();
    JLabel intensityCutoffLabel = new JLabel();
    intensityCutoffTF = new JTextField();
    prolineTitleCbx = new JCheckBox();
    JPanel fragmentsPanel = new JPanel();

    String[] cleanMethodValues = {"None", "eClean", "pClean"};
    cleanMethodCombo = new JComboBox(cleanMethodValues);
    cleanMethodCombo.setSelectedIndex(0);
    cleanMethodCombo.addActionListener(e -> cleanMethodUpdated());

    cleanLabelingMethodLabel = new JLabel();

    final List<String> labelingMethods = Arrays.stream(MGFECleaner.IsobaricTag.values()).map(v -> v.name()).collect(Collectors.toList());
    labelingMethods.add(0, "None");
    String[] labelingMethodValues =  labelingMethods.toArray(String[]::new);
    cleanLabelingMethodCombo = new JComboBox(labelingMethodValues);
    cleanLabelingMethodCombo.setSelectedIndex(0);
    cleanLabelingMethodCombo.addActionListener(e ->  cleanLabelingMethodUpdated() );

    cleanConfigLabel = new JLabel();
    CommandArguments.CleanConfig[] configValues =  CommandArguments.CleanConfig.values();
    cleanConfigCombo = new JComboBox(configValues);
    cleanConfigCombo.addActionListener(e ->  cleanConfigUpdated() );
    cleanConfigCombo.setSelectedItem(CommandArguments.CleanConfig.LABEL_FREE);

    cleanMethodUpdated();

    setLayout(new GridBagLayout());
    final Insets defaultInsets = new Insets(5, 5, 5, 5);

    GridBagConstraints insidePanelGBC = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    GridBagConstraints globalPanelGBC  = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(5, 5, 5, 5), 0, 0);

    globalPanelGBC.gridx =0;
    globalPanelGBC.gridy =0;

    //======== output file panel  ========

    if(showOutputTF) {
      outputFilePanel = new OutputFilePanel();
      add(outputFilePanel,globalPanelGBC);
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

    add(optionsPanel, globalPanelGBC);
    globalPanelGBC.gridy++;

    //======== fragmentsPanel ========

    fragmentsPanel.setBorder(new TitledBorder("fragmentation peaks"));
    fragmentsPanel.setLayout(new GridBagLayout());

    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy = 0;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    insidePanelGBC.gridwidth = 1;
    //---- pCleanCbx ----
    JLabel cleanMethodLabel = new JLabel();
    cleanMethodLabel.setText("clean Method:");
    fragmentsPanel.add(cleanMethodLabel,insidePanelGBC);

    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    fragmentsPanel.add(cleanMethodCombo, insidePanelGBC);

    //---- pCleanConfigLabel ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy++;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.gridwidth = 1;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    cleanConfigLabel.setText("clean Configuration:");
    fragmentsPanel.add(cleanConfigLabel,insidePanelGBC);
    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    fragmentsPanel.add(cleanConfigCombo, insidePanelGBC);

      //---- pCleanMethodLabel ----
    insidePanelGBC.gridx = 0;
    insidePanelGBC.gridy++;
    insidePanelGBC.weightx = 0.0;
    insidePanelGBC.gridwidth = 1;
    insidePanelGBC.fill = GridBagConstraints.NONE;
    cleanLabelingMethodLabel.setText("labeling method:");
    fragmentsPanel.add(cleanLabelingMethodLabel,insidePanelGBC);
    insidePanelGBC.gridx++;
    insidePanelGBC.weightx = 1.0;
    insidePanelGBC.fill = GridBagConstraints.BOTH;
    fragmentsPanel.add(cleanLabelingMethodCombo, insidePanelGBC);

    add(fragmentsPanel, globalPanelGBC);
  }

  private void precComputerConfigUpdated() {
    String[] precComputationParams = ((String) precComputerCombo.getSelectedItem()).split("-");
    if (precComputationParams[0].trim().toLowerCase().equalsIgnoreCase("mgf_boost")) {
      mgfBoostConfigTemplate = MgfBoostConfigTemplate.valueOf(precComputationParams[1].trim().toUpperCase());
    } else {
      mgfBoostConfigTemplate = null;
    }
  }

  private void cleanLabelingMethodUpdated(){
    if(updateOnGoing)
      return;
    try {
      updateOnGoing = true;
      if (!((String) cleanLabelingMethodCombo.getSelectedItem()).equalsIgnoreCase("none")) {
        cleanConfigCombo.setSelectedItem(CommandArguments.CleanConfig.TMT_LABELED);
      }
    } finally {
      updateOnGoing = false;
    }
  }

  private void cleanConfigUpdated(){
    if(updateOnGoing)
      return;

    try {
      updateOnGoing = true;
      if(!cleanConfigCombo.getSelectedItem().equals(CommandArguments.CleanConfig.TMT_LABELED)){
        cleanLabelingMethodCombo.setEnabled(false);
        cleanLabelingMethodCombo.setSelectedIndex(0);
      } else {
        cleanLabelingMethodCombo.setEnabled(true);
      }
    } finally {
      updateOnGoing = false;
    }
  }

  private void cleanMethodUpdated(){
    if(updateOnGoing){
      return;
    }
    try {
      updateOnGoing = true;
      if (cleanMethodCombo.getSelectedItem().toString().equalsIgnoreCase("none")) {
        cleanLabelingMethodLabel.setEnabled(false);
        cleanConfigLabel.setEnabled(false);
        cleanLabelingMethodCombo.setEnabled(false);
        cleanConfigCombo.setEnabled(false);
      } else {
        cleanLabelingMethodLabel.setEnabled(true);
        cleanConfigLabel.setEnabled(true);
        cleanConfigCombo.setEnabled(true);
        if(!cleanConfigCombo.getSelectedItem().equals(CommandArguments.CleanConfig.TMT_LABELED))
          cleanLabelingMethodCombo.setEnabled(false);
        else
          cleanLabelingMethodCombo.setEnabled(true);

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

      f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        CommandArguments.MzDBCreateMgfCommand command = new CommandArguments.MzDBCreateMgfCommand();
        if(panel.buildCommand(command)) {
          System.out.println("Command args : " + ToStringBuilder.reflectionToString(command));
          System.exit(0);
        } else {
          panel.showErrorMessage();
        }
      }
    });
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }

}
