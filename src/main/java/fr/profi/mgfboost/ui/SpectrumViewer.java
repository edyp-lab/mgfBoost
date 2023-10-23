package fr.profi.mgfboost.ui;

import fr.proline.mzscope.model.*;
import fr.proline.mzscope.ui.AbstractSpectrumPanel;
import fr.proline.mzscope.ui.IRawFileViewer;
import fr.proline.mzscope.utils.Display;
import fr.proline.mzscope.utils.MzScopeCallback;
import fr.proline.studio.utils.CyclicColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Map;

public class SpectrumViewer extends JPanel implements IRawFileViewer {

  private AbstractSpectrumPanel spectrumPanel;

  private Spectrum spectrum;
  private Spectrum referenceSpectrum;

  public SpectrumViewer() {
    initComponents();
  }

  private void initComponents() {
    spectrumPanel = new AbstractSpectrumPanel(this) {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {

      }

      @Override
      public void plotPanelMouseClicked(MouseEvent e, double xValue, double yValue) {

      }

      @Override
      protected JToolBar getSpectrumToolbar() {
        return new JToolBar();
      }

    };
    spectrumPanel.initComponents();
    setLayout(new BorderLayout());
    add(spectrumPanel, BorderLayout.CENTER);
  }


  @Override
  public IRawFile getCurrentRawfile() {
    return null;
  }

  @Override
  public void extractAndDisplay(ExtractionRequest extractionRequest, Display display, MzScopeCallback mzScopeCallback) {

  }

  @Override
  public Color displayChromatogram(IChromatogram iChromatogram, Display display) {
    return null;
  }

  @Override
  public void displayChromatograms(Map<IRawFile, IChromatogram> map, Display display) {

  }

  @Override
  public void displayPeakel(IPeakel iPeakel) {

  }

  @Override
  public void displayScan(long l) {

  }

  @Override
  public void setReferenceSpectrum(Spectrum spectrum, Float aFloat) {
    if (aFloat > 0) {
      this.spectrum = spectrum;
      spectrumPanel.displayScan(spectrum);
    } else {
      this.referenceSpectrum = spectrum;
      spectrumPanel.setReferenceSpectrum(spectrum, aFloat);
    }
  }

  @Override
  public IChromatogram getCurrentChromatogram() {
    return null;
  }

  @Override
  public Iterable<IChromatogram> getAllChromatograms() {
    return null;
  }

  @Override
  public Color getPlotColor(String s) {
    return CyclicColorPalette.getColor(1);
  }

  @Override
  public Display.Mode getChromatogramDisplayMode() {
    return null;
  }

  @Override
  public Spectrum getCurrentSpectrum() {
    return spectrum;
  }

  @Override
  public void changeForceFittedToCentroid() {

  }
}
