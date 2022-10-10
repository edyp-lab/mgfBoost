# MGF Boost 



## Getting started

MGFBoost provides a graphical user interface to a set of tools (commands) that can be used to : 

- generate an MGF file from raw data in mzDB format
- convert Maxquant peaklist to MGF format
- merge MGF files by substituting fragments from corresponding scans of one MGF file to another.



In the main window choose `File>Open` and select the file type to open (MGF, mzDB or MaxQuant). For MaxQuant the result
root folder which contains the `combined` folder and `mqpar.xml` file must be selected. The MS/MS spectrum contained in
the opened file are represented as an MS2 collection in the left panel.

Right-click on an MS2 collection to perform one of the available command on the collection or to view the list of MS2
 spectrum in the collection. Viewing a large number of MS2 spectra could take a few minutes. 

If the selected command generates a new MGF file, the output file is automatically added to the collection list at 
the end of the process.         

To merge two MGF files select two MGF MS2 collections on the left panel and right-click on one of them.  