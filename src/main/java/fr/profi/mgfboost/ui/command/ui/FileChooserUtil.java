package fr.profi.mgfboost.ui.command.ui;

import javax.swing.*;

public class FileChooserUtil {

  public static void saveAsChooser(JTextField tf) {
    JFileChooser chooser = new JFileChooser();
    int res = chooser.showSaveDialog(tf.getTopLevelAncestor());
    if (res == JFileChooser.APPROVE_OPTION) {
      tf.setText(chooser.getSelectedFile().getAbsolutePath());
    }
  }
}
