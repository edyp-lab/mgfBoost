package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.MergeMgfPanel;
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

public class MergeMgfCommand implements ICommand {

  private final static Logger logger = LoggerFactory.getLogger(CleanMgfCommand.class);

  CommandArguments.MgfMergerCommand command  = new CommandArguments.MgfMergerCommand();

  private MergeMgfPanel configurationPanel = null;

  @Override
  public void execute(File... files) throws Exception {
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MGFProcessing.mergeMgf(command);
  }

  private MergeMgfPanel _getConfigurationPanel() {
    if (configurationPanel == null) {
      configurationPanel = new MergeMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public String getCommandName() {
    return CommandArguments.MgfMergerCommand.class.getSimpleName();
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
  public void setInputCollections(List<MS2Collection> selectedCollections) {
    command.inputFileName1 = selectedCollections.get(0).getFile().getAbsolutePath();
    command.inputFileName2 = selectedCollections.get(1).getFile().getAbsolutePath();
  }

  @Override
  public MS2Collection getOutputMS2Collection() {
    return new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
  }

}
