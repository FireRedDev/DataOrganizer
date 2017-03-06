package mover;

import data.DataType;
import java.io.*;
import java.util.*;
import org.apache.commons.io.*;

/**
 * DataMover
 * <p>
 * DataMover ist das Herzstück von Dataorganizer, da hier Dateien sortiert
 * werden. DataMover kennt eine Dateitypliste. 
 * Es werden Dateien aus dem Ordner ZusortieredeDateien sortiert.
 * Alle Dateien aus diesem Ordner werden je nach Extension und somit Dateityp
 * in einen eigenen Ordner (Dokumente,Bilder,..) verschoben.
 * </p>
 */
public class DataMover {

    private File Ordner;
    private List<DataType> datatype;
    private static int id;

    public DataMover(DataType datatyp) {
        File dir = new File("ZusortierendeDateien");
        datatype = new LinkedList<>();
        
        //Ordner Erstellen
        dir.mkdir();
        setOrdner(dir);
        datatype.add(datatyp);
    }

    public void order() {
        for (DataType typ : datatype) {
            typ.order();
        }
    }

    /**
     * sortieren nach Dateityp
     * 
     * @throws IOException 
     */
    public void sort() throws IOException {

        File[] directoryListing = Ordner.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                for (DataType type : datatype) {
                    for (int num = 0; num < type.getExtensionlist().size(); num++) {
                        //System.out.print(FilenameUtils.getExtension(child.getName()));
                        //System.out.print(type.getExtensionlist().get(num).getExtension());
                        if (FilenameUtils.getExtension(child.getName()).equals(type.getExtensionlist().get(num).getExtension())) {
                            FileUtils.moveFileToDirectory(child, type.getOrdner(), false);
                        }
                    }
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public File getOrdner() {
        return Ordner;
    }

    private void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

    public void addDataType(DataType typ) {
        datatype.add(typ);
    }

}
