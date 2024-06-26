/*
 * Created by JFormDesigner on Thu Sep 01 14:09:07 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.mgf.MGFECleaner;
import fr.profi.mzknife.mgf.PCleanConfigTemplate;
import fr.profi.mzknife.mgf.PCleanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author unknown
 */
public class PCleanMgfPanel extends AbstractCommandPanel<CommandArguments.PCleanCommand> {

  private final static Logger logger = LoggerFactory.getLogger(PCleanMgfPanel.class);

  private JTextField iTolTF;
  private JCheckBox aa2Cbx;
  private JCheckBox imonCbx;
  private JComboBox labelMethodCombo;
  private JCheckBox reporterFilterCbx;
  private JCheckBox labelFilterCbx;
  private JCheckBox lowFilterCbx;
  private JCheckBox highFilterCbx;
  private JCheckBox isoReductionCbx;
  private JCheckBox chargeDeconvolutionCbx;
  private JCheckBox mergeIonsCbx;
  private JCheckBox largerThanPrecursorCbx;

  public PCleanMgfPanel() {
    initComponents();
  }

  public AbstractCommandPanel<CommandArguments.PCleanCommand> updatePanelFromCommand(CommandArguments.PCleanCommand command) {
    iTolTF.setText(command.itol.toString());
    aa2Cbx.setSelected(command.aa2);
    imonCbx.setSelected(command.ionFilter);
    reporterFilterCbx.setSelected(command.repFilter);
    labelFilterCbx.setSelected(command.labelFilter);
    lowFilterCbx.setSelected(command.low);
    highFilterCbx.setSelected(command.high);
    isoReductionCbx.setSelected(command.isoReduction);
    chargeDeconvolutionCbx.setSelected(command.chargeDeconv);
    largerThanPrecursorCbx.setSelected(command.largerThanPrecursor);
    mergeIonsCbx.setSelected(command.ionsMerge);
    labelMethodCombo.setSelectedItem((command.labelMethod == null || command.labelMethod.isEmpty()) ? "None" : command.labelMethod);
    return this;
  }


  public boolean buildCommand(CommandArguments.PCleanCommand command) {
    command.outputFileName = outputFilePanel.getOutputFilepath();
    command.itol = Double.parseDouble(iTolTF.getText().trim());
    command.aa2 = aa2Cbx.isSelected();
    command.ionFilter = imonCbx.isSelected();
    command.repFilter = reporterFilterCbx.isSelected();
    command.labelFilter = labelFilterCbx.isSelected();
    command.low = lowFilterCbx.isSelected();
    command.high = highFilterCbx.isSelected();
    command.isoReduction = isoReductionCbx.isSelected();
    command.chargeDeconv = chargeDeconvolutionCbx.isSelected();
    command.largerThanPrecursor = largerThanPrecursorCbx.isSelected();
    command.ionsMerge = mergeIonsCbx.isSelected();
    String methodName = (String) labelMethodCombo.getSelectedItem();
    command.labelMethod = (methodName.equalsIgnoreCase("none")) ? null : methodName.toUpperCase();

    return true;
  }


  private void initComponents() {
    JPanel globalParamsPanel = new JPanel();
    iTolTF = new JTextField();
    aa2Cbx = new JCheckBox();
    imonCbx = new JCheckBox();

    JPanel module1ParamsPanel = new JPanel();
    final List<String> labelingMethods = Arrays.stream(MGFECleaner.IsobaricTag.values()).map(v -> v.name()).collect(Collectors.toList());
    labelingMethods.add(0, "None");
    String[] labelingMethodValues =  labelingMethods.toArray(String[]::new); //{"None", "ITRAQ4PLEX", "ITRAQ8PLEX", "TMT6PLEX", "TMT10PLEX", "TMT11PLEX", "TMT16PLEX", "TMT18PLEX"};
    labelMethodCombo = new JComboBox(labelingMethodValues);

    reporterFilterCbx = new JCheckBox();
    labelFilterCbx = new JCheckBox();
    lowFilterCbx = new JCheckBox();
    highFilterCbx = new JCheckBox();

    JPanel module2ParamsPanel = new JPanel();
    isoReductionCbx = new JCheckBox();
    chargeDeconvolutionCbx = new JCheckBox();
    mergeIonsCbx = new JCheckBox();
    largerThanPrecursorCbx = new JCheckBox();

    labelMethodCombo.addActionListener(e -> {
      updateModule1ParametersStatus();
    });
    labelMethodCombo.setSelectedIndex(0);
    //======== this ========
    setLayout(new GridBagLayout());
    final Insets defaultInsets = new Insets(0, 5, 0, 5);
    GridBagConstraints insidePanelGBC = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    GridBagConstraints globalPanelGBC  = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(5, 5, 5, 5), 0, 0);

    globalPanelGBC.gridx = 0;
    globalPanelGBC.gridy = 0;

    {
      outputFilePanel = new OutputFilePanel();
      add(outputFilePanel, globalPanelGBC);
      globalPanelGBC.gridy++;
    }

    //======== Load Template  =======
    {
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy = 0;
      JPanel templatePanel = new JPanel();
      templatePanel.setBorder(new TitledBorder("configuration template"));
      templatePanel.setLayout(new GridBagLayout());

      insidePanelGBC.weightx = 0.0;
      final JButton templateBtn = new JButton("load template");
      templateBtn.addActionListener(e -> {

        String[] templateValues =  Arrays.stream(CommandArguments.CleanConfig.values()).map(v -> v.getDisplayValue()).toArray(String[]::new);
        String selectedOption = (String)JOptionPane.showInputDialog(
                this,
                "configuration template :",
                "Choose configuration template",
                JOptionPane.QUESTION_MESSAGE,
                null,
                templateValues,
                templateValues[0]);

        if (selectedOption != null) {
            updateCleanConfiguration(CommandArguments.CleanConfig.getConfigFromDisplayValue(selectedOption));
        }
      });
      templatePanel.add(templateBtn, insidePanelGBC);

      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy++;
      insidePanelGBC.gridwidth = 2;

      add(templatePanel, globalPanelGBC);
      globalPanelGBC.gridy++;
    }

    //======== globalParamsPanel ========
    {
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy = 0;
      insidePanelGBC.gridwidth = 1;
      insidePanelGBC.weightx = 0.0;
      globalParamsPanel.setBorder(new TitledBorder("global parameters"));
      globalParamsPanel.setLayout(new GridBagLayout());

      //---- label2 ----
      JLabel label2 = new JLabel("m/z fragments tolerance (Da):");
      globalParamsPanel.add(label2, insidePanelGBC);

      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.BOTH;
      globalParamsPanel.add(iTolTF, insidePanelGBC);

      //---- aa2Cbx ----
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy++;
      insidePanelGBC.anchor = GridBagConstraints.WEST;
      insidePanelGBC.gridwidth = 2;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.NONE;
      aa2Cbx.setText("consider mass gap of two AA");
      globalParamsPanel.add(aa2Cbx,insidePanelGBC);

      //---- mionCbx ----
      insidePanelGBC.gridy++;
      imonCbx.setText("filter immonium ions");
      globalParamsPanel.add(imonCbx, insidePanelGBC);

      add(globalParamsPanel, globalPanelGBC);
      globalPanelGBC.gridy++;
    }



    //======== module1ParamsPanel ========
    {
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy = 0;
      insidePanelGBC.gridwidth = 1;
      insidePanelGBC.weightx = 0.0;
      module1ParamsPanel.setBorder(new TitledBorder("module1 parameters"));
      module1ParamsPanel.setLayout(new GridBagLayout());

      //---- label3 ----

      JLabel label3 = new JLabel("label method:");
      module1ParamsPanel.add(label3,insidePanelGBC);

      insidePanelGBC.gridx++;
      insidePanelGBC.weightx = 1.0;
      insidePanelGBC.fill = GridBagConstraints.BOTH;
      module1ParamsPanel.add(labelMethodCombo, insidePanelGBC);

      //---- reporterFilterCbx ----
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy++;
      insidePanelGBC.gridwidth = 2;
      insidePanelGBC.anchor = GridBagConstraints.WEST;
      reporterFilterCbx.setText("filter reporter ions");
      module1ParamsPanel.add(reporterFilterCbx, insidePanelGBC);

      //---- labelFilterCbx ----
      insidePanelGBC.gridy++;
      labelFilterCbx.setText("filter label-associated ions");
      module1ParamsPanel.add(labelFilterCbx, insidePanelGBC);

      //---- lowFilterCbx ----
      insidePanelGBC.gridy++;
      lowFilterCbx.setText("clear low b/y-ion window");
      module1ParamsPanel.add(lowFilterCbx, insidePanelGBC);

      //---- highFilterCbx ----
      insidePanelGBC.gridy++;
      highFilterCbx.setText("clear high b/y-ions window");
      module1ParamsPanel.add(highFilterCbx, insidePanelGBC);

      add(module1ParamsPanel, globalPanelGBC);
      globalPanelGBC.gridy++;
    }


    //======== module2ParamsPanel ========
    {
      insidePanelGBC.gridx = 0;
      insidePanelGBC.gridy = 0;
      module2ParamsPanel.setBorder(new TitledBorder("module2 parameters"));
      module2ParamsPanel.setLayout(new GridBagLayout());

      //---- isoReductionCbx ----
      isoReductionCbx.setText("do isotopic reduction");
      module2ParamsPanel.add(isoReductionCbx, insidePanelGBC);

      //---- chargeDeconvolutionCbx ----
      insidePanelGBC.gridy++;
      chargeDeconvolutionCbx.setText("do charge deconvolution");
      module2ParamsPanel.add(chargeDeconvolutionCbx, insidePanelGBC);

      //---- mergeIonsCbx ----
      insidePanelGBC.gridy++;
      mergeIonsCbx.setText("merge ions of similar masses");
      module2ParamsPanel.add(mergeIonsCbx, insidePanelGBC);

      //---- largerThanPrecursorCbx ----
      insidePanelGBC.gridy++;
      largerThanPrecursorCbx.setText("filter ions larger than precursor\u2019s mass");
      module2ParamsPanel.add(largerThanPrecursorCbx,insidePanelGBC);

      add(module2ParamsPanel, globalPanelGBC);
    }

  }

  private void updateCleanConfiguration(CommandArguments.CleanConfig cleanConfig) {

    final PCleanConfigTemplate pCleanConfigTemplate = cleanConfig.getPCleanConfigTemplate();
    iTolTF.setText(Double.toString(PCleanProcessor.MS2_DEFAULT_TOL));
    aa2Cbx.setSelected(false);
    imonCbx.setSelected(pCleanConfigTemplate.getImonFilter());
    reporterFilterCbx.setSelected(pCleanConfigTemplate.getRepFilter());
    labelFilterCbx.setSelected(pCleanConfigTemplate.getLabelFilter());
    lowFilterCbx.setSelected(pCleanConfigTemplate.getLowWinFilter());
    highFilterCbx.setSelected(pCleanConfigTemplate.getHighWinFilter());
    isoReductionCbx.setSelected(pCleanConfigTemplate.getIsoReduction());
    chargeDeconvolutionCbx.setSelected(pCleanConfigTemplate.getChargeDeconv());
    largerThanPrecursorCbx.setSelected(pCleanConfigTemplate.getLargerThanPrecursor());
    mergeIonsCbx.setSelected(pCleanConfigTemplate.getIonsMerge());
    labelMethodCombo.setSelectedItem("None");
  }

  private void updateModule1ParametersStatus() {
    boolean labelMethod = (!((String) labelMethodCombo.getSelectedItem()).equalsIgnoreCase("none"));
    reporterFilterCbx.setEnabled(labelMethod);
    labelFilterCbx.setEnabled(labelMethod);
    lowFilterCbx.setEnabled(labelMethod);
    highFilterCbx.setEnabled(labelMethod);
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new PCleanMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }

}

