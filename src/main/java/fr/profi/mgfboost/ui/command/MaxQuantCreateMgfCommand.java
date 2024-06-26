package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.command.ui.MaxQuantCreateMgfPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MaxQuantProcessing;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MaxQuantCreateMgfCommand extends AbstractCommand<CommandArguments.MaxQuantMGFCommand> {

  private final static Logger logger = LoggerFactory.getLogger(MaxQuantCreateMgfCommand.class);

  public MaxQuantCreateMgfCommand() {
    command = new CommandArguments.MaxQuantMGFCommand();
  }

  @Override
  public MS2Collection execute(File... files) throws Exception {

    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MaxQuantProcessing.createMgf(command);

    return new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.SINGLE_FILE;
  }

  protected AbstractCommandPanel<CommandArguments.MaxQuantMGFCommand> getConfigurationPanel() {
    if(configurationPanel == null) {
      configurationPanel = new MaxQuantCreateMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public AbstractCommandPanel<CommandArguments.MaxQuantMGFCommand> getConfigurationPanel(List<MS2Collection> selectedCollections) {

    File[] aplFiles = MS2Collection.MaxQuant.locateAPLFiles(selectedCollections.get(0));
    if (aplFiles.length > 0 && aplFiles[0] != null) command.inputFileName1 = aplFiles[0].getAbsolutePath();
    if (aplFiles.length > 1 && aplFiles[1] != null) command.inputFileName2 = aplFiles[1].getAbsolutePath();
    configurationPanel = new MaxQuantCreateMgfPanel(command);
    if (command.inputFileName1 != null && !command.inputFileName1.isEmpty()) {
      configurationPanel.setOutputRootFolder(new File(command.inputFileName1).getParentFile());
    }

    return configurationPanel;
  }

}
