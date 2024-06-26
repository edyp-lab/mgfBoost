package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.command.ui.OutputFilePanel;
import fr.profi.mgfboost.ui.command.ui.PCleanMgfPanel;
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

public class PCleanMgfCommand extends AbstractCommand<CommandArguments.PCleanCommand> {

  private final static Logger logger = LoggerFactory.getLogger(PCleanMgfCommand.class);

  public PCleanMgfCommand() {
    command = new CommandArguments.PCleanCommand();
  }

  @Override
  public MS2Collection execute(File... files) throws Exception {
    command.mgf = files[0].getAbsolutePath();
    String originalOutFilePath =  command.outputFileName;

    if (commandType.equals(CommandType.BATCH_INPUT)) {
      command.outputFileName = getDestFile(files[0], command.outputFileName, ".mgf").getAbsolutePath();
    }
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MGFProcessing.pCleanMgf(command);
    final MS2Collection ms2Collection = new MS2Collection(new File(command.outputFileName), MainPanel.FileType.MGF);
    command.outputFileName = originalOutFilePath;
    return ms2Collection;
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.BATCH_OF_SINGLE_FILES;
  }

  protected AbstractCommandPanel<CommandArguments.PCleanCommand> getConfigurationPanel() {
    if(configurationPanel == null) {
      configurationPanel = new PCleanMgfPanel().updatePanelFromCommand(command);
    }
    return configurationPanel;
  }

  @Override
  public AbstractCommandPanel<CommandArguments.PCleanCommand> getConfigurationPanel(List<MS2Collection> selectedCollections) {

    commandType = (selectedCollections.size() > 1) ? CommandType.BATCH_INPUT : CommandType.SINGLE_INPUT;
    configurationPanel = new PCleanMgfPanel().updatePanelFromCommand(command);
    configurationPanel.setOutputRootFolder(selectedCollections.get(0).getFile().getParentFile());
    configurationPanel.setOutputType( (selectedCollections.size() > 1) ? OutputFilePanel.OutputType.FOLDER : OutputFilePanel.OutputType.FILE);

    return configurationPanel;
  }

//  @Override
//  public boolean fromJSON(String jsonString) {
//    try {
//
//      final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//      final JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
//      String cleanConfigStr = ((JsonObject) jsonElement).get("cleanConfig").getAsString();
//      final CommandArguments.CleanConfig cleanConfig = CommandArguments.CleanConfig.valueOf(cleanConfigStr);
//      final CommandArguments.PCleanCommand newCommand = gson.fromJson(((JsonObject) jsonElement).get("command"), CommandArguments.PCleanCommand.class);
//      getConfigurationPanel().updatePanelFromCommand(newCommand);
//      ((PCleanMgfPanel)getConfigurationPanel()).setCleanConfig(cleanConfig);
//
//      return true;
//    }  catch (Exception e) {
//      logger.debug("Error while loading settings :", e);
//      return false;
//    }
//  }
//
//  @Override
//  public String toJSON() {
//    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    Map<String, Object> values = new HashMap<>();
//    values.put("command", command);
//    values.put("cleanConfig", ((PCleanMgfPanel)getConfigurationPanel()).getCleanConfig());
//    return gson.toJson(values);
//  }

}
