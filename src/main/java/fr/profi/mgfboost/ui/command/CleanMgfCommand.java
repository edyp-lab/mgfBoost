package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.CleanMgfPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MGFProcessing;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CleanMgfCommand implements ICommand {

  private final static Logger logger = LoggerFactory.getLogger(CleanMgfCommand.class);

  CommandArguments.PCleanCommand command  = new CommandArguments.PCleanCommand();

  private CleanMgfPanel configurationPanel = null;

  @Override
  public void execute(File... files) throws Exception {
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MGFProcessing.pCleanMgf(command);
  }

  private CleanMgfPanel _getConfigurationPanel() {
    if(configurationPanel == null) {
      configurationPanel = new CleanMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public JPanel getConfigurationPanel() {
    return _getConfigurationPanel();
  }

  @Override
  public String getCommandName() {
    return command.getClass().getSimpleName();
  }

  @Override
  public boolean buildCommand() {
    _getConfigurationPanel().buildCommand(command);
    return true;
  }

  @Override
  public void setInputCollections(List<MS2Collection> selectedCollections) {
    command.mgf = selectedCollections.get(0).getFile().getAbsolutePath();
  }

  @Override
  public MS2Collection getOutputMS2Collection() {
    return new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
  }

}
