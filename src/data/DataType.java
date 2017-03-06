/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.zone;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * C.G
 */
public class DataType {

    private LinkedList<Extension> extensionlist;
    private File Ordner;

    private String name;

    public DataType(String name) {
        this.name = name;
        extensionlist = new LinkedList<>();
        File dir = new File(name);
        dir.mkdir();
        setOrdner(dir);
    }

    public void order() {

        File[] directoryListing = Ordner.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {

                try {
                    BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
                    LocalDateTime date = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                    System.out.print(date);
                    String name = date.getMonth().toString() + "_" + date.getYear();
                    System.out.println(name);
                    File diry = new File(Ordner.getPath(), name);
                    diry.mkdir();
                    try {
                        FileUtils.moveFileToDirectory(child, diry, false);
                    } catch (java.io.IOException ex) {
                        System.out.println(ex.getMessage());
                    }

//System.out.println("creationTime: " + attr.creationTime());
                } catch (IOException ex) {
                    Logger.getLogger(DataType.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public void addExtension(Extension aThis) {
        extensionlist.add(aThis);
    }

    public void removeExtension(Extension aThis) {
        extensionlist.remove(aThis);
    }

    public LinkedList<Extension> getExtensionlist() {
        return extensionlist;
    }

    public void setExtensionlist(LinkedList<Extension> extensionlist) {
        this.extensionlist = extensionlist;
    }

    public File getOrdner() {
        return Ordner;
    }

    public void setOrdner(File Ordner) {
        this.Ordner = Ordner;
    }

}
