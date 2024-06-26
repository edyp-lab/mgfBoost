package fr.profi.mgfboost.ui.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.AbstractCommandPanel;
import fr.profi.mgfboost.ui.command.ui.MzdbCreateMgfPanel;
import fr.profi.mgfboost.ui.command.ui.OutputFilePanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MzDbProcessing;
import fr.profi.mzknife.mzdb.MgfBoostConfigTemplate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MzdbCreateMgfCommand extends AbstractCommand<CommandArguments.MzDBCreateMgfCommand> {

  private final static Logger logger = LoggerFactory.getLogger(PCleanMgfCommand.class);

  public MzdbCreateMgfCommand() {
    command = new CommandArguments.MzDBCreateMgfCommand();
    command.exportProlineTitle = true;
  }

  @Override
  public MS2Collection execute(File... files) throws Exception {
    command.mzdbFile = files[0].getAbsolutePath();
    String originalOutFilePath =  command.outputFile;
    if (commandType.equals(CommandType.BATCH_INPUT)) {
      command.outputFile = getDestFile(files[0], command.outputFile, ".mgf").getAbsolutePath();
    }
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MzDbProcessing.mzdbcreateMgf(command);
    final MS2Collection ms2Collection = new MS2Collection(new File(command.outputFile), MainPanel.FileType.MGF);
    command.outputFile = originalOutFilePath;

    return ms2Collection;
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.BATCH_OF_SINGLE_FILES;
  }

  protected AbstractCommandPanel<CommandArguments.MzDBCreateMgfCommand> getConfigurationPanel() {
    if (configurationPanel == null) {
      configurationPanel = new MzdbCreateMgfPanel().updatePanelFromCommand(command);
    }
    return configurationPanel;
  }

  @Override
  public AbstractCommandPanel<CommandArguments.MzDBCreateMgfCommand> getConfigurationPanel(List<MS2Collection> selectedCollections) {

    commandType = (selectedCollections.size() > 1) ? CommandType.BATCH_INPUT : CommandType.SINGLE_INPUT;

    configurationPanel = new MzdbCreateMgfPanel().updatePanelFromCommand(command);
    configurationPanel.setOutputRootFolder(selectedCollections.get(0).getFile().getParentFile());
    configurationPanel.setOutputType( (selectedCollections.size() > 1) ? OutputFilePanel.OutputType.FOLDER : OutputFilePanel.OutputType.FILE);
    return configurationPanel;
  }

  @Override
  public boolean fromJSON(String jsonString) {
    try {
      final Gson gson = new GsonBuilder().setPrettyPrinting().create();
      final JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
      MgfBoostConfigTemplate boostConfigTemplate = null;
      if (((JsonObject) jsonElement).get("mgfBoostConfigTemplate") != null) {
        String boostConfigTemplateStr = ((JsonObject) jsonElement).get("mgfBoostConfigTemplate").getAsString();
        boostConfigTemplate = MgfBoostConfigTemplate.valueOf(boostConfigTemplateStr);
      }
      final CommandArguments.MzDBCreateMgfCommand newCommand = gson.fromJson(((JsonObject) jsonElement).get("command"), CommandArguments.MzDBCreateMgfCommand.class);
      getConfigurationPanel().updatePanelFromCommand(newCommand);
      ((MzdbCreateMgfPanel) getConfigurationPanel()).setMgfBoostConfigTemplate(boostConfigTemplate);
      return true;
    }  catch (Exception e) {
      logger.debug("Error while loading settings :", e);
      return false;
    }
  }

  @Override
  public String toJSON() {
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Map<String, Object> values = new HashMap<>();
    values.put("command", command);
    values.put("mgfBoostConfigTemplate", ((MzdbCreateMgfPanel)getConfigurationPanel()).getMgfBoostConfigTemplate());
    return gson.toJson(values);
  }

}
