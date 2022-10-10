package fr.profi.mgfboost.ui;

import fr.proline.mzscope.model.IFeature;
import fr.proline.mzscope.model.IRawFile;
import fr.proline.mzscope.ui.IMzScopeController;
import fr.proline.mzscope.ui.IRawFileViewer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class MainPanel extends JPanel implements IMzScopeController {

  private final static String LAST_DIR = "mgfBoost.last.open.folder";
  private SpectrumViewer viewer;

  @Override
  public IRawFileViewer getCurrentRawFileViewer() {
    return viewer;
  }

  @Override
  public IRawFileViewer getRawFileViewer(IRawFile iRawFile, boolean b) {
    return viewer;
  }

  @Override
  public IRawFileViewer getTabbedMultiRawFileViewer() {
    return viewer;
  }

  @Override
  public void displayFeatures(Map<String, List<IFeature>> map) {
    throw new UnsupportedOperationException();
  }

  public enum FileType {
    MZDB, MGF, MAXQUANT
  }

  private JToolBar toolBar;

  private MS2CollectionManager ms2CollectionManager;
  private JTabbedPane tabbedPane;

  private JFileChooser fileChooser = null;

  private JProgressBar progressBar;

  public MainPanel() {
    ms2CollectionManager = new MS2CollectionManager(this);
    initComponents();
  }

  public JTabbedPane getTabbedPane() {
    return tabbedPane;
  }

  private void initComponents() {
    toolBar = new JToolBar();
    JSplitPane splitPane = new JSplitPane();
    JScrollPane scrollPane = new JScrollPane();
    tabbedPane = new JTabbedPane();
    viewer = new SpectrumViewer();

    setLayout(new BorderLayout());

    toolBar.setFloatable(false);
    add(toolBar, BorderLayout.NORTH);

    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerSize(6);
    scrollPane.setViewportView(ms2CollectionManager.getMs2CollectionList());
    splitPane.setLeftComponent(scrollPane);

    JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane1.setOneTouchExpandable(true);
    splitPane1.setDividerSize(6);
    splitPane1.setDividerLocation(100);
    splitPane1.setLeftComponent(tabbedPane);
    splitPane1.setRightComponent(viewer);

    splitPane.setRightComponent(splitPane1);
    add(splitPane, BorderLayout.CENTER);

    JPanel statusPanel = new JPanel();
    statusPanel.setLayout(new BorderLayout());
    progressBar = new JProgressBar();
    statusPanel.add(progressBar, BorderLayout.CENTER);

    add(statusPanel, BorderLayout.SOUTH);
  }

  public void displayExecutionStatus(String label, boolean running) {
    progressBar.setIndeterminate(running);
    progressBar.setStringPainted(running);
    progressBar.setString(label);
  }

  protected void openFileAction(String fileType) {
    Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    String prefName = new StringBuilder(LAST_DIR).append('.').append(fileType.toLowerCase()).toString();
    String directory = prefs.get(prefName, getFileChooser(fileType).getCurrentDirectory().getAbsolutePath());
    fileChooser.setCurrentDirectory(new File(directory));
    fileChooser.setMultiSelectionEnabled(true);
    int returnVal = fileChooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File[] files = fileChooser.getSelectedFiles();
      for (File file : files) {
        prefs.put(prefName, file.getParentFile().getAbsolutePath());
        ms2CollectionManager.addToMS2Collection(file, FileType.valueOf(fileType.toUpperCase()));
      }
    } else {
      System.out.println("File access cancelled by user.");
    }

  }

  private JFileChooser getFileChooser(String fileType) {
    if (this.fileChooser == null) {
      this.fileChooser = new JFileChooser();
    }
    this.fileChooser.setDialogTitle("Open " + fileType + " file");
    this.fileChooser.resetChoosableFileFilters();
    this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    if (!fileType.equalsIgnoreCase(FileType.MAXQUANT.name())) {
      final CustomFilter customFilter = new CustomFilter(fileType);
      this.fileChooser.addChoosableFileFilter(customFilter);
      this.fileChooser.setFileFilter(customFilter);
    } else {
      this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    return this.fileChooser;
  }
}

class CustomFilter extends FileFilter {

  String fileExtension;
  String fileType;

  public CustomFilter(String fileType) {
    fileExtension = "." + fileType;
    this.fileType = fileType;
  }

  @Override
  public boolean accept(File file) {
    return file.isDirectory() || file.getAbsolutePath().toLowerCase().endsWith(fileExtension);
  }

  @Override
  public String getDescription() {
    // This description will be displayed in the dialog,
    // hard-coded = ugly, should be done via I18N
    return fileType + " file (*" + fileExtension + ")";
  }

}
