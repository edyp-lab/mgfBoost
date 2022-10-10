package fr.profi.mgfboost;

import com.almworks.sqlite4java.SQLiteException;
import fr.profi.mzdb.MzDbReader;
import fr.profi.mzdb.model.SpectrumHeader;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;

@Ignore
public class ReaderTest {

  @Test
  public void testHeaders()  {
    long start = System.currentTimeMillis();

    String mzdbFilePath = "C:/Local/bruley/Data/Proline/Data/mzdb/Exploris/Xpl1_002790.mzDB";

    try {

      MzDbReader mzDbReader = new MzDbReader(mzdbFilePath, true);
      mzDbReader.enablePrecursorListLoading();
      mzDbReader.enableScanListLoading();


      for (SpectrumHeader sh : mzDbReader.getMs2SpectrumHeaders()) {

      }

      mzDbReader.close();

      float took = (System.currentTimeMillis() - start) / 1000f;

    } catch (SQLiteException e) {
      throw new RuntimeException(e);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
