package data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import mover.DataMover;
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
    private DataMover mover;

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
     * @throws java.io.IOException
     */
    public void order(GeneralController controller) throws IOException {
        File[] directoryListing;
        directoryListing = Ordner.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
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

    public void addExtension(Extension neu) {
        if (!extensionlist.contains(neu)) {
            extensionlist.add(neu);
            neu.setDataTyp(this);
        }

    }

    public void removeExtension(Extension ex) {
        if (extensionlist.contains(ex)) {
            extensionlist.remove(ex);
            ex.setExtension(null);
        }
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

    public DataMover getMover() {
        return mover;
    }

    public void setMover(DataMover neu) {
        if (mover != neu) {
            if (mover != null) {
                mover.removeDataType(this);
            }
            mover = neu;

            if (neu != null) {
                neu.addDataType(this);
            }
        }
    }

    public boolean search(String ex) {
        for (Extension e : extensionlist) {
            if (e.toString().equals(ex)) {
                return true;
            }
        }
        return false;
    }
}
