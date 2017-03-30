package data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final String name;

    //Um Deutsche Monatsnamen zue erhalten
    private final String[] monat = {"Jänner", "Februar", "Maerz", "April", "Mai",
        "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};

    public DataType(String name) {
        this.name = name;
        extensionlist = new LinkedList<>();
        File dir = new File(name);
        setOrdner(dir);
    }

    /**
     * Order
     * <p>
     * Mithilfe dieser Funktion werden Dateien in den Dateitypordnern in
     * Monatsordner sortiert.
     * </p>
     */
    public void order(GeneralController controller) {

        File[] directoryListing;
        String aus = controller.getZielProp();

        //Falls kein Ausgangsordner gewählt wurde, wird User-Verzeichnis\ZusoriterendeDateien verwendet
        if (aus != null) {
            directoryListing = new File(aus + "\\" + Ordner).listFiles();
        } else {
            controller.setAusProp(FileUtils.getUserDirectoryPath() + "\\" + Ordner);
            directoryListing = new File(FileUtils.getUserDirectoryPath() + "\\" + Ordner).listFiles();
        }

        if (directoryListing != null) {
            for (File child : directoryListing) {

                try {
                    BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
                    Instant creationTime = attr.lastModifiedTime().toInstant();

                    //String filename = monat[creationTime.get(MONTH_OF_YEAR) - 1] + "_" +creationTime.get(YEAR);
                     LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);

                    String filename = monat[date.getMonthValue() - 1] + "_" + date.getYear();
                    //System.out.println(name);
                    if (controller.getZielProp() == null) {
                        controller.setZielProp(FileUtils.getUserDirectoryPath() + "\\sortiert");
                    }
                    File diry = new File(controller.getZielProp() + "\\" + Ordner.getPath(), filename);
                    diry.mkdir();
                    try {
                        FileUtils.moveFileToDirectory(child, diry, true);
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
        System.out.println("Dateien von " + controller.getAusProp() + " nach " + controller.getZielProp() + " nach Monat sortiert!");
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
