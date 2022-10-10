package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.model.MS2Collection;

import javax.swing.*;
import java.io.File;
import java.util.List;

public interface ICommand {

  public void execute(File... files) throws Exception;

  public String getCommandName();

  public JPanel getConfigurationPanel();

  boolean buildCommand();

  void setInputCollections(List<MS2Collection> selectedFiles);

  MS2Collection getOutputMS2Collection();
}
