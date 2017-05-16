package data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import org.apache.commons.io.FileUtils;
import viewController.GeneralController;

/**
 * DataType
 * <p>
 * Datentyp ist eine Sammlung von Extensions. Der Name vom Datentyp ist
 * gleichzeitig der Ordnername nach dem sortieren.
 * </p>
 */
public class DataType {

    private LinkedList<Extension> extensionlist;
    private File Ordner;

    //Um Deutsche Monatsnamen zu erhalten
//    private final String[] monat = {"Jänner", "Februar", "Maerz", "April", "Mai","Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
    public DataType(File Ordner) {
//        this.name = name;
        extensionlist = new LinkedList<>();
//        File dir = new File(name);
//        setOrdner(dir);
        this.Ordner = Ordner;
    }

    /**
     * Order
     * <p>
     * Mithilfe dieser Funktion werden Dateien in den Dateitypordnern in
     * Monatsordner sortiert.
     * </p>
     *
     * @param controller
     */
    public void order(GeneralController controller) {
        File[] directoryListing;
        directoryListing = Ordner.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {

                try {
                    String filename = datum(child);
                    //System.out.println(name);
//                    if (controller.getAusProp() == null) {
//                        controller.setAusProp(FileUtils.getUserDirectoryPath() + "\\sortiert");
//                    }
                    File diry = new File(Ordner.getAbsolutePath(), filename);
                    diry.mkdir();
                    try {
                        FileUtils.moveFileToDirectory(child, diry, true);
                    } catch (java.io.IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                    //System.out.println("creationTime: " + attr.creationTime());
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
//        System.out.println("Dateien von " + controller.getAusProp() + " sortiert!");
    }

    private String datum(File child) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
        Instant creationTime = attr.lastModifiedTime().toInstant();
        //String filename = monat[creationTime.get(MONTH_OF_YEAR) - 1] + "_" +creationTime.get(YEAR);
        LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);
        String monat = Integer.toString(date.getMonthValue());
        if (date.getMonthValue() < 10) {
            monat = "0" + date.getMonthValue();
        }
        String filename = date.getYear() + "_" + monat;
        return filename;
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

    @Override
    public String toString() {
        return Ordner.getAbsolutePath();
    }

    public boolean contains(Extension e) {
        return extensionlist.contains(e);
    }

}
