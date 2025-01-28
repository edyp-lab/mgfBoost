package fr.profi.mgfboost.ui;

import fr.profi.ms.model.MSMSSpectrum;
import fr.profi.mzscope.ui.MGFPanel;
import fr.proline.mzscope.model.Spectrum;
import fr.proline.mzscope.ui.IMzScopeController;
import fr.proline.mzscope.ui.IRawFileViewer;
import fr.proline.studio.table.BeanTableModel;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MS2CollectionPanel extends MGFPanel {


  enum Orientation { UP, DOWN }

  public MS2CollectionPanel(List<MSMSSpectrum> spectrum, IMzScopeController appController) {
    super(spectrum, appController);
  }


  @Override
  protected JToolBar getToolbar() {

    JToolBar jToolBar1 = new JToolBar();

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    JButton displayUpBtn = new JButton();
    displayUpBtn.setText("Display ^");
    displayUpBtn.setToolTipText("Display selected spectrum");
    displayUpBtn.addActionListener(evt -> displaySpectrum(evt, Orientation.UP));
    jToolBar1.add(displayUpBtn);

    JButton displayDownBtn = new JButton();
    displayDownBtn.setText("Display v");
    displayDownBtn.setToolTipText("Display selected spectrum");
    displayDownBtn.addActionListener(evt -> displaySpectrum(evt, Orientation.DOWN));
    jToolBar1.add(displayDownBtn);

    return jToolBar1;
  }

  protected void displaySpectrum(java.awt.event.ActionEvent evt, Orientation orientation) {

    int selectedRow = spectrumTable.convertRowIndexToNonFilteredModel(spectrumTable.getSelectedRow());
    if (selectedRow >= 0) {

      MSMSSpectrum spectrum = ((BeanTableModel<MSMSSpectrum>) spectrumTableModel.getBaseModel()).getData().get(selectedRow);
      Spectrum mzScopeSpectrum = convertToSpectrum(spectrum);

      double maxMgfIntensity = Arrays.stream(spectrum.getIntensityValues()).max().getAsDouble();
      IRawFileViewer viewer = appController.getCurrentRawFileViewer();
      if (orientation == Orientation.UP) {
        viewer.setReferenceSpectrum(mzScopeSpectrum,1.0f);
      } else {
        float scaleFactor = -1.0f;
        if (viewer.getCurrentSpectrum() != null) {
          float[] currentIntensities = viewer.getCurrentSpectrum().getIntensities();
          double maxCurrentIntensity = IntStream.range(0, currentIntensities.length).mapToDouble(i -> currentIntensities[i]).max().getAsDouble();
          scaleFactor = (float) (-maxCurrentIntensity / maxMgfIntensity);
        }
        viewer.setReferenceSpectrum(mzScopeSpectrum, scaleFactor);
      }
    }
  }

  private Spectrum convertToSpectrum(MSMSSpectrum spectrum) {
    double[] intensities = spectrum.getIntensityValues();
    float[] fIntensities = new float[intensities.length];
    for (int k = 0; k < intensities.length; k++) {
      fIntensities[k] = (float)intensities[k];
    }
    Spectrum mzScopeSpectrum = new Spectrum(-1, (float)spectrum.getRetentionTime(), spectrum.getMassValues(), fIntensities, 1, Spectrum.ScanType.CENTROID);
    return mzScopeSpectrum;
  }

}
