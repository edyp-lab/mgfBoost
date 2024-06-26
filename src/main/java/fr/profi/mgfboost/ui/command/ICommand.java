package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;

import java.io.File;
import java.util.List;

public interface ICommand<T> {

  enum ExecutionMode { SINGLE_FILE, PAIRS_OF_FILES, BATCH_OF_SINGLE_FILES; }

  MS2Collection execute(File... files) throws Exception;

  String getCommandName();

  ExecutionMode getExecutionMode();

  AbstractCommandPanel<T> getConfigurationPanel(List<MS2Collection> selectedFiles);

  boolean buildCommand();

  void showErrorMessage();

  String toJSON();

  boolean fromJSON(String jsonString);
}
