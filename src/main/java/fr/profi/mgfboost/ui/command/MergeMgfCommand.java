package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.command.ui.MergeMgfPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MGFProcessing;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MergeMgfCommand extends AbstractCommand<CommandArguments.MgfMergerCommand> {

  private final static Logger logger = LoggerFactory.getLogger(PCleanMgfCommand.class);

  public MergeMgfCommand() {
    command = new CommandArguments.MgfMergerCommand();
  }

  @Override
  public MS2Collection execute(File... files) throws Exception {
    command.inputFileName1 = files[0].getAbsolutePath();
    command.inputFileName2 = files[1].getAbsolutePath();
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MGFProcessing.mergeMgf(command);
    return new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
  }

  protected AbstractCommandPanel<CommandArguments.MgfMergerCommand> getConfigurationPanel() {
    if (configurationPanel == null) {
      configurationPanel = new MergeMgfPanel().updatePanelFromCommand(command);
    }
    return configurationPanel;
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.PAIRS_OF_FILES;
  }

  @Override
  public AbstractCommandPanel<CommandArguments.MgfMergerCommand> getConfigurationPanel(List<MS2Collection> selectedCollections) {
    command.inputFileName1 = selectedCollections.get(0).getFile().getAbsolutePath();
    command.inputFileName2 =  selectedCollections.get(1).getFile().getAbsolutePath();
    configurationPanel = new MergeMgfPanel().updatePanelFromCommand(command);
    configurationPanel.setOutputRootFolder(selectedCollections.get(0).getFile().getParentFile());
    return configurationPanel;
  }

}
