package data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.LinkedList;
import mover.DataMover;
import org.apache.commons.io.FileUtils;
import GeneralViewController.GeneralController;

/**
 * DataType
 * <br>
 * Datentyp ist eine Sammlung von Extensions. Der Name vom Datentyp ist
 * gleichzeitig der Ordnername nach dem sortieren.
 */
public class DataType {

    private LinkedList<Extension> extensionlist;
    private File Ordner;
    private DataMover mover;

    public DataType(File Ordner) {
        extensionlist = new LinkedList<>();
        this.Ordner = Ordner;
    }

    /**
     * Order
     * <br>
     * Mithilfe dieser Funktion werden Dateien in den Dateitypordnern in
     * Monatsordner sortiert.
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
                File diry = new File(Ordner.getAbsolutePath(), filename);
                diry.mkdir();
                try {
                    FileUtils.moveFileToDirectory(child, diry, true);
                } catch (java.io.IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Datum für Ordnername
     * <br>
     * Mithilfe dieser Funktion bekommt man das Datum als String YYYY_MM zurück
     *
     * @param child File zum auslesen des Datums
     * @return String YYYY_MM
     * @throws IOException
     */
    private String datum(File child) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
        Instant creationTime = attr.lastModifiedTime().toInstant();
        LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);
        String monat = Integer.toString(date.getMonthValue());
        if (date.getMonthValue() < 10) {
            monat = "0" + date.getMonthValue();
        }
        String filename = date.getYear() + "_" + monat;
        return filename;
    }

    /**
     * Extension hinzufügen
     *
     * @param neu neue Extension
     */
    public void addExtension(Extension neu) {
        if (!extensionlist.contains(neu)) {
            extensionlist.add(neu);
            neu.setDataTyp(this);
        }
    }

    /**
     * Extension löschen
     *
     * @param ex zu löschende Extension
     */
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

    /**
     * Extensionliste
     *
     * @return Extensionliste als String mit Beistrichen
     */
    public String Extensions() {
        String s = "";
        for (Extension e : extensionlist) {
            s += e.toString() + ",";
        }
        return s;
    }

    /**
     * Extension vorhanden?
     *
     * @param e Extension
     * @return true/false
     */
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

    /**
     * Extension vorhanden?
     *
     * @param ex Extension
     * @return true/false
     */
    public boolean search(String ex) {
        for (Extension e : extensionlist) {
            if (e.toString().equals(ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extensionliste löschen
     */
    public void clearExtensionlist() {
        extensionlist.clear();
    }
}
