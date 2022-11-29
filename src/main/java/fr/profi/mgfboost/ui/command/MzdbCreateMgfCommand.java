package fr.profi.mgfboost.ui.command;

import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mgfboost.ui.command.ui.MzdbCreateMgfPanel;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.mzknife.CommandArguments;
import fr.profi.mzknife.MzDbProcessing;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MzdbCreateMgfCommand implements ICommand {

  private final static Logger logger = LoggerFactory.getLogger(CleanMgfCommand.class);

  private CommandArguments.MzDBCreateMgfCommand command;

  private MzdbCreateMgfPanel configurationPanel = null;

  public MzdbCreateMgfCommand() {
    command = new CommandArguments.MzDBCreateMgfCommand();
    command.exportProlineTitle = true;
  }

  @Override
  public void execute(File... files) throws Exception {
    logger.info("Execute command "+this.getCommandName()+" on "+ Arrays.stream(files).map(f -> f.getName()).collect(Collectors.joining()));
    logger.info("Command args : "+ ToStringBuilder.reflectionToString(command));
    MzDbProcessing.mzdbcreateMgf(command);
  }

  @Override
  public String getCommandName() {
    return CommandArguments.MzDBCreateMgfCommand.class.getSimpleName();
  }

  private MzdbCreateMgfPanel _getConfigurationPanel() {
    if (configurationPanel == null) {
      configurationPanel = new MzdbCreateMgfPanel(command);
    }
    return configurationPanel;
  }

  @Override
  public JPanel getConfigurationPanel() {
    return _getConfigurationPanel();
  }

  @Override
  public boolean buildCommand() {
    return _getConfigurationPanel().buildCommand(command);
  }

  @Override
  public void showErrorMessage() {
     _getConfigurationPanel().showErrorMessage();
  }

  @Override
  public void setInputCollections(List<MS2Collection> selectedCollections) {
    command.mzdbFile = selectedCollections.get(0).getFile().getAbsolutePath();
  }

  @Override
  public MS2Collection getOutputMS2Collection() {
    return new MS2Collection(new File(command.outputFile), MainPanel.FileType.MGF);
  }

}
