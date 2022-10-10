/*
 * Created by JFormDesigner on Thu Sep 01 14:09:07 CEST 2022
 */

package fr.profi.mgfboost.ui.command.ui;

import fr.profi.mzknife.CommandArguments;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author unknown
 */
public class CleanMgfPanel extends JPanel {

  private JTextField outputTF;
  private JTextField iTolTF;
  private JCheckBox aa2Cbx;
  private JCheckBox mionCbx;
  private JComboBox labelMethodCombo;
  private JCheckBox reporterFilterCbx;
  private JCheckBox labelFilterCbx;
  private JCheckBox lowFilterCbx;
  private JCheckBox highFilterCbx;
  private JCheckBox isoReductionCbx;
  private JCheckBox chargeDeconvolutionCbx;
  private JCheckBox mergeIonsCbx;
  private JCheckBox largerThanPrecursorCbx;

  public CleanMgfPanel() {
    initComponents();
  }

  public CleanMgfPanel(CommandArguments.PCleanCommand command) {
    initComponents();
    initValues(command);
  }

  private void initValues(CommandArguments.PCleanCommand command) {
    iTolTF.setText(command.itol.toString());
    aa2Cbx.setSelected(command.aa2);
    mionCbx.setSelected(command.ionFilter);
    reporterFilterCbx.setSelected(command.repFilter);
    labelFilterCbx.setSelected(command.labelFilter);
    lowFilterCbx.setSelected(command.low);
    highFilterCbx.setSelected(command.high);
    isoReductionCbx.setSelected(command.isoReduction);
    chargeDeconvolutionCbx.setSelected(command.chargeDeconv);
    largerThanPrecursorCbx.setSelected(command.largerThanPrecursor);
    mergeIonsCbx.setSelected(command.ionsMerge);
    labelMethodCombo.setSelectedItem((command.labelMethod == null || command.labelMethod.isEmpty()) ? "None" : command.labelMethod);
  }


  public void buildCommand(CommandArguments.PCleanCommand command) {
    command.outputFileName = outputTF.getText().trim();
    command.itol = Double.parseDouble(iTolTF.getText().trim());
    command.aa2 = aa2Cbx.isSelected();
    command.ionFilter = mionCbx.isSelected();
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
  }

  private void initComponents() {
    JLabel label1 = new JLabel();
    outputTF = new JTextField();
    JButton outputBtn = new JButton();
    JPanel panel1 = new JPanel();
    JLabel label2 = new JLabel();
    iTolTF = new JTextField();
    aa2Cbx = new JCheckBox();
    mionCbx = new JCheckBox();
    JPanel panel2 = new JPanel();
    JLabel label3 = new JLabel();
    String[] labelMethodValues = {"None", "ITRAQ4PLEX", "ITRAQ8PLEX", "TMT6PLEX", "TMT10PLEX", "TMT11PLEX", "TMT16PLEX", "TMT18PLEX"};
    labelMethodCombo = new JComboBox(labelMethodValues);
    reporterFilterCbx = new JCheckBox();
    labelFilterCbx = new JCheckBox();
    lowFilterCbx = new JCheckBox();
    highFilterCbx = new JCheckBox();
    JPanel panel3 = new JPanel();
    isoReductionCbx = new JCheckBox();
    chargeDeconvolutionCbx = new JCheckBox();
    mergeIonsCbx = new JCheckBox();
    largerThanPrecursorCbx = new JCheckBox();

    labelMethodCombo.addActionListener(e -> {
      boolean labelMethod = (!((String) labelMethodCombo.getSelectedItem()).equalsIgnoreCase("none"));
      reporterFilterCbx.setEnabled(labelMethod);
      labelFilterCbx.setEnabled(labelMethod);
      lowFilterCbx.setEnabled(labelMethod);
      highFilterCbx.setEnabled(labelMethod);
    });
    labelMethodCombo.setSelectedIndex(0);
    //======== this ========
    setLayout(new GridBagLayout());
    ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0};
    ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
    ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
    ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

    JPanel outputPanel = new JPanel();

    //---- label1 ----
    label1.setText("output File:");
    add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 5, 10, 10), 0, 0));
    add(outputTF, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 5, 10, 10), 0, 0));

    //---- outputBtn ----
    outputBtn.setText("choose");
    outputBtn.addActionListener(e -> FileChooserUtil.saveAsChooser(outputTF));

    add(outputBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(5, 5, 10, 5), 0, 0));

    //======== panel1 ========
    {
      panel1.setBorder(new TitledBorder("global parameters"));
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0};
      ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
      ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

      //---- label2 ----
      label2.setText("m/z fragments tolerance (Da)");
      panel1.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 5), 0, 0));
      panel1.add(iTolTF, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- aa2Cbx ----
      aa2Cbx.setText("consider mass gap of two AA");
      panel1.add(aa2Cbx, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- mionCbx ----
      mionCbx.setText("filter immonium ions");
      panel1.add(mionCbx, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    }
    add(panel1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 5, 0), 0, 0));

    //======== panel2 ========
    {
      panel2.setBorder(new TitledBorder("module1 parameters"));
      panel2.setLayout(new GridBagLayout());
      ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {110, 0, 0};
      ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
      ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
      ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

      //---- label3 ----
      label3.setText("label method:");
      panel2.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 5), 0, 0));
      panel2.add(labelMethodCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- reporterFilterCbx ----
      reporterFilterCbx.setText("filter reporter ions");
      panel2.add(reporterFilterCbx, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- labelFilterCbx ----
      labelFilterCbx.setText("filter label-associated ions");
      panel2.add(labelFilterCbx, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- lowFilterCbx ----
      lowFilterCbx.setText("clear low b/y-ion window");
      panel2.add(lowFilterCbx, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- highFilterCbx ----
      highFilterCbx.setText("clear high b/y-ions window");
      panel2.add(highFilterCbx, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    }
    add(panel2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 5, 0), 0, 0));

    //======== panel3 ========
    {
      panel3.setBorder(new TitledBorder("module2 parameters"));
      panel3.setLayout(new GridBagLayout());
      ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {0, 0};
      ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
      ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
      ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

      //---- isoReductionCbx ----
      isoReductionCbx.setText("do isotopic reduction");
      panel3.add(isoReductionCbx, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- chargeDeconvolutionCbx ----
      chargeDeconvolutionCbx.setText("do charge deconvolution");
      panel3.add(chargeDeconvolutionCbx, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- mergeIonsCbx ----
      mergeIonsCbx.setText("merge ions of similar masses");
      panel3.add(mergeIonsCbx, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 5, 0), 0, 0));

      //---- largerThanPrecursorCbx ----
      largerThanPrecursorCbx.setText("filter ions larger than precursor\u2019s mass");
      panel3.add(largerThanPrecursorCbx, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    }
    add(panel3, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
      GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());
    f.getContentPane().add(new CleanMgfPanel(), BorderLayout.CENTER);
    f.pack();
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    java.awt.EventQueue.invokeLater(() -> f.setVisible(true));
  }
}
