package fr.profi.mgfboost.ui.command.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class AbstractCommandPanel<T> extends JPanel {

  protected OutputFilePanel outputFilePanel;

  public abstract boolean buildCommand(T t);

  public abstract AbstractCommandPanel<T> updatePanelFromCommand(T t);

  public void setOutputRootFolder(File parentFile) {
    if (outputFilePanel != null) outputFilePanel.setRootFolder(parentFile);
  }

  public void showErrorMessage() {}

  public void setOutputType(OutputFilePanel.OutputType type) {
    if (outputFilePanel != null) outputFilePanel.setOutputType(type);
  }

  public static void doWithoutFiringAction(final JComboBox<?> component,final Runnable f) {
    final ActionListener[] actionListeners = component.getActionListeners();
    for (final ActionListener listener : actionListeners)
      component.removeActionListener(listener);
    try {
      f.run();
    } finally {
      for (final ActionListener listener : actionListeners)
        component.addActionListener(listener);
    }
  }
}
