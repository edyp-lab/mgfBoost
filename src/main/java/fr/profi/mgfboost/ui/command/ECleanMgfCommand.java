package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.command.ui.ECleanMgfPanel;
import fr.profi.mgfboost.ui.command.ui.OutputFilePanel;
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

public class ECleanMgfCommand extends AbstractCommand<CommandArguments.MgfCleanerCommand> {

  private final static Logger logger = LoggerFactory.getLogger(ECleanMgfCommand.class);

  public ECleanMgfCommand() {
    command = new CommandArguments.MgfCleanerCommand();
  }

  @Override
  public MS2Collection execute(File... files) throws Exception {
    command.inputFileName = files[0].getAbsolutePath();
    String originalOutFilePath =  command.outputFileName;
    if (commandType.equals(CommandType.BATCH_INPUT)) {
      command.outputFileName = getDestFile(files[0], command.outputFileName, ".mgf").getAbsolutePath();
    }
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MGFProcessing.cleanMgf(command);
    final MS2Collection ms2Collection = new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
    command.outputFileName = originalOutFilePath;
    return ms2Collection;
  }

  protected AbstractCommandPanel<CommandArguments.MgfCleanerCommand> getConfigurationPanel() {
    if (configurationPanel == null) {
      configurationPanel = new ECleanMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public AbstractCommandPanel<CommandArguments.MgfCleanerCommand> getConfigurationPanel(List<MS2Collection> selectedCollections) {

    commandType = (selectedCollections.size() > 1) ? CommandType.BATCH_INPUT : CommandType.SINGLE_INPUT;
    configurationPanel = new ECleanMgfPanel(command);
    configurationPanel.setOutputRootFolder(selectedCollections.get(0).getFile().getParentFile());
    configurationPanel.setOutputType( (selectedCollections.size() > 1) ? OutputFilePanel.OutputType.FOLDER : OutputFilePanel.OutputType.FILE);

    return configurationPanel;
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.BATCH_OF_SINGLE_FILES;
  }

}
