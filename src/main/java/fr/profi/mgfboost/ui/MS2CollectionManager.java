package fr.profi.mgfboost.ui;

import fr.profi.mgfboost.ui.command.*;
import fr.profi.mgfboost.ui.model.MS2Collection;
import fr.profi.ms.model.MSMSSpectrum;
import fr.profi.mzknife.CommandArguments;
import fr.proline.studio.ImageUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MS2CollectionManager  {

  private final static Logger logger = LoggerFactory.getLogger(MS2CollectionManager.class);

  private final JPopupMenu popupMenu;

  private final MainPanel mainPanel;

  private final JList<MS2Collection> ms2CollectionList;

  private final DefaultListModel<MS2Collection> ms2CollectionListModel;

  private final Map<String, ICommand> commands = new HashMap<>();

  private int runningTasksIncr = 0;

  public MS2CollectionManager(MainPanel mainPanel) {
    this.mainPanel = mainPanel;

    commands.put(CommandArguments.MzDBCreateMgfCommand.class.getSimpleName(), new MzdbCreateMgfCommand());
    commands.put(CommandArguments.PCleanCommand.class.getSimpleName(), new PCleanMgfCommand());
    commands.put(CommandArguments.MgfCleanerCommand.class.getSimpleName(), new ECleanMgfCommand());
    commands.put(CommandArguments.MaxQuantMGFCommand.class.getSimpleName(), new MaxQuantCreateMgfCommand());
    commands.put(CommandArguments.MgfMergerCommand.class.getSimpleName(), new MergeMgfCommand());

    ms2CollectionList = new JList<>();
    ms2CollectionListModel = new DefaultListModel<>();
    ms2CollectionList.setModel(ms2CollectionListModel);
    ms2CollectionList.setCellRenderer(new MS2CollectionRenderer(ms2CollectionList.getCellRenderer()));
    popupMenu = new JPopupMenu();
    initPopupMenu();
    ms2CollectionList.setComponentPopupMenu(popupMenu);
    ms2CollectionList.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mousePressed(java.awt.event.MouseEvent evt) {
        updatePopupMenu();
        if (ms2CollectionList.getSelectedIndex() > -1 && SwingUtilities.isRightMouseButton(evt)) {
          popupMenu.show((JComponent) evt.getSource(), evt.getX(), evt.getY());
        } else if (evt.getClickCount() == 2) {
          viewCollectionAction();
        }
      }
    });

  }

  private void initPopupMenu() {

    popupMenu.removeAll();

    JMenuItem viewRawFileMI = new JMenuItem();
    viewRawFileMI.setText("View");
    viewRawFileMI.addActionListener(actionEvent -> viewCollectionAction());
    popupMenu.add(viewRawFileMI);

    JMenuItem closeRawFileMI = new JMenuItem();
    closeRawFileMI.setText("Close");
    closeRawFileMI.addActionListener(actionEvent -> closeCollectionAction());
    popupMenu.add(closeRawFileMI);

  }

  private void updatePopupMenu() {
    List<MS2Collection> selectedCollections =  ms2CollectionList.getSelectedValuesList();
    if (selectedCollections.size() > 0) {
      initPopupMenu();
      popupMenu.addSeparator();
    }
    final List<MainPanel.FileType> fileTypes = selectedCollections.stream().map(c -> c.getFileType()).distinct().collect(Collectors.toList());
    if (fileTypes.size() == 1) {
      switch (fileTypes.get(0)) {
        case MGF:
          addMGFActions(selectedCollections);
          break;
        case MZDB:
          addMzdbActions(selectedCollections);
          break;
        case MAXQUANT:
          addMQActions(selectedCollections);
          break;
      }
    }
  }

  private void addMQActions(List<MS2Collection> selectedCollections) {

    if (isCommandSelectionConsistent(selectedCollections, commands.get(CommandArguments.MaxQuantMGFCommand.class.getSimpleName()))) {
      JMenuItem createMGFMI = new JMenuItem();
      createMGFMI.setActionCommand(CommandArguments.MaxQuantMGFCommand.class.getSimpleName());
      createMGFMI.setText("Create MGF");
      createMGFMI.addActionListener(this::performAction);
      popupMenu.add(createMGFMI);
    }
  }

  private static boolean isCommandSelectionConsistent(List<MS2Collection> selectedCollections, ICommand command) {

    return (command.getExecutionMode().equals(ICommand.ExecutionMode.SINGLE_FILE) && selectedCollections.size() == 1)
            || (command.getExecutionMode().equals(ICommand.ExecutionMode.BATCH_OF_SINGLE_FILES) && selectedCollections.size() > 0)
            || (command.getExecutionMode().equals(ICommand.ExecutionMode.PAIRS_OF_FILES) && selectedCollections.size() == 2);

  }

  private void addMzdbActions(List<MS2Collection> selectedCollections) {

    if (isCommandSelectionConsistent(selectedCollections, commands.get(CommandArguments.MzDBCreateMgfCommand.class.getSimpleName()))) {
      JMenuItem createMGFMI = new JMenuItem();
      createMGFMI.setActionCommand(CommandArguments.MzDBCreateMgfCommand.class.getSimpleName());
      createMGFMI.setText("Create MGF");
      createMGFMI.addActionListener(this::performAction);
      popupMenu.add(createMGFMI);
    }

  }

  private void addMGFActions(List<MS2Collection> selectedCollections) {

    if (isCommandSelectionConsistent(selectedCollections, commands.get(CommandArguments.PCleanCommand.class.getSimpleName()))) {
      JMenuItem pCleanMGFMI = new JMenuItem();
      pCleanMGFMI.setActionCommand(CommandArguments.PCleanCommand.class.getSimpleName());
      pCleanMGFMI.setText("Clean MGF with pClean");
      pCleanMGFMI.addActionListener(this::performAction);
      popupMenu.add(pCleanMGFMI);
    }

    if (isCommandSelectionConsistent(selectedCollections, commands.get(CommandArguments.MgfCleanerCommand.class.getSimpleName()))) {
      JMenuItem eCleanMGFMI = new JMenuItem();
      eCleanMGFMI.setActionCommand(CommandArguments.MgfCleanerCommand.class.getSimpleName());
      eCleanMGFMI.setText("Clean MGF with edyp Clean (eClean)");
      eCleanMGFMI.addActionListener(this::performAction);
      popupMenu.add(eCleanMGFMI);
    }

    if (isCommandSelectionConsistent(selectedCollections, commands.get(CommandArguments.MgfMergerCommand.class.getSimpleName()))) {
      JMenuItem mergeMGFMI = new JMenuItem();
      mergeMGFMI.setActionCommand(CommandArguments.MgfMergerCommand.class.getSimpleName());
      mergeMGFMI.setText("Merge MGF");
      mergeMGFMI.addActionListener(this::performAction);
      popupMenu.add(mergeMGFMI);
    }

  }

  private void performAction(ActionEvent actionEvent) {
    List<MS2Collection> selectedCollections =  ms2CollectionList.getSelectedValuesList();
    ICommand command = commands.get(actionEvent.getActionCommand());

    ConfigurationDialog configurationDialog = new ConfigurationDialog(command, selectedCollections);
    configurationDialog.setLocationRelativeTo(mainPanel);
    configurationDialog.setVisible(true);
    if (configurationDialog.isValidated()) {

      updateExecutionStatus("Command running ...", 1);

      SwingWorker worker = new SwingWorker<Integer, MS2Collection>() {
        @Override
        protected Integer doInBackground() throws Exception {

          if (command.getExecutionMode().equals(ICommand.ExecutionMode.SINGLE_FILE) || command.getExecutionMode().equals(ICommand.ExecutionMode.BATCH_OF_SINGLE_FILES)) {
            for (MS2Collection c : selectedCollections) {
              publish(command.execute(c.getFile()));
            }
          } else if (command.getExecutionMode().equals(ICommand.ExecutionMode.PAIRS_OF_FILES)) {
              publish(command.execute(selectedCollections.get(0).getFile(), selectedCollections.get(1).getFile()));
          }
          return selectedCollections.size();
        }

        @Override
        protected void process(List<MS2Collection> chunks) {
          for (MS2Collection outputCollection : chunks) {
            if ((outputCollection != null) && (outputCollection.getFile().exists())) {
              ms2CollectionListModel.addElement(outputCollection);
            }
          }
        }

        @Override
        protected void done() {
          try {
              updateExecutionStatus("", -1);
            } catch (Exception e) {
              logger.error("Error during command execution ", e);
            }
        }
      };

      worker.execute();

    }
  }

  private void viewCollectionAction() {
    MS2Collection selectedCollection =  ms2CollectionList.getSelectedValuesList().get(0);
    updateExecutionStatus("Preparing display ...", 1);
    ms2CollectionList.setEnabled(false);
    SwingWorker worker = new SwingWorker<List<MSMSSpectrum>, Void>() {
      @Override
      protected List<MSMSSpectrum> doInBackground() throws Exception {
        return selectedCollection.getSpectrumList();
      }

      @Override
      protected void done() {
        try {
          updateExecutionStatus("", -1);
          List<MSMSSpectrum> spectra = get();
          if (spectra != null && !spectra.isEmpty()) {
            mainPanel.getTabbedPane().add(selectedCollection.getName(), new MS2CollectionPanel(spectra, mainPanel));
          } else {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                    "Sorry, the selected operation is not yet implemented. Please generate an MGF file\n" +
                    "from the selected collection and then visualize the MS2 spectrum from the generated MGF.");
          }
        } catch (Exception e) {
          logger.error("Cannot load MS2 spectrum from the selected collection", e);
        } finally {
          ms2CollectionList.setEnabled(true);
        }
      }
    };

    worker.execute();
  }

  private void updateExecutionStatus(String text, int increment) {
    runningTasksIncr += increment;
    String label = ((increment > 0) && (runningTasksIncr > 1) || ((increment < 0) && (runningTasksIncr > 0))) ? runningTasksIncr+" tasks running ..." : text;
    mainPanel.displayExecutionStatus(label, runningTasksIncr > 0);
  }

  private void closeCollectionAction() {
    for (MS2Collection collection : ms2CollectionList.getSelectedValuesList()) {
      ms2CollectionListModel.removeElement(collection);
    }
  }

  public boolean addToMS2Collection(File file, MainPanel.FileType type) {
    ms2CollectionListModel.addElement(new MS2Collection(file,type));
    return false;
  }

  public JList<MS2Collection> getMs2CollectionList() {
    return ms2CollectionList;
  }


}

class MS2CollectionRenderer implements ListCellRenderer {

  private static final ImageIcon mzdbIcon = ImageUtilities.loadImageIcon("fr/profi/mgfboost/images/iconeMzDB.png", false);
  private static final ImageIcon mgfIcon = ImageUtilities.loadImageIcon("fr/profi/mgfboost/images/iconeMGF.png", false);
  private static final ImageIcon mqIcon = ImageUtilities.loadImageIcon("fr/profi/mgfboost/images/iconeMQ.png", false);

  ListCellRenderer defaultListCellRenderer;

  public MS2CollectionRenderer(ListCellRenderer defaultListCellRenderer) {
    this.defaultListCellRenderer = defaultListCellRenderer;
  }

  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    Component c = defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    MS2Collection collection = (MS2Collection) value;
    ((JLabel)c).setText(collection.getName());
    ((JLabel) c).setIcon(getIcon(collection.getFileType()));
    return c;
  }

  private static ImageIcon getIcon(MainPanel.FileType fileType) {
    switch (fileType) {
      case MZDB: return mzdbIcon;
      case MGF: return mgfIcon;
      case MAXQUANT: return mqIcon;
    }
    return null;
  }


}

class ConfigurationDialog extends JDialog {

  private final ICommand command;

  private boolean isValidated = false;

  public ConfigurationDialog(ICommand command, List<MS2Collection> selectedFiles) {
    this.command = command;
    JPanel configurationPanel = command.getConfigurationPanel(selectedFiles);
    setModal(true);
    setTitle(command.getCommandName()+ " parameters");
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(configurationPanel, BorderLayout.CENTER);
    getContentPane().add(getOkCancelButtons(), BorderLayout.SOUTH);
    pack();
  }

  public boolean isValidated() {
    return isValidated;
  }

  private JPanel getOkCancelButtons() {
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


    JButton loadParametersBtn = new JButton();
    loadParametersBtn.setText("Load");
    loadParametersBtn.addActionListener(e -> {

      JFileChooser chooser = new JFileChooser();
      int res = chooser.showOpenDialog(this);
      if (res == JFileChooser.APPROVE_OPTION) {
        final String fileName = chooser.getSelectedFile().getAbsolutePath();
          try {
            final String jsonString = FileUtils.readFileToString(new File(fileName), "UTF-8");
            command.fromJSON(jsonString);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
      }

    });
    buttonsPanel.add(loadParametersBtn);

    JButton saveParametersBtn = new JButton();
    saveParametersBtn.setText("Save");
    saveParametersBtn.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      int res = chooser.showSaveDialog(this);
      if (res == JFileChooser.APPROVE_OPTION) {
        final String fileName = chooser.getSelectedFile().getAbsolutePath();
        if (command.buildCommand()) {
          try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(command.toJSON());
            writer.close();
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        } else {
          command.showErrorMessage();
        }
      }

    });
    buttonsPanel.add(saveParametersBtn);

    buttonsPanel.add(new JPanel());

    JButton okBtn = new JButton();
    okBtn.setText("Ok");
    okBtn.addActionListener(e -> {

      if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
        // for debug purpose only : only dump the command to the standard output
        command.buildCommand();
        System.out.println("Command args : "+ ToStringBuilder.reflectionToString(((AbstractCommand<?>) command).getCommand(), ToStringStyle.MULTI_LINE_STYLE));
      } else {
        // the real command execution
        if (command.buildCommand()) {
          isValidated = true;
          this.setVisible(false);
        } else {
          command.showErrorMessage();
        }
      }
    });
    getRootPane().setDefaultButton(okBtn);
    buttonsPanel.add(okBtn);

    JButton cancelBtn = new JButton();
    cancelBtn.setText("Cancel");
    cancelBtn.addActionListener(e -> this.setVisible(false));

    buttonsPanel.add(cancelBtn);

    return buttonsPanel;
  }

}
