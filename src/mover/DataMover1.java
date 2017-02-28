/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mover;

import data.DataType;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * C.G
 */
public class DataMover1 {

    private File Ordner;
    private List<DataType> datatype;
    private static int id;

    public DataMover1(DataType datatyp) {
        File dir = new File("Zusortierende_Dateien" + getId());
        datatype = new LinkedList<>();
        //Ordner Erstellen
        dir.mkdir();
        setOrdner(dir);
        datatype.add(datatyp);
    }

    public static int getId() {
        id = id + 1;
        return id;
    }

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
