package mover;

import data.DataType;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.*;
import org.apache.commons.io.*;
import viewController.GeneralController;

/**
 * DataMover
 * <p>
 * DataMover ist das Herzstück von Dataorganizer, da hier Dateien sortiert
 * werden. DataMover kennt eine Dateitypliste. Es werden Dateien aus dem Ordner
 * ZusortieredeDateien sortiert. Alle Dateien aus diesem Ordner werden je nach
 * Extension und somit Dateityp in einen eigenen Ordner (Dokumente,Bilder,..)
 * verschoben.
 * </p>
 */
public class DataMover {

    private List<DataType> datatype;
    GeneralController controller;

    /**
     * Standardkonstruktor, Benötigt einen Controller und einen Datentyp
     *
     * @param datatyp Datatype
     * @param controller GenerallController
     */
    public DataMover(DataType datatyp, GeneralController controller) {
//        File dir = new File("ZusortierendeDateien");

        datatype = new LinkedList<>();

        //Ordner Erstellen
//        dir.mkdir();
//        setOrdner(dir);
        datatype.add(datatyp);
        this.controller = controller;
    }

    /**
     * sortieren nach Monat aufrufen
     */
    public void order() {
        for (DataType typ : datatype) {
            typ.order(controller);
        }
    }

    /**
     * sortieren nach Dateityp
     *
     * @throws IOException
     */
    public void sort() throws IOException {
        boolean rename = controller.isDateNamingProp();
        boolean verschieben = controller.isVerschiebenProp();

        int anz = 0;
        File f;

        File[] directoryListing;
        String aus = controller.getAusProp();
        if (aus != null) {
            directoryListing = new File(controller.getAusProp()).listFiles();

            if (directoryListing != null) {
                for (File child : directoryListing) {
                    for (DataType type : datatype) {
                        for (int num = 0; num < type.getExtensionlist().size(); num++) {
                            if (FilenameUtils.getExtension(child.getName()).equals(type.getExtensionlist().get(num).getExtension())) {
                                try {
                                    if (rename) {
                                        String filename = datum(child);
                                        f = new File(type.toString() + "\\" + filename + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));

                                    } else {
                                        f = new File(type.toString() + "\\" + child.getName());
                                    }
                                    if (verschieben) {
                                        FileUtils.moveFile(child, f);
                                    } else {
                                        FileUtils.copyFile(child, f);
                                    }
                                } catch (FileExistsException ex) {
                                    f = new File(type.toString() + "\\" + child.getName().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                    anz++;
                                    if (verschieben) {
                                        FileUtils.moveFile(child, f);
                                    } else {
                                        FileUtils.copyFile(child, f);
                                    }
                                } catch (IOException ex) {
                                    System.out.println("IOException");
                                } catch (NullPointerException e) {
                                    System.out.println("NullPointerException");
                                }
                            }
                        }
                    }
                }
//            Alert Box
//            Platform.runLater(() -> {
//                Alert alConfirm = new Alert(Alert.AlertType.INFORMATION);
//                alConfirm.setHeaderText("Dateien wurden sortiert!");
//                alConfirm.show();
//            });
                System.out.println("Dateien von " + controller.getAusProp() + " sortiert!");
            } else {

                System.out.println("Ausgangsordner nicht definiert!");
            }
        }
    }

    /**
     * Datum auslesen
     *
     * @param child File
     * @return YYYY_MM_DD
     * @throws IOException Exception
     */
    private String datum(File child) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(child.toPath(), BasicFileAttributes.class
        );
        Instant creationTime = attr.lastModifiedTime().toInstant();
        //String filename = monat[creationTime.get(MONTH_OF_YEAR) - 1] + "_" +creationTime.get(YEAR);
        LocalDateTime date = LocalDateTime.ofInstant(creationTime, ZoneOffset.UTC);
        String monat = Integer.toString(date.getMonthValue());
        if (date.getMonthValue() < 10) {
            monat = "0" + date.getMonthValue();
        }
        String tag = Integer.toString(date.getDayOfMonth());
        if (date.getDayOfMonth() < 10) {
            tag = "0" + date.getDayOfMonth();
        }
        String filename = date.getYear() + "_" + monat + "_" + tag;
        return filename;
    }

    /**
     * Add DataTyp
     *
     * @param typ DataType
     */
    public void addDataType(DataType typ) {
        datatype.add(typ);
    }

    public List<DataType> getDatatype() {
        return datatype;
    }

    public boolean contains(DataType d) {
        return datatype.contains(d);
    }
}
