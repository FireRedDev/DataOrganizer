/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mover;

import data.DataType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * C.G
 */
public class DataMover {

    private File Ordner;
    private DataType datatype;
    private static int id;

    public DataMover(DataType datatyp) {
        File dir = new File("zusortierend_" + this.getId());
        //Ordner Erstellen
        dir.mkdir();
        setOrdner(dir);
        datatype = datatyp;
    }

    public static int getId() {
        id = id + 1;
        return id;
    }

    public void sort() throws IOException {
//Sortierfunktion ist fertig ISI
//Derzeit nur mit einem einzigem Dateityp -> müsste eine Liste werden
        File[] directoryListing = Ordner.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                for (int num = 0; num < datatype.getExtensionlist().size(); num++) {
                    System.out.print(FilenameUtils.getExtension(child.getName()));
                    System.out.print(datatype.getExtensionlist().get(num).getExtension());
                    if (FilenameUtils.getExtension(child.getName()).equals(datatype.getExtensionlist().get(num).getExtension())) {
                        FileUtils.moveFileToDirectory(child, Ordner, false);
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

    public void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

}
