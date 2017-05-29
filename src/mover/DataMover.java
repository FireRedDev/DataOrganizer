package mover;

import data.DataType;
import data.RegexRule;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.RegexFileFilter;
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
    private List<RegexRule> regexrules;
    GeneralController controller;
    private int anz;

    /**
     * Standardkonstruktor, Benötigt einen Controller und einen Datentyp
     *
     * @param datatyp Datatype
     * @param controller GenerallController
     */
    public DataMover(DataType datatyp, GeneralController controller) {
//        File dir = new File("ZusortierendeDateien");

        datatype = new LinkedList<>();
        regexrules = new LinkedList<>();
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
        try {
            for (DataType typ : datatype) {
                typ.order(controller);
            }
        } catch (IOException ex) {
            controller.showErrorMessage("Fehler beim sortieren nach Datum!");
        }
    }

    /**
     * sortieren nach Regex
     *
     * @param directoryListing
     * @param input
     * @throws IOException
     */
    public void sortbyRegex(File[] directoryListing) throws IOException {
        boolean subfolder = controller.issortSubFolderProp();
        boolean verschieben = controller.isVerschiebenProp();
        File dir = new File(controller.getAusProp());
        for (RegexRule rule : regexrules) {
            FileFilter fileFilter = new RegexFileFilter(rule.getRegex());
            File[] files = dir.listFiles(fileFilter);
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() && subfolder) {
                    System.out.print("foundFolder");
                    //jz miasma nu die regexrules durchgeh und imma den jeweiligen regex in den jeweilien ordner verschieben
                    this.sortbyRegex(files[i].listFiles());
                }
                //Jetzt müsste man FIles[i] an sein Ziel verschieben.
                //movetosomewhere(rule.getordner)
                verschieben(verschieben, files[i], rule.getOrdner());
            }
        }
    }

    /**
     * sortieren nach Dateityp
     *
     * @throws IOException
     */
    public void sort(File[] directoryListing) throws IOException {
        boolean rename = controller.isDateNamingProp();
        boolean verschieben = controller.isVerschiebenProp();
        boolean subfolder = controller.issortSubFolderProp();
  boolean sortviaRegex = controller.issortviaRegexProp();
        anz = 0;
        File f;
//                      File file = CH1.getSelectedFile();
//File[] files = file.listFiles();
//for(int i=0; i< files.length; i++) {
//       if(files[i].isDirectory()) {
//             TA1.append(files[i].getName());
//       }
//}
if(sortviaRegex) {
     sortbyRegex(directoryListing);
}
else {
        String aus = controller.getAusProp();
        if (aus != null) {

            Instant start = Instant.now();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.isDirectory() && subfolder) {
                        System.out.print("foundFolder");
                        this.sort(child.listFiles());
                    } else {
                        for (DataType type : datatype) {
                            if (type.search(FilenameUtils.getExtension(child.getName()))) {
                                try {
                                    //property adden nd vagessn
                                    //fehler

                                    if (rename) {
                                        String filename = datum(child);
                                        filename = type.toString() + "\\" + filename + "." + FilenameUtils.getExtension(child.getName());
                                        if (new File(filename).exists()) {
                                            f = new File(filename.substring(0, filename.indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                            anz++;
                                        } else {
                                            f = new File(filename);
                                        }

                                    } else {
                                        f = new File(type.toString() + "\\" + child.getName());
                                    }
                                    if (f.exists()) {
                                        f = new File(type.toString() + "\\" + f.getAbsoluteFile().toString().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                        anz++;
                                    }
                                    verschieben(verschieben, child, f);
                                } catch (FileExistsException ex) {
                                    f = new File(type.toString() + "\\" + child.getName().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                                    anz++;
                                    verschieben(verschieben, child, f);
                                } catch (IOException ex) {
                                    controller.showErrorMessage("IOException");
                                } catch (NullPointerException e) {
                                    controller.showErrorMessage("NullPointerException");
                                }
                                break;
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
                Instant end = Instant.now();
                System.out.println("Sortierzeit: " + Duration.between(start, end).toNanos());
                controller.showSuccessMessage(" Dateien von " + controller.getAusProp() + " sortiert!");
            } else {
                controller.showErrorMessage("Ausgangsordner nicht definiert!");
            }
        }}
    }

    private void verschieben(boolean verschieben, File child, File f) throws IOException {
        if (verschieben) {
            FileUtils.moveFile(child, f);
        } else {
            FileUtils.copyFile(child, f);
        }
    }

    private File exists(File f, DataType type, File child) {
        if (f.exists()) {
            f = new File(type.toString() + "\\" + f.getAbsoluteFile().toString().substring(0, child.getName().indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
            anz++;
        }
        return f;
    }

    private File rename(boolean rename, File child, DataType type) throws IOException {
        File f;
        if (rename) {
            String filename = datum(child);
            filename = type.toString() + "\\" + filename + "." + FilenameUtils.getExtension(child.getName());
            if (new File(filename).exists()) {
                f = new File(filename.substring(0, filename.indexOf(".")) + "(" + anz + ")." + FilenameUtils.getExtension(child.getName()));
                anz++;
            } else {
                f = new File(filename);
            }

        } else {
            f = new File(type.toString() + "\\" + child.getName());
        }
        return f;
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
        if (!datatype.contains(typ)) {
            datatype.add(typ);
            typ.setMover(this);
        }
    }

    public void removeDataType(DataType typ) {
        if (datatype.contains(typ)) {
            datatype.remove(typ);
            typ.setMover(this);
        }
    }

    public List<DataType> getDatatype() {
        return datatype;
    }

    public void addRegexRule(RegexRule typ) {
        if (!regexrules.contains(typ)) {
            regexrules.add(typ);
            // typ.setMover(this);
        }
    }

    public void removeRegexRule(DataType typ) {
        if (regexrules.contains(typ)) {
            regexrules.remove(typ);
            // typ.setMover(this);
        }
    }

    public List<RegexRule> getRegexRules() {
        return regexrules;
    }

    public boolean contains(DataType d) {
        return datatype.contains(d);
    }
}
