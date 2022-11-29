package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.MaxQuantCreateMgfPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MaxQuantProcessing;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaxQuantCreateMgfCommand implements ICommand {

  private final static Logger logger = LoggerFactory.getLogger(MaxQuantCreateMgfCommand.class);

  CommandArguments.MaxQuantMGFCommand command  = new CommandArguments.MaxQuantMGFCommand();

  private MaxQuantCreateMgfPanel configurationPanel = null;

  @Override
  public void execute(File... files) throws Exception {
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MaxQuantProcessing.createMgf(command);

  }

  @Override
  public String getCommandName() {
    return CommandArguments.MaxQuantMGFCommand.class.getSimpleName();
  }

  private MaxQuantCreateMgfPanel _getConfigurationPanel() {
    if(configurationPanel == null) {
      configurationPanel = new MaxQuantCreateMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public JPanel getConfigurationPanel() {
    return _getConfigurationPanel();
  }

  @Override
  public boolean buildCommand() {
    _getConfigurationPanel().buildCommand(command);
    return true;
  }

  @Override
  public void showErrorMessage() {
    //No message to display yet
  }

  @Override
  public void setInputCollections(List<MS2Collection> selectedCollections) {
    File[] aplFiles = MS2Collection.MaxQuant.locateAPLFiles(selectedCollections.get(0));
    if (aplFiles.length > 0 && aplFiles[0] != null) command.inputFileName1 = aplFiles[0].getAbsolutePath();
    if (aplFiles.length > 1 && aplFiles[1] != null) command.inputFileName2 = aplFiles[1].getAbsolutePath();
  }

  @Override
  public MS2Collection getOutputMS2Collection() {
    return new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
  }

}
