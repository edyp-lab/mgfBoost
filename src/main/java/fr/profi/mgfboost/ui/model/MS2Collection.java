package fr.profi.mgfboost.ui.model;

import com.almworks.sqlite4java.SQLiteException;
import fr.profi.mgfboost.ui.MainPanel;
import fr.profi.mzdb.MzDbReader;
import fr.profi.mzdb.model.SpectrumData;
import fr.profi.mzdb.model.SpectrumHeader;
import fr.profi.mzscope.InvalidMGFFormatException;
import fr.profi.mzscope.MGFConstants;
import fr.profi.mzscope.MGFReader;
import fr.profi.mzscope.MSMSSpectrum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MS2Collection {

  private final static Logger logger = LoggerFactory.getLogger(MS2Collection.class);

  private File file;
  private MainPanel.FileType type;

  public static class MaxQuant {

    public static File[] locateAPLFiles(MS2Collection collection) {
      if (collection.getFileType() == MainPanel.FileType.MAXQUANT) {
        File[] files = new File[2];

        final Optional<Path> sil0FileOpt = findFile(collection.getFile().toPath(), ".sil0.apl");
        final Optional<Path> peakFileOpt = findFile(collection.getFile().toPath(), ".peak.apl");

        files[0] = sil0FileOpt.isPresent() ? sil0FileOpt.get().toFile() : null;
        files[1] = peakFileOpt.isPresent() ? peakFileOpt.get().toFile() : null;
        return files;
      }
      return new File[0];
    }

    private static Optional<Path> findFile(Path path, String extension) {

      try (Stream<Path> walk = Files.walk(path, 3)) {
        final Optional<Path> optionalPath = walk.filter(Files::isRegularFile).filter(p -> {
          try {
            return p.getFileName().toString().toLowerCase().endsWith(extension) && Files.size(p) > 0;
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }).findFirst();
        return optionalPath;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static class Mzdb {

    public static List<MSMSSpectrum> readSpectrumList(String filepath) {
      MzDbReader reader = null;
      try {

        reader = new MzDbReader(filepath, true);
        reader.enablePrecursorListLoading();
        reader.enableScanListLoading();

        final SpectrumHeader[] ms2SpectrumHeaders = reader.getMs2SpectrumHeaders();
        List<MSMSSpectrum> spectrumList = new ArrayList<>(ms2SpectrumHeaders.length);
        for (SpectrumHeader header : ms2SpectrumHeaders) {
          final MSMSSpectrum msmsSpectrum = new MSMSSpectrum(header.getPrecursorMz(), header.getTIC(), header.getPrecursorCharge(), header.getElutionTime());
          msmsSpectrum.setAnnotation(MGFConstants.SCANS, header.getSpectrumId());
          final SpectrumData spectrumData = reader.getSpectrum(header.getSpectrumId()).getData();
          final float[] intensityList = spectrumData.getIntensityList();
          final double[] mzList = spectrumData.getMzList();
          for (int k = 0; k < mzList.length; k++) {
            msmsSpectrum.addPeak(mzList[k], intensityList[k]);
          }
          spectrumList.add(msmsSpectrum);
        }
        reader.close();
        return spectrumList;
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      } catch (SQLiteException e) {
        throw new RuntimeException(e);
      } catch (StreamCorruptedException e) {
        throw new RuntimeException(e);
      } finally {
        if (reader != null) reader.close();
      }
    }
  }

  public MS2Collection(File file, MainPanel.FileType type) {
    this.file = file;
    this.type = type;
  }

  public String getName() {
    return file.getName();
  }

  public MainPanel.FileType getFileType() {
    return type;
  }

  public File getFile() {
    return file;
  }

  public List<MSMSSpectrum> getSpectrumList() {
    if (getFileType() == MainPanel.FileType.MGF) {
      MGFReader reader = new MGFReader();
      try {
        return reader.read(getFile());
      } catch (InvalidMGFFormatException e) {
        logger.error("Unable to read MGF file " + getFile().getAbsolutePath(), e);
      }
    } else if (getFileType() == MainPanel.FileType.MZDB) {
      return Mzdb.readSpectrumList(getFile().getAbsolutePath());
    }
    return null;
  }
}
