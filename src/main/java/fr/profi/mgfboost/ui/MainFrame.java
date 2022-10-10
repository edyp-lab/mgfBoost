package fr.profi.mgfboost.ui;

import fr.profi.mzscope.ui.RawMinerFrame;
import fr.profi.util.version.IVersion;
import fr.proline.studio.ImageUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;

public class MainFrame extends JFrame {

  private final static Logger logger = LoggerFactory.getLogger(MainFrame.class);

  private final MainPanel mainPanel;
  public MainFrame() {

    ServiceLoader<IVersion> versionLoader = ServiceLoader.load(IVersion.class);
    Iterator<IVersion> iter = versionLoader.iterator();
    String version = "snapshot";
    while(iter.hasNext()) {
      IVersion v = iter.next();
      if (v.getModuleName().equalsIgnoreCase("mzScope")) {
        version = v.getVersion();
      }
    }

    ImageIcon appIcon = ImageUtilities.loadImageIcon("fr/profi/mgfboost/images/mgfBoost.png", false);
    setIconImage(appIcon.getImage());
    setTitle("MgfBoost "+version);
    mainPanel = new MainPanel();
    initComponents();
    getContentPane().add(mainPanel);

    setSize(700,500);
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
  }

  private void initComponents() {
    final JMenuBar jMenuBar = new JMenuBar();
    final JMenu fileMenu = new JMenu("File");
    jMenuBar.add(fileMenu);
    final JMenu openMenu = new JMenu("Open");
    fileMenu.add(openMenu);
    JMenuItem menuItem = new JMenuItem("MzDB File");
    menuItem.addActionListener(this::openMzdbFileActionPerformed);
    openMenu.add(menuItem);
    menuItem = new JMenuItem("MGF File");
    menuItem.addActionListener(this::openMGFFileActionPerformed);
    openMenu.add(menuItem);
    menuItem = new JMenuItem("MaxQuant Folder");
    menuItem.addActionListener(this::openMQFileActionPerformed);
    openMenu.add(menuItem);
    setJMenuBar(jMenuBar);
  }

  private void openMQFileActionPerformed(ActionEvent actionEvent) {
    mainPanel.openFileAction(MainPanel.FileType.MAXQUANT.name().toLowerCase());
  }

  private void openMGFFileActionPerformed(ActionEvent actionEvent) {
    mainPanel.openFileAction(MainPanel.FileType.MGF.name().toLowerCase());
  }

  private void openMzdbFileActionPerformed(java.awt.event.ActionEvent evt) {
    mainPanel.openFileAction(MainPanel.FileType.MZDB.name().toLowerCase());
  }

  public static void main(String[] args) {
    try {
      Locale.setDefault(new Locale("en", "US"));
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Windows".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(RawMinerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
  }
}
