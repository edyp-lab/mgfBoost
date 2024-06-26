package fr.profi.mgfboost.ui.command;

import com.google.gson.Gson;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;

import java.io.File;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractCommand<T> implements ICommand<T> {

  static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  public enum CommandType{ SINGLE_INPUT, BATCH_INPUT };

  protected T command;

  protected CommandType commandType = CommandType.SINGLE_INPUT;

  protected AbstractCommandPanel<T> configurationPanel;

  protected abstract AbstractCommandPanel<T> getConfigurationPanel();

  public T getCommand() {
    return command;
  }

  public String getCommandName() {
    return command.getClass().getSimpleName();
  }

  public String toJSON() {
    return new Gson().toJson(command);
  }

  public boolean fromJSON(String jsonString) {
    try {
      final T t = new Gson().fromJson(jsonString, (Class<T>) this.command.getClass());
      getConfigurationPanel().updatePanelFromCommand(t);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean buildCommand() {
    if (configurationPanel != null)  {
      return configurationPanel.buildCommand(command);
    }
    return false;
  }

  @Override
  public void showErrorMessage() {}

  protected static File getDestFile(File inputFile, String parentFolder, String defaultExtension){
    String inputFileName = inputFile.getName();
    String dstFilePath = inputFileName.substring(0,inputFileName.lastIndexOf('.')) + defaultExtension;
    File dstFile = new File(parentFolder, dstFilePath);
    if (dstFile.exists()) {
      String timestamp = MessageFormat.format("{0}", LocalDateTime.now().format(DATETIME_FORMATTER));
      dstFilePath = new StringBuilder(inputFileName.substring(0,inputFileName.lastIndexOf('.'))).append("-").append(timestamp).append(defaultExtension).toString();
      dstFile = new File(parentFolder, dstFilePath);
    }

    return  dstFile;
  }


}
