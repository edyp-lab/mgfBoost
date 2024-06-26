package fr.profi.mgfboost.ui.command.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OutputFilePanel extends JPanel {


  public enum OutputType { FILE, FOLDER; }

  private OutputType outputType = OutputType.FILE;
  private JTextField outputTF;
  private File rootFolder;

  public OutputFilePanel() {
    initComponents();
  }

  private void initComponents() {

    outputTF = new JTextField();
    outputTF.setColumns(15);
    JButton outputBtn = new JButton();

    setLayout(new GridBagLayout());

    final Insets defaultInsets = new Insets(0, 5, 0, 5);

    GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInsets, 0, 0);

    JLabel label = new JLabel("output file:");
    add(label, gbc);

    gbc.gridx++;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    add(outputTF, gbc);

    gbc.gridx++;
    gbc.weightx = 0.0;
    gbc.fill = GridBagConstraints.NONE;
    outputBtn.setText("Choose");
    outputBtn.addActionListener(e -> showSaveAsChooserDialog());

    add(outputBtn, gbc);

  }

  public void setOutputType(OutputType type) {
    this.outputType = type;
  }


  public String getOutputFilepath() {
      return outputTF.getText().trim();
  }

  public void showSaveAsChooserDialog() {
    JFileChooser chooser = new JFileChooser();
    File parentFile = (outputTF.getText() != null) && (!outputTF.getText().isEmpty()) ? new File(outputTF.getText()).getParentFile() : rootFolder;
    chooser.setCurrentDirectory(parentFile);
    chooser.setFileSelectionMode((outputType.equals(OutputType.FILE) ? JFileChooser.FILES_ONLY : JFileChooser.DIRECTORIES_ONLY));
    int res = chooser.showSaveDialog(outputTF.getTopLevelAncestor());
    if (res == JFileChooser.APPROVE_OPTION) {
      outputTF.setText(chooser.getSelectedFile().getAbsolutePath());
    }
  }


  public void setRootFolder(File parentFile) {
    this.rootFolder = parentFile;
  }
}
